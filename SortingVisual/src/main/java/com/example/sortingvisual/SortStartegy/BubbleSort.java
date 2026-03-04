package com.example.sortingvisual.SortStartegy;

public class BubbleSort implements SortingStrategy {

    @Override
    public int[] sort(int[] array, SortStats stats) {
        int n=array.length;
        stats.startTimer();
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                stats.recordComparison();
                if(array[j]>array[j+1]){
                    stats.recordInterchange();
                    int temp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=temp;
                }
            }

        }
        stats.stopTimer();
        return array;
    }
    


}
