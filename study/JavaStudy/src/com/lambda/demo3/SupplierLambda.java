package com.lambda.demo3;

import java.util.function.Supplier;

/*
求数组最大值
 */
public class SupplierLambda {
    public static void main(String[] args) {
        int arr[] = {2,3,4,54,534,4234};
        int max = getMax(() ->{
            int temp = arr[0];
            for (int i = 1; i < arr.length;i++){
                if (arr[i] > temp){
                    temp = arr[i];
                }
            }
            return temp;
        });
        System.out.println(max);
    }

    public static int getMax(Supplier<Integer> supplier){
        return supplier.get();
    }
}
