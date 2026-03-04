package com.example.sortingvisual.SortStartegy;

public class HeapSort implements SortingStrategy {

    @Override
    public int[] sort(int[] array, SortStats stats) {
        stats.startTimer();
        buildHeap(array,stats);
        int len=array.length;
        for(int i=len-1;i>0;i--){
            stats.recordInterchange();
            int temp=array[0];
            array[0]=array[i];
            array[i]=temp;
            heapifyDown(array, i, 0,stats);
        }
        stats.stopTimer();
        return array;
    }


    public void buildHeap(int [] array,SortStats stats){
        int n=array.length;
        for(int i=n/2-1;i>=0;i--){
            heapifyDown(array,n,i,stats);
        }

        
    }

    public void heapifyDown(int [] array,int n,int i,SortStats stats){
        int largest=i;
        int left=2*i+1;
        int right=2*i+2;
        if(left<n&&array[left]>array[largest]){
            stats.recordComparison();
            largest=left;
        }
        if(right<n&&array[right]>array[largest]){
            stats.recordComparison();
            largest=right;
        }
        if(largest!=i){
            stats.recordComparison();
            int temp=array[i];
            array[i]=array[largest];
            array[largest]=temp;
            stats.recordInterchange();

            heapifyDown(array, n, largest,stats);
        }
    }



}
