package com.example.sortingvisual.SortStartegy;

public class MergeSort implements SortingStrategy {
    @Override
    public int[] sort(int[] array, SortStats stats) {
        stats.startTimer();
        mergeSort(array,stats);
        stats.stopTimer();
        return array;
    }
    public int[] mergeSort(int[] array, SortStats stats) {
        int len=array.length;
        if(len<=1){
            return array;
        }
        int middle=len/2;
        int [] leftArray=new int[middle];
        int [] rightArray=new int[len-middle];
        int i=0; // index for left array
        int j=0; // index for right array
        // Split the array into two halves
        for(;i<len;i++){
            if(i<middle){
                leftArray[i]=array[i];
            }
            else{
                rightArray[j]=array[i];
                j++;
            }
        }
        leftArray = mergeSort(leftArray, stats);
        rightArray = mergeSort(rightArray, stats);
        merge(leftArray, rightArray, array, stats);

        return array;
    }
    int [] merge(int[] leftArray, int[] rightArray, int[] array, SortStats stats) {
        int leftSize=array.length/2;
        int rightSize=array.length-leftSize;
        int i=0,l=0,r=0;
        while(l<leftSize&&r<rightSize){
            stats.recordComparison();
            if(leftArray[l]<rightArray[r]){
                array[i]=leftArray[l];
                l++;
            }
            else{
                stats.recordInterchange();
                array[i]=rightArray[r];
                r++;
            }
            i++;

        }
        //those are the remaining elements in left array or right array
        while(l<leftSize){
            array[i]=leftArray[l];
            l++;
            i++;
        }
        while(r<rightSize){
            array[i]=rightArray[r];
            r++;
            i++;
        }
        return array;
    }
}
