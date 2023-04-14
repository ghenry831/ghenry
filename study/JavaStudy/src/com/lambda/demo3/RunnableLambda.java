package com.lambda.demo3;

public class RunnableLambda {
    public static void main(String[] args) {
        //用匿名内部类实现多线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                System.out.println(name + "线程已启动1");
            }
        }).start();

        //使用Lambda表达式实现多线程
        new Thread(() -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + "线程已启动2");
        }).start();
    }
}
