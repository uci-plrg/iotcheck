/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder core (jpf-core) platform is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gov.nasa.jpf.listener;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.vm.bytecode.LocalVariableInstruction;
import gov.nasa.jpf.vm.bytecode.ReadInstruction;
import gov.nasa.jpf.vm.bytecode.StoreInstruction;
import gov.nasa.jpf.vm.bytecode.WriteInstruction;
import gov.nasa.jpf.vm.choice.IntChoiceFromSet;

import java.io.PrintWriter;

import java.util.*;

/**
 * Listener using data flow analysis to find conflicts between smartApps.
 **/

public class ConflictTracker extends ListenerAdapter {
  // Public graph: to allow the StateReducer class to access it
  public static final HashMap<Integer, Node> nodes = new HashMap<Integer, Node>(); // Nodes of a graph
  // Private
  private final PrintWriter out;
  private final HashSet<String> conflictSet = new HashSet<String>(); // Variables we want to track
  private final HashSet<String> appSet = new HashSet<String>(); // Apps we want to find their conflicts
  private final HashSet<String> manualSet = new HashSet<String>(); // Writer classes with manual inputs to detect direct-direct(No Conflict) interactions
  private HashSet<Transition> transitions = new HashSet<Transition>();
  private ArrayList<Update> currUpdates = new ArrayList<Update>();
  private long timeout;
  private long startTime;
  private Node parentNode = new Node(-2);
  private String operation;
  private String detail;
  private String errorMessage;
  private int depth;
  private int id;
  private boolean manual = false;
  private boolean conflictFound = false;
  private int currentEvent = -1;
  private boolean debugMode = false;

  private final String SET_LOCATION_METHOD = "setLocationMode";
  private final String LOCATION_VAR = "locationMode";

  public ConflictTracker(Config config, JPF jpf) {
    out = new PrintWriter(System.out, true);
    debugMode = config.getBoolean("debug_mode", false);

    String[] conflictVars = config.getStringArray("variables");
    // We are not tracking anything if it is null
    if (conflictVars != null) {
      for (String var : conflictVars) {
        conflictSet.add(var);
      }
    }
    String[] apps = config.getStringArray("apps");
    // We are not tracking anything if it is null
    if (apps != null) {
      for (String var : apps) {
        appSet.add(var);
      }
    }

    // Timeout input from config is in minutes, so we need to convert into millis
    timeout = config.getInt("timeout", 0) * 60 * 1000;
    startTime = System.currentTimeMillis();
  }

  void propagateChange(Edge newEdge) {
    HashSet<Edge> changed = new HashSet<Edge>();

    // Add the current node to the changed set
    changed.add(newEdge);

    while(!changed.isEmpty()) {
      // Get the first element of the changed set and remove it
      Edge edgeToProcess = changed.iterator().next();
      changed.remove(edgeToProcess);

      //If propagating change on edge causes change, enqueue all the target node's out edges
      if (propagateEdge(edgeToProcess)) {
        Node dst = edgeToProcess.getDst();
        for (Edge e : dst.getOutEdges()) {
          changed.add(e);
        }
      }
    }
  }

  boolean propagateEdge(Edge e) {
    HashMap<IndexObject, HashSet<Update>> srcUpdates = e.getSrc().getLastUpdates();
    HashMap<IndexObject, HashSet<Update>> dstUpdates = e.getDst().getLastUpdates();
    ArrayList<Update> edgeUpdates = e.getUpdates();
    HashMap<IndexObject, Update> lastupdate = new HashMap<IndexObject, Update>();
    boolean changed = false;

    //Go through each update on the current transition
    for(int i=0; i<edgeUpdates.size(); i++) {
      Update u = edgeUpdates.get(i);
      IndexObject io = u.getIndex();
      HashSet<Update> confupdates = null;

      //See if we have already updated this device attribute
      if (lastupdate.containsKey(io)) {
        confupdates = new HashSet<Update>();
        confupdates.add(lastupdate.get(io));
      } else if (srcUpdates.containsKey(io)){
        confupdates = srcUpdates.get(io);
      }

      //Check for conflict with the appropriate update set if we are not a manual transition
      //If this is debug mode, then we do not report any conflicts
      if (!debugMode && confupdates != null && !u.isManual()) {
        for(Update u2: confupdates) {
          if (conflicts(u, u2)) {
            //throw new RuntimeException(createErrorMessage(u, u2));
            conflictFound = true;
            errorMessage = createErrorMessage(u, u2);
          }
        }
      }
      lastupdate.put(io, u);
    }
    for(IndexObject io: srcUpdates.keySet()) {
      //Only propagate src changes if the transition doesn't update the device attribute
      if (!lastupdate.containsKey(io)) {
        //Make sure destination has hashset in map
        if (!dstUpdates.containsKey(io))
          dstUpdates.put(io, new HashSet<Update>());

        changed |= dstUpdates.get(io).addAll(srcUpdates.get(io));
      }
    }
    for(IndexObject io: lastupdate.keySet()) {
      //Make sure destination has hashset in map
      if (!dstUpdates.containsKey(io))
        dstUpdates.put(io, new HashSet<Update>());

      changed |= dstUpdates.get(io).add(lastupdate.get(io));
    }
    return changed;
  }

  //Method to check for conflicts between two updates
  //Have conflict if same device, same attribute, different app, different vaalue
  boolean conflicts(Update u, Update u2) {
    return (!u.getApp().equals(u2.getApp())) &&
            u.getDeviceId().equals(u2.getDeviceId()) &&
            u.getAttribute().equals(u2.getAttribute()) &&
            (!u.getValue().equals(u2.getValue()));
  }

  String createErrorMessage(Update u, Update u2) {
    String message = "Conflict found between the two apps. "+u.getApp()+
            " has written the value: "+u.getValue()+
            " to the attribute: "+u.getAttribute()+" while "
            +u2.getApp()+" is writing the value: "
            +u2.getValue()+" to the same variable!";
    System.out.println(message);
    return message;
  }

  Edge createEdge(Node parent, Node current, Transition transition, int evtNum) {
    //Check if this transition is explored.  If so, just skip everything
    if (transitions.contains(transition))
      return null;

    //Create edge
    Edge e = new Edge(parent, current, transition, evtNum, currUpdates);
    parent.addOutEdge(e);

    //Mark transition as explored
    transitions.add(transition);
    return e;
  }

  static class Node {
    Integer id;
    Vector<Edge> outEdges = new Vector<Edge>();
    HashMap<IndexObject, HashSet<Update>> lastUpdates = new HashMap<IndexObject,  HashSet<Update>>();

    Node(Integer id) {
      this.id = id;
    }

    Integer getId() {
      return id;
    }

    Vector<Edge> getOutEdges() {
      return outEdges;
    }

    void addOutEdge(Edge e) {
      outEdges.add(e);
    }

    HashMap<IndexObject, HashSet<Update>> getLastUpdates() {
      return lastUpdates;
    }
  }

  //Each Edge corresponds to a transition
  static class Edge {
    Node source, destination;
    Transition transition;
    int eventNumber;
    ArrayList<Update> updates = new ArrayList<Update>();

    Edge(Node src, Node dst, Transition t, int evtNum, ArrayList<Update> _updates) {
      this.source = src;
      this.destination = dst;
      this.transition = t;
      this.eventNumber = evtNum;
      this.updates.addAll(_updates);
    }

    Node getSrc() {
      return source;
    }

    Node getDst() {
      return destination;
    }

    Transition getTransition() {
      return transition;
    }

    int getEventNumber() { return eventNumber; }

    ArrayList<Update> getUpdates() {
      return updates;
    }
  }

  static class Update {
    String appName;
    String deviceId;
    String attribute;
    String value;
    boolean ismanual;

    Update(String _appName, String _deviceId, String _attribute, String _value, boolean _ismanual) {
      this.appName = _appName;
      this.deviceId = _deviceId;
      this.attribute = _attribute;
      this.value = _value;
      this.ismanual = _ismanual;
    }

    boolean isManual() {
      return ismanual;
    }

    String getApp() {
      return appName;
    }

    String getDeviceId() {
      return deviceId;
    }

    String getAttribute() {
      return attribute;
    }

    String getValue() {
      return value;
    }

    //Gets an index object for indexing updates by just device and attribute
    IndexObject getIndex() {
      return new IndexObject(this);
    }

    public boolean equals(Object o) {
      if (!(o instanceof Update))
        return false;
      Update u=(Update)o;
      return (getDeviceId().equals(u.getDeviceId()) &&
              getAttribute().equals(u.getAttribute()) &&
              getApp().equals(u.getApp()) &&
              getValue().equals(u.getValue()));
    }

    public int hashCode() {
      return (getDeviceId().hashCode() << 3) ^
              (getAttribute().hashCode() << 2) ^
              (getApp().hashCode() << 1) ^
              getValue().hashCode();
    }

    public String toString() {
      return "<"+getAttribute()+", "+getValue()+", "+getApp()+", "+ismanual+">";
    }
  }

  static class IndexObject {
    Update u;
    IndexObject(Update _u) {
      this.u = _u;
    }

    public boolean equals(Object o) {
      if (!(o instanceof IndexObject))
        return false;
      IndexObject io=(IndexObject)o;
      return (u.getDeviceId().equals(io.u.getDeviceId()) &&
              u.getAttribute().equals(io.u.getAttribute()));
    }
    public int hashCode() {
      return (u.getDeviceId().hashCode() << 1) ^ u.getAttribute().hashCode();
    }
  }

  @Override
  public void stateRestored(Search search) {
    id = search.getStateId();
    depth = search.getDepth();
    operation = "restored";
    detail = null;

    out.println("The state is restored to state with id: "+id+", depth: "+depth);

    // Update the parent node
    parentNode = getNode(id);
  }

  @Override
  public void searchStarted(Search search) {
    out.println("----------------------------------- search started");
  }

  private Node getNode(Integer id) {
    if (!nodes.containsKey(id))
      nodes.put(id, new Node(id));
    return nodes.get(id);
  }

  public void printGraph() {
    System.out.println("digraph testgraph {");
    for(Integer i : nodes.keySet()) {
      Node n = nodes.get(i);
      System.out.print("N"+i+"[label=\"");

      for(IndexObject io:n.lastUpdates.keySet()) {
        for(Update u:n.lastUpdates.get(io)) {
          System.out.print(u.toString().replace("\"", "\\\"")+", ");
        }
      }
      System.out.println("\"];");
      for(Edge e:n.outEdges) {
        System.out.print("N"+e.getSrc().getId()+"->N"+e.getDst().getId()+"[label=\"");
        for(Update u:e.getUpdates()) {
          System.out.print(u.toString().replace("\"", "\\\"")+", ");
        }
        System.out.println("\"];");
      }
    }

    System.out.println("}");
  }

  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {

    if (currentCG instanceof IntChoiceFromSet) {
      IntChoiceFromSet icsCG = (IntChoiceFromSet) currentCG;
      currentEvent = icsCG.getNextChoice();
    }
  }

  @Override
  public void stateAdvanced(Search search) {
    String theEnd = null;
    Transition transition = search.getTransition();
    id = search.getStateId();
    depth = search.getDepth();
    operation = "forward";

    // Add the node to the list of nodes
    Node currentNode = getNode(id);

    // Create an edge based on the current transition
    Edge newEdge = createEdge(parentNode, currentNode, transition, currentEvent);

    // Reset the temporary variables and flags
    currUpdates.clear();
    manual = false;

    // If we have a new Edge, check for conflicts
    if (newEdge != null)
      propagateChange(newEdge);

    if (search.isNewState()) {
      detail = "new";
    } else {
      detail = "visited";
    }

    if (search.isEndState()) {
      out.println("This is the last state!");
      theEnd = "end";
    }

    out.println("The state is forwarded to state with id: "+id+", depth: "+depth+" which is "+detail+" state: "+"% "+theEnd);

    // Update the parent node
    parentNode = currentNode;
  }

  @Override
  public void stateBacktracked(Search search) {
    id = search.getStateId();
    depth = search.getDepth();
    operation = "backtrack";
    detail = null;

    out.println("The state is backtracked to state with id: "+id+", depth: "+depth);

    // Update the parent node
    parentNode = getNode(id);
  }

  @Override
  public void searchFinished(Search search) {
    out.println("----------------------------------- search finished");

    //Comment out the following line to print the explored graph
    printGraph();
  }

  private String getValue(ThreadInfo ti, Instruction inst, byte type) {
    StackFrame frame;
    int lo, hi;

    frame = ti.getTopFrame();

    if ((inst instanceof JVMLocalVariableInstruction) ||
            (inst instanceof JVMFieldInstruction))
    {
      if (frame.getTopPos() < 0)
        return(null);

      lo = frame.peek();
      hi = frame.getTopPos() >= 1 ? frame.peek(1) : 0;

      // TODO: Fix for integer values (need to dig deeper into the stack frame to find the right value other than 0)
      // TODO: Seems to be a problem since this is Groovy (not Java)
      if (type == Types.T_INT || type == Types.T_LONG || type == Types.T_SHORT) {
        int offset = 0;
        while (lo == 0) {
          lo = frame.peek(offset);
          offset++;
        }
      }

      return(decodeValue(type, lo, hi));
    }

    if (inst instanceof JVMArrayElementInstruction)
      return(getArrayValue(ti, type));

    return(null);
  }

  private final static String decodeValue(byte type, int lo, int hi) {
    switch (type) {
      case Types.T_ARRAY:   return(null);
      case Types.T_VOID:    return(null);

      case Types.T_BOOLEAN: return(String.valueOf(Types.intToBoolean(lo)));
      case Types.T_BYTE:    return(String.valueOf(lo));
      case Types.T_CHAR:    return(String.valueOf((char) lo));
      case Types.T_DOUBLE:  return(String.valueOf(Types.intsToDouble(lo, hi)));
      case Types.T_FLOAT:   return(String.valueOf(Types.intToFloat(lo)));
      case Types.T_INT:     return(String.valueOf(lo));
      case Types.T_LONG:    return(String.valueOf(Types.intsToLong(lo, hi)));
      case Types.T_SHORT:   return(String.valueOf(lo));

      case Types.T_REFERENCE:
        ElementInfo ei = VM.getVM().getHeap().get(lo);
        if (ei == null)
          return(null);

        ClassInfo ci = ei.getClassInfo();
        if (ci == null)
          return(null);

        if (ci.getName().equals("java.lang.String"))
          return('"' + ei.asString() + '"');

        return(ei.toString());

      default:
        System.err.println("Unknown type: " + type);
        return(null);
    }
  }

  private String getArrayValue(ThreadInfo ti, byte type) {
    StackFrame frame;
    int lo, hi;

    frame = ti.getTopFrame();
    lo    = frame.peek();
    hi    = frame.getTopPos() >= 1 ? frame.peek(1) : 0;

    return(decodeValue(type, lo, hi));
  }

  private byte getType(ThreadInfo ti, Instruction inst) {
    StackFrame frame;
    FieldInfo fi;
    String type;

    frame = ti.getTopFrame();
    if ((frame.getTopPos() >= 0) && (frame.isOperandRef())) {
      return (Types.T_REFERENCE);
    }

    type = null;

    if (inst instanceof JVMLocalVariableInstruction) {
      type = ((JVMLocalVariableInstruction) inst).getLocalVariableType();
    } else if (inst instanceof JVMFieldInstruction){
      fi = ((JVMFieldInstruction) inst).getFieldInfo();
      type = fi.getType();
    }

    if (inst instanceof JVMArrayElementInstruction) {
      return (getTypeFromInstruction(inst));
    }

    if (type == null) {
      return (Types.T_VOID);
    }

    return (decodeType(type));
  }

  private final static byte getTypeFromInstruction(Instruction inst) {
    if (inst instanceof JVMArrayElementInstruction)
      return(getTypeFromInstruction((JVMArrayElementInstruction) inst));

    return(Types.T_VOID);
  }

  private final static byte decodeType(String type) {
    if (type.charAt(0) == '?'){
      return(Types.T_REFERENCE);
    } else {
      return Types.getBuiltinType(type);
    }
  }

  // Find the variable writer
  // It should be one of the apps listed in the .jpf file
  private String getWriter(List<StackFrame> sfList, HashSet<String> writerSet) {
    // Start looking from the top of the stack backward
    for(int i=sfList.size()-1; i>=0; i--) {
      MethodInfo mi = sfList.get(i).getMethodInfo();
      if(!mi.isJPFInternal()) {
        String method = mi.getStackTraceName();
        // Check against the writers in the writerSet
        for(String writer : writerSet) {
          if (method.contains(writer)) {
            return writer;
          }
        }
      }
    }

    return null;
  }

  private void writeWriterAndValue(String writer, String attribute, String value) {
    Update u = new Update(writer, "DEVICE", attribute, value, manual);
    currUpdates.add(u);
  }

  @Override
  public void instructionExecuted(VM vm, ThreadInfo ti, Instruction nextInsn, Instruction executedInsn) {
    if (timeout > 0) {
      if (System.currentTimeMillis() - startTime > timeout) {
        StringBuilder sbTimeOut = new StringBuilder();
        sbTimeOut.append("Execution timeout: " + (timeout / (60 * 1000)) + " minutes have passed!");
        Instruction nextIns = ti.createAndThrowException("java.lang.RuntimeException", sbTimeOut.toString());
        ti.setNextPC(nextIns);
      }
    }

    if (conflictFound) {

      StringBuilder sb = new StringBuilder();
      sb.append(errorMessage);
      Instruction nextIns = ti.createAndThrowException("java.lang.RuntimeException", sb.toString());
      ti.setNextPC(nextIns);
    } else {

      if (executedInsn instanceof WriteInstruction) {
        String varId = ((WriteInstruction) executedInsn).getFieldInfo().getFullName();
        // Check if we have an update to isManualTransaction to update manual field
        if (varId.contains("isManualTransaction")) {
          byte type = getType(ti, executedInsn);
          String value = getValue(ti, executedInsn, type);
          System.out.println();
          manual = (value.equals("true"))?true:false;
        }
        for (String var : conflictSet) {
          if (varId.contains(var)) {
            // Get variable info
            byte type = getType(ti, executedInsn);
            String value = getValue(ti, executedInsn, type);
            String writer = getWriter(ti.getStack(), appSet);

            // Just return if the writer is not one of the listed apps in the .jpf file
            if (writer == null)
              return;

            // Update the current updates
            writeWriterAndValue(writer, var, value);
          }
        }
      }
    }
  }
}
