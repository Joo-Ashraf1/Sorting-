package com.example.sortingvisual.SortStartegy;

public class HeapSort implements SortingStrategy {

    @Override
    public int[] sort(int[] array) {
        buildHeap(array);
        int len=array.length;
        for(int i=len-1;i>0;i--){
            int temp=array[0];
            array[0]=array[i];
            array[i]=temp;
            heapifyDown(array, i, 0);
        }
        return array;
    }


    public void buildHeap(int [] array){
        int n=array.length;
        for(int i=n/2-1;i>=0;i--){
            heapifyDown(array,n,i);
        }

        
    }

    public void heapifyDown(int [] array,int n,int i){
        int largest=i;
        int left=2*i+1;
        int right=2*i+2;
        if(left<n&&array[left]>array[largest]){
            largest=left;
        }
        if(right<n&&array[right]>array[largest]){
            largest=right;
        }
        if(largest!=i){
            int temp=array[i];
            array[i]=array[largest];
            array[largest]=temp;
            heapifyDown(array, n, largest);
        }
    }



}
