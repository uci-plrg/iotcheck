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

package gov.nasa.jpf.vm;

import gov.nasa.jpf.annotation.MJI;

public class JPF_sun_reflect_generics_reflectiveObjects_WildcardTypeImpl extends NativePeer {

    // TODO: Need to fix the following 2 methods if needed!!!
    /*@MJI
    public int getUpperBounds_____3Ljava_lang_reflect_Type_2 (MJIEnv env, int objRef){
        ThreadInfo ti = env.getThreadInfo();
        ClassInfo ci = ClassLoaderInfo.getCurrentResolvedClassInfo("java.lang.Object");
        if (!ci.isRegistered()) {
            ci.registerClass(ti);
        }
        int refObj = ci.getClassObjectRef();

        int aRef = env.newObjectArray("Ljava/lang/reflect/Type;", 1);
        // Set references for every array element
        env.setReferenceArrayElement(aRef, 0, refObj);

        return aRef;
    }

    @MJI
    public int getLowerBounds_____3Ljava_lang_reflect_Type_2 (MJIEnv env, int objRef){

        int aRef = env.newObjectArray("Ljava/lang/reflect/Type;", 1);
        // Set references for every array element
        env.setReferenceArrayElement(aRef, 0, MJIEnv.NULL);

        return aRef;
    }*/
}
