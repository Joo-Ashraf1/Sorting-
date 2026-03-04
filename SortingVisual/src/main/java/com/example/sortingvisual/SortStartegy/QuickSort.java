package com.example.sortingvisual.SortStartegy;

import java.util.Random;

public class QuickSort implements SortingStrategy {

    @Override
    public int[] sort(int[] array, SortStats stats) {
        stats.startTimer();
        if(array==null||array.length<=1){
            return array;
        }
        quickSort(array,0,array.length-1, stats);
        stats.stopTimer();
        return array;
    }

    private void quickSort(int[]array,int l,int h, SortStats stats){
        if(l<h){
            int pivot=partition(array, l, h, stats);
            quickSort(array, l, pivot-1, stats);
            quickSort(array, pivot+1, h, stats);
        }

    }

    private int partition(int[]array,int l,int h, SortStats stats){
        Random rand=new Random();
        int pivotIndex=rand.nextInt(h-l+1)+l;
        exchange(array, h, pivotIndex, stats);
        int pivot=array[h];
        int i=l-1;
        for(int j=l;j<h;j++){
            stats.recordComparison();
            if(array[j]<=pivot){
                i++;
                exchange(array, i, j, stats);

            }
        }
        exchange(array, i+1, h, stats);
        return i+1;

    }

    private void exchange(int[]array,int l,int h, SortStats stats){
        stats.recordInterchange();
        int temp=array[l];
        array[l]=array[h];
        array[h]=temp;
    }

}
