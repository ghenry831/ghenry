package com.lambda.demo3;

import java.util.function.Predicate;

//断言
public class PredicateLambda {
    public static void main(String[] args) {

        andMethod(s -> s.contains("w"),s -> s.contains("H"));
        orMethod(s -> s.contains("W"),s -> s.contains("H"));
        negateMethod(s -> s.length() < 5);
    }

    static void andMethod(Predicate<String> one,Predicate<String> two){
        boolean isVaild = one.and(two).test("Helloworld");
        System.out.println("字符串符合要求吗：" + isVaild);
    }

    static void orMethod(Predicate<String> one,Predicate<String> two){
        boolean isVaild = one.or(two).test("Helloworld");
        System.out.println("字符串符合要求吗：" + isVaild);
    }

    static void negateMethod(Predicate<String> predicate){
        boolean veryLong = predicate.negate().test("Helloworld");
        System.out.println("字符串很长吗：" + veryLong);
    }

}
