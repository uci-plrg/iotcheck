import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import java.math.BigInteger;
import java.security.ProtectionDomain;

import java.beans.Introspector;

public class Reflection {

    interface GenericSuperShort<XYZ> {

    }

    class GenericShort<TUVW,ABCD> {
    }

    class Generic<TUVW,ABCD,KLM,NOP,XYZ> extends GenericShort<TUVW,ABCD> implements GenericSuperShort<XYZ>, Serializable {

    }

    class SampleClass<VWXZ> {
        private String sampleField;

        public Class<?> setSampleField(Class<?> clazz,
			Class<? extends List> list, Class<? super Map> map, Class<?> clazz2, Class<VWXZ> clazz3,
			List<String> listString, Map<Integer,String> mapString, 
			Generic<Integer,String,Double,Short,Float> test, 
			String sampleField, int one, short two, double three, Object obj) {
            
			this.sampleField = sampleField;
            return clazz;
        }
		 
	   
	   /*public String getSampleField() {
		  return sampleField;
	   }*/
	   
	   /*public void setSampleField(String sampleField) {
	      this.sampleField = sampleField;
	   }
	   
	   public List<String> setSampleField(List<String> listString) {
		  return listString;
	   }*/
	}

   public static void main(String[] args) throws Exception {
	   
	  //BigInteger bi = new BigInteger("-1");
	  //System.out.println(bi);
	  /*StringBuilder sb = new StringBuilder(0);
	  sb.append('[');
	  sb.append(']');
	  System.out.println(sb.toString());*/
	  
	  //Class cls = Class.forName("groovy.runtime.metaclass.Logger.LoggerMetaClass");
	  //Class cls2 = Class.forName("groovy.runtime.metaclass.[Ljava.lang.Object;MetaClass");
	  //Class cls2 = Class.forName("[Ljava.lang.Object;BeanInfo");
	  Class cls = Object[].class;
	  System.out.println("Bean introspection do not ignore bean info: " + cls.getSimpleName());
	  System.out.println("Bean introspection do not ignore bean info: " + cls.getName());
      Object obj = Introspector.getBeanInfo(cls);
	   
	  /* TODO: Enumerate all methods in Class.class
      Method[] methods = Collection.class.getMethods();
      for(Method mth : methods) {
       	  System.out.println("===========================");
      	  //System.out.println("Method: " + mth.getName());
		  Type[] parameters = mth.getGenericParameterTypes();
		  for (int i = 0; i < parameters.length; i++) {
		     System.out.println(parameters[i]);
		  }
		  System.out.println();
		  Type returnType = mth.getGenericReturnType();
		  System.out.println(returnType + "\n");
      }*/

      /*Method[] methods = Collection.class.getMethods();
      //  Method[] methods = Class.class.getMethods();
        Method method = null;
        for(Method meth : methods) {
			System.out.println("===========================");
			//System.out.println("Method: " + meth.toString());
			Type[] parameters = meth.getGenericParameterTypes();
			for (int i = 0; i < parameters.length; i++) {
				System.out.println(parameters[i]);
			}
			Type returnType = meth.getGenericReturnType();
			System.out.println(returnType);
			System.out.println("===========================");
        }*/
	  
        /* TODO: This is an excerpt of the BigInteger library
		int radix = 10;
        String val = "-1";
        int signum = 0;
        final int[] mag;

        int cursor = 0, numDigits;
        final int len = val.length();

        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            throw new NumberFormatException("Radix out of range");
        if (len == 0)
            throw new NumberFormatException("Zero length BigInteger");

        // Check for at most one leading sign
        int sign = 1;
        int index1 = val.lastIndexOf('-');
        int index2 = val.lastIndexOf('+');
        if (index1 >= 0) {
            if (index1 != 0 || index2 >= 0) {
                throw new NumberFormatException("Illegal embedded sign character");
            }
            sign = -1;
            cursor = 1;
        } else if (index2 >= 0) {
            if (index2 != 0) {
                throw new NumberFormatException("Illegal embedded sign character");
            }
            cursor = 1;
        }
		System.out.println(cursor);
        if (cursor == len)
            throw new NumberFormatException("Zero length BigInteger");

        // Skip leading zeros and compute number of digits in magnitude
        while (cursor < len &&
                Character.digit(val.charAt(cursor), radix) == 0) {
            cursor++;
        }

        if (cursor == len) {
            signum = 0;
            //mag = ZERO.mag;
            //mag = null;
            return;
        }

        numDigits = len - cursor;
        signum = sign;

        long numBits = ((numDigits * bitsPerDigit[radix]) >>> 10) + 1;
        if (numBits + 31 >= (1L << 32)) {
            System.out.println("Overflow!");
        }
        int numWords = (int) (numBits + 31) >>> 5;
        int[] magnitude = new int[numWords];

        // Process first (potentially short) digit group
        int firstGroupLen = numDigits % digitsPerInt[radix];
        if (firstGroupLen == 0)
            firstGroupLen = digitsPerInt[radix];
		int cursor2 = cursor + firstGroupLen;
        String group = val.substring(cursor, cursor2);
        magnitude[numWords - 1] = Integer.parseInt(group, radix);
        if (magnitude[numWords - 1] < 0)
            throw new NumberFormatException("Illegal digit");*/
	  
      /*Type superCls = Generic.class.getGenericSuperclass();
      //Type superCls = String.class.getGenericSuperclass();
      System.out.println(superCls);
        System.out.println();
        Type[] interfaces = Generic.class.getGenericInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            System.out.println(interfaces[i]);
        }*/
      
      
	  /*Method[] methods = Map.class.getMethods();
	  Method method = null;
      for(Method mth : methods) {
        if (mth.getName().equals("putAll")) {
        //if (mth.getName().equals("isAssignableFrom")) {
        //if (mth.getName().equals("getSuperclass")) {
           method = mth;
		   break;
        }
      }
      Type[] parameters = method.getGenericParameterTypes();
      //Type[] parameters = methods[0].getGenericParameterTypes();
      for (int i = 0; i < parameters.length; i++) {
         System.out.println(parameters[i]);
      }
      System.out.println();
      Type returnType = method.getGenericReturnType();
      System.out.println(returnType);*/

      /*Class[] parameterTypes = methods[0].getParameterTypes();
      for(Class parameterType: parameterTypes){
         System.out.println(parameterType.getName());   
 
      }
      System.out.println();*/
      /*TypeVariable[] typeParameters = Generic.class.getTypeParameters();
      //TypeVariable[] typeParameters = SampleClass.class.getTypeParameters();
      for(TypeVariable typeVar: typeParameters){
         System.out.println(typeVar);   
 
      }
      System.out.println();

      Type[] bounds = typeParameters[0].getBounds();
      for (Type bound : bounds) {
          System.out.println(bound);
      }
      System.out.println();*/
	  
	  //ProtectionDomain pd = Class.class.getProtectionDomain();
      //System.out.println(pd);
   }
}

