package com.example.sortingvisual.SortStartegy;

import java.util.List;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class SortVisualizationTask extends Task<Void> {
    private final List<SortStep> steps;
    private final long delayMs;
    private final Consumer<SortStep> onStep;

    public SortVisualizationTask(List<SortStep> steps, long delayMs, Consumer<SortStep> onStep) {
        this.steps = steps;
        this.delayMs = delayMs;
        this.onStep = onStep;
    }


    @Override
    protected Void call() throws Exception {
        for(int i=0;i<steps.size();i++){
            if(isCancelled()) break;
            final SortStep step = steps.get(i);
            Platform.runLater(() -> onStep.accept(step));

            updateProgress(i + 1, steps.size());
            Thread.sleep(delayMs);
        }
        return null;
    }

}
