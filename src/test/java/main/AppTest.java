package main;

class AppTest {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadTest());
        Thread thread2 = new Thread(new ThreadTest());
        thread1.start();
        thread2.start();
    }
}