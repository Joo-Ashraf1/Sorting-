package com.example.sortingvisual.SortStartegy;

import java.util.Random;

public class QuickSort implements SortingStrategy {

    @Override
    public int[] sort(int[] array) {
        if(array==null||array.length<=1){
            return array;
        }
        quickSort(array,0,array.length-1);
        return array;
    }

    private void quickSort(int[]array,int l,int h){
        if(l<h){
            int pivot=partition(array, l, h);
            quickSort(array, l, pivot-1);
            quickSort(array, pivot+1, h);
        }

    }

    private int partition(int[]array,int l,int h){
        Random rand=new Random();
        int pivotIndex=rand.nextInt(h-l+1)+l;
        exchange(array, h, pivotIndex);
        int pivot=array[h];
        int i=l-1;
        for(int j=l;j<h;j++){
            if(array[j]<=pivot){
                i++;
                exchange(array, i, j);

            }
        }
        exchange(array, i+1, h);
        return i+1;

    }

    private void exchange(int[]array,int l,int h){
        int temp=array[l];
        array[l]=array[h];
        array[h]=temp;
    }

}
