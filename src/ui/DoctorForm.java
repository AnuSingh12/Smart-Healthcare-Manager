package ui;

import dao.DoctorDAO;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Doctor;

public class DoctorForm extends Application {

    private Doctor editingDoctor;
    private ObservableList<Doctor> doctorList;

    public DoctorForm() {}

    public DoctorForm(Doctor doctor, ObservableList<Doctor> list) {
        this.editingDoctor = doctor;
        this.doctorList = list;
    }

    @Override
    public void start(Stage stage) {
        // Grid for form fields
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        grid.setVgap(25);
        grid.setHgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom right, #E7D3D3, #FBF3D5);");

        String inputStyle = "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #896C6C;" +
                "-fx-border-width: 2;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10 14 10 14;" +
                "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;";

        // Fields
        TextField nameField = new TextField(); 
        nameField.setPromptText("Enter doctor name"); 
        nameField.setStyle(inputStyle); 
        nameField.setPrefWidth(300);

        ComboBox<String> specializationBox = new ComboBox<>();
        specializationBox.setItems(FXCollections.observableArrayList(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology",
            "Gynecology", "ENT", "General Medicine", "Urology", "Psychiatry"
        ));
        specializationBox.setPromptText("Select Specialization");
        specializationBox.setStyle(inputStyle);
        specializationBox.setPrefWidth(300);

        TextField feesField = new TextField(); 
        feesField.setPromptText("Enter fees"); 
        feesField.setStyle(inputStyle); 
        feesField.setPrefWidth(300);

        TextField contactField = new TextField(); 
        contactField.setPromptText("Enter contact number"); 
        contactField.setStyle(inputStyle); 
        contactField.setPrefWidth(300);

        ComboBox<String> availabilityBox = new ComboBox<>();
        availabilityBox.getItems().addAll("Morning", "Afternoon", "Evening", "Full Day");
        availabilityBox.setPromptText("Select availability");
        availabilityBox.setStyle(inputStyle);
        availabilityBox.setPrefWidth(300);

        // Labels
        String labelStyle = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;";
        Label nameLbl = new Label("Name:"); nameLbl.setStyle(labelStyle);
        Label specializationLbl = new Label("Specialization:"); specializationLbl.setStyle(labelStyle);
        Label feesLbl = new Label("Fees:"); feesLbl.setStyle(labelStyle);
        Label contactLbl = new Label("Contact:"); contactLbl.setStyle(labelStyle);
        Label availabilityLbl = new Label("Availability:"); availabilityLbl.setStyle(labelStyle);

        // Submit button
        Button submitBtn = new Button(editingDoctor == null ? "Add Doctor" : "Update Doctor");
        submitBtn.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-background-radius: 25;" +
            "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 40 12 40;"
        );

        // Prefill for edit
        if (editingDoctor != null) {
            nameField.setText(editingDoctor.getName());
            specializationBox.setValue(editingDoctor.getSpecialization());
            feesField.setText(String.valueOf(editingDoctor.getFees()));
            availabilityBox.setValue(editingDoctor.getAvailability());
            contactField.setText(editingDoctor.getContact());
        }

        // Add to grid
        grid.add(nameLbl, 0, 0); grid.add(nameField, 1, 0);
        grid.add(specializationLbl, 0, 1); grid.add(specializationBox, 1, 1);
        grid.add(feesLbl, 0, 2); grid.add(feesField, 1, 2);
        grid.add(contactLbl, 0, 3); grid.add(contactField, 1, 3);
        grid.add(availabilityLbl, 0, 4); grid.add(availabilityBox, 1, 4);
        grid.add(submitBtn, 1, 5);

        submitBtn.setOnAction(e -> {
            try {
                // 1. Check if all fields are filled
                if (nameField.getText().isEmpty() ||
                    specializationBox.getValue() == null ||
                    feesField.getText().isEmpty() ||
                    contactField.getText().isEmpty() ||
                    availabilityBox.getValue() == null) 
                {
                    new Alert(Alert.AlertType.ERROR, "⚠ Please fill all fields!").showAndWait();
                    return;
                }

                // 2. Validate contact number (only digits & exactly 10 length)
                String contact = contactField.getText();
                if (!contact.matches("\\d{10}")) {
                    new Alert(Alert.AlertType.ERROR, "⚠ Enter a valid 10-digit contact number!").showAndWait();
                    return;
                }

                // 3. Validate fees as number
                double fees;
                try {
                    fees = Double.parseDouble(feesField.getText());
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "⚠ Fees must be a valid number!").showAndWait();
                    return;
                }

                // 4. Add / Update doctor logic
                if (editingDoctor == null) {
                    Doctor d = new Doctor(
                        nameField.getText(),
                        specializationBox.getValue(),
                        fees,
                        availabilityBox.getValue(),
                        contact
                    );

                    if (DoctorDAO.addDoctor(d)) {
                        new Alert(Alert.AlertType.INFORMATION, "Doctor Added Successfully").showAndWait();
                        if (doctorList != null) doctorList.add(d);
                        stage.close();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Error Adding Doctor").showAndWait();
                    }
                } else {
                    editingDoctor.setName(nameField.getText());
                    editingDoctor.setSpecialization(specializationBox.getValue());
                    editingDoctor.setFees(fees);
                    editingDoctor.setAvailability(availabilityBox.getValue());
                    editingDoctor.setContact(contact);

                    if (DoctorDAO.updateDoctor(editingDoctor)) {
                        new Alert(Alert.AlertType.INFORMATION, "Doctor Updated Successfully").showAndWait();

                        if (doctorList != null) {
                            doctorList.removeIf(d -> d.getDoctorId() == editingDoctor.getDoctorId());
                            doctorList.add(editingDoctor);
                        }
                        stage.close();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Error Updating Doctor").showAndWait();
                    }
                }

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "⚠ Something went wrong!").showAndWait();
            }
        });


        // Top StackPane with heading and circular buttons (left/right)
        StackPane topStack = new StackPane();
        topStack.setPadding(new Insets(20));

        Label heading = new Label(editingDoctor == null ? "Add New Doctor" : "Edit Doctor");
        heading.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
            "-fx-padding: 15 30 15 30;" +
            "-fx-background-radius: 15;"
        );
        heading.setAlignment(Pos.CENTER);

        // Left button → Dashboard
        Button backBtn = new Button("←");
        backBtn.setShape(new Circle(25));
        backBtn.setMinSize(50, 50);
        backBtn.setMaxSize(50, 50);
        backBtn.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #896C6C;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px;"
        );
        backBtn.setOnAction(e -> {
            stage.close();
            Dashboard.showForm();
        });

        // Right button → DoctorListForm
        Button listBtn = new Button("➔");
        listBtn.setShape(new Circle(25));
        listBtn.setMinSize(50, 50);
        listBtn.setMaxSize(50, 50);
        listBtn.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #896C6C;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px;"
        );
        listBtn.setOnAction(e -> DoctorListForm.showForm());

        // Hover animations
        addHoverEffect(listBtn, -10);
        addHoverEffect(backBtn, 10);

        topStack.getChildren().addAll(heading, listBtn, backBtn);
        StackPane.setAlignment(heading, Pos.CENTER);
        StackPane.setAlignment(listBtn, Pos.CENTER_RIGHT);
        StackPane.setAlignment(backBtn, Pos.CENTER_LEFT);

        // Center StackPane for form
        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(grid);

        BorderPane root = new BorderPane();
        root.setTop(topStack);
        root.setCenter(centerPane);
        root.setStyle("-fx-background-color: #E7D3D3;");

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle(editingDoctor == null ? "Add Doctor" : "Edit Doctor");
        stage.setMaximized(true);
        stage.show();
    }

    private void addHoverEffect(Button btn, double slideX) {
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
        try { new DoctorForm().start(new Stage()); } catch(Exception e) { e.printStackTrace(); }
    }

    public static void showForm(Doctor doctor, ObservableList<Doctor> list) {
        try { new DoctorForm(doctor, list).start(new Stage()); } catch(Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { launch(); }
}