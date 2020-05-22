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

package gov.nasa.jpf.vm.serialize;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.annotation.FilterField;
import gov.nasa.jpf.annotation.FilterFrame;
import gov.nasa.jpf.vm.AnnotationInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.serialize.AmmendableFilterConfiguration.FieldAmmendment;
import gov.nasa.jpf.vm.serialize.AmmendableFilterConfiguration.FrameAmmendment;

public class SmartThingsConfig
  implements FieldAmmendment, FrameAmmendment {

  public boolean staticClass(ClassInfo ci) {
    String pName = ci.getName();
    if (pName.startsWith("java."))
      return true;

    if (pName.startsWith("sun."))
      return true;

    if (pName.startsWith("com.sun"))
      return true;

    if (pName.startsWith("org."))
      return true;

    if (pName.startsWith("groovy."))
      return true;

    if (pName.startsWith("groovyjarjarasm."))
      return true;

    if (pName.startsWith("gov."))
      return true;

    if (pName.startsWith("["))
      return true;

    return false;
  }
  public boolean ignoreClass(ClassInfo ci) {
    String pName = ci.getName();
    if (pName.startsWith("org")) {
      if (pName.startsWith("org.codehaus.groovy")) {
        //      System.out.println("Ignoring "+pName);
        return true;
      }
      if (pName.startsWith("org.apache.groovy")) {
        //      System.out.println("Ignoring "+pName);
        return true;
      }
    } else if (pName.startsWith("java")) {
      if (pName.startsWith("java.lang")) {
        if (pName.startsWith("java.lang.reflect")) {
          return true;
        }
        if (pName.startsWith("java.lang.ref")) {
          return true;
        }
        if (pName.startsWith("java.lang.ClassLoader")) {
          return true;
        }
        if (pName.startsWith("java.lang.Class"))
          return true;
        if (pName.startsWith("java.lang.Thread"))
          return true;
        if (pName.startsWith("java.lang.Package"))
          return true;
      } else {
      if (pName.startsWith("java.util.concurrent")) {
        //      System.out.println("Ignoring "+pName);
        return true;
      }
      if (pName.startsWith("java.util.logging")) {
        //      System.out.println("Ignoring "+pName);
        return true;
      }
      if (pName.startsWith("java.beans")) {
        return true;
      }
      if (pName.startsWith("java.io.OutputStreamWriter"))
        return true;
      if (pName.startsWith("java.io.PrintStream"))
        return true;
      if (pName.startsWith("java.io.BufferedWriter"))
        return true;
      }
      if (pName.startsWith("java.nio.charset"))
        return true;
      
    } else if (pName.startsWith("groovy")) {
      if (pName.startsWith("groovy.lang")) {
        //      System.out.println("Ignoring "+pName);
        return true;
      }
      if (pName.startsWith("groovyjarjarasm.asm")) {
        //      System.out.println("Ignoring "+pName);
        return true;
      }
    }
    if (pName.startsWith("com.sun.beans")) {
      return true;
    }
    if (pName.startsWith("sun.reflect")) {
      return true;
    }
    if (pName.startsWith("sun.misc.SharedSecrets"))
      return true;
    if (pName.startsWith("sun.util.logging"))
      return true;
    if (pName.startsWith("sun.net.www"))
      return true;
    if (pName.startsWith("gov.nasa"))
      return true;
    return false;
  }
  
  @Override
  public boolean ammendFieldInclusion(FieldInfo fi, boolean sofar) {
    ClassInfo ci = fi.getClassInfo();
    if (ignoreClass(ci))
      return POLICY_IGNORE;
    ClassInfo civ = fi.getTypeClassInfo();
    if (ignoreClass(civ))
      return POLICY_IGNORE;      
    return sofar;
  }

  @Override
  public FramePolicy ammendFramePolicy(MethodInfo mi, FramePolicy sofar) {
    ClassInfo ci = mi.getClassInfo();
    if (ignoreClass(ci)) {
      sofar.includeLocals = false;
      sofar.includeOps = false;
      sofar.includePC = false;
    } else {
      //      System.out.println("Including M: " +mi);
    }

    return sofar;
  }

  public static final SmartThingsConfig instance = new SmartThingsConfig();
}
