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
package sun.reflect.generics.reflectiveObjects;

import sun.reflect.generics.tree.FieldTypeSignature;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

// TODO: Fix for Groovy's model-checking
/**
 * MJI model class for sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
 *
 * This is a JPF specific version of a system class because we can't use the real,
 * platform VM specific version (it's native all over the place, its field
 * structure isn't documented, most of its methods are private, hence we can't
 * even instantiate it properly).
 *
 * Note that this class never gets seen by the real VM - it's for JPF's eyes only.
 *
 */

/** Implementing class for ParameterizedType interface. */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?>  rawType;
    private final Type   ownerType;

    private ParameterizedTypeImpl(Class<?> rawType,
                                  Type[] actualTypeArguments,
                                  Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType             = rawType;
        this.ownerType = (ownerType != null) ? ownerType : rawType.getDeclaringClass();
        validateConstructorArguments();
    }

    private void validateConstructorArguments() {
        TypeVariable<?>[] formals = rawType.getTypeParameters();
        // check correct arity of actual type args
        if (formals.length != actualTypeArguments.length){
            throw new MalformedParameterizedTypeException();
        }
        for (int i = 0; i < actualTypeArguments.length; i++) {
            // check actuals against formals' bounds
        }
    }

    /**
     * Static factory. Given a (generic) class, actual type arguments
     * and an owner type, creates a parameterized type.
     */
    public static ParameterizedTypeImpl make(Class<?> rawType,
                                             Type[] actualTypeArguments,
                                             Type ownerType) {
        return new ParameterizedTypeImpl(rawType, actualTypeArguments,
                                         ownerType);
    }

    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    /**
     * Returns the <tt>Type</tt> object representing the class or interface
     * that declared this type.
     *
     * @return the <tt>Type</tt> object representing the class or interface
     *     that declared this type
     */
    public Class<?> getRawType() {
        return rawType;
    }


    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ParameterizedType) {
            // Check that information is equivalent
            ParameterizedType that = (ParameterizedType) o;

            if (this == that)
                return true;

            Type thatOwner   = that.getOwnerType();
            Type thatRawType = that.getRawType();

            if (false) { // Debugging
                boolean ownerEquality = (ownerType == null ?
                                         thatOwner == null :
                                         ownerType.equals(thatOwner));
                boolean rawEquality = (rawType == null ?
                                       thatRawType == null :
                                       rawType.equals(thatRawType));

                boolean typeArgEquality = Arrays.equals(actualTypeArguments, // avoid clone
                                                        that.getActualTypeArguments());
                for (Type t : actualTypeArguments) {
                    System.out.printf("\t\t%s%s%n", t, t.getClass());
                }

                System.out.printf("\towner %s\traw %s\ttypeArg %s%n",
                                  ownerEquality, rawEquality, typeArgEquality);
                return ownerEquality && rawEquality && typeArgEquality;
            }

            return
                Objects.equals(ownerType, thatOwner) &&
                Objects.equals(rawType, thatRawType) &&
                Arrays.equals(actualTypeArguments, // avoid clone
                              that.getActualTypeArguments());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return
            Arrays.hashCode(actualTypeArguments) ^
            Objects.hashCode(ownerType) ^
            Objects.hashCode(rawType);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (ownerType != null) {
            if (ownerType instanceof Class)
                sb.append(((Class)ownerType).getName());
            else
                sb.append(ownerType.toString());

            sb.append(".");

            if (ownerType instanceof ParameterizedTypeImpl) {
                // Find simple name of nested type by removing the
                // shared prefix with owner.
                sb.append(rawType.getName().replace( ((ParameterizedTypeImpl)ownerType).rawType.getName() + "$",
                                         ""));
            } else
                sb.append(rawType.getName());
        } else
            sb.append(rawType.getName());

        if (actualTypeArguments != null &&
            actualTypeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for(Type t: actualTypeArguments) {
                if (!first)
                    sb.append(", ");
                sb.append(t.getTypeName());
                first = false;
            }
            sb.append(">");
        }

        return sb.toString();
    }
}

