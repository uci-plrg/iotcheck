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
import gov.nasa.jpf.vm.bytecode.ReadInstruction;
import gov.nasa.jpf.vm.bytecode.WriteInstruction;
import gov.nasa.jpf.vm.choice.IntChoiceFromSet;
import gov.nasa.jpf.vm.choice.IntIntervalGenerator;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;
import java.io.IOException;

// TODO: Fix for Groovy's model-checking
// TODO: This is a setter to change the values of the ChoiceGenerator to implement POR
/**
 * Simple tool to log state changes.
 *
 * This DPOR implementation is augmented by the algorithm presented in this SPIN paper:
 * http://spinroot.com/spin/symposia/ws08/spin2008_submission_33.pdf
 *
 * The algorithm is presented on page 11 of the paper. Basically, we have a graph G
 * (i.e., visible operation dependency graph).
 * This DPOR implementation actually fixes the algorithm in the SPIN paper that does not
 * consider cases where a state could be matched early. In this new algorithm/implementation,
 * each run is terminated iff:
 * - we find a state that matches a state in a previous run, or
 * - we have a matched state in the current run that consists of cycles that contain all choices/events.
 */
public class DPORStateReducer extends ListenerAdapter {

  // Information printout fields for verbose mode
  private boolean verboseMode;
  private boolean stateReductionMode;
  private final PrintWriter out;
  private PrintWriter fileWriter;
  private String detail;
  private int depth;
  private int id;
  private Transition transition;

  // DPOR-related fields
  // Basic information
  private Integer[] choices;
  private Integer[] refChoices; // Second reference to a copy of choices (choices may be modified for fair scheduling)
  private int choiceCounter;
  private int maxEventChoice;
  // Data structure to track the events seen by each state to track cycles (containing all events) for termination
  private HashSet<Integer> currVisitedStates; // States being visited in the current execution
  private HashSet<Integer> justVisitedStates; // States just visited in the previous choice/event
  private HashSet<Integer> prevVisitedStates; // States visited in the previous execution
  private HashMap<Integer, HashSet<Integer>> stateToEventMap;
  // Data structure to analyze field Read/Write accesses and conflicts
  private HashMap<Integer, LinkedList<BacktrackExecution>> backtrackMap;  // Track created backtracking points
  private PriorityQueue<Integer> backtrackStateQ;                 // Heap that returns the latest state
  private Execution currentExecution;                             // Holds the information about the current execution
  private HashSet<String> doneBacktrackSet;                       // Record state ID and trace already constructed
  private HashMap<Integer, RestorableVMState> restorableStateMap; // Maps state IDs to the restorable state object
  private HashMap<Integer, Integer> stateToChoiceCounterMap;      // Maps state IDs to the choice counter
  private HashMap<Integer, ArrayList<Execution>> rGraph;          // Create a reachability graph for past executions

  // Boolean states
  private boolean isBooleanCGFlipped;
  private boolean isEndOfExecution;

  // Statistics
  private int numOfConflicts;
  private int numOfTransitions;
	
  public DPORStateReducer(Config config, JPF jpf) {
    verboseMode = config.getBoolean("printout_state_transition", false);
    stateReductionMode = config.getBoolean("activate_state_reduction", true);
    if (verboseMode) {
      out = new PrintWriter(System.out, true);
    } else {
      out = null;
    }
    String outputFile = config.getString("file_output");
    if (!outputFile.isEmpty()) {
      try {
        fileWriter = new PrintWriter(new FileWriter(outputFile, true), true);
      } catch (IOException e) {
      }
    }
    isBooleanCGFlipped = false;
		numOfConflicts = 0;
		numOfTransitions = 0;
    restorableStateMap = new HashMap<>();
    initializeStatesVariables();
  }

  @Override
  public void stateRestored(Search search) {
    if (verboseMode) {
      id = search.getStateId();
      depth = search.getDepth();
      transition = search.getTransition();
      detail = null;
      out.println("\n==> DEBUG: The state is restored to state with id: " + id + " -- Transition: " + transition +
              " and depth: " + depth + "\n");
    }
  }

  @Override
  public void searchStarted(Search search) {
    if (verboseMode) {
      out.println("\n==> DEBUG: ----------------------------------- search started" + "\n");
    }
  }

  @Override
  public void stateAdvanced(Search search) {
    if (verboseMode) {
      id = search.getStateId();
      depth = search.getDepth();
      transition = search.getTransition();
      if (search.isNewState()) {
        detail = "new";
      } else {
        detail = "visited";
      }

      if (search.isEndState()) {
        out.println("\n==> DEBUG: This is the last state!\n");
        detail += " end";
      }
      out.println("\n==> DEBUG: The state is forwarded to state with id: " + id + " with depth: " + depth +
              " which is " + detail + " Transition: " + transition + "\n");
    }
    if (stateReductionMode) {
      updateStateInfo(search);
    }
  }

  @Override
  public void stateBacktracked(Search search) {
    if (verboseMode) {
      id = search.getStateId();
      depth = search.getDepth();
      transition = search.getTransition();
      detail = null;

      out.println("\n==> DEBUG: The state is backtracked to state with id: " + id + " -- Transition: " + transition +
              " and depth: " + depth + "\n");
    }
    if (stateReductionMode) {
      updateStateInfo(search);
    }
  }

  static Logger log = JPF.getLogger("report");

  @Override
  public void searchFinished(Search search) {
    if (stateReductionMode) {
      // Number of conflicts = first trace + subsequent backtrack points
      numOfConflicts += 1 + doneBacktrackSet.size();
    }
    if (verboseMode) {
      out.println("\n==> DEBUG: ----------------------------------- search finished");
      out.println("\n==> DEBUG: State reduction mode  : " + stateReductionMode);
      out.println("\n==> DEBUG: Number of conflicts   : " + numOfConflicts);
      out.println("\n==> DEBUG: Number of transitions : " + numOfTransitions);
      out.println("\n==> DEBUG: ----------------------------------- search finished" + "\n");

      fileWriter.println("==> DEBUG: State reduction mode  : " + stateReductionMode);
      fileWriter.println("==> DEBUG: Number of conflicts   : " + numOfConflicts);
      fileWriter.println("==> DEBUG: Number of transitions : " + numOfTransitions);
      fileWriter.println();
      fileWriter.close();
    }
  }

  @Override
  public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
    if (stateReductionMode) {
      // Initialize with necessary information from the CG
      if (nextCG instanceof IntChoiceFromSet) {
        IntChoiceFromSet icsCG = (IntChoiceFromSet) nextCG;
        if (!isEndOfExecution) {
          // Check if CG has been initialized, otherwise initialize it
          Integer[] cgChoices = icsCG.getAllChoices();
          // Record the events (from choices)
          if (choices == null) {
            choices = cgChoices;
            // Make a copy of choices as reference
            refChoices = copyChoices(choices);
            // Record the max event choice (the last element of the choice array)
            maxEventChoice = choices[choices.length - 1];
          }
          icsCG.setNewValues(choices);
          icsCG.reset();
          // Use a modulo since choiceCounter is going to keep increasing
          int choiceIndex = choiceCounter % choices.length;
          icsCG.advance(choices[choiceIndex]);
        } else {
          // Set done all CGs while transitioning to a new execution
          icsCG.setDone();
        }
      }
    }
  }

  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {

    if (stateReductionMode) {
      // Check the boolean CG and if it is flipped, we are resetting the analysis
      if (currentCG instanceof BooleanChoiceGenerator) {
        if (!isBooleanCGFlipped) {
          isBooleanCGFlipped = true;
        } else {
          // Number of conflicts = first trace + subsequent backtrack points
          numOfConflicts = 1 + doneBacktrackSet.size();
          // Allocate new objects for data structure when the boolean is flipped from "false" to "true"
          initializeStatesVariables();
        }
      }
      // Check every choice generated and ensure fair scheduling!
      if (currentCG instanceof IntChoiceFromSet) {
        IntChoiceFromSet icsCG = (IntChoiceFromSet) currentCG;
        // If this is a new CG then we need to update data structures
        resetStatesForNewExecution(icsCG, vm);
        // If we don't see a fair scheduling of events/choices then we have to enforce it
        fairSchedulingAndBacktrackPoint(icsCG, vm);
        // Explore the next backtrack point: 
        // 1) if we have seen this state or this state contains cycles that involve all events, and
        // 2) after the current CG is advanced at least once
        if (terminateCurrentExecution() && choiceCounter > 0) {
          exploreNextBacktrackPoints(vm, icsCG);
        } else {
          numOfTransitions++;
        }
        // Map state to event
        mapStateToEvent(icsCG.getNextChoice());
        justVisitedStates.clear();
        choiceCounter++;
      }
    } else {
      numOfTransitions++;
    }
  }

  @Override
  public void instructionExecuted(VM vm, ThreadInfo ti, Instruction nextInsn, Instruction executedInsn) {
    if (stateReductionMode) {
      if (!isEndOfExecution) {
        // Has to be initialized and a integer CG
        ChoiceGenerator<?> cg = vm.getChoiceGenerator();
        if (cg instanceof IntChoiceFromSet || cg instanceof IntIntervalGenerator) {
          int currentChoice = choiceCounter - 1;  // Accumulative choice w.r.t the current trace
          if (currentChoice < 0) { // If choice is -1 then skip
            return;
          }
          currentChoice = checkAndAdjustChoice(currentChoice, vm);
          // Record accesses from executed instructions
          if (executedInsn instanceof JVMFieldInstruction) {
            // Analyze only after being initialized
            String fieldClass = ((JVMFieldInstruction) executedInsn).getFieldInfo().getFullName();
            // We don't care about libraries
            if (!isFieldExcluded(fieldClass)) {
              analyzeReadWriteAccesses(executedInsn, fieldClass, currentChoice);
            }
          } else if (executedInsn instanceof INVOKEINTERFACE) {
            // Handle the read/write accesses that occur through iterators
            analyzeReadWriteAccesses(executedInsn, ti, currentChoice);
          }
          // Analyze conflicts from next instructions
          if (nextInsn instanceof JVMFieldInstruction) {
            // Skip the constructor because it is called once and does not have shared access with other objects
            if (!nextInsn.getMethodInfo().getName().equals("<init>")) {
              String fieldClass = ((JVMFieldInstruction) nextInsn).getFieldInfo().getFullName();
              if (!isFieldExcluded(fieldClass)) {
                findFirstConflictAndCreateBacktrackPoint(currentChoice, nextInsn, fieldClass);
              }
            }
          }
        }
      }
    }
  }


  // == HELPERS

  // -- INNER CLASSES

  // This class compactly stores backtrack execution:
  // 1) backtrack choice list, and
  // 2) backtrack execution
  private class BacktrackExecution {
    private Integer[] choiceList;
    private Execution execution;

    public BacktrackExecution(Integer[] choList, Execution exec) {
      choiceList = choList;
      execution = exec;
    }

    public Integer[] getChoiceList() {
      return choiceList;
    }

    public Execution getExecution() {
      return execution;
    }
  }

  // This class compactly stores backtrack points:
  // 1) backtrack state ID, and
  // 2) backtracking choices
  private class BacktrackPoint {
    private IntChoiceFromSet backtrackCG; // CG at this backtrack point
    private int stateId;                  // State at this backtrack point
    private int choice;                   // Choice chosen at this backtrack point

    public BacktrackPoint(IntChoiceFromSet cg, int stId, int cho) {
      backtrackCG = cg;
      stateId = stId;
      choice = cho;
    }

    public IntChoiceFromSet getBacktrackCG() { return backtrackCG; }

    public int getStateId() {
      return stateId;
    }

    public int getChoice() {
      return choice;
    }
  }

  // This class stores a representation of the execution graph node
  private class Execution {
    private HashMap<IntChoiceFromSet, Integer> cgToChoiceMap;   // Map between CG to choice numbers for O(1) access
    private ArrayList<BacktrackPoint> executionTrace;           // The BacktrackPoint objects of this execution
    private int parentChoice;                                   // The parent's choice that leads to this execution
    private Execution parent;                                   // Store the parent for backward DFS to find conflicts
    private HashMap<Integer, ReadWriteSet> readWriteFieldsMap;  // Record fields that are accessed

    public Execution() {
      cgToChoiceMap = new HashMap<>();
      executionTrace = new ArrayList<>();
      parentChoice = -1;
      parent = null;
      readWriteFieldsMap = new HashMap<>();
    }

    public void addBacktrackPoint(BacktrackPoint newBacktrackPoint) {
      executionTrace.add(newBacktrackPoint);
    }

    public void clearCGToChoiceMap() {
      cgToChoiceMap = null;
    }

    public ArrayList<BacktrackPoint> getExecutionTrace() {
      return executionTrace;
    }

    public int getChoiceFromCG(IntChoiceFromSet icsCG) {
      return cgToChoiceMap.get(icsCG);
    }

    public int getParentChoice() {
      return parentChoice;
    }

    public Execution getParent() {
      return parent;
    }

    public HashMap<Integer, ReadWriteSet> getReadWriteFieldsMap() {
      return readWriteFieldsMap;
    }

    public void mapCGToChoice(IntChoiceFromSet icsCG, int choice) {
      cgToChoiceMap.put(icsCG, choice);
    }

    public void setParentChoice(int parChoice) {
      parentChoice = parChoice;
    }

    public void setParent(Execution par) {
      parent = par;
    }
  }

  // This class compactly stores Read and Write field sets
  // We store the field name and its object ID
  // Sharing the same field means the same field name and object ID
  private class ReadWriteSet {
    private HashMap<String, Integer> readSet;
    private HashMap<String, Integer> writeSet;

    public ReadWriteSet() {
      readSet = new HashMap<>();
      writeSet = new HashMap<>();
    }

    public void addReadField(String field, int objectId) {
      readSet.put(field, objectId);
    }

    public void addWriteField(String field, int objectId) {
      writeSet.put(field, objectId);
    }

    public Set<String> getReadSet() {
      return readSet.keySet();
    }

    public Set<String> getWriteSet() {
      return writeSet.keySet();
    }

    public boolean readFieldExists(String field) {
      return readSet.containsKey(field);
    }

    public boolean writeFieldExists(String field) {
      return writeSet.containsKey(field);
    }

    public int readFieldObjectId(String field) {
      return readSet.get(field);
    }

    public int writeFieldObjectId(String field) {
      return writeSet.get(field);
    }
  }

  // -- CONSTANTS
  private final static String DO_CALL_METHOD = "doCall";
  // We exclude fields that come from libraries (Java and Groovy), and also the infrastructure
  private final static String[] EXCLUDED_FIELDS_CONTAINS_LIST = {"_closure"};
  private final static String[] EXCLUDED_FIELDS_ENDS_WITH_LIST =
          // Groovy library created fields
          {"stMC", "callSiteArray", "metaClass", "staticClassInfo", "__constructor__",
          // Infrastructure
          "sendEvent", "Object", "reference", "location", "app", "state", "log", "functionList", "objectList",
          "eventList", "valueList", "settings", "printToConsole", "app1", "app2"};
  private final static String[] EXCLUDED_FIELDS_STARTS_WITH_LIST =
          // Java and Groovy libraries
          { "java", "org", "sun", "com", "gov", "groovy"};
  private final static String[] EXCLUDED_FIELDS_READ_WRITE_INSTRUCTIONS_STARTS_WITH_LIST = {"Event"};
  private final static String GET_PROPERTY_METHOD =
          "invokeinterface org.codehaus.groovy.runtime.callsite.CallSite.callGetProperty";
  private final static String GROOVY_CALLSITE_LIB = "org.codehaus.groovy.runtime.callsite";
  private final static String JAVA_INTEGER = "int";
  private final static String JAVA_STRING_LIB = "java.lang.String";

  // -- FUNCTIONS
  private void fairSchedulingAndBacktrackPoint(IntChoiceFromSet icsCG, VM vm) {
    // Check the next choice and if the value is not the same as the expected then force the expected value
    int choiceIndex = choiceCounter % refChoices.length;
    int nextChoice = icsCG.getNextChoice();
    if (refChoices[choiceIndex] != nextChoice) {
      int expectedChoice = refChoices[choiceIndex];
      int currCGIndex = icsCG.getNextChoiceIndex();
      if ((currCGIndex >= 0) && (currCGIndex < refChoices.length)) {
        icsCG.setChoice(currCGIndex, expectedChoice);
      }
    }
    // Record state ID and choice/event as backtrack point
    int stateId = vm.getStateId();
    currentExecution.addBacktrackPoint(new BacktrackPoint(icsCG, stateId, refChoices[choiceIndex]));
    currentExecution.mapCGToChoice(icsCG, choiceCounter);
    // Store restorable state object for this state (always store the latest)
    RestorableVMState restorableState = vm.getRestorableState();
    restorableStateMap.put(stateId, restorableState);
  }

  private Integer[] copyChoices(Integer[] choicesToCopy) {

    Integer[] copyOfChoices = new Integer[choicesToCopy.length];
    System.arraycopy(choicesToCopy, 0, copyOfChoices, 0, choicesToCopy.length);
    return copyOfChoices;
  }

  // --- Functions related to cycle detection and reachability graph

  // Detect cycles in the current execution/trace
  // We terminate the execution iff:
  // (1) the state has been visited in the current execution
  // (2) the state has one or more cycles that involve all the events
  // With simple approach we only need to check for a re-visited state.
  // Basically, we have to check that we have executed all events between two occurrences of such state.
  private boolean containsCyclesWithAllEvents(int stId) {

    // False if the state ID hasn't been recorded
    if (!stateToEventMap.containsKey(stId)) {
      return false;
    }
    HashSet<Integer> visitedEvents = stateToEventMap.get(stId);
    // Check if this set contains all the event choices
    // If not then this is not the terminating condition
    for(int i=0; i<=maxEventChoice; i++) {
      if (!visitedEvents.contains(i)) {
        return false;
      }
    }
    return true;
  }

  private void initializeStatesVariables() {
    // DPOR-related
    choices = null;
    refChoices = null;
    choiceCounter = 0;
    maxEventChoice = 0;
    // Cycle tracking
    currVisitedStates = new HashSet<>();
    justVisitedStates = new HashSet<>();
    prevVisitedStates = new HashSet<>();
    stateToEventMap = new HashMap<>();
    // Backtracking
    backtrackMap = new HashMap<>();
    backtrackStateQ = new PriorityQueue<>(Collections.reverseOrder());
    currentExecution = new Execution();
    doneBacktrackSet = new HashSet<>();
    stateToChoiceCounterMap = new HashMap<>();
    rGraph = new HashMap<>();
    // Booleans
    isEndOfExecution = false;
  }

  private void mapStateToEvent(int nextChoiceValue) {
    // Update all states with this event/choice
    // This means that all past states now see this transition
    Set<Integer> stateSet = stateToEventMap.keySet();
    for(Integer stateId : stateSet) {
      HashSet<Integer> eventSet = stateToEventMap.get(stateId);
      eventSet.add(nextChoiceValue);
    }
  }

  private void saveExecutionToRGraph(int stateId) {
    // Save execution state into the reachability graph only if
    // (1) It is not a revisited state from a past execution, or
    // (2) It is just a new backtracking point
    if (!prevVisitedStates.contains(stateId) ||
            choiceCounter <= 1) {
      ArrayList<Execution> reachableExecutions;
      if (!prevVisitedStates.contains(stateId)) {
        reachableExecutions = new ArrayList<>();
        rGraph.put(stateId, reachableExecutions);
      } else {
        reachableExecutions = rGraph.get(stateId);
      }
      reachableExecutions.add(currentExecution);
    }
  }

  private boolean terminateCurrentExecution() {
    // We need to check all the states that have just been visited
    // Often a transition (choice/event) can result into forwarding/backtracking to a number of states
    for(Integer stateId : justVisitedStates) {
      if (prevVisitedStates.contains(stateId) || containsCyclesWithAllEvents(stateId)) {
        return true;
      }
    }
    return false;
  }

  private void updateStateInfo(Search search) {
    // Update the state variables
    // Line 19 in the paper page 11 (see the heading note above)
    int stateId = search.getStateId();
    // Insert state ID into the map if it is new
    if (!stateToEventMap.containsKey(stateId)) {
      HashSet<Integer> eventSet = new HashSet<>();
      stateToEventMap.put(stateId, eventSet);
    }
    saveExecutionToRGraph(stateId);
    analyzeReachabilityAndCreateBacktrackPoints(search.getVM(), stateId);
    stateToChoiceCounterMap.put(stateId, choiceCounter);
    justVisitedStates.add(stateId);
    currVisitedStates.add(stateId);
  }

  // --- Functions related to Read/Write access analysis on shared fields

  private void addNewBacktrackPoint(int stateId, Integer[] newChoiceList, Execution parentExecution, int parentChoice) {
    // Insert backtrack point to the right state ID
    LinkedList<BacktrackExecution> backtrackExecList;
    if (backtrackMap.containsKey(stateId)) {
      backtrackExecList = backtrackMap.get(stateId);
    } else {
      backtrackExecList = new LinkedList<>();
      backtrackMap.put(stateId, backtrackExecList);
    }
    // Add the new backtrack execution object
    Execution newExecution = new Execution();
    newExecution.setParent(parentExecution);
    newExecution.setParentChoice(parentChoice);
    backtrackExecList.addFirst(new BacktrackExecution(newChoiceList, newExecution));
    // Add to priority queue
    if (!backtrackStateQ.contains(stateId)) {
      backtrackStateQ.add(stateId);
    }
  }

  // Analyze Read/Write accesses that are directly invoked on fields
  private void analyzeReadWriteAccesses(Instruction executedInsn, String fieldClass, int currentChoice) {
    // Do the analysis to get Read and Write accesses to fields
    ReadWriteSet rwSet = getReadWriteSet(currentChoice);
    int objectId = ((JVMFieldInstruction) executedInsn).getFieldInfo().getClassInfo().getClassObjectRef();
    // Record the field in the map
    if (executedInsn instanceof WriteInstruction) {
      // Exclude certain field writes because of infrastructure needs, e.g., Event class field writes
      for (String str : EXCLUDED_FIELDS_READ_WRITE_INSTRUCTIONS_STARTS_WITH_LIST) {
        if (fieldClass.startsWith(str)) {
          return;
        }
      }
      rwSet.addWriteField(fieldClass, objectId);
    } else if (executedInsn instanceof ReadInstruction) {
      rwSet.addReadField(fieldClass, objectId);
    }
  }

  // Analyze Read accesses that are indirect (performed through iterators)
  // These accesses are marked by certain bytecode instructions, e.g., INVOKEINTERFACE
  private void analyzeReadWriteAccesses(Instruction instruction, ThreadInfo ti, int currentChoice) {
    // Get method name
    INVOKEINTERFACE insn = (INVOKEINTERFACE) instruction;
    if (insn.toString().startsWith(GET_PROPERTY_METHOD) &&
            insn.getMethodInfo().getName().equals(DO_CALL_METHOD)) {
      // Extract info from the stack frame
      StackFrame frame = ti.getTopFrame();
      int[] frameSlots = frame.getSlots();
      // Get the Groovy callsite library at index 0
      ElementInfo eiCallsite = VM.getVM().getHeap().get(frameSlots[0]);
      if (!eiCallsite.getClassInfo().getName().startsWith(GROOVY_CALLSITE_LIB)) {
        return;
      }
      // Get the iterated object whose property is accessed
      ElementInfo eiAccessObj = VM.getVM().getHeap().get(frameSlots[1]);
      if (eiAccessObj == null) {
        return;
      }
      // We exclude library classes (they start with java, org, etc.) and some more
      String objClassName = eiAccessObj.getClassInfo().getName();
      if (excludeThisForItStartsWith(EXCLUDED_FIELDS_STARTS_WITH_LIST, objClassName) ||
          excludeThisForItStartsWith(EXCLUDED_FIELDS_READ_WRITE_INSTRUCTIONS_STARTS_WITH_LIST, objClassName)) {
        return;
      }
      // Extract fields from this object and put them into the read write
      int numOfFields = eiAccessObj.getNumberOfFields();
      for(int i=0; i<numOfFields; i++) {
        FieldInfo fieldInfo = eiAccessObj.getFieldInfo(i);
        if (fieldInfo.getType().equals(JAVA_STRING_LIB) || fieldInfo.getType().equals(JAVA_INTEGER)) {
          String fieldClass = fieldInfo.getFullName();
          ReadWriteSet rwSet = getReadWriteSet(currentChoice);
          int objectId = fieldInfo.getClassInfo().getClassObjectRef();
          // Record the field in the map
          rwSet.addReadField(fieldClass, objectId);
        }
      }
    }
  }

  private int checkAndAdjustChoice(int currentChoice, VM vm) {
    // If current choice is not the same, then this is caused by the firing of IntIntervalGenerator
    // for certain method calls in the infrastructure, e.g., eventSince()
    ChoiceGenerator<?> currentCG = vm.getChoiceGenerator();
    // This is the main event CG
    if (currentCG instanceof IntIntervalGenerator) {
      // This is the interval CG used in device handlers
      ChoiceGenerator<?> parentCG = ((IntIntervalGenerator) currentCG).getPreviousChoiceGenerator();
      // Iterate until we find the IntChoiceFromSet CG
      while (!(parentCG instanceof IntChoiceFromSet)) {
        parentCG = ((IntIntervalGenerator) parentCG).getPreviousChoiceGenerator();
      }
      // Find the choice related to the IntIntervalGenerator CG from the map
      currentChoice = currentExecution.getChoiceFromCG((IntChoiceFromSet) parentCG);
    }
    return currentChoice;
  }

  private void createBacktrackingPoint(int backtrackChoice, int conflictChoice, Execution execution) {

    // Create a new list of choices for backtrack based on the current choice and conflicting event number
    // E.g. if we have a conflict between 1 and 3, then we create the list {3, 1, 0, 2}
    // for the original set {0, 1, 2, 3}
    Integer[] newChoiceList = new Integer[refChoices.length];
    //int firstChoice = choices[actualChoice];
    ArrayList<BacktrackPoint> pastTrace = execution.getExecutionTrace();
    ArrayList<BacktrackPoint> currTrace = currentExecution.getExecutionTrace();
    int btrackChoice = currTrace.get(backtrackChoice).getChoice();
    int stateId = pastTrace.get(conflictChoice).getStateId();
    // Check if this trace has been done from this state
    if (isTraceAlreadyConstructed(btrackChoice, stateId)) {
      return;
    }
    // Put the conflicting event numbers first and reverse the order
    newChoiceList[0] = btrackChoice;
    newChoiceList[1] = pastTrace.get(conflictChoice).getChoice();
    // Put the rest of the event numbers into the array starting from the minimum to the upper bound
    for (int i = 0, j = 2; i < refChoices.length; i++) {
      if (refChoices[i] != newChoiceList[0] && refChoices[i] != newChoiceList[1]) {
        newChoiceList[j] = refChoices[i];
        j++;
      }
    }
    // Parent choice is conflict choice - 1
    addNewBacktrackPoint(stateId, newChoiceList, execution, conflictChoice - 1);
  }

  private boolean excludeThisForItContains(String[] excludedStrings, String className) {
    for (String excludedField : excludedStrings) {
      if (className.contains(excludedField)) {
        return true;
      }
    }
    return false;
  }

  private boolean excludeThisForItEndsWith(String[] excludedStrings, String className) {
    for (String excludedField : excludedStrings) {
      if (className.endsWith(excludedField)) {
        return true;
      }
    }
    return false;
  }

  private boolean excludeThisForItStartsWith(String[] excludedStrings, String className) {
    for (String excludedField : excludedStrings) {
      if (className.startsWith(excludedField)) {
        return true;
      }
    }
    return false;
  }

  private void exploreNextBacktrackPoints(VM vm, IntChoiceFromSet icsCG) {

		// Check if we are reaching the end of our execution: no more backtracking points to explore
		// cgMap, backtrackMap, backtrackStateQ are updated simultaneously (checking backtrackStateQ is enough)
		if (!backtrackStateQ.isEmpty()) {
			// Set done all the other backtrack points
			for (BacktrackPoint backtrackPoint : currentExecution.getExecutionTrace()) {
				backtrackPoint.getBacktrackCG().setDone();
			}
			// Reset the next backtrack point with the latest state
			int hiStateId = backtrackStateQ.peek();
			// Restore the state first if necessary
			if (vm.getStateId() != hiStateId) {
				RestorableVMState restorableState = restorableStateMap.get(hiStateId);
				vm.restoreState(restorableState);
			}
			// Set the backtrack CG
			IntChoiceFromSet backtrackCG = (IntChoiceFromSet) vm.getChoiceGenerator();
			setBacktrackCG(hiStateId, backtrackCG);
		} else {
			// Set done this last CG (we save a few rounds)
			icsCG.setDone();
		}
		// Save all the visited states when starting a new execution of trace
		prevVisitedStates.addAll(currVisitedStates);
		// This marks a transitional period to the new CG
		isEndOfExecution = true;
  }

  private void findFirstConflictAndCreateBacktrackPoint(int currentChoice, Instruction nextInsn, String fieldClass) {
    // Check for conflict (go backward from current choice and get the first conflict)
    Execution execution = currentExecution;
    // Choice/event we want to check for conflict against (start from actual choice)
    int pastChoice = currentChoice;
    // Perform backward DFS through the execution graph
    while (true) {
      // Get the next conflict choice
      if (pastChoice > 0) {
        // Case #1: check against a previous choice in the same execution for conflict
        pastChoice = pastChoice - 1;
      } else { // pastChoice == 0 means we are at the first BacktrackPoint of this execution path
        // Case #2: check against a previous choice in a parent execution
        int parentChoice = execution.getParentChoice();
        if (parentChoice > -1) {
          // Get the parent execution
          execution = execution.getParent();
          pastChoice = execution.getParentChoice();
        } else {
          // If parent is -1 then this is the first execution (it has no parent) and we stop here
          break;
        }
      }
      // Check if a conflict is found
      if (isConflictFound(nextInsn, fieldClass, currentChoice, pastChoice, execution)) {
        createBacktrackingPoint(currentChoice, pastChoice, execution);
        break;  // Stop at the first found conflict
      }
    }
  }

  private boolean isConflictFound(Instruction nextInsn, String fieldClass, int currentChoice,
                                  int pastChoice, Execution pastExecution) {

    HashMap<Integer, ReadWriteSet> pastRWFieldsMap = pastExecution.getReadWriteFieldsMap();
    ArrayList<BacktrackPoint> pastTrace = pastExecution.getExecutionTrace();
    ArrayList<BacktrackPoint> currTrace = currentExecution.getExecutionTrace();
    // Skip if this event does not have any Read/Write set or the two events are basically the same event (number)
    if (!pastRWFieldsMap.containsKey(pastChoice) ||
            currTrace.get(currentChoice).getChoice() == pastTrace.get(pastChoice).getChoice()) {
      return false;
    }
    HashMap<Integer, ReadWriteSet> currRWFieldsMap = pastExecution.getReadWriteFieldsMap();
    ReadWriteSet rwSet = currRWFieldsMap.get(pastChoice);
    int currObjId = ((JVMFieldInstruction) nextInsn).getFieldInfo().getClassInfo().getClassObjectRef();
    // Check for conflicts with Write fields for both Read and Write instructions
    if (((nextInsn instanceof WriteInstruction || nextInsn instanceof ReadInstruction) &&
            rwSet.writeFieldExists(fieldClass) && rwSet.writeFieldObjectId(fieldClass) == currObjId) ||
            (nextInsn instanceof WriteInstruction && rwSet.readFieldExists(fieldClass) &&
                    rwSet.readFieldObjectId(fieldClass) == currObjId)) {
      return true;
    }
    return false;
  }

  private boolean isConflictFound(int reachableChoice, int conflictChoice, Execution execution) {

    ArrayList<BacktrackPoint> executionTrace = execution.getExecutionTrace();
    HashMap<Integer, ReadWriteSet> execRWFieldsMap = execution.getReadWriteFieldsMap();
    // Skip if this event does not have any Read/Write set or the two events are basically the same event (number)
    if (!execRWFieldsMap.containsKey(conflictChoice) ||
            executionTrace.get(reachableChoice).getChoice() == executionTrace.get(conflictChoice).getChoice()) {
      return false;
    }
    // Current R/W set
    ReadWriteSet currRWSet = execRWFieldsMap.get(reachableChoice);
    // R/W set of choice/event that may have a potential conflict
    ReadWriteSet evtRWSet = execRWFieldsMap.get(conflictChoice);
    // Check for conflicts with Read and Write fields for Write instructions
    Set<String> currWriteSet = currRWSet.getWriteSet();
    for(String writeField : currWriteSet) {
      int currObjId = currRWSet.writeFieldObjectId(writeField);
      if ((evtRWSet.readFieldExists(writeField) && evtRWSet.readFieldObjectId(writeField) == currObjId) ||
              (evtRWSet.writeFieldExists(writeField) && evtRWSet.writeFieldObjectId(writeField) == currObjId)) {
        return true;
      }
    }
    // Check for conflicts with Write fields for Read instructions
    Set<String> currReadSet = currRWSet.getReadSet();
    for(String readField : currReadSet) {
      int currObjId = currRWSet.readFieldObjectId(readField);
      if (evtRWSet.writeFieldExists(readField) && evtRWSet.writeFieldObjectId(readField) == currObjId) {
        return true;
      }
    }
    // Return false if no conflict is found
    return false;
  }

  private ReadWriteSet getReadWriteSet(int currentChoice) {
    // Do the analysis to get Read and Write accesses to fields
    ReadWriteSet rwSet;
    // We already have an entry
    HashMap<Integer, ReadWriteSet> currReadWriteFieldsMap = currentExecution.getReadWriteFieldsMap();
    if (currReadWriteFieldsMap.containsKey(currentChoice)) {
      rwSet = currReadWriteFieldsMap.get(currentChoice);
    } else { // We need to create a new entry
      rwSet = new ReadWriteSet();
      currReadWriteFieldsMap.put(currentChoice, rwSet);
    }
    return rwSet;
  }

  private boolean isFieldExcluded(String field) {
    // Check against "starts-with", "ends-with", and "contains" list
    if (excludeThisForItStartsWith(EXCLUDED_FIELDS_STARTS_WITH_LIST, field) ||
            excludeThisForItEndsWith(EXCLUDED_FIELDS_ENDS_WITH_LIST, field) ||
            excludeThisForItContains(EXCLUDED_FIELDS_CONTAINS_LIST, field)) {
      return true;
    }

    return false;
  }

  // Check if this trace is already constructed
  private boolean isTraceAlreadyConstructed(int firstChoice, int stateId) {
    // Concatenate state ID and only the first event in the string, e.g., "1:1 for the trace 10234 at state 1"
    // TODO: THIS IS AN OPTIMIZATION!
    // This is the optimized version because after we execute, e.g., the trace 1:10234, we don't need to try
    // another trace that starts with event 1 at state 1, e.g., the trace 1:13024
    // The second time this event 1 is explored, it will generate the same state as the first one
    StringBuilder sb = new StringBuilder();
    sb.append(stateId);
    sb.append(':');
    sb.append(firstChoice);
    // Check if the trace has been constructed as a backtrack point for this state
    if (doneBacktrackSet.contains(sb.toString())) {
      return true;
    }
    doneBacktrackSet.add(sb.toString());
    return false;
  }

  // Reset data structure for each new execution
  private void resetStatesForNewExecution(IntChoiceFromSet icsCG, VM vm) {
    if (choices == null || choices != icsCG.getAllChoices()) {
      // Reset state variables
      choiceCounter = 0;
      choices = icsCG.getAllChoices();
      refChoices = copyChoices(choices);
      // Clear data structures
      currVisitedStates = new HashSet<>();
      stateToChoiceCounterMap = new HashMap<>();
      stateToEventMap = new HashMap<>();
      isEndOfExecution = false;
    }
  }

  // Set a backtrack point for a particular state
  private void setBacktrackCG(int stateId, IntChoiceFromSet backtrackCG) {
    // Set a backtrack CG based on a state ID
    LinkedList<BacktrackExecution> backtrackExecutions = backtrackMap.get(stateId);
    BacktrackExecution backtrackExecution = backtrackExecutions.removeLast();
    backtrackCG.setNewValues(backtrackExecution.getChoiceList());  // Get the last from the queue
    backtrackCG.setStateId(stateId);
    backtrackCG.reset();
    // Update current execution with this new execution
    Execution newExecution = backtrackExecution.getExecution();
    if (newExecution.getParentChoice() == -1) {
      // If it is -1 then that means we should start from the end of the parent trace for backward DFS
      ArrayList<BacktrackPoint> parentTrace = newExecution.getParent().getExecutionTrace();
      newExecution.setParentChoice(parentTrace.size() - 1);
    }
    // Try to free some memory since this map is only used for the current execution
    currentExecution.clearCGToChoiceMap();
    currentExecution = newExecution;
    // Remove from the queue if we don't have more backtrack points for that state
    if (backtrackExecutions.isEmpty()) {
      backtrackMap.remove(stateId);
      backtrackStateQ.remove(stateId);
    }
  }

  // --- Functions related to the reachability analysis when there is a state match

  // TODO: OPTIMIZATION!
  // Check and make sure that state ID and choice haven't been explored for this trace
  private boolean isAlreadyChecked(HashSet<String> checkedStateIdAndChoice, BacktrackPoint backtrackPoint) {
    int stateId = backtrackPoint.getStateId();
    int choice = backtrackPoint.getChoice();
    StringBuilder sb = new StringBuilder();
    sb.append(stateId);
    sb.append(':');
    sb.append(choice);
    // Check if the trace has been constructed as a backtrack point for this state
    if (checkedStateIdAndChoice.contains(sb.toString())) {
      return true;
    }
    checkedStateIdAndChoice.add(sb.toString());
    return false;
  }

  // We use backtrackPointsList to analyze the reachable states/events when there is a state match:
  // 1) Whenever there is state match, there is a cycle of events
  // 2) We need to analyze and find conflicts for the reachable choices/events in the cycle
  // 3) Then we create a new backtrack point for every new conflict
  private void analyzeReachabilityAndCreateBacktrackPoints(VM vm, int stateId) {
    // Perform this analysis only when:
    // 1) there is a state match,
    // 2) this is not during a switch to a new execution,
    // 3) at least 2 choices/events have been explored (choiceCounter > 1),
    // 4) the matched state has been encountered in the current execution, and
    // 5) state > 0 (state 0 is for boolean CG)
    if (!vm.isNewState() && !isEndOfExecution && choiceCounter > 1 && (stateId > 0)) {
      if (currVisitedStates.contains(stateId)) {
        // Update the backtrack sets in the cycle
        updateBacktrackSetsInCycle(stateId);
      } else if (prevVisitedStates.contains(stateId)) { // We visit a state in a previous execution
        // Update the backtrack sets in a previous execution
        updateBacktrackSetsInPreviousExecutions(stateId);
      }
    }
  }

  // Get the start event for the past execution trace when there is a state matched from a past execution
  private int getPastConflictChoice(int stateId, ArrayList<BacktrackPoint> pastBacktrackPointList) {
    // Iterate and find the first occurrence of the state ID
    // It is guaranteed that a choice should be found because the state ID is in the list
    int pastConfChoice = 0;
    for(int i = 0; i<pastBacktrackPointList.size(); i++) {
      BacktrackPoint backtrackPoint = pastBacktrackPointList.get(i);
      int stId = backtrackPoint.getStateId();
      if (stId == stateId) {
        pastConfChoice = i;
        break;
      }
    }
    return pastConfChoice;
  }

  // Get a sorted list of reachable state IDs starting from the input stateId
  private ArrayList<Integer> getReachableStateIds(Set<Integer> stateIds, int stateId) {
    // Only include state IDs equal or greater than the input stateId: these are reachable states
    ArrayList<Integer> sortedStateIds = new ArrayList<>();
    for(Integer stId : stateIds) {
      if (stId >= stateId) {
        sortedStateIds.add(stId);
      }
    }
    Collections.sort(sortedStateIds);
    return sortedStateIds;
  }

  // Update the backtrack sets in the cycle
  private void updateBacktrackSetsInCycle(int stateId) {
    // Find the choice/event that marks the start of this cycle: first choice we explore for conflicts
    int reachableChoice = stateToChoiceCounterMap.get(stateId);
    int cycleEndChoice = choiceCounter - 1;
    // Find conflicts between choices/events in this cycle (we scan forward in the cycle, not backward)
    while (reachableChoice < cycleEndChoice) {
      for (int conflictChoice = reachableChoice + 1; conflictChoice <= cycleEndChoice; conflictChoice++) {
        if (isConflictFound(reachableChoice, conflictChoice, currentExecution)) {
          createBacktrackingPoint(reachableChoice, conflictChoice, currentExecution);
        }
      }
      reachableChoice++;
    }
  }

  // Update the backtrack sets in a previous execution
  private void updateBacktrackSetsInPreviousExecution(Execution rExecution, int stateId,
                                                      HashSet<String> checkedStateIdAndChoice) {
    // Find the choice/event that marks the start of the subtrace from the previous execution
    ArrayList<BacktrackPoint> pastExecutionTrace = rExecution.getExecutionTrace();
    HashMap<Integer, ReadWriteSet> pastReadWriteFieldsMap = rExecution.getReadWriteFieldsMap();
    int pastConfChoice = getPastConflictChoice(stateId, pastExecutionTrace);
    int reachableChoice = choiceCounter;
    // Iterate from the starting point until the end of the past execution trace
    while (pastConfChoice < pastExecutionTrace.size() - 1) {  // BacktrackPoint list always has a surplus of 1
      // Get the info of the event from the past execution trace
      BacktrackPoint confBtrackPoint = pastExecutionTrace.get(pastConfChoice);
      if (!isAlreadyChecked(checkedStateIdAndChoice, confBtrackPoint)) {
        ReadWriteSet rwSet = pastReadWriteFieldsMap.get(pastConfChoice);
        // Append this event to the current list and map
        ArrayList<BacktrackPoint> currentTrace = currentExecution.getExecutionTrace();
        HashMap<Integer, ReadWriteSet> currRWFieldsMap = currentExecution.getReadWriteFieldsMap();
        currentTrace.add(confBtrackPoint);
        currRWFieldsMap.put(choiceCounter, rwSet);
        for (int conflictChoice = reachableChoice - 1; conflictChoice >= 0; conflictChoice--) {
          if (isConflictFound(reachableChoice, conflictChoice, currentExecution)) {
            createBacktrackingPoint(reachableChoice, conflictChoice, currentExecution);
          }
        }
        // Remove this event to replace it with a new one
        currentTrace.remove(currentTrace.size() - 1);
        currRWFieldsMap.remove(choiceCounter);
      }
      pastConfChoice++;
    }
  }

  // Update the backtrack sets in previous executions
  private void updateBacktrackSetsInPreviousExecutions(int stateId) {
    // Don't check a past trace twice!
    HashSet<Execution> checkedTrace = new HashSet<>();
    // Don't check the same event twice for a revisited state
    HashSet<String> checkedStateIdAndChoice = new HashSet<>();
    // Get sorted reachable state IDs
    ArrayList<Integer> reachableStateIds = getReachableStateIds(rGraph.keySet(), stateId);
    // Iterate from this state ID until the biggest state ID
    for(Integer stId : reachableStateIds) {
      // Find the right reachability graph object that contains the stateId
      ArrayList<Execution> rExecutions = rGraph.get(stId);
      for (Execution rExecution : rExecutions) {
        if (!checkedTrace.contains(rExecution)) {
          updateBacktrackSetsInPreviousExecution(rExecution, stateId, checkedStateIdAndChoice);
          checkedTrace.add(rExecution);
        }
      }
    }
  }
}
