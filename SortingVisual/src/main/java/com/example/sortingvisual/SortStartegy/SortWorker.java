package com.example.sortingvisual.SortStartegy;



public class SortWorker {
    private  SortingStrategy strategy;
    private SortStats lastStats = new SortStats();

    public void setStrategy(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public int[] sort(int[] array) {
        lastStats = new SortStats();
        strategy.sort(array, lastStats);
        return strategy.sort(array, lastStats);
    }
    public SortStats getLastStats() {
        return lastStats;
    }
}
