package com.safe.campus;

public class main {


    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};



        int target = 3;
        int binarySearch = binarySearch(arr, target);
        System.out.println("The index of target is " + binarySearch);
    }

    public static int binarySearch(int[] arr, int t) {
        if (arr.length <= 0) {
            return -1;
        }
        int left = 0;
        int right = arr.length - 1;
        int middle;
        while (left <= right) {
            middle = arr[left + right] / 2;
            if (arr[middle] > t) {
                right = middle - 1;
            } else if (arr[middle] < t) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

}
