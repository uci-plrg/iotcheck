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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

// TODO: Fix for Groovy's model-checking
/**
 * MJI model class for sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl
 *
 * This is a JPF specific version of a system class because we can't use the real,
 * platform VM specific version (it's native all over the place, its field
 * structure isn't documented, most of its methods are private, hence we can't
 * even instantiate it properly).
 *
 * Note that this class never gets seen by the real VM - it's for JPF's eyes only.
 *
 * For now this only supports a few basic methods.
 */
public class GenericArrayTypeImpl
        implements GenericArrayType {
    private final Type genericComponentType;

    // private constructor enforces use of static factory
    private GenericArrayTypeImpl(Type ct) {
        genericComponentType = ct;
    }

    public static GenericArrayTypeImpl make(Type ct) {
        return new GenericArrayTypeImpl(ct);
    }

    public Type getGenericComponentType() {
        return genericComponentType; // return cached component type
    }

    public String toString() {
        Type componentType = getGenericComponentType();
        StringBuilder sb = new StringBuilder();

        if (componentType instanceof Class)
            sb.append(((Class)componentType).getName() );
        else
            sb.append(componentType.toString());
        sb.append("[]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GenericArrayType) {
            GenericArrayType that = (GenericArrayType) o;

            return Objects.equals(genericComponentType, that.getGenericComponentType());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(genericComponentType);
    }
}