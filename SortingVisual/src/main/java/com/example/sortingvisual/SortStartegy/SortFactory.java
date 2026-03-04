package com.example.sortingvisual.SortStartegy;

public class SortFactory {
    public static SortingStrategy create(String name){
        if(name==null){
            return null;
        }
        switch (name.toLowerCase()){
            case "insertion": return new InsertionSort();
            case "bubble": return new  BubbleSort();
            case "selection": return new SelectionSort();
            case "quick": return new QuickSort();
            case "merge": return new MergeSort();
            case "heap": return new HeapSort();
            default: return null;
        }
    }
}
