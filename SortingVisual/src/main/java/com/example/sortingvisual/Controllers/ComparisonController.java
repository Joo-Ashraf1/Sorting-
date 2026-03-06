package com.example.sortingvisual.Controllers;
import com.example.sortingvisual.Comparison.ComparisonResult;
import com.example.sortingvisual.SortStartegy.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.util.*;
public class ComparisonController implements Initializable {
    
    @FXML private CheckBox comp_cbBubble, comp_cbSelection, comp_cbInsertion;
    @FXML private CheckBox comp_cbQuick,  comp_cbMerge,    comp_cbHeap;

    @FXML private CheckBox comp_cbRandom, comp_cbSorted, comp_cbInverse;

    @FXML private TextField  comp_tfSizes;
    @FXML private TextField  comp_tfRuns;

    @FXML private Button     comp_btnRun;
    @FXML private Button     comp_btnClear;
    @FXML private Button     comp_btnFile;

    @FXML private ProgressBar comp_progress;
    @FXML private Label        comp_statusLabel;

    @FXML private TableView<ResultRow> comp_table;

    // Table columns (inject by fx:id)
    @FXML private TableColumn<ResultRow, String>  col_algorithm;
    @FXML private TableColumn<ResultRow, Integer> col_size;
    @FXML private TableColumn<ResultRow, String>  col_mode;
    @FXML private TableColumn<ResultRow, Integer> col_runs;
    @FXML private TableColumn<ResultRow, String>  col_avg;
    @FXML private TableColumn<ResultRow, String>  col_min;
    @FXML private TableColumn<ResultRow, String>  col_max;
    @FXML private TableColumn<ResultRow, Long>    col_comparisons;
    @FXML private TableColumn<ResultRow, Long>    col_interchanges;
    private final ObservableList<ResultRow> tableData = FXCollections.observableArrayList();
    private Task<?> activeTask;

 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        comp_progress.setVisible(false);
        comp_statusLabel.setText("Ready.");

        comp_tfSizes.setText("100, 500, 1000, 5000, 10000");
        comp_tfRuns.setText("5");

        comp_cbBubble.setSelected(true);
        comp_cbInsertion.setSelected(true);
        comp_cbMerge.setSelected(true);
        comp_cbQuick.setSelected(true);
        comp_cbRandom.setSelected(true);
    }
    private void setupTable() {
        col_algorithm   .setCellValueFactory(new PropertyValueFactory<>("algorithm"));
        col_size        .setCellValueFactory(new PropertyValueFactory<>("size"));
        col_mode        .setCellValueFactory(new PropertyValueFactory<>("mode"));
        col_runs        .setCellValueFactory(new PropertyValueFactory<>("runs"));
        col_avg         .setCellValueFactory(new PropertyValueFactory<>("avgMs"));
        col_min         .setCellValueFactory(new PropertyValueFactory<>("minMs"));
        col_max         .setCellValueFactory(new PropertyValueFactory<>("maxMs"));
        col_comparisons .setCellValueFactory(new PropertyValueFactory<>("comparisons"));
        col_interchanges.setCellValueFactory(new PropertyValueFactory<>("interchanges"));

        rightAlign(col_size); rightAlign(col_runs);
        rightAlign(col_avg);  rightAlign(col_min);  rightAlign(col_max);
        rightAlign(col_comparisons); rightAlign(col_interchanges);

        comp_table.setItems(tableData);
        comp_table.setPlaceholder(new Label("Run a comparison to see results here."));
    }

    private <T> void rightAlign(TableColumn<ResultRow, T> col) {
        col.setStyle("-fx-alignment: CENTER-RIGHT;");
    }


    @FXML
    private void onRun() {
        List<String> algoNames  = getSelectedAlgorithms();
        List<String> arrayModes = getSelectedModes();
        List<Integer> sizes     = parseSizes();
        int runs                = parseRuns();

        if (algoNames.isEmpty()) { status("Select at least one algorithm."); return; }
        if (arrayModes.isEmpty()) { status("Select at least one array type."); return; }
        if (sizes.isEmpty())     { status("Enter at least one valid array size."); return; }
        if (runs <= 0)           { status("Number of runs must be ≥ 1."); return; }

        runBenchmark(algoNames, arrayModes, sizes, runs, null, null);
    }

    @FXML
    private void onLoadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Integer File(s)");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files", "*.txt", "*.csv"));

        Window window = comp_btnFile.getScene().getWindow();
        List<File> files = fc.showOpenMultipleDialog(window);
        if (files == null || files.isEmpty()) return;

        List<String> algoNames = getSelectedAlgorithms();
        if (algoNames.isEmpty()) { status("Select at least one algorithm first."); return; }
        int runs = parseRuns();

        runBenchmark(algoNames, null, null, runs, files, null);
    }

    @FXML
    private void onClear() {
        tableData.clear();
        status("Table cleared.");
    }


    private void runBenchmark(
            List<String> algoNames,
            List<String> arrayModes,
            List<Integer> sizes,
            int runs,
            List<File> files,
            Void unused) {
        int totalWork = 0;
        if (files != null) {
            totalWork = algoNames.size() * files.size() * runs;
        } else {
            totalWork = algoNames.size() * (arrayModes != null ? arrayModes.size() : 0) * sizes.size() * runs;
        }
        final int finalTotal = Math.max(totalWork, 1);

        comp_btnRun.setDisable(true);
        comp_btnFile.setDisable(true);
        comp_progress.setVisible(true);
        comp_progress.setProgress(0);

        activeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int done = 0;

                if (files != null) {
                    for (File f : files) {
                        int[] original;
                        try { original = readFile(f); }
                        catch (Exception e) {
                            updateMessage("Error reading " + f.getName() + ": " + e.getMessage());
                            continue;
                        }
                        String label = f.getName();

                        for (String algoName : algoNames) {
                            SortingStrategy strategy = SortFactory.create(algoName);
                            ComparisonResult r = ComparisonResult.benchmark(
                                    strategy, displayName(algoName), original, label, runs);
                            addRow(r);
                            done += runs;
                            updateProgress(done, finalTotal);
                        }
                    }
                } else {
                    for (Integer size : sizes) {
                        for (String modeName : arrayModes) {
                            ArrayGenerator.ArrayType type = modeToType(modeName);
                            int[] original = ArrayGenerator.generate(size, type);

                            for (String algoName : algoNames) {
                                if (isCancelled()) return null;
                                SortingStrategy strategy = SortFactory.create(algoName);
                                ComparisonResult r = ComparisonResult.benchmark(
                                        strategy, displayName(algoName), original, modeName, runs);
                                addRow(r);
                                done += runs;
                                updateProgress(done, finalTotal);
                            }
                        }
                    }
                }
                return null;
            }

            private void addRow(ComparisonResult r) {
                javafx.application.Platform.runLater(() ->
                        tableData.add(new ResultRow(r)));
            }
        };

        activeTask.progressProperty().addListener((obs, o, n) ->
                comp_progress.setProgress(n.doubleValue()));
        activeTask.messageProperty().addListener((obs, o, n) -> status(n));

        activeTask.setOnSucceeded(e -> {
            status("Done. " + tableData.size() + " result(s) in table.");
            comp_progress.setVisible(false);
            comp_btnRun.setDisable(false);
            comp_btnFile.setDisable(false);
        });
        activeTask.setOnFailed(e -> {
            status("Error: " + activeTask.getException().getMessage());
            comp_progress.setVisible(false);
            comp_btnRun.setDisable(false);
            comp_btnFile.setDisable(false);
        });

        Thread t = new Thread(activeTask);
        t.setDaemon(true);
        t.start();
    }

    

    private List<String> getSelectedAlgorithms() {
        List<String> list = new ArrayList<>();
        if (comp_cbBubble   .isSelected()) list.add("bubble");
        if (comp_cbSelection.isSelected()) list.add("selection");
        if (comp_cbInsertion.isSelected()) list.add("insertion");
        if (comp_cbQuick    .isSelected()) list.add("quick");
        if (comp_cbMerge    .isSelected()) list.add("merge");
        if (comp_cbHeap     .isSelected()) list.add("heap");
        return list;
    }

    private List<String> getSelectedModes() {
        List<String> list = new ArrayList<>();
        if (comp_cbRandom .isSelected()) list.add("random");
        if (comp_cbSorted .isSelected()) list.add("sorted");
        if (comp_cbInverse.isSelected()) list.add("inversely sorted");
        return list;
    }

    private List<Integer> parseSizes() {
        List<Integer> sizes = new ArrayList<>();
        String raw = comp_tfSizes.getText().trim();
        for (String s : raw.split("[,\\s]+")) {
            try {
                int v = Integer.parseInt(s.trim());
                if (v > 0 && v <= 10000) sizes.add(v);
            } catch (NumberFormatException ignored) {}
        }
        return sizes;
    }

    private int parseRuns() {
        try { return Math.max(1, Integer.parseInt(comp_tfRuns.getText().trim())); }
        catch (NumberFormatException e) { return 1; }
    }

    private int[] readFile(File f) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append(",");
        }
        String[] parts = sb.toString().split("[,\\s]+");
        int[] arr = new int[parts.length];
        int count = 0;
        for (String p : parts) {
            try { arr[count++] = Integer.parseInt(p.trim()); }
            catch (NumberFormatException ignored) {}
        }
        return Arrays.copyOf(arr, count);
    }

    private ArrayGenerator.ArrayType modeToType(String mode) {
        switch (mode) {
            case "sorted":          return ArrayGenerator.ArrayType.SORTED;
            case "inversely sorted":return ArrayGenerator.ArrayType.INVERSELY_SORTED;
            default:                return ArrayGenerator.ArrayType.RANDOM;
        }
    }

    private String displayName(String key) {
        switch (key) {
            case "bubble":    return "Bubble Sort";
            case "selection": return "Selection Sort";
            case "insertion": return "Insertion Sort";
            case "quick":     return "Quick Sort";
            case "merge":     return "Merge Sort";
            case "heap":      return "Heap Sort";
            default:          return key;
        }
    }

    private void status(String msg) {
        javafx.application.Platform.runLater(() -> comp_statusLabel.setText(msg));
    }
    public static class ResultRow {
        private final SimpleStringProperty  algorithm;
        private final SimpleIntegerProperty size;
        private final SimpleStringProperty  mode;
        private final SimpleIntegerProperty runs;
        private final SimpleStringProperty  avgMs;
        private final SimpleStringProperty  minMs;
        private final SimpleStringProperty  maxMs;
        private final SimpleLongProperty    comparisons;
        private final SimpleLongProperty    interchanges;

        ResultRow(ComparisonResult r) {
            algorithm    = new SimpleStringProperty(r.algorithmName);
            size         = new SimpleIntegerProperty(r.arraySize);
            mode         = new SimpleStringProperty(r.arrayMode);
            runs         = new SimpleIntegerProperty(r.numberOfRuns);
            avgMs        = new SimpleStringProperty(String.format("%.3f ms", r.avgRuntimeMs));
            minMs        = new SimpleStringProperty(String.format("%.3f ms", r.minRuntimeMs));
            maxMs        = new SimpleStringProperty(String.format("%.3f ms", r.maxRuntimeMs));
            comparisons  = new SimpleLongProperty(r.comparisons);
            interchanges = new SimpleLongProperty(r.interchanges);
        }

        public String  getAlgorithm()   { return algorithm.get();   }
        public int     getSize()        { return size.get();        }
        public String  getMode()        { return mode.get();        }
        public int     getRuns()        { return runs.get();        }
        public String  getAvgMs()       { return avgMs.get();       }
        public String  getMinMs()       { return minMs.get();       }
        public String  getMaxMs()       { return maxMs.get();       }
        public long    getComparisons() { return comparisons.get(); }
        public long    getInterchanges(){ return interchanges.get();}
    }

}
