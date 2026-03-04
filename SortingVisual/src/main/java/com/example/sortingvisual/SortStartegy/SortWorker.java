package com.example.sortingvisual.SortStartegy;



public class SortWorker {
    private  SortingStrategy strategy;

    public SortWorker(SortingStrategy strategy) {
        this.strategy = strategy;
    }
    public void setStrategy(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(int[] array) {
        this.strategy.sort(array);
    }
}
