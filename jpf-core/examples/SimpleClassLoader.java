import java.util.Map;
import java.util.HashMap;

/**
 * Special class loader, which when running on Sun VM allows to generate accessor classes for any method
 */
public class SimpleClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        SimpleClassLoader sim = new SimpleClassLoader();
        //Class clsObj = sim.loadClass("[Ljava.lang.Object;BeanInfo;");
        
        //Class cls = sim.loadClass("java/lang/Object");
        Class cls = sim.loadClass("java.lang.Object");
        //Class cls = Class.forName("groovy.runtime.metaclass.[Ljava.lang.Object;MetaClass");
    }
}
