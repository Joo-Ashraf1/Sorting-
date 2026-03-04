package com.example.sortingvisual.SortStartegy;

import java.util.List;

public class InsertionSort implements SortingStrategy {
    @Override
    public int[] sort(int[] list) {
        int n = list.length;
        for(int i=1;i<n;i++){
            int key=list[i];
            int j=i-1;
            while(j>=0&&key<list[j]){
                list[j+1]=list[j];
                j--;
            }
            list[j+1]=key;
        }
        return list;
    }
}
