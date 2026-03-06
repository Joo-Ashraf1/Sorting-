package com.example.sortingvisual.SortStartegy;

public class SortStep {
    public final int[] arraySnapshot;
    public final int indexA;
    public final int indexB;
    public final long comparisonsNow;
    public final long interchangesNow;
    public SortStep(int[] array, int a, int b, long cmp, long swp) {
        this.arraySnapshot  = ArrayGenerator.copy(array);
        this.indexA         = a;
        this.indexB         = b;
        this.comparisonsNow = cmp;
        this.interchangesNow = swp;
    }

}
