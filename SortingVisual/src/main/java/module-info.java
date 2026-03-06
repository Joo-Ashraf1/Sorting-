module com.example.sortingvisual {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;

    opens com.example.sortingvisual to javafx.fxml;
    opens com.example.sortingvisual.Controllers to javafx.fxml;
    opens com.example.sortingvisual.SortStartegy to javafx.fxml;
    opens com.example.sortingvisual.Comparison to javafx.fxml;

    exports com.example.sortingvisual;
    exports com.example.sortingvisual.Controllers;
    exports com.example.sortingvisual.SortStartegy;
    exports com.example.sortingvisual.Comparison;
}