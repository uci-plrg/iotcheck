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

import java.io.PrintWriter;
import java.util.*;

// TODO: Fix for Groovy's model-checking
// TODO: This is a setter to change the values of the ChoiceGenerator to implement POR
/**
 * Simple tool to log state changes.
 *
 * This DPOR implementation is augmented by the algorithm presented in this SPIN paper:
 * http://spinroot.com/spin/symposia/ws08/spin2008_submission_33.pdf
 *
 * The algorithm is presented on page 11 of the paper. Basically, we create a graph G
 * (i.e., visible operation dependency graph)
 * that maps inter-related threads/sub-programs that trigger state changes.
 * The key to this approach is that we evaluate graph G in every iteration/recursion to
 * only update the backtrack sets of the threads/sub-programs that are reachable in graph G
 * from the currently running thread/sub-program.
 */
public class StateReducer extends ListenerAdapter {

  // Debug info fields
  private boolean debugMode;
  private boolean stateReductionMode;
  private final PrintWriter out;
  private String detail;
  private int depth;
  private int id;
  private Transition transition;

  // State reduction fields
  private Integer[] choices;
  private Integer[] refChoices;
  private IntChoiceFromSet currCG;
  private int choiceCounter;
  private Integer choiceUpperBound;
  private Integer maxUpperBound;
  private boolean isInitialized;
  private boolean isResetAfterAnalysis;
  private boolean isBooleanCGFlipped;
  private HashMap<IntChoiceFromSet, Integer> cgMap;
  // Record the mapping between event number and field accesses (Read and Write)
  private HashMap<Integer, ReadWriteSet> readWriteFieldsMap;
  // The following is the backtrack map (set) that stores all the backtrack information
  // e.g., event number 1 can have two backtrack sequences: {3,1,2,4,...} and {2,1,3,4,...}
  private HashMap<Integer, LinkedList<Integer[]>> backtrackMap;
  // Stores explored backtrack lists in the form of HashSet of Strings
  private HashSet<String> backtrackSet;
  private HashMap<Integer, HashSet<Integer>> conflictPairMap;

  // Map that represents graph G
  // (i.e., visible operation dependency graph (VOD Graph)
  private HashMap<Integer, HashSet<Integer>> vodGraphMap;
  // Set that represents hash table H
  // (i.e., hash table that records encountered states)
  // VOD graph is updated when the state has not yet been seen
  // Current state
  private HashSet<Integer> justVisitedStates;
  // Previous choice number
  private int prevChoiceValue;
  // HashSet that stores references to unused CGs
  private HashSet<IntChoiceFromSet> unusedCG;

  //private HashMap<Integer, ConflictTracker.Node> stateGraph;
  private HashMap<Integer, HashSet<Integer>> stateToEventMap;
  // Map state to event
  // Visited states in the previous and current executions/traces for terminating condition
  private HashSet<Integer> prevVisitedStates;
  private HashSet<Integer> currVisitedStates;

  public StateReducer(Config config, JPF jpf) {
    debugMode = config.getBoolean("debug_state_transition", false);
    stateReductionMode = config.getBoolean("activate_state_reduction", true);
    if (debugMode) {
      out = new PrintWriter(System.out, true);
    } else {
      out = null;
    }
    detail = null;
    depth = 0;
    id = 0;
    transition = null;
    isBooleanCGFlipped = false;
    vodGraphMap = new HashMap<>();
    justVisitedStates = new HashSet<>();
    prevChoiceValue = -1;
    cgMap = new HashMap<>();
    readWriteFieldsMap = new HashMap<>();
    backtrackMap = new HashMap<>();
    backtrackSet = new HashSet<>();
    conflictPairMap = new HashMap<>();
    unusedCG = new HashSet<>();
    stateToEventMap = new HashMap<>();
    prevVisitedStates = new HashSet<>();
    currVisitedStates = new HashSet<>();
    initializeStateReduction();
  }

  private void initializeStateReduction() {
    if (stateReductionMode) {
      choices = null;
      refChoices = null;
      currCG = null;
      choiceCounter = 0;
      choiceUpperBound = 0;
      maxUpperBound = 0;
      isInitialized = false;
      isResetAfterAnalysis = false;
      cgMap.clear();
      resetReadWriteAnalysis();
      backtrackMap.clear();
      backtrackSet.clear();
      stateToEventMap.clear();
      prevVisitedStates.clear();
      currVisitedStates.clear();
    }
  }

  @Override
  public void stateRestored(Search search) {
    if (debugMode) {
      id = search.getStateId();
      depth = search.getDepth();
      transition = search.getTransition();
      detail = null;
      out.println("\n==> DEBUG: The state is restored to state with id: " + id + " -- Transition: " + transition +
              " and depth: " + depth + "\n");
    }
  }

  //--- the ones we are interested in
  @Override
  public void searchStarted(Search search) {
    if (debugMode) {
      out.println("\n==> DEBUG: ----------------------------------- search started" + "\n");
    }
  }

  private void resetReadWriteAnalysis() {
    // Reset the following data structure when the choice counter reaches 0 again
    conflictPairMap.clear();
    readWriteFieldsMap.clear();
  }

  private IntChoiceFromSet setNewCG(IntChoiceFromSet icsCG) {
    icsCG.setNewValues(choices);
    icsCG.reset();
    // Use a modulo since choiceCounter is going to keep increasing
    int choiceIndex = choiceCounter % choices.length;
    icsCG.advance(choices[choiceIndex]);
    if (choiceIndex == 0) {
      resetReadWriteAnalysis();
    }
    return icsCG;
  }

  private Integer[] copyChoices(Integer[] choicesToCopy) {

    Integer[] copyOfChoices = new Integer[choicesToCopy.length];
    System.arraycopy(choicesToCopy, 0, copyOfChoices, 0, choicesToCopy.length);
    return copyOfChoices;
  }

  private void continueExecutingThisTrace(IntChoiceFromSet icsCG) {
      // We repeat the same trace if a state match is not found yet
      IntChoiceFromSet setCG = setNewCG(icsCG);
      unusedCG.add(setCG);
  }

  private void initializeChoiceGenerators(IntChoiceFromSet icsCG, Integer[] cgChoices) {
    if (choiceCounter <= choiceUpperBound && !cgMap.containsValue(choiceCounter)) {
      // Update the choices of the first CG and add '-1'
      if (choices == null) {
        // Initialize backtrack set that stores all the explored backtrack lists
        maxUpperBound = cgChoices.length;
        // All the choices are always the same so we only need to update it once
        // Get the choice array and final choice in the array
        choices = cgChoices;
        // Make a copy of choices as reference
        refChoices = copyChoices(choices);
        String firstChoiceListString = buildStringFromChoiceList(choices);
        backtrackSet.add(firstChoiceListString);
      }
      IntChoiceFromSet setCG = setNewCG(icsCG);
      cgMap.put(setCG, refChoices[choiceCounter]);
    } else {
      continueExecutingThisTrace(icsCG);
    }
  }

  private void manageChoiceGeneratorsInSubsequentTraces(IntChoiceFromSet icsCG) {
    // If this is the first iteration of the trace then set other CGs done
    if (choiceCounter <= choiceUpperBound) {
      icsCG.setDone();
    } else {
      // If this is the subsequent iterations of the trace then set up new CGs to continue the execution
      continueExecutingThisTrace(icsCG);
    }
  }

  @Override
  public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
    if (stateReductionMode) {
      // Initialize with necessary information from the CG
      if (nextCG instanceof IntChoiceFromSet) {
        IntChoiceFromSet icsCG = (IntChoiceFromSet) nextCG;
        // Check if CG has been initialized, otherwise initialize it
        Integer[] cgChoices = icsCG.getAllChoices();
        if (!isInitialized) {
          // Get the upper bound from the last element of the choices
          choiceUpperBound = cgChoices[cgChoices.length - 1];
          isInitialized = true;
        }
        // Record the subsequent Integer CGs only until we hit the upper bound
        if (!isResetAfterAnalysis) {
          initializeChoiceGenerators(icsCG, cgChoices);
        } else {
          // Set new CGs to done so that the search algorithm explores the existing CGs
          //icsCG.setDone();
          manageChoiceGeneratorsInSubsequentTraces(icsCG);
        }
      }
    }
  }

  private void setDoneUnusedCG() {
    // Set done every CG in the unused CG set
    for (IntChoiceFromSet cg : unusedCG) {
      cg.setDone();
    }
    unusedCG.clear();
  }

  private void resetAllCGs() {

    isResetAfterAnalysis = true;
    // Extract the event numbers that have backtrack lists
    Set<Integer> eventSet = backtrackMap.keySet();
    // Return if there is no conflict at all (highly unlikely)
    if (eventSet.isEmpty()) {
      // Set every CG to done!
      for (IntChoiceFromSet cg : cgMap.keySet()) {
        cg.setDone();
      }
      return;
    }
    // Reset every CG with the first backtrack lists
    for (IntChoiceFromSet cg : cgMap.keySet()) {
      int event = cgMap.get(cg);
      LinkedList<Integer[]> choiceLists = backtrackMap.get(event);
      if (choiceLists != null && choiceLists.peekFirst() != null) {
        Integer[] choiceList = choiceLists.removeFirst();
        // Deploy the new choice list for this CG
        cg.setNewValues(choiceList);
        cg.reset();
      } else {
        cg.setDone();
      }
    }
    setDoneUnusedCG();
    saveVisitedStates();
  }

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
    boolean containsCyclesWithAllEvts = false;
    if (checkIfAllEventsInvolved(visitedEvents)) {
      containsCyclesWithAllEvts = true;
    }

    return containsCyclesWithAllEvts;
  }

  private boolean checkIfAllEventsInvolved(HashSet<Integer> visitedEvents) {

    // Check if this set contains all the event choices
    // If not then this is not the terminating condition
    for(int i=0; i<=choiceUpperBound; i++) {
      if (!visitedEvents.contains(i)) {
        return false;
      }
    }
    return true;
  }

  private void saveVisitedStates() {
    // CG is being reset
    // Save all the visited states
    prevVisitedStates.addAll(currVisitedStates);
    currVisitedStates.clear();
  }

  private void updateChoicesForNewExecution(IntChoiceFromSet icsCG) {
    if (choices == null || choices != icsCG.getAllChoices()) {
      currCG = icsCG;
      choices = icsCG.getAllChoices();
      refChoices = copyChoices(choices);
      // Reset a few things for the sub-graph
      resetReadWriteAnalysis();
      choiceCounter = 0;
    }
  }

  private void checkAndEnforceFairScheduling(IntChoiceFromSet icsCG) {
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

  private void updateVODGraph(int currChoiceValue) {
    // Update the graph when we have the current choice value
    HashSet<Integer> choiceSet;
    if (vodGraphMap.containsKey(prevChoiceValue)) {
      // If the key already exists, just retrieve it
      choiceSet = vodGraphMap.get(prevChoiceValue);
    } else {
      // Create a new entry
      choiceSet = new HashSet<>();
      vodGraphMap.put(prevChoiceValue, choiceSet);
    }
    choiceSet.add(currChoiceValue);
    prevChoiceValue = currChoiceValue;
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

  private void exploreNextBacktrackSets(IntChoiceFromSet icsCG) {
    // Traverse the sub-graphs
    if (isResetAfterAnalysis) {
      // Do this for every CG after finishing each backtrack list
      // We try to update the CG with a backtrack list if the state has been visited multiple times
      if (icsCG.getNextChoiceIndex() > 0 && cgMap.containsKey(icsCG)) {
        int event = cgMap.get(icsCG);
        LinkedList<Integer[]> choiceLists = backtrackMap.get(event);
        if (choiceLists != null && choiceLists.peekFirst() != null) {
          Integer[] choiceList = choiceLists.removeFirst();
          // Deploy the new choice list for this CG
          icsCG.setNewValues(choiceList);
          icsCG.reset();
        } else {
          // Set done if this was the last backtrack list
          icsCG.setDone();
        }
        setDoneUnusedCG();
        saveVisitedStates();
      }
    } else {
      // Update and reset the CG if needed (do this for the first time after the analysis)
      // Start backtracking if this is a visited state and it is not a repeating state
      resetAllCGs();
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
          initializeStateReduction();
        }
      }
      // Check every choice generated and make sure that all the available choices
      // are chosen first before repeating the same choice of value twice!
      if (currentCG instanceof IntChoiceFromSet) {
        IntChoiceFromSet icsCG = (IntChoiceFromSet) currentCG;
        // Update the current pointer to the current set of choices
        updateChoicesForNewExecution(icsCG);
        // If we don't see a fair scheduling of events/choices then we have to enforce it
        checkAndEnforceFairScheduling(icsCG);
        // Map state to event
        mapStateToEvent(icsCG.getNextChoice());
        // Update the VOD graph always with the latest
        updateVODGraph(icsCG.getNextChoice());
        // Check if we have seen this state or this state contains cycles that involve all events
        if (terminateCurrentExecution()) {
          exploreNextBacktrackSets(icsCG);
        }
        justVisitedStates.clear();
        choiceCounter++;
      }
    }
  }

  private void checkAndRecordNewState(int stateId) {
    // Insert state ID into the map if it is new
    if (!stateToEventMap.containsKey(stateId)) {
      HashSet<Integer> eventSet = new HashSet<>();
      stateToEventMap.put(stateId, eventSet);
    }
  }

  private void updateStateInfo(Search search) {
    if (stateReductionMode) {
      // Update the state variables
      // Line 19 in the paper page 11 (see the heading note above)
      int stateId = search.getStateId();
      currVisitedStates.add(stateId);
      checkAndRecordNewState(stateId);
      justVisitedStates.add(stateId);
    }
  }

  @Override
  public void stateAdvanced(Search search) {
    if (debugMode) {
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
    updateStateInfo(search);
  }

  @Override
  public void stateBacktracked(Search search) {
    if (debugMode) {
      id = search.getStateId();
      depth = search.getDepth();
      transition = search.getTransition();
      detail = null;

      out.println("\n==> DEBUG: The state is backtracked to state with id: " + id + " -- Transition: " + transition +
              " and depth: " + depth + "\n");
    }
    updateStateInfo(search);
  }

  @Override
  public void searchFinished(Search search) {
    if (debugMode) {
      out.println("\n==> DEBUG: ----------------------------------- search finished" + "\n");
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

  private boolean recordConflictPair(int currentEvent, int eventNumber) {
    HashSet<Integer> conflictSet;
    if (!conflictPairMap.containsKey(currentEvent)) {
      conflictSet = new HashSet<>();
      conflictPairMap.put(currentEvent, conflictSet);
    } else {
      conflictSet = conflictPairMap.get(currentEvent);
    }
    // If this conflict has been recorded before, we return false because
    // we don't want to service this backtrack point twice
    if (conflictSet.contains(eventNumber)) {
      return false;
    }
    // If it hasn't been recorded, then do otherwise
    conflictSet.add(eventNumber);
    return true;
  }

  private String buildStringFromChoiceList(Integer[] newChoiceList) {

    // When we see a choice list shorter than the upper bound, e.g., [3,2] for choices 0,1,2, and 3,
    //  then we have to pad the beginning before we store it, because [3,2] actually means [0,1,3,2]
    int actualListLength = newChoiceList.length;
    int diff = maxUpperBound - actualListLength;
    StringBuilder sb = new StringBuilder();
    // Pad the beginning if necessary
    for (int i = 0; i < diff; i++) {
      sb.append(i);
    }
    // Then continue with the actual choice list
    // We don't include the '-1' at the end
    for (int i = 0; i < newChoiceList.length; i++) {
      sb.append(newChoiceList[i]);
    }
    return sb.toString();
  }

  private void checkAndAddBacktrackList(LinkedList<Integer[]> backtrackChoiceLists, Integer[] newChoiceList) {

    String newChoiceListString = buildStringFromChoiceList(newChoiceList);
    // Add only if we haven't seen this combination before
    if (!backtrackSet.contains(newChoiceListString)) {
      backtrackSet.add(newChoiceListString);
      backtrackChoiceLists.addLast(newChoiceList);
    }
  }

  private void createBacktrackChoiceList(int currentChoice, int conflictEventNumber) {

    LinkedList<Integer[]> backtrackChoiceLists;
    // Create a new list of choices for backtrack based on the current choice and conflicting event number
    // If we have a conflict between 1 and 3, then we create the list {3, 1, 2, 4, 5} for backtrack
    // The backtrack point is the CG for event number 1 and the list length is one less than the original list
    // (originally of length 6) since we don't start from event number 0
    if (!isResetAfterAnalysis) {
      // Check if we have a list for this choice number
      // If not we create a new one for it
      if (!backtrackMap.containsKey(conflictEventNumber)) {
        backtrackChoiceLists = new LinkedList<>();
        backtrackMap.put(conflictEventNumber, backtrackChoiceLists);
      } else {
        backtrackChoiceLists = backtrackMap.get(conflictEventNumber);
      }
      int maxListLength = choiceUpperBound + 1;
      int listLength = maxListLength - conflictEventNumber;
      Integer[] newChoiceList = new Integer[listLength];
      // Put the conflicting event numbers first and reverse the order
      newChoiceList[0] = refChoices[currentChoice];
      newChoiceList[1] = refChoices[conflictEventNumber];
      // Put the rest of the event numbers into the array starting from the minimum to the upper bound
      for (int i = conflictEventNumber + 1, j = 2; j < listLength; i++) {
        if (refChoices[i] != refChoices[currentChoice]) {
          newChoiceList[j] = refChoices[i];
          j++;
        }
      }
      checkAndAddBacktrackList(backtrackChoiceLists, newChoiceList);
      // The start index for the recursion is always 1 (from the main branch)
    } else { // This is a sub-graph
      // There is a case/bug that after a re-initialization, currCG is not yet initialized
      if (currCG != null && cgMap.containsKey(currCG)) {
        int backtrackListIndex = cgMap.get(currCG);
        backtrackChoiceLists = backtrackMap.get(backtrackListIndex);
        int listLength = refChoices.length;
        Integer[] newChoiceList = new Integer[listLength];
        // Copy everything before the conflict number
        for (int i = 0; i < conflictEventNumber; i++) {
          newChoiceList[i] = refChoices[i];
        }
        // Put the conflicting events
        newChoiceList[conflictEventNumber] = refChoices[currentChoice];
        newChoiceList[conflictEventNumber + 1] = refChoices[conflictEventNumber];
        // Copy the rest
        for (int i = conflictEventNumber + 1, j = conflictEventNumber + 2; j < listLength - 1; i++) {
          if (refChoices[i] != refChoices[currentChoice]) {
            newChoiceList[j] = refChoices[i];
            j++;
          }
        }
        checkAndAddBacktrackList(backtrackChoiceLists, newChoiceList);
      }
    }
  }

  // We exclude fields that come from libraries (Java and Groovy), and also the infrastructure
  private final static String[] EXCLUDED_FIELDS_STARTS_WITH_LIST =
          // Java and Groovy libraries
          { "java", "org", "sun", "com", "gov", "groovy"};
  private final static String[] EXCLUDED_FIELDS_ENDS_WITH_LIST =
          // Groovy library created fields
          {"stMC", "callSiteArray", "metaClass", "staticClassInfo", "__constructor__",
          // Infrastructure
          "sendEvent", "Object", "reference", "location", "app", "state", "log", "functionList", "objectList",
          "eventList", "valueList", "settings", "printToConsole", "app1", "app2"};
  private final static String[] EXCLUDED_FIELDS_CONTAINS_LIST = {"_closure"};
  private final static String[] EXCLUDED_FIELDS_READ_WRITE_INSTRUCTIONS_STARTS_WITH_LIST = {"Event"};

  private boolean excludeThisForItStartsWith(String[] excludedStrings, String className) {
    for (String excludedField : excludedStrings) {
      if (className.startsWith(excludedField)) {
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

  private boolean excludeThisForItContains(String[] excludedStrings, String className) {
    for (String excludedField : excludedStrings) {
      if (className.contains(excludedField)) {
        return true;
      }
    }
    return false;
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

  // This method checks whether a choice is reachable in the VOD graph from a reference choice
  // This is a BFS search
  private boolean isReachableInVODGraph(int checkedChoice, int referenceChoice) {
    // Record visited choices as we search in the graph
    HashSet<Integer> visitedChoice = new HashSet<>();
    visitedChoice.add(referenceChoice);
    LinkedList<Integer> nodesToVisit = new LinkedList<>();
    // If the state doesn't advance as the threads/sub-programs are executed (basically there is no new state),
    // there is a chance that the graph doesn't have new nodes---thus this check will return a null.
    if (vodGraphMap.containsKey(referenceChoice)) {
      nodesToVisit.addAll(vodGraphMap.get(referenceChoice));
      while(!nodesToVisit.isEmpty()) {
        int currChoice = nodesToVisit.getFirst();
        if (currChoice == checkedChoice) {
          return true;
        }
        if (visitedChoice.contains(currChoice)) {
          // If there is a loop then we don't find it
          return false;
        }
        // Continue searching
        visitedChoice.add(currChoice);
        HashSet<Integer> currChoiceNextNodes = vodGraphMap.get(currChoice);
        if (currChoiceNextNodes != null) {
          // Add only if there is a mapping for next nodes
          for (Integer nextNode : currChoiceNextNodes) {
            // Skip cycles
            if (nextNode == currChoice) {
              continue;
            }
            nodesToVisit.addLast(nextNode);
          }
        }
      }
    }
    return false;
  }

  private int getCurrentChoice(VM vm) {
    ChoiceGenerator<?> currentCG = vm.getChoiceGenerator();
    // This is the main event CG
    if (currentCG instanceof IntChoiceFromSet) {
      return ((IntChoiceFromSet) currentCG).getNextChoiceIndex();
    } else {
      // This is the interval CG used in device handlers
      ChoiceGenerator<?> parentCG = ((IntIntervalGenerator) currentCG).getPreviousChoiceGenerator();
      return ((IntChoiceFromSet) parentCG).getNextChoiceIndex();
    }
  }

  private final static String GROOVY_CALLSITE_LIB = "org.codehaus.groovy.runtime.callsite";
  private final static String JAVA_STRING_LIB = "java.lang.String";
  private final static String JAVA_INTEGER = "int";
  private final static String DO_CALL_METHOD = "doCall";
  private final static String GET_PROPERTY_METHOD =
          "invokeinterface org.codehaus.groovy.runtime.callsite.CallSite.callGetProperty";

  private ReadWriteSet getReadWriteSet(int currentChoice) {
    // Do the analysis to get Read and Write accesses to fields
    ReadWriteSet rwSet;
    // We already have an entry
    if (readWriteFieldsMap.containsKey(refChoices[currentChoice])) {
      rwSet = readWriteFieldsMap.get(refChoices[currentChoice]);
    } else { // We need to create a new entry
      rwSet = new ReadWriteSet();
      readWriteFieldsMap.put(refChoices[currentChoice], rwSet);
    }
    return rwSet;
  }

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

  @Override
  public void instructionExecuted(VM vm, ThreadInfo ti, Instruction nextInsn, Instruction executedInsn) {
    if (stateReductionMode) {
      // Has to be initialized and a integer CG
      ChoiceGenerator<?> cg = vm.getChoiceGenerator();
      if (isInitialized && (cg instanceof IntChoiceFromSet || cg instanceof IntIntervalGenerator)) {
        int currentChoice = getCurrentChoice(vm);
        if (currentChoice < 0) { // If choice is -1 then skip
          return;
        }
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
          // The constructor is only called once when the object is initialized
          // It does not have shared access with other objects
          MethodInfo mi = nextInsn.getMethodInfo();
          if (!mi.getName().equals("<init>")) {
            String fieldClass = ((JVMFieldInstruction) nextInsn).getFieldInfo().getFullName();
            // We don't care about libraries
            if (!isFieldExcluded(fieldClass)) {
              // Check for conflict (go backward from currentChoice and get the first conflict)
              // If the current event has conflicts with multiple events, then these will be detected
              // one by one as this recursively checks backward when backtrack set is revisited and executed.
              for (int eventNumber = currentChoice - 1; eventNumber >= 0; eventNumber--) {
                // Skip if this event number does not have any Read/Write set
                if (!readWriteFieldsMap.containsKey(refChoices[eventNumber])) {
                  continue;
                }
                ReadWriteSet rwSet = readWriteFieldsMap.get(refChoices[eventNumber]);
                int currObjId = ((JVMFieldInstruction) nextInsn).getFieldInfo().getClassInfo().getClassObjectRef();
                // 1) Check for conflicts with Write fields for both Read and Write instructions
                if (((nextInsn instanceof WriteInstruction || nextInsn instanceof ReadInstruction) &&
                        rwSet.writeFieldExists(fieldClass) && rwSet.writeFieldObjectId(fieldClass) == currObjId) ||
                        (nextInsn instanceof WriteInstruction && rwSet.readFieldExists(fieldClass) &&
                                rwSet.readFieldObjectId(fieldClass) == currObjId)) {
                  // We do not record and service the same backtrack pair/point twice!
                  // If it has been serviced before, we just skip this
                  if (recordConflictPair(currentChoice, eventNumber)) {
                    // Lines 4-8 of the algorithm in the paper page 11 (see the heading note above)
                    if (vm.isNewState() || isReachableInVODGraph(refChoices[currentChoice], refChoices[currentChoice-1])) {
                      createBacktrackChoiceList(currentChoice, eventNumber);
                      // Break if a conflict is found!
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
