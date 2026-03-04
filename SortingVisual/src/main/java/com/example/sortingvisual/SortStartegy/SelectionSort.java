package com.example.sortingvisual.SortStartegy;

public class SelectionSort implements SortingStrategy  {

    @Override
    public int[] sort(int[] array) {
        int n=array.length;
        for(int i=0;i<n;i++){
            int min_idx=i;
            for(int j=i+1;j<n;j++){
                if(array[min_idx]>array[j]){
                    min_idx=j;
                }

            }
            if(min_idx!=i){
                int temp=array[i];
                array[i]=array[min_idx];
                array[min_idx]=temp;
            }


        }
        return array;
        
    }
    
}
