package com.example.sortingvisual.Comparison;

import com.example.sortingvisual.SortStartegy.ArrayGenerator;
import com.example.sortingvisual.SortStartegy.SortStats;
import com.example.sortingvisual.SortStartegy.SortingStrategy;

public class ComparisonResult {
    public final String algorithmName;
    public final int    arraySize;
    public final String arrayMode;       
    public final int    numberOfRuns;
    public final double avgRuntimeMs;
    public final double minRuntimeMs;
    public final double maxRuntimeMs;
    public final long   comparisons;     
    public final long   interchanges;
    public ComparisonResult(String algorithmName, int arraySize, String arrayMode, int numberOfRuns,
                            double avgRuntimeMs, double minRuntimeMs, double maxRuntimeMs,
                            long comparisons, long interchanges) {
        this.algorithmName = algorithmName;
        this.arraySize = arraySize;
        this.arrayMode = arrayMode;
        this.numberOfRuns = numberOfRuns;
        this.avgRuntimeMs = avgRuntimeMs;
        this.minRuntimeMs = minRuntimeMs;
        this.maxRuntimeMs = maxRuntimeMs;
        this.comparisons = comparisons;
        this.interchanges = interchanges;
    }
    public static ComparisonResult benchmark(
            SortingStrategy strategy,
            String algorithmName,
            int[] original,
            String arrayMode,
            int runs) {

        double totalMs = 0, minMs = Double.MAX_VALUE, maxMs = 0;
        long lastComparisons = 0, lastInterchanges = 0;

        for (int i = 0; i < runs; i++) {
            int[] copy  = ArrayGenerator.copy(original);
            SortStats s = new SortStats();
            strategy.sort(copy, s);
            double ms = s.getRuntimeMs();
            totalMs  += ms;
            if (ms < minMs) minMs = ms;
            if (ms > maxMs) maxMs = ms;
            lastComparisons  = s.getComparisons();
            lastInterchanges = s.getInterchanges();
        }

        return new ComparisonResult(
                algorithmName, original.length, arrayMode,
                runs, totalMs / runs, minMs, maxMs,
                lastComparisons, lastInterchanges);
    }

    @Override
    public String toString() {
        return String.format(
            "%-16s | size=%-6d | %-18s | runs=%-2d | avg=%.3f ms | min=%.3f ms | max=%.3f ms | cmp=%-8d | swp=%d",
            algorithmName, arraySize, arrayMode, numberOfRuns,
            avgRuntimeMs, minRuntimeMs, maxRuntimeMs,
            comparisons, interchanges);
    }
}
