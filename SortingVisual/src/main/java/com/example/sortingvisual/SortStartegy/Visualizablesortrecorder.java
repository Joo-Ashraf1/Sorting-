package com.example.sortingvisual.SortStartegy;
import java.util.ArrayList;
import java.util.List;
public class Visualizablesortrecorder implements SortingStrategy {
    private String algoKey;
    private List <SortStep> steps=new ArrayList<>();
    public Visualizablesortrecorder(String algoKey) {
        this.algoKey = algoKey;
    }
    @Override
    public int[] sort(int[] array, SortStats stats) {
        steps.clear();
        switch (algoKey.toLowerCase()) {
            case "bubble":    return bubbleSort(array, stats);
            case "selection": return selectionSort(array, stats);
            case "insertion": return insertionSort(array, stats);
            case "quick":     return quickSort(array, stats);
            case "merge":     return mergeSort(array, stats);
            case "heap":      return heapSort(array, stats);
            default:          return array;
        }
    }

    public List<SortStep> getSteps() {
         return steps; 
    }

     private void snap(int[] a, int idxA, int idxB, SortStats s) {
        steps.add(new SortStep(a, idxA, idxB, s.getComparisons(), s.getInterchanges()));
    }

    private void swap(int[] a, int i, int j, SortStats s) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
        s.recordInterchange();
        snap(a, i, j, s);
    }

    private int[] bubbleSort(int[] a, SortStats s) {
        s.startTimer();
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                s.recordComparison();
                snap(a, j, j + 1, s);
                if (a[j] > a[j + 1]) swap(a, j, j + 1, s);
            }
        }
        s.stopTimer();
        return a;
    }
    private int[] selectionSort(int[] a, SortStats s) {
        s.startTimer();
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                s.recordComparison();
                snap(a, minIdx, j, s);
                if (a[j] < a[minIdx]) minIdx = j;
            }
            if (minIdx != i) swap(a, i, minIdx, s);
        }
        s.stopTimer();
        return a;
    }

    private int[] insertionSort(int[] a, SortStats s) {
        s.startTimer();
        int n = a.length;
        for (int i = 1; i < n; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                s.recordComparison();
                snap(a, j, j + 1, s);
                a[j + 1] = a[j];
                s.recordInterchange();
                j--;
            }
            if (j >= 0) s.recordComparison();
            a[j + 1] = key;
            snap(a, j + 1, -1, s);
        }
        s.stopTimer();
        return a;
    }
    private int[] quickSort(int[] a, SortStats s) {
        s.startTimer();
        quickSortRec(a, 0, a.length - 1, s);
        s.stopTimer();
        return a;
    }

    private void quickSortRec(int[] a, int lo, int hi, SortStats s) {
        if (lo < hi) {
            int p = partition(a, lo, hi, s);
            quickSortRec(a, lo, p - 1, s);
            quickSortRec(a, p + 1, hi, s);
        }
    }

    private int partition(int[] a, int lo, int hi, SortStats s) {
        int pivIdx = lo + (hi - lo) / 2;
        swap(a, pivIdx, hi, s);
        int pivot = a[hi], i = lo - 1;
        for (int j = lo; j < hi; j++) {
            s.recordComparison();
            snap(a, j, hi, s);
            if (a[j] <= pivot) { i++; swap(a, i, j, s); }
        }
        swap(a, i + 1, hi, s);
        return i + 1;
    }


    private int[] mergeSort(int[] a, SortStats s) {
        s.startTimer();
        mergeSortRec(a, s);
        s.stopTimer();
        return a;
    }

    private void mergeSortRec(int[] a, SortStats s) {
        if (a.length <= 1) return;
        int mid = a.length / 2;
        int[] left  = java.util.Arrays.copyOfRange(a, 0, mid);
        int[] right = java.util.Arrays.copyOfRange(a, mid, a.length);
        mergeSortRec(left, s);
        mergeSortRec(right, s);
        mergeInto(a, left, right, s);
    }

    private void mergeInto(int[] dest, int[] left, int[] right, SortStats s) {
        int l = 0, r = 0, i = 0;
        while (l < left.length && r < right.length) {
            s.recordComparison();
            if (left[l] <= right[r]) {
                dest[i++] = left[l++];
            } else {
                dest[i++] = right[r++];
                s.recordInterchange();
            }
            snap(dest, i - 1, -1, s);
        }
        while (l < left.length) { dest[i++] = left[l++]; snap(dest, i-1, -1, s); }
        while (r < right.length){ dest[i++] = right[r++]; snap(dest, i-1, -1, s); }
    }



    private int[] heapSort(int[] a, SortStats s) {
        s.startTimer();
        buildHeap(a, s);
        for (int i = a.length - 1; i > 0; i--) {
            swap(a, 0, i, s);
            heapifyDown(a, i, 0, s);
        }
        s.stopTimer();
        return a;
    }

    private void buildHeap(int[] a, SortStats s) {
        for (int i = a.length / 2 - 1; i >= 0; i--)
            heapifyDown(a, a.length, i, s);
    }

    private void heapifyDown(int[] a, int n, int i, SortStats s) {
        int largest = i, l = 2*i+1, r = 2*i+2;
        if (l < n) { s.recordComparison(); snap(a, i, l, s); if (a[l] > a[largest]) largest = l; }
        if (r < n) { s.recordComparison(); snap(a, i, r, s); if (a[r] > a[largest]) largest = r; }
        if (largest != i) { swap(a, i, largest, s); heapifyDown(a, n, largest, s); }
    }




}
