package ui;

import dao.DoctorDAO;
import model.Doctor;
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

public class DoctorListForm {

    private TableView<Doctor> table;

    public void start(Stage stage) {
        stage.setTitle("Doctor List");

        ObservableList<Doctor> data = FXCollections.observableArrayList(DoctorDAO.getAllDoctors());

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columns
        TableColumn<Doctor, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));

        TableColumn<Doctor, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Doctor, String> specCol = new TableColumn<>("Specialization");
        specCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        TableColumn<Doctor, Double> feesCol = new TableColumn<>("Fees");
        feesCol.setCellValueFactory(new PropertyValueFactory<>("fees"));

        TableColumn<Doctor, String> availCol = new TableColumn<>("Availability");
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

        TableColumn<Doctor, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        // Action buttons
        TableColumn<Doctor, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setStyle("-fx-background-radius: 15; -fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5); -fx-text-fill: white; -fx-font-weight: bold;");
                deleteBtn.setStyle("-fx-background-color: #fe4f4f; -fx-text-fill: white; -fx-background-radius: 10;");

                editBtn.setOnAction(e -> {
                    Doctor d = getTableView().getItems().get(getIndex());
                    DoctorForm.showForm(d, data);
                });

                deleteBtn.setOnAction(e -> {
                    Doctor d = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Delete " + d.getName() + "?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            if (DoctorDAO.deleteDoctor(d.getDoctorId())) {
                                data.remove(d);
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new HBox(8, editBtn, deleteBtn));
            }
        });

        table.getColumns().addAll(idCol, nameCol, specCol, feesCol, availCol, contactCol, actionCol);

        // Heading
        Label heading = new Label("Doctor Records");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #5C4444;");
        heading.setAlignment(Pos.CENTER);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or specialization...");
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

        FilteredList<Doctor> filteredData = new FilteredList<>(data, d -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(d -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lc = newVal.toLowerCase();
                return d.getName().toLowerCase().contains(lc) ||
                        d.getSpecialization().toLowerCase().contains(lc);
            });
        });

        SortedList<Doctor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // --- Left top back button to DoctorForm ---
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
        backBtn.setOnAction(e -> DoctorForm.showForm(null, data));
        addHoverEffect(backBtn, 10);

        // Top stackpane to center heading/search and left button
        VBox headingSearchBox = new VBox(10, heading, searchField);
        headingSearchBox.setAlignment(Pos.CENTER);

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
        new DoctorListForm().start(new Stage());
    }
}