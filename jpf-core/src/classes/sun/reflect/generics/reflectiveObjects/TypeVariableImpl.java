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

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.FieldTypeSignature;

// TODO: Fix for Groovy's model-checking
/**
 * MJI model class for sun.reflect.generics.reflectiveObjects.TypeVariableImpl
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
public class TypeVariableImpl<D extends GenericDeclaration>
    extends LazyReflectiveObjectGenerator implements TypeVariable<D> {

    D genericDeclaration;
    private String name;

    // constructor is private to enforce access through static factory
    private TypeVariableImpl(D decl, String n, FieldTypeSignature[] bs,
                             GenericsFactory f) {
        super(f);
        genericDeclaration = decl;
        name = n;
    }

    // Accessors
    private FieldTypeSignature[] getBoundASTs() {
        throw new UnsupportedOperationException();
    }

    /**
     * Factory method.
     */
    public static <T extends GenericDeclaration>
                             TypeVariableImpl<T> make(T decl, String name,
                                                      FieldTypeSignature[] bs,
                                                      GenericsFactory f) {

        if (!((decl instanceof Class) ||
                //(decl instanceof Method) ||
                (decl instanceof Constructor))) {
            throw new AssertionError("Unexpected kind of GenericDeclaration" +
                    decl.getClass().toString());
        }
        return new TypeVariableImpl<T>(decl, name, bs, f);
    }

    public native Type[] getBounds();

    public D getGenericDeclaration(){
        return genericDeclaration;
    }

    public String getName()   {
        return name;
    }

    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TypeVariable &&
                o.getClass() == TypeVariableImpl.class) {
            TypeVariable<?> that = (TypeVariable<?>) o;

            GenericDeclaration thatDecl = that.getGenericDeclaration();
            String thatName = that.getName();

            return Objects.equals(genericDeclaration, thatDecl) &&
                Objects.equals(name, thatName);

        } else
            return false;
    }

    @Override
    public int hashCode() {
        return genericDeclaration.hashCode() ^ name.hashCode();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        throw new UnsupportedOperationException();
    }

    public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
        throw new UnsupportedOperationException();
    }

    public Annotation[] getAnnotations() {
        throw new UnsupportedOperationException();
    }

    public Annotation[] getDeclaredAnnotations() {
        throw new UnsupportedOperationException();
    }

    public AnnotatedType[] getAnnotatedBounds() {
        throw new UnsupportedOperationException();
    }

}
