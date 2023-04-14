package com.lambda.demo3;

import java.util.function.Consumer;

public class ConsumerLambda {
    public static void main(String[] args) {
        consumerString(s -> {
            System.out.println(s);
        });

        consumerString(s -> System.out.println(s.toUpperCase()),
                s -> System.out.println(s.toLowerCase()));
    }

    static void consumerString(Consumer<String> function){
        function.accept("Hello");
    }

    static void consumerString(Consumer<String> first,Consumer<String> sercond){
        first.andThen(sercond).accept("Hello");
    }
}
