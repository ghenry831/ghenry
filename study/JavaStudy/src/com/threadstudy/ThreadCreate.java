package com.threadstudy;

public class ThreadCreate {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(ThreadCreate::action, "t1");
        Thread t2 = new Thread(ThreadCreate::action, "t2");
        Thread t3 = new Thread(ThreadCreate::action, "t3");

        t1.start();
        t1.join();

        t2.start();
        t2.join();

        t3.start();
        t3.join();
    }


    public static void action() {
        System.out.printf("线程 [%s] 正在运行\n", Thread.currentThread().getName());
    }
}