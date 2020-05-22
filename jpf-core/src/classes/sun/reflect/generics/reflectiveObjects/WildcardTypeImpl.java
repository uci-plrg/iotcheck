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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.FieldTypeSignature;
import sun.reflect.generics.visitor.Reifier;
import java.util.Arrays;

// TODO: Fix for Groovy's model-checking
/**
 * MJI model class for sun.reflect.generics.reflectiveObjects.WildcardTypeImpl
 *
 * This is a JPF specific version of a system class because we can't use the real,
 * platform VM specific version (it's native all over the place, its field
 * structure isn't documented, most of its methods are private, hence we can't
 * even instantiate it properly).
 *
 * Note that this class never gets seen by the real VM - it's for JPF's eyes only.
 *
 */
public class WildcardTypeImpl extends LazyReflectiveObjectGenerator
        implements WildcardType {

    private Type[] upperBounds;
    private Type[] lowerBounds;
    private FieldTypeSignature[] upperBoundASTs;
    private FieldTypeSignature[] lowerBoundASTs;

    // constructor is private to enforce access through static factory
    private WildcardTypeImpl(FieldTypeSignature[] ubs,
                             FieldTypeSignature[] lbs,
                             GenericsFactory f) {
        super(f);
        upperBoundASTs = ubs;
        lowerBoundASTs = lbs;
    }

    public static WildcardTypeImpl make(FieldTypeSignature[] ubs,
                                        FieldTypeSignature[] lbs,
                                        GenericsFactory f) {
        return new WildcardTypeImpl(ubs, lbs, f);
    }

    // Accessors
    private FieldTypeSignature[] getUpperBoundASTs() {
        throw new UnsupportedOperationException();
    }

    private FieldTypeSignature[] getLowerBoundASTs() {
        throw new UnsupportedOperationException();
    }

    public Type[] getUpperBounds() {
        return upperBounds;
    }

    public Type[] getLowerBounds() {
        return lowerBounds;
    }

    public String toString() {
        Type[] lowerBounds = getLowerBounds();
        Type[] bounds = lowerBounds;
        StringBuilder sb = new StringBuilder();

        if (lowerBounds.length > 0)
            sb.append("? super ");
        else {
            Type[] upperBounds = getUpperBounds();
            if (upperBounds.length > 0 && !upperBounds[0].equals(Object.class) ) {
                bounds = upperBounds;
                sb.append("? extends ");
            } else
                return "?";
        }

        // TODO: Commented out since it's producing <clinit> that blocks us from getting a new object without
        // TODO: initializing it through the constructor.
        //assert bounds.length > 0;

        boolean first = true;
        for(Type bound: bounds) {
            if (!first)
                sb.append(" & ");

            first = false;
            sb.append(bound.getTypeName());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WildcardType) {
            WildcardType that = (WildcardType) o;
            return
                    Arrays.equals(this.getLowerBounds(),
                            that.getLowerBounds()) &&
                            Arrays.equals(this.getUpperBounds(),
                                    that.getUpperBounds());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        Type [] lowerBounds = getLowerBounds();
        Type [] upperBounds = getUpperBounds();

        return Arrays.hashCode(lowerBounds) ^ Arrays.hashCode(upperBounds);
    }
}