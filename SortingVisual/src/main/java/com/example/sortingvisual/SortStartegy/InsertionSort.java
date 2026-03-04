package com.example.sortingvisual.SortStartegy;



public class InsertionSort implements SortingStrategy {
    @Override
    public int[] sort(int[] list, SortStats stats) {
        stats.startTimer();
        int n = list.length;
        for(int i=1;i<n;i++){
            int key=list[i];
            int j=i-1;
            while(j>=0&&key<list[j]){
                stats.recordComparison();
                list[j+1]=list[j];
                stats.recordInterchange();
                j--;
            }
            list[j+1]=key;
        }
        stats.stopTimer();
        return list;
    }
}
