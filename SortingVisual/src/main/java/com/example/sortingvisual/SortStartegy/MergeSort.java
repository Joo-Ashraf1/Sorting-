package com.example.sortingvisual.SortStartegy;

public class MergeSort implements SortingStrategy {
    @Override
    public int[] sort(int[] array) {
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
        leftArray = sort(leftArray);
        rightArray = sort(rightArray);
        merge(leftArray, rightArray, array);

        return array;
    }
    int [] merge(int[] leftArray, int[] rightArray, int[] array) {
        int leftSize=array.length/2;
        int rightSize=array.length-leftSize;
        int i=0,l=0,r=0;
        while(l<leftSize&&r<rightSize){
            if(leftArray[l]<rightArray[r]){
                array[i]=leftArray[l];
                l++;
            }
            else{
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
