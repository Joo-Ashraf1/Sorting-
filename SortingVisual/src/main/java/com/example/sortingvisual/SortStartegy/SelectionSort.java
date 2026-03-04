package com.example.sortingvisual.SortStartegy;

public class SelectionSort implements SortingStrategy  {

    @Override
    public int[] sort(int[] array, SortStats stats) {
        stats.startTimer();
        int n=array.length;
        for(int i=0;i<n;i++){
            int min_idx=i;
            for(int j=i+1;j<n;j++){
                stats.recordComparison();
                if(array[min_idx]>array[j]){
                    min_idx=j;
                }

            }
            if(min_idx!=i){
                stats.recordInterchange();
                int temp=array[i];
                array[i]=array[min_idx];
                array[min_idx]=temp;
            }


        }
        stats.stopTimer();
        return array;
        
    }
    
}
