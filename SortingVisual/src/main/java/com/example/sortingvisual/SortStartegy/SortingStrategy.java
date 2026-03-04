package com.example.sortingvisual.SortStartegy;



public interface SortingStrategy {
    int[] sort(int[] array,SortStats sortStats);
    default int[] sort(int[] array) {
        return sort(array, new SortStats());
    }
}
