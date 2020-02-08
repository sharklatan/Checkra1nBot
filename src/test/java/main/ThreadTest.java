package main;

public class ThreadTest implements Runnable {
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Executing run method");
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
        System.out.println("Done");
    }
}