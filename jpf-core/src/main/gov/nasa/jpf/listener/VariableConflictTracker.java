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
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.vm.bytecode.LocalVariableInstruction;
import gov.nasa.jpf.vm.bytecode.ReadInstruction;
import gov.nasa.jpf.vm.bytecode.StoreInstruction;
import gov.nasa.jpf.vm.bytecode.WriteInstruction;

import java.util.*;

// TODO: Fix for Groovy's model-checking
// TODO: This is a listener created to detect device conflicts and global variable conflicts

// TODO: This is the old version of our conlfict tracker. Please see ConflictTracker.java.
/**
 * Simple listener tool to track conflicts between 2 apps.
 * A conflict is defined as one app trying to change the state of a variable
 * into its opposite value after being set by the other app,
 * e.g., app1 attempts to change variable A to false after A has been set by app2 to true earlier.
 */
public class VariableConflictTracker extends ListenerAdapter {

  private final HashMap<String, VarChange> writeMap = new HashMap<>();
  private final HashMap<String, VarChange> readMap = new HashMap<>();
  private final HashSet<String> conflictSet = new HashSet<>();
  private final HashSet<String> appSet = new HashSet<>();
  private boolean trackLocationVar;
  private long timeout;
  private long startTime;

  private final String SET_LOCATION_METHOD = "setLocationMode";
  private final String LOCATION_VAR = "location.mode";

  public VariableConflictTracker(Config config) {
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
    trackLocationVar = config.getBoolean("track_location_var_conflict", false);
    // Timeout input from config is in minutes, so we need to convert into millis
    timeout = config.getInt("timeout", 0) * 60 * 1000;
    startTime = System.currentTimeMillis();
  }

  @Override
  public void instructionExecuted(VM vm, ThreadInfo ti, Instruction nextInsn, Instruction executedInsn) {
    // Instantiate timeoutTimer
    if (timeout > 0) {
      if (System.currentTimeMillis() - startTime > timeout) {
        StringBuilder sb = new StringBuilder();
        sb.append("Execution timeout: " + (timeout / (60 * 1000)) + " minutes have passed!");
        Instruction nextIns = ti.createAndThrowException("java.lang.RuntimeException", sb.toString());
        ti.setNextPC(nextIns);
      }
    }

    // CASE #1: Detecting variable write-after-write conflict
    if (executedInsn instanceof WriteInstruction) {
      // Check for write-after-write conflict
      String varId = ((WriteInstruction) executedInsn).getFieldInfo().getFullName();
      for(String var : conflictSet) {

        if (varId.contains(var)) {
          // Get variable info
          byte type  = getType(ti, executedInsn);
          String value = getValue(ti, executedInsn, type);
          //System.out.println("\n\n" + ti.getStackTrace() + "\n\n");
          String writer = getWriter(ti.getStack());         
          // Just return if the writer is not one of the listed apps in the .jpf file
          if (writer == null)
            return;

          // Check and throw error if conflict is detected
          checkWriteMapAndThrowError(var, value, writer, ti);
        }
      }
    }

    // CASE #2: Detecting global variable location.mode write-after-write conflict
    if (trackLocationVar) {
      MethodInfo mi = executedInsn.getMethodInfo();
      // Find the last load before return and get the value here
      if (mi.getName().equals(SET_LOCATION_METHOD) &&
              executedInsn instanceof ALOAD && nextInsn instanceof ARETURN) {
        byte type  = getType(ti, executedInsn);
        String value = getValue(ti, executedInsn, type);

        // Extract the writer app name
        ClassInfo ci = mi.getClassInfo();
        String writer = ci.getName();

        // Check and throw error if conflict is detected
        checkWriteMapAndThrowError(LOCATION_VAR, value, writer, ti);
      }
    }
  }

  private void checkWriteMapAndThrowError(String var, String value, String writer, ThreadInfo ti) {

    if (writeMap.containsKey(var)) {
      // Subsequent writes to the variable
      VarChange current = writeMap.get(var);
      if (current.writer != writer) {
        // Conflict is declared when:
        // 1) Current writer != previous writer, e.g., App1 vs. App2
        // 2) Current value != previous value, e.g., "locked" vs. "unlocked"
        if (!current.value.equals(value)) {

          StringBuilder sb = new StringBuilder();
          sb.append("Conflict between apps " + current.writer + " and " + writer + ": ");
          sb.append(writer + " has attempted to write the value " + value + " into ");
          sb.append("variable " + var + " that had already had the value " + current.value);
          sb.append(" previously written by " + current.writer);
          Instruction nextIns = ti.createAndThrowException("java.lang.RuntimeException", sb.toString());
          ti.setNextPC(nextIns);
        }
      } else {
        // No conflict is declared if this variable is written subsequently by the same writer
        current.writer = writer;
        current.value = value;
      }
    } else {
      // First write to the variable only if it is not null
			if (value != null) {
				VarChange change = new VarChange(writer, value);
				writeMap.put(var, change);
			}
    }
  }

  class VarChange {
    String writer;
    String value;
    
    VarChange(String writer, String value) {
      this.writer = writer;
      this.value = value;
    }
  }

  // Find the variable writer
  // It should be one of the apps listed in the .jpf file
  private String getWriter(List<StackFrame> sfList) {
    // Start looking from the top of the stack backward
    for(int i=sfList.size()-1; i>=0; i--) {
      MethodInfo mi = sfList.get(i).getMethodInfo();
      if(!mi.isJPFInternal()) {
        String method = mi.getStackTraceName();
        // Check against the apps in the appSet
        for(String app : appSet) {
          // There is only one writer at a time but we need to always
          // check all the potential writers in the list
          if (method.contains(app)) {
            return app;
          }
        }
      }
    }

    return null;
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

  private final static byte getTypeFromInstruction(JVMArrayElementInstruction inst) {
    String name;

    name = inst.getClass().getName();
    name = name.substring(name.lastIndexOf('.') + 1);

    switch (name.charAt(0)) {
      case 'A': return(Types.T_REFERENCE);
      case 'B': return(Types.T_BYTE);      // Could be a boolean but it is better to assume a byte.
      case 'C': return(Types.T_CHAR);
      case 'F': return(Types.T_FLOAT);
      case 'I': return(Types.T_INT);
      case 'S': return(Types.T_SHORT);
      case 'D': return(Types.T_DOUBLE);
      case 'L': return(Types.T_LONG);
    }

    return(Types.T_VOID);
  }

  private final static String encodeType(byte type) {
    switch (type) {
      case Types.T_BYTE:    return("B");
      case Types.T_CHAR:    return("C");
      case Types.T_DOUBLE:  return("D");
      case Types.T_FLOAT:   return("F");
      case Types.T_INT:     return("I");
      case Types.T_LONG:    return("J");
      case Types.T_REFERENCE:  return("L");
      case Types.T_SHORT:   return("S");
      case Types.T_VOID:    return("V");
      case Types.T_BOOLEAN: return("Z");
      case Types.T_ARRAY:   return("[");
    }

    return("?");
  }

  private final static byte decodeType(String type) {
    if (type.charAt(0) == '?'){
      return(Types.T_REFERENCE);
    } else {
      return Types.getBuiltinType(type);
    }
  }

  private String getName(ThreadInfo ti, Instruction inst, byte type) {
    String name;
    int index;
    boolean store;

    if ((inst instanceof JVMLocalVariableInstruction) ||
            (inst instanceof JVMFieldInstruction)) {
      name = ((LocalVariableInstruction) inst).getVariableId();
      name = name.substring(name.lastIndexOf('.') + 1);

      return(name);
    }

    if (inst instanceof JVMArrayElementInstruction) {
      store  = inst instanceof StoreInstruction;
      name   = getArrayName(ti, type, store);
      index  = getArrayIndex(ti, type, store);
      return(name + '[' + index + ']');
    }

    return(null);
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

       return(decodeValue(type, lo, hi));
    }

    if (inst instanceof JVMArrayElementInstruction)
      return(getArrayValue(ti, type));

    return(null);
  }

  private String getArrayName(ThreadInfo ti, byte type, boolean store) {
    String attr;
    int offset;

    offset = calcOffset(type, store) + 1;
    // <2do> String is really not a good attribute type to retrieve!
    StackFrame frame = ti.getTopFrame();
    attr   = frame.getOperandAttr( offset, String.class);

    if (attr != null) {
      return(attr);
    }

    return("?");
  }

  private int getArrayIndex(ThreadInfo ti, byte type, boolean store) {
    int offset;

    offset = calcOffset(type, store);

    return(ti.getTopFrame().peek(offset));
  }

  private final static int calcOffset(byte type, boolean store) {
    if (!store)
      return(0);

    return(Types.getTypeSize(type));
  }

  private String getArrayValue(ThreadInfo ti, byte type) {
    StackFrame frame;
    int lo, hi;

    frame = ti.getTopFrame();
    lo    = frame.peek();
    hi    = frame.getTopPos() >= 1 ? frame.peek(1) : 0;

    return(decodeValue(type, lo, hi));
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
}
