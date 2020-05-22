public class Racer implements Runnable {
     int d = 42;

     public void run () {
          System.out.println("Thread 2!");
          doSomething(1001);
          //d = 0;                              // (1)
     }

     public static void main (String[] args){
          Racer racer = new Racer();
          Thread t = new Thread(racer);
          t.start();
          
          Thread t2 = new Thread(racer);
          t2.start();

          System.out.println("Thread main!");
          doSomething(1000);
          int c = 420 / racer.d;              // (2)
          System.out.println(c);
     }
     
     static void doSomething (int n) {
          try { Thread.sleep(n); } catch (InterruptedException ix) {}
     }
}
