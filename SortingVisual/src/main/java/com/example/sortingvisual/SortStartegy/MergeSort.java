package com.example.sortingvisual.SortStartegy;

public class MergeSort implements SortingStrategy {
    @Override
    public int[] sort(int[] array) {
        if (array.length <= 1) {
            return array;
        }
        int[] temp = new int[array.length];
        mergeSort(array, temp, 0, array.length - 1);
        return array;
    }

    private void mergeSort(int[] array, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        mergeSort(array, temp, left, mid);
        mergeSort(array, temp, mid + 1, right);
        merge(array, temp, left, mid, right);
    }

    private void merge(int[] array, int[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) {
            temp[i] = array[i];
        }
        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                array[k] = temp[i];
                i++;
            } else {
                array[k] = temp[j];
                j++;
            }
            k++;
        }
        while (i <= mid) {
            array[k] = temp[i];
            i++;
            k++;
        }
        while (j <= right) {
            array[k] = temp[j];
            j++;
            k++;
        }
    }
}
