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

import java.io.PrintWriter;

import java.util.*;

// TODO: Fix for Groovy's model-checking
// TODO: This is a setter to change the values of the ChoiceGenerator to implement POR
// TODO: This is a more efficient implementation of StateReducer; it is using the Sleep Sets technique
/**
 * simple tool to log state changes
 */
public class EfficientStateReducer extends ListenerAdapter {

  // Debug info fields
  private boolean debugMode;
  private boolean stateReductionMode;
  private final PrintWriter out;
  volatile private String detail;
  volatile private int depth;
  volatile private int id;
  Transition transition;

  // State reduction fields
  private Integer[] choices;
  private IntChoiceFromSet currCG;
  private int choiceCounter;
  private Integer choiceUpperBound;
  private boolean isInitialized;
  private boolean isResetAfterAnalysis;
  private boolean isBooleanCGFlipped;
  private HashMap<IntChoiceFromSet,Integer> cgMap = new HashMap<>();
  // Record the mapping between event number and field accesses (Read and Write)
  private HashMap<Integer,ReadWriteSet> readWriteFieldsMap = new HashMap<>();
  // The following is the backtrack map (set) that stores all the backtrack information
  // e.g., event number 1 can have two backtrack sequences: {3,1,2,4,...} and {2,1,3,4,...}
  private HashMap<Integer,LinkedList<Integer[]>> backtrackMap = new HashMap<>();
  private HashMap<Integer,HashSet<Integer>> conflictPairMap = new HashMap<>();
  private HashMap<Integer,HashSet<Integer>> conflictPairReversedMap = new HashMap<>();
  private HashMap<Integer,HashSet<Integer>> sleepSetMap = new HashMap<>();
  // Map choicelist with start index
  private HashMap<Integer[],Integer> choiceListStartIndexMap = new HashMap<>();

  public EfficientStateReducer (Config config, JPF jpf) {
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
    initializeStateReduction();
  }

  private void initializeStateReduction() {
    if (stateReductionMode) {
      choices = null;
      currCG = null;
      choiceCounter = 0;
      choiceUpperBound = 0;
      isInitialized = false;
      isResetAfterAnalysis = false;
      cgMap.clear();
      readWriteFieldsMap.clear();
      backtrackMap.clear();
      conflictPairMap.clear();
      conflictPairReversedMap.clear();
      sleepSetMap.clear();
      choiceListStartIndexMap.clear();
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

  @Override
  public void choiceGeneratorRegistered (VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
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
        if (!isResetAfterAnalysis && choiceCounter <= choiceUpperBound && !cgMap.containsValue(choiceCounter)) {
          // Update the choices of the first CG and add '-1'
          if (choices == null) {
            // All the choices are always the same so we only need to update it once
            choices = new Integer[cgChoices.length + 1];
            System.arraycopy(cgChoices, 0, choices, 0, cgChoices.length);
            choices[choices.length - 1] = -1;
          }
          icsCG.setNewValues(choices);
          icsCG.reset();
          // Advance the current Integer CG
          // This way we explore all the event numbers in the first pass
          icsCG.advance(choices[choiceCounter]);
          cgMap.put(icsCG, choices[choiceCounter]);
          choiceCounter++;
        } else {
          // Set done the subsequent CGs
          // We only need n CGs (n is event numbers)
          icsCG.setDone();
        }
      }
    }
  }

  private void generateSleepSets() {

    // Check and put the number into the sleep set if there is no conflict between the two
    for (int i=0; i<=choiceUpperBound; i++) {
      HashSet<Integer> sleepSet = new HashSet<>();
      sleepSetMap.put(i,sleepSet);
      for (int j=0; j<=choiceUpperBound; j++) {
        if (i != j ) {
          // Scour the data structure to find conflict
          HashSet<Integer> conflictSet = new HashSet<>();
          HashSet<Integer> reversedConflictSet = new HashSet<>();
          if (conflictPairMap.containsKey(i)) {
            conflictSet = conflictPairMap.get(i);
          }
          // Scour the reversed conflict mapper as well
          if (conflictPairReversedMap.containsKey(i)) {
            reversedConflictSet = conflictPairReversedMap.get(i);
          }
          if (!conflictSet.contains(j) && !reversedConflictSet.contains(j)) {
            sleepSet.add(j);
          }
        }
      }
    }
  }

  private int getChoiceIndex(int event) {
    for(int i=0; i<choices.length-1; i++) {
      if (choices[i] == event) {
        return i;
      }
    }
    // Return -1 if not found
    return -1;
  }

  private void generateBacktrackSets() {
    int end = !isResetAfterAnalysis ? 0 : choiceListStartIndexMap.get(choices);
    for(Integer currentChoice : conflictPairMap.keySet()) {
      HashSet<Integer> conflictSet = conflictPairMap.get(currentChoice);
      for(Integer eventNumber : conflictSet) {
        // Both indices have to be greater than or equal to "end" index
        // We don't want to repeat the same sequence
        if (getChoiceIndex(currentChoice) >= end && getChoiceIndex(eventNumber) >= end) {
          createBacktrackChoiceList(currentChoice, eventNumber);
        }
      }
    }
  }

  private void resetAllCGs() {
    // Extract the event numbers that have backtrack lists
    Set<Integer> eventSet = backtrackMap.keySet();
    // Return if there is no conflict at all (highly unlikely)
    if (eventSet.isEmpty()) {
      return;
    }
    // Reset every CG with the first backtrack lists
    for(IntChoiceFromSet cg : cgMap.keySet()) {
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
  }

  @Override
  public void choiceGeneratorAdvanced (VM vm, ChoiceGenerator<?> currentCG) {

    if(stateReductionMode) {
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
        if (choices == null || choices != icsCG.getAllChoices()) {
          choiceListStartIndexMap.remove(choices);
          currCG = icsCG;
          choices = icsCG.getAllChoices();
        }
        // Traverse the sub-graphs
        if (isResetAfterAnalysis) {
          // Do this for every CG after finishing each backtrack list
          if (icsCG.getNextChoice() == -1) {
            int event = cgMap.get(icsCG);
            LinkedList<Integer[]> choiceLists = backtrackMap.get(event);
            if (choiceLists != null && choiceLists.peekFirst() != null) {
              // Generate backtrack sets from this choiceList
              generateBacktrackSets();
              Integer[] choiceList = choiceLists.removeFirst();
              // Deploy the new choice list for this CG
              icsCG.setNewValues(choiceList);
              icsCG.reset();
            } else {
              // Set done if this was the last backtrack list
              icsCG.setDone();
            }
          }
        }
        // Update and reset the CG if needed (do this for the first time after the analysis)
        if (!isResetAfterAnalysis && icsCG.getNextChoice() == -1) {
          generateSleepSets();
          generateBacktrackSets();
          resetAllCGs();
          isResetAfterAnalysis = true;
        }
      }
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
    private HashMap<String,Integer> readSet;
    private HashMap<String,Integer> writeSet;

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
    ReadWriteSet rwSet;
    // We already have an entry
    if (readWriteFieldsMap.containsKey(choices[currentChoice])) {
      rwSet = readWriteFieldsMap.get(choices[currentChoice]);
    } else { // We need to create a new entry
      rwSet = new ReadWriteSet();
      readWriteFieldsMap.put(choices[currentChoice], rwSet);
    }
    int objectId = ((JVMFieldInstruction) executedInsn).getFieldInfo().getClassInfo().getClassObjectRef();
    // Record the field in the map
    if (executedInsn instanceof WriteInstruction) {
      // Exclude certain field writes because of infrastructure needs, e.g., Event class field writes
      for(String str : EXCLUDED_FIELDS_WRITE_INSTRUCTIONS_STARTS_WITH_LIST) {
        if (fieldClass.startsWith(str)) {
          return;
        }
      }
      rwSet.addWriteField(fieldClass, objectId);
    } else if (executedInsn instanceof ReadInstruction) {
      rwSet.addReadField(fieldClass, objectId);
    }
  }

  private boolean recordConflictPair(HashMap<Integer,HashSet<Integer>> confPairMap, int currentEvent, int eventNumber) {
    HashSet<Integer> conflictSet;
    if (!confPairMap.containsKey(currentEvent)) {
      conflictSet = new HashSet<>();
      confPairMap.put(currentEvent, conflictSet);
    } else {
      conflictSet = confPairMap.get(currentEvent);
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

  private Integer[] copyIntegerListToArray(List<Integer> list) {
    Integer[] integerArray = new Integer[list.size()];
    for (int i=0; i<list.size(); i++) {
      integerArray[i] = list.get(i);
    }
    return integerArray;
  }

  private void completeBacktrackChoiceList(List<Integer> newChoiceList, int currentChoice, int conflictEventNumber,
                                         LinkedList<Integer[]> backtrackChoiceLists) {
    // Put the conflicting event numbers first and reverse the order
    newChoiceList.add(choices[currentChoice]);
    newChoiceList.add(choices[conflictEventNumber]);
    // Put the rest of the event numbers into the array starting from the minimum to the upper bound
    for (int i = conflictEventNumber + 1; i < choices.length - 1; i++) {
      // Check the sleep sets for excepted events that do not conflict with the current one
      int prevChoiceNum = newChoiceList.get(newChoiceList.size()-1);
      HashSet<Integer> sleepSet = sleepSetMap.get(prevChoiceNum);
      if (choices[i] != choices[currentChoice] && !sleepSet.contains(choices[i])) {
        newChoiceList.add(choices[i]);
      }
    }
    // Set the last element to '-1' as the end of the sequence
    newChoiceList.add(-1);
    Integer[] newChoiceArray = copyIntegerListToArray(newChoiceList);
    backtrackChoiceLists.addLast(newChoiceArray);
    if (!isResetAfterAnalysis) {
      // The start index for the recursion is always 1 (from the main branch)
      choiceListStartIndexMap.put(newChoiceArray, 1);
    } else {
      choiceListStartIndexMap.put(newChoiceArray, conflictEventNumber + 1);
    }
  }

  private void createBacktrackChoiceList(int currentChoice, int conflictEventNumber) {

    LinkedList<Integer[]> backtrackChoiceLists;
    // Create a new list of choices for backtrack based on the current choice and conflicting event number
    // If we have a conflict between 1 and 3, then we create the list {3, 1, 2, 4, 5} for backtrack
    // The backtrack point is the CG for event number 1 and the list length is one less than the original list
    // since we don't start from event number 0
    if (!isResetAfterAnalysis) {
      // Check if we have a list for this choice number
      // If not we create a new one for it
      if (!backtrackMap.containsKey(conflictEventNumber)) {
        backtrackChoiceLists = new LinkedList<>();
        backtrackMap.put(conflictEventNumber, backtrackChoiceLists);
      } else {
        backtrackChoiceLists = backtrackMap.get(conflictEventNumber);
      }
      List<Integer> newChoiceList = new ArrayList<>();
      completeBacktrackChoiceList(newChoiceList, currentChoice, conflictEventNumber, backtrackChoiceLists);
    } else { // This is a sub-graph
      int backtrackListIndex = cgMap.get(currCG);
      backtrackChoiceLists = backtrackMap.get(backtrackListIndex);
      List<Integer> newChoiceList = new ArrayList<>();
      // Copy everything before the conflict number
      int conflictEventCurrentNumber = getChoiceIndex(conflictEventNumber);
      for(int i = 0; i < conflictEventCurrentNumber; i++) {
        newChoiceList.add(choices[i]);
      }
      // Put the conflicting events
      int currentChoiceCurrentNumber = getChoiceIndex(currentChoice);
      completeBacktrackChoiceList(newChoiceList, currentChoiceCurrentNumber, conflictEventCurrentNumber,
              backtrackChoiceLists);
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
  private final static String[] EXCLUDED_FIELDS_WRITE_INSTRUCTIONS_STARTS_WITH_LIST = {"Event"};

  private boolean isFieldExcluded(String field) {
    // Check against "starts-with" list
    for(String str : EXCLUDED_FIELDS_STARTS_WITH_LIST) {
      if (field.startsWith(str)) {
        return true;
      }
    }
    // Check against "ends-with" list
    for(String str : EXCLUDED_FIELDS_ENDS_WITH_LIST) {
      if (field.endsWith(str)) {
        return true;
      }
    }
    // Check against "contains" list
    for(String str : EXCLUDED_FIELDS_CONTAINS_LIST) {
      if (field.contains(str)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void instructionExecuted(VM vm, ThreadInfo ti, Instruction nextInsn, Instruction executedInsn) {
    if (stateReductionMode) {
      if (isInitialized && !isResetAfterAnalysis) {
        if (choiceCounter > choices.length - 1) {
          // We do not compute the conflicts for the choice '-1'
          return;
        }
        int currentChoice = choiceCounter - 1;
        // Record accesses from executed instructions
        if (executedInsn instanceof JVMFieldInstruction) {
          // Analyze only after being initialized
          String fieldClass = ((JVMFieldInstruction) executedInsn).getFieldInfo().getFullName();
          // We don't care about libraries
          if (!isFieldExcluded(fieldClass)) {
            analyzeReadWriteAccesses(executedInsn, fieldClass, currentChoice);
          }
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
              // For the main graph we go down to 0, but for subgraph, we only go down to 1 since 0 contains
              // the reversed event
              // Check for conflict (go backward from currentChoice and get the first conflict)
              // If the current event has conflicts with multiple events, then these will be detected
              // one by one as this recursively checks backward when backtrack set is revisited and executed.
              for (int eventNumber = currentChoice - 1; eventNumber >= 0; eventNumber--) {
                // Skip if this event number does not have any Read/Write set
                if (!readWriteFieldsMap.containsKey(choices[eventNumber])) {
                  continue;
                }
                ReadWriteSet rwSet = readWriteFieldsMap.get(choices[eventNumber]);
                int currObjId = ((JVMFieldInstruction) nextInsn).getFieldInfo().getClassInfo().getClassObjectRef();
                // 1) Check for conflicts with Write fields for both Read and Write instructions
                if (((nextInsn instanceof WriteInstruction || nextInsn instanceof ReadInstruction) &&
                        rwSet.writeFieldExists(fieldClass) && rwSet.writeFieldObjectId(fieldClass) == currObjId) ||
                        (nextInsn instanceof WriteInstruction && rwSet.readFieldExists(fieldClass) &&
                                rwSet.readFieldObjectId(fieldClass) == currObjId)) {
                  // We do not record and service the same backtrack pair/point twice!
                  // If it has been serviced before, we just skip this
                  if (recordConflictPair(conflictPairMap, currentChoice, eventNumber)) {
                    recordConflictPair(conflictPairReversedMap, eventNumber, currentChoice);
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
