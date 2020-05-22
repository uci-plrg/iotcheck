package sun.reflect;

/**
 * MJI model class for sun.reflect.generics.reflectiveObjects.MagicAccessorImpl
 *
 * This is a JPF specific version of a system class because we can't use the real,
 * platform VM specific version (it's native all over the place, its field
 * structure isn't documented, most of its methods are private, hence we can't
 * even instantiate it properly).
 *
 * Note that this class never gets seen by the real VM - it's for JPF's eyes only.
 *
 */
public class MagicAccessorImpl {
}
