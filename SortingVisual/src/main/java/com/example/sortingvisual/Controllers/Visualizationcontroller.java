package com.example.sortingvisual.Controllers;

import com.example.sortingvisual.SortStartegy.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
public class Visualizationcontroller implements Initializable {
    @FXML private TextField  vis_tfInput;
    @FXML private Slider     vis_sliderSpeed;
    @FXML private Label      vis_speedLabel;
    @FXML private Button     vis_btnVisualize;
    @FXML private Button     vis_btnStop;
    @FXML private Label      vis_statusLabel;

    @FXML private Canvas vis_canvas_bubble;
    @FXML private Canvas vis_canvas_selection;
    @FXML private Canvas vis_canvas_insertion;
    @FXML private Canvas vis_canvas_quick;
    @FXML private Canvas vis_canvas_merge;
    @FXML private Canvas vis_canvas_heap;

    @FXML private Label vis_cmp_bubble,    vis_swp_bubble,    vis_done_bubble;
    @FXML private Label vis_cmp_selection, vis_swp_selection, vis_done_selection;
    @FXML private Label vis_cmp_insertion, vis_swp_insertion, vis_done_insertion;
    @FXML private Label vis_cmp_quick,     vis_swp_quick,     vis_done_quick;
    @FXML private Label vis_cmp_merge,     vis_swp_merge,     vis_done_merge;
    @FXML private Label vis_cmp_heap,      vis_swp_heap,      vis_done_heap;

    private static final String[] ALGO_KEYS = {
        "bubble", "selection", "insertion", "quick", "merge", "heap"
    };

    private static final String[] ALGO_NAMES = {
        "BUBBLE SORT", "SELECTION SORT", "INSERTION SORT",
        "QUICK SORT",  "MERGE SORT",     "HEAP SORT"
    };

    private static final Color[] ALGO_COLORS = {
        Color.web("#00f5d4"),  
        Color.web("#f72585"),  
        Color.web("#ffd60a"),  
        Color.web("#4cc9f0"),  
        Color.web("#b14ef5"),  
        Color.web("#f77f00"), 
    };

    private final List<Task<Void>> runningTasks = new ArrayList<>();
    private int[] originalArray;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vis_btnStop.setDisable(true);
        vis_statusLabel.setText("Enter numbers and click VISUALIZE ALL.");

        vis_sliderSpeed.valueProperty().addListener((obs, o, n) -> {
            int ms = (int) vis_sliderSpeed.getValue();
            vis_speedLabel.setText(ms + " ms");
        });
        vis_speedLabel.setText((int) vis_sliderSpeed.getValue() + " ms");

        vis_tfInput.setText("38, -5, 72, 0, -19, 45, 13, -3, 60, 27, -11, 55, 8, -30, 42");

        for (int i = 0; i < ALGO_KEYS.length; i++) {
            Canvas c = getCanvas(i);
            if (c != null) drawIdle(c, ALGO_COLORS[i]);
        }
    }

    @FXML
    private void onVisualize() {
        int[] parsed = parseInput();
        if (parsed == null || parsed.length < 2) {
            vis_statusLabel.setText("Enter at least 2 numbers (comma-separated, max 100).");
            return;
        }
        if (parsed.length > 100) {
            vis_statusLabel.setText("Visualization supports up to 100 elements.");
            return;
        }

        originalArray = parsed;
        stopAll();
        runningTasks.clear();
        resetStatLabels();

        long delayMs = (long) vis_sliderSpeed.getValue();

        vis_btnVisualize.setDisable(true);
        vis_btnStop.setDisable(false);
        vis_statusLabel.setText("Visualizing " + parsed.length + " elements across 6 algorithms…");

        for (int i = 0; i < ALGO_KEYS.length; i++) {
            launchAlgorithm(i, ArrayGenerator.copy(originalArray), delayMs);
        }
    }

    @FXML
    private void onStop() {
        stopAll();
        vis_statusLabel.setText("Stopped.");
        vis_btnVisualize.setDisable(false);
        vis_btnStop.setDisable(true);
    }


    private void launchAlgorithm(int index, int[] array, long delayMs) {
        Canvas canvas   = getCanvas(index);
        Label cmpLabel  = getCmpLabel(index);
        Label swpLabel  = getSwpLabel(index);
        Label doneLabel = getDoneLabel(index);
        Color color     = ALGO_COLORS[index];

        if (canvas == null) return;

        Visualizablesortrecorder recorder = new Visualizablesortrecorder(ALGO_KEYS[index]);
        SortStats stats = new SortStats();
        recorder.sort(ArrayGenerator.copy(array), stats);
        List<SortStep> steps = recorder.getSteps();

        drawStep(canvas, steps.isEmpty() ? new SortStep(array, -1, -1, 0, 0) : steps.get(0), color);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                for (SortStep step : steps) {
                    if (isCancelled()) break;
                    Platform.runLater(() -> {
                        drawStep(canvas, step, color);
                        if (cmpLabel != null)
                            cmpLabel.setText("CMP: " + step.comparisonsNow);
                        if (swpLabel != null)
                            swpLabel.setText("SWP: " + step.interchangesNow);
                    });
                    Thread.sleep(delayMs);
                }
                if (!isCancelled()) {
                    Platform.runLater(() -> {
                        if (!steps.isEmpty()) {
                            SortStep last = steps.get(steps.size() - 1);
                            drawFinal(canvas, last.arraySnapshot, color);
                            if (cmpLabel  != null) cmpLabel.setText("CMP: " + last.comparisonsNow);
                            if (swpLabel  != null) swpLabel.setText("SWP: " + last.interchangesNow);
                        }
                        if (doneLabel != null) doneLabel.setText("✓ DONE");
                        checkAllDone();
                    });
                }
                return null;
            }
        };

        runningTasks.add(task);
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private void checkAllDone() {
        boolean allDone = runningTasks.stream().allMatch(t -> t.isDone() || t.isCancelled());
        if (allDone) {
            vis_btnVisualize.setDisable(false);
            vis_btnStop.setDisable(true);
            vis_statusLabel.setText("All algorithms completed.");
        }
    }

    private void stopAll() {
        for (Task<Void> t : runningTasks) {
            if (!t.isDone()) t.cancel();
        }
    }


    private void drawStep(Canvas canvas, SortStep step, Color color) {
        drawBars(canvas, step.arraySnapshot, step.indexA, step.indexB, color, false);
    }

    private void drawFinal(Canvas canvas, int[] array, Color color) {
        drawBars(canvas, array, -1, -1, color, true);
    }

    private void drawIdle(Canvas canvas, Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.setFill(Color.web("#0a0e1a"));
        gc.fillRect(0, 0, w, h);
        gc.setStroke(color.deriveColor(0, 1, 1, 0.15));
        gc.setLineWidth(1);
        gc.strokeRect(0.5, 0.5, w - 1, h - 1);
    }
    // i want to handle if the array have negativee numbers
 
    private void drawBars(Canvas canvas, int[] array, int idxA, int idxB,
                          Color baseColor, boolean allSorted) {
        if (array == null || array.length == 0) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double cw = canvas.getWidth();
        double ch = canvas.getHeight();

        gc.setFill(Color.web("#0a0e1a"));
        gc.fillRect(0, 0, cw, ch);

        
        gc.setStroke(Color.web("#1a2540"));
        gc.setLineWidth(0.5);
        for (int row = 1; row < 4; row++) {
            double y = ch * row / 4.0;
            gc.strokeLine(0, y, cw, y);
        }

        int n = array.length;
        int min = array[0], max = array[0];
        for (int v : array) { if (v < min) min = v; if (v > max) max = v; }

        
        int shift = -min;  
        int range = (max - min);
        if (range == 0) range = 1; // i had corner case with {3,3,3,3}

        double padding = 2.0;
        double barW  = Math.max(1.0, (cw - padding * (n + 1)) / n);
        double maxBarH = ch - 4;

        for (int i = 0; i < n; i++) {
            double normalised = (double)(array[i] + shift) / range; // 0.0 – 1.0
            double barH = Math.max(2.0, normalised * maxBarH);
            double x = padding + i * (barW + padding);
            double y = ch - barH;

            Color barColor;
            if (allSorted) {
                barColor = baseColor;
            } else if (i == idxA) {
                barColor = Color.web("#ffffff");        // comparing: white flash
            } else if (i == idxB) {
                barColor = Color.web("#f72585");        
            } else {
                barColor = baseColor.deriveColor(0, 0.7, 0.6, 1.0);
            }

            gc.setFill(barColor.deriveColor(0, 1, 1, allSorted ? 0.85 : 0.7));
            gc.fillRect(x, y, barW, barH);
            gc.setStroke(barColor);
            gc.setLineWidth(1.0);
            gc.strokeLine(x, y, x + barW, y);
        }
        gc.setStroke(baseColor.deriveColor(0, 1, 1, 0.2));
        gc.setLineWidth(1);
        gc.strokeRect(0.5, 0.5, cw - 1, ch - 1);
    }

    private int[] parseInput() {
        String raw = vis_tfInput.getText().trim();
        if (raw.isEmpty()) return null;
        String[] parts = raw.split("[,\\s]+");
        List<Integer> vals = new ArrayList<>();
        for (String p : parts) {
            try { vals.add(Integer.parseInt(p.trim())); }
            catch (NumberFormatException ignored) {}
        }
        if (vals.isEmpty()) return null;
        int[] arr = new int[vals.size()];
        for (int i = 0; i < vals.size(); i++) arr[i] = vals.get(i);
        return arr;
    }

    // oos some helper method 

    private Canvas getCanvas(int i) {
        switch (i) {
            case 0: return vis_canvas_bubble;
            case 1: return vis_canvas_selection;
            case 2: return vis_canvas_insertion;
            case 3: return vis_canvas_quick;
            case 4: return vis_canvas_merge;
            case 5: return vis_canvas_heap;
            default: return null;
        }
    }

    private Label getCmpLabel(int i) {
        switch (i) {
            case 0: return vis_cmp_bubble;
            case 1: return vis_cmp_selection;
            case 2: return vis_cmp_insertion;
            case 3: return vis_cmp_quick;
            case 4: return vis_cmp_merge;
            case 5: return vis_cmp_heap;
            default: return null;
        }
    }

    private Label getSwpLabel(int i) {
        switch (i) {
            case 0: return vis_swp_bubble;
            case 1: return vis_swp_selection;
            case 2: return vis_swp_insertion;
            case 3: return vis_swp_quick;
            case 4: return vis_swp_merge;
            case 5: return vis_swp_heap;
            default: return null;
        }
    }

    private Label getDoneLabel(int i) {
        switch (i) {
            case 0: return vis_done_bubble;
            case 1: return vis_done_selection;
            case 2: return vis_done_insertion;
            case 3: return vis_done_quick;
            case 4: return vis_done_merge;
            case 5: return vis_done_heap;
            default: return null;
        }
    }

    private void resetStatLabels() {
        for (int i = 0; i < 6; i++) {
            Label c = getCmpLabel(i), s = getSwpLabel(i), d = getDoneLabel(i);
            if (c != null) c.setText("CMP: 0");
            if (s != null) s.setText("SWP: 0");
            if (d != null) d.setText("● RUNNING");
        }
    }
}