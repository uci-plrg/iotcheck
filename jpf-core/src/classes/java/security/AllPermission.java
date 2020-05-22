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
package java.security;

import java.util.Enumeration;
import sun.security.util.SecurityConstants;

// TODO: Fix for Groovy's model-checking
// TODO: This model class is a placeholder for future implementation
/**
 * MJI model class for java.security.AllPermission library abstraction
 */
public final class AllPermission extends Permission {

    private static final long serialVersionUID = -2916474571451318075L;

    public AllPermission() {
        super("<all permissions>");
    }

    public boolean implies(Permission p) {
        return true;
    }

    public boolean equals(Object obj) {
        return (obj instanceof AllPermission);
    }

    public int hashCode() {
        return 1;
    }

    public String getActions() {
        return "<all actions>";
    }

    public PermissionCollection newPermissionCollection() {
        return new AllPermissionCollection();
    }

}


final class AllPermissionCollection
        extends PermissionCollection
        implements java.io.Serializable
{

    // use serialVersionUID from JDK 1.2.2 for interoperability
    private static final long serialVersionUID = -4023755556366636806L;

    private boolean all_allowed; // true if any all permissions have been added

    public AllPermissionCollection() {
        all_allowed = false;
    }

    public void add(Permission permission) {
        if (! (permission instanceof AllPermission))
            throw new IllegalArgumentException("invalid permission: "+
                    permission);
        if (isReadOnly())
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");

        all_allowed = true; // No sync; staleness OK
    }

    public boolean implies(Permission permission) {
        return all_allowed; // No sync; staleness OK
    }

    public Enumeration<Permission> elements() {
        return new Enumeration<Permission>() {
            private boolean hasMore = all_allowed;

            public boolean hasMoreElements() {
                return hasMore;
            }

            public Permission nextElement() {
                hasMore = false;
                return SecurityConstants.ALL_PERMISSION;
            }
        };
    }
}
