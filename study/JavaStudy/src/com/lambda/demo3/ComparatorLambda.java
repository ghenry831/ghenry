package com.lambda.demo3;

import java.util.Arrays;
import java.util.Comparator;

public class ComparatorLambda {
    public static void main(String[] args) {
        String[] strs = {"sdfsdf","dsd","ss","sdff"};
        //使用匿名内部类的方式实现比较器
        Comparator comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        };
        //Arrays.sort(strs,comparator);
        System.out.println(Arrays.toString(strs));

        //使用Lambda
        Arrays.sort(strs,(o1, o2) -> o1.length() - o2.length() );
        System.out.println(Arrays.toString(strs));
    }
}
