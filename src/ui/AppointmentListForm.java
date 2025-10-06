package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import dao.AppointmentDAO;
import dao.PatientDAO;
import dao.DoctorDAO;
import model.Appointment;
import model.Patient;
import model.Doctor;

import javafx.beans.property.SimpleStringProperty;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class AppointmentListForm {

    private static TableView<Appointment> table;
    private static ObservableList<Appointment> appointments;

    public static void showForm() {
        Stage stage = new Stage();
        stage.setTitle("Appointments List");

        table = new TableView<>();

        // --- Columns ---
        TableColumn<Appointment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(cellData -> {
            Patient p = PatientDAO.getPatientById(cellData.getValue().getPatientId());
            String name = p != null ? p.getName() : "Unknown";
            return new SimpleStringProperty(cellData.getValue().getPatientId() + " - " + name);
        });

        TableColumn<Appointment, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(cellData -> {
            Doctor d = DoctorDAO.getDoctorById(cellData.getValue().getDoctorId());
            String name = d != null ? d.getName() : "Unknown";
            return new SimpleStringProperty(cellData.getValue().getDoctorId() + " - " + name);
        });

        TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Appointment, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");

            {
                editBtn.setMinWidth(80);
                editBtn.setMinHeight(20);
                editBtn.setStyle(
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 15;" +
                        "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 3 15 3 15;"
                );

                editBtn.setOnAction(e -> {
                    Appointment a = getTableView().getItems().get(getIndex());
                    AppointmentForm.showForm(a);
                    AppointmentListForm.refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    HBox box = new HBox(editBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(idCol, patientCol, doctorCol, dateCol, timeCol, statusCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- Load data ---
        appointments = FXCollections.observableArrayList(AppointmentDAO.getAllAppointments());

        // --- Search & heading ---
        Label heading = new Label("Appointments Records");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #5C4444;");
        heading.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID, Patient, Doctor or Status...");
        searchField.setMaxWidth(350);
        searchField.setPrefHeight(35);
        searchField.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-padding: 5 10 5 10;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;" +
                "-fx-border-color: #896C6C;" +
                "-fx-border-width: 2;" +
                "-fx-focus-color: #896C6C;" +
                "-fx-faint-focus-color: transparent;"
        );

        FilteredList<Appointment> filteredData = new FilteredList<>(appointments, a -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(a -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lc = newVal.toLowerCase();

                Patient p = PatientDAO.getPatientById(a.getPatientId());
                Doctor d = DoctorDAO.getDoctorById(a.getDoctorId());

                boolean matchPatient = p != null && p.getName().toLowerCase().contains(lc);
                boolean matchDoctor = d != null && d.getName().toLowerCase().contains(lc);
                boolean matchStatus = a.getStatus().toLowerCase().contains(lc);
                boolean matchId = String.valueOf(a.getAppointmentId()).contains(lc);

                return matchPatient || matchDoctor || matchStatus || matchId;
            });
        });

        SortedList<Appointment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // --- Left button for AppointmentForm ---
        Button backBtn = new Button("←");
        backBtn.setShape(new Circle(30));
        backBtn.setMinSize(60, 60); backBtn.setMaxSize(60, 60);
        backBtn.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-background-radius: 30;" +
            "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0;"
        );
        backBtn.setOnAction(e -> AppointmentForm.showForm());
        addHoverEffect(backBtn, 10);


        // --- Heading & search VBox (centered) ---
        VBox headingSearchBox = new VBox(10, heading, searchField);
        headingSearchBox.setAlignment(Pos.CENTER);

        // --- Top StackPane to place left button + heading center ---
        StackPane topStack = new StackPane();
        topStack.setPadding(new Insets(20));
        topStack.getChildren().addAll(headingSearchBox, backBtn);
        StackPane.setAlignment(headingSearchBox, Pos.CENTER);
        StackPane.setAlignment(backBtn, Pos.CENTER_LEFT);

        // --- Root layout ---
        BorderPane root = new BorderPane();
        root.setTop(topStack);
        root.setCenter(table);

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private static void addHoverEffect(Button btn, double slideX) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(250), btn);
        scaleUp.setToX(1.5); scaleUp.setToY(1.5);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(250), btn);
        scaleDown.setToX(1.0); scaleDown.setToY(1.0);
        TranslateTransition slide = new TranslateTransition(Duration.millis(250), btn);
        slide.setByX(slideX);
        TranslateTransition slideBack = new TranslateTransition(Duration.millis(250), btn);
        slideBack.setByX(-slideX);

        btn.setOnMouseEntered(e -> { scaleUp.playFromStart(); slide.playFromStart(); });
        btn.setOnMouseExited(e -> { scaleDown.playFromStart(); slideBack.playFromStart(); });
    }

    public static void refreshTable() {
        if (appointments != null) {
            appointments.setAll(AppointmentDAO.getAllAppointments());
        }
    }

    public static void main(String[] args) {
        showForm();
    }
}