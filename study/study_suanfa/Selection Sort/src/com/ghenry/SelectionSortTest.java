package com.ghenry;

public class SelectionSortTest {
    // 我们的算法类不允许产生任何实例
    private SelectionSortTest() {
    }

    public static void sort(int[] arr) {
        int tmp;
        for(int i=0;i<arr.length;i++){
            for(int j=i;j<arr.length;j++){
                if(arr[j]<arr[i]){
                    tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }



    public static void main(String[] args) {

        int[] arr = {10, 1, 6, 7, 8, 5, 4, 3, 2, 9};
        SelectionSortTest.sort(arr);
        for(int i:arr)
            System.out.print(i + " ");
    }
}