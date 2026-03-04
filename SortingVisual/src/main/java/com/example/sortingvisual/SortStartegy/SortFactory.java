package com.example.sortingvisual.SortStartegy;

public class SortFactory {
    public static SortingStrategy create(String name){
        if(name==null){
            return null;
        }
        switch (name.toLowerCase()){
            case "insertion": return new InsertionSort();
            case "bubble": return new
        }
    }
}
