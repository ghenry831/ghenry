package com.lambda.demo2;

public class LambdaSyntaxTest {
    public static void main(String[] args) {
        //0.Lambda表达式基本格式
        IMathOperation mathOperation0 = (int a ,int b) -> {
            return a + b;
        };
        System.out.println(mathOperation0.operation(1,2));
        //1.省略大括号
        IGreeting greeting = (String msg) -> System.out.println("Hello " + msg);
        greeting.sayHello("Lambda");


        //2.省略小括号
        greeting = msg -> System.out.println("Hello " + msg);
        greeting.sayHello("2222");

        //3.省略return
        IMathOperation mathOperation1 = (int a ,int b) -> a - b;
        System.out.println(mathOperation1.operation(3,4));

        //4.省略参数类型和大括号
        IMathOperation mathOperation2 = ( a ,b) -> a * b;
        System.out.println(mathOperation2.operation(3,4));
    }
}
