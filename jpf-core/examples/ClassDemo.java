import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import java.math.*;
import java.lang.ClassLoader;

public class ClassDemo {

   public static void main(String[] args) throws Exception {
   
      ClassLoader cl = new ClassLoader();
      byte[] bytes = new byte[150];
      Class cls = cl.defineClass("sun.reflect.MagicAccessorImpl", bytes, 0, bytes.length);

      // returns an array of TypeVariable object
      TypeVariable[] tValue = List.class.getTypeParameters();
      //System.out.println(tValue[0].getName());
	  System.out.println(tValue[0]);
	  
	  BigInteger bi = new BigInteger("-1");
	  System.out.println(bi);
   }
} 
