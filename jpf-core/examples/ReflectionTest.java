import java.lang.reflect.*;
import java.util.Collection;

public class ReflectionTest {

    public Collection<String> c;

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println(Collection.class.getTypeParameters()[0]); // E
        Field field = ReflectionTest.class.getField("c");
        System.out.println(field.getGenericType()); // java.util.Collection<java.lang.String>
    }

}
