package com.ghenry;

public class InsertionSortTest {
    public static void main(String[] args) {
        int[] arr = {10, 1, 6, 7, 8, 5, 4, 3, 2, 9};
        int tmp;
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j - 1] > arr[j]) {
                    tmp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }

        for (int z : arr)
            System.out.print(z + " ");
    }
}
