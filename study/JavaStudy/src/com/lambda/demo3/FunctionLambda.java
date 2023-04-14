package com.lambda.demo3;

import java.util.function.Function;

public class FunctionLambda {
    public static void main(String[] args) {
        method(str -> Integer.parseInt(str) + 10 ,integer -> integer + 3);

        String str = "测试,28";
        int age = getAgeNum(str,
                s -> s.split(",")[1],
                s -> Integer.parseInt(s),
                s -> s + 2);
        System.out.println(age);
    }
    static void method(Function<String,Integer> one ,Function<Integer,Integer> two){
        int num = one.andThen(two).apply("10");
        System.out.println(num + 20);
    }

    static int getAgeNum(String str,Function<String,String> one,Function<String,Integer> two,Function<Integer,Integer> three){
        return one.andThen(two).andThen(three).apply(str);
    }
}
