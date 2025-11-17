package com.own.auto;

public class ThreadExample {
    public static void main(String[] args) {
        System.out.println("Main Thread");
        Thread t = new Thread(() -> System.out.println("thread1 start"));
        t.start();
        Thread t2 =new MyThread();
        t2.start();
    }

}
class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("thread1 start");
    }
}
class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("thread2 start");
    }
}
