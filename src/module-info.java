module HospitalManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;

    opens ui to javafx.fxml;
    exports ui;
    exports dao;
    exports model;
}
