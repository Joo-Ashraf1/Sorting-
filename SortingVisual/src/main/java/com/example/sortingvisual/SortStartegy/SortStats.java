package com.example.sortingvisual.SortStartegy;

public class SortStats {
    private long comparisons   = 0;
    private long interchanges  = 0;
    private long startTimeNs   = 0;
    private long endTimeNs     = 0;

    public void recordComparison()  { comparisons++;  }
    public void recordInterchange() { interchanges++; }

    public void startTimer() { startTimeNs = System.nanoTime(); }
    public void stopTimer()  { endTimeNs   = System.nanoTime(); }


    public long getComparisons()  { return comparisons;  }
    public long getInterchanges() { return interchanges; }


    public double getRuntimeMs() {
        return (endTimeNs - startTimeNs) / 1_000_000.0;
    }

    public void reset() {
        comparisons  = 0;
        interchanges = 0;
        startTimeNs  = 0;
        endTimeNs    = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Runtime=%.3f ms | Comparisons=%d | Interchanges=%d",
                getRuntimeMs(), comparisons, interchanges
        );
    }
}
