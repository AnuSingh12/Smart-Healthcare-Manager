package ui;

import dao.PatientDAO;
import model.Patient;
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
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.layout.HBox;

public class PatientListForm {

    private static TableView<Patient> table;
    private static ObservableList<Patient> data;

    public void start(Stage stage) {
        stage.setTitle("Patient List");

        table = new TableView<>();
        data = FXCollections.observableArrayList(PatientDAO.getAllPatients());

        // Columns
        TableColumn<Patient, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Patient, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<Patient, String> diseaseCol = new TableColumn<>("Disease");
        diseaseCol.setCellValueFactory(new PropertyValueFactory<>("disease"));

        TableColumn<Patient, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("admitStatus"));

        // Action Column
        TableColumn<Patient, Void> actionCol = new TableColumn<>("Actions");
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
                    Patient p = getTableView().getItems().get(getIndex());
                    PatientForm.showFormForEdit(p, table);
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

        table.getColumns().addAll(idCol, nameCol, ageCol, genderCol, contactCol, diseaseCol, statusCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Heading
        Label heading = new Label("Patient Records");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #5C4444;");
        heading.setAlignment(Pos.CENTER);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or contact...");
        searchField.setMaxWidth(300);
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

        FilteredList<Patient> filteredData = new FilteredList<>(data, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(patient -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lc = newVal.toLowerCase();
                return patient.getName().toLowerCase().contains(lc) ||
                       patient.getContact().toLowerCase().contains(lc);
            });
        });

        SortedList<Patient> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // --- Left top back button ---
        Button backBtn = new Button("←");
        backBtn.setShape(new Circle(30));
        backBtn.setMinSize(60, 60);
        backBtn.setMaxSize(60, 60);
        backBtn.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-background-radius: 30;" +
                "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0;"
        );
        backBtn.setOnAction(e -> PatientForm.showForm());
        addHoverEffect(backBtn, 10);

        // Heading + search VBox
        VBox headingSearchBox = new VBox(10, heading, searchField);
        headingSearchBox.setAlignment(Pos.CENTER);

        // Top StackPane to combine left button and center heading
        StackPane topStack = new StackPane();
        topStack.setPadding(new Insets(20));
        topStack.getChildren().addAll(headingSearchBox, backBtn);
        StackPane.setAlignment(headingSearchBox, Pos.CENTER);
        StackPane.setAlignment(backBtn, Pos.CENTER_LEFT);

        // Root layout
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

    public static void showForm() {
        new PatientListForm().start(new Stage());
    }
}