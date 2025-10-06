package ui;

import dao.PatientDAO;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TableView;
import model.Patient;

public class PatientForm extends Application {

    private Patient existingPatient;
    private TableView<Patient> table;

    public PatientForm() {}

    public PatientForm(Patient patient, TableView<Patient> table) {
        this.existingPatient = patient;
        this.table = table;
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

        TextField nameField = new TextField(); nameField.setPromptText("Enter patient name"); nameField.setStyle(inputStyle); nameField.setPrefWidth(300);
        TextField ageField = new TextField(); ageField.setPromptText("Enter age"); ageField.setStyle(inputStyle); ageField.setPrefWidth(300);
        TextField genderField = new TextField(); genderField.setPromptText("Enter gender"); genderField.setStyle(inputStyle); genderField.setPrefWidth(300);
        TextField contactField = new TextField(); contactField.setPromptText("Enter contact number"); contactField.setStyle(inputStyle); contactField.setPrefWidth(300);
        TextField diseaseField = new TextField(); diseaseField.setPromptText("Enter disease"); diseaseField.setStyle(inputStyle); diseaseField.setPrefWidth(300);

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Admitted", "Outpatient", "Discharged");
        statusBox.setPromptText("Select status");
        statusBox.setStyle(inputStyle);
        statusBox.setPrefWidth(300);

        Button submitBtn = new Button(existingPatient == null ? "Add Patient" : "Update Patient");
        submitBtn.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-radius: 25;" +
                        "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 40 12 40;"
        );

        String labelStyle = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;";
        Label nameLbl = new Label("Name:"); nameLbl.setStyle(labelStyle);
        Label ageLbl = new Label("Age:"); ageLbl.setStyle(labelStyle);
        Label genderLbl = new Label("Gender:"); genderLbl.setStyle(labelStyle);
        Label contactLbl = new Label("Contact:"); contactLbl.setStyle(labelStyle);
        Label diseaseLbl = new Label("Disease:"); diseaseLbl.setStyle(labelStyle);
        Label statusLbl = new Label("Status:"); statusLbl.setStyle(labelStyle);

        grid.add(nameLbl, 0, 0); grid.add(nameField, 1, 0);
        grid.add(ageLbl, 0, 1); grid.add(ageField, 1, 1);
        grid.add(genderLbl, 0, 2); grid.add(genderField, 1, 2);
        grid.add(contactLbl, 0, 3); grid.add(contactField, 1, 3);
        grid.add(diseaseLbl, 0, 4); grid.add(diseaseField, 1, 4);
        grid.add(statusLbl, 0, 5); grid.add(statusBox, 1, 5);
        grid.add(submitBtn, 1, 6);

        if(existingPatient != null) {
            nameField.setText(existingPatient.getName());
            ageField.setText(String.valueOf(existingPatient.getAge()));
            genderField.setText(existingPatient.getGender());
            contactField.setText(existingPatient.getContact());
            diseaseField.setText(existingPatient.getDisease());
            statusBox.setValue(existingPatient.getAdmitStatus());
        }

        submitBtn.setOnAction(e -> {
            try {
                // 1. Check empty fields
                if (nameField.getText().isEmpty() ||
                    ageField.getText().isEmpty() ||
                    genderField.getText().isEmpty() ||
                    contactField.getText().isEmpty() ||
                    diseaseField.getText().isEmpty() ||
                    statusBox.getValue() == null) 
                {
                    new Alert(Alert.AlertType.ERROR, "⚠ Please fill all the fields!").showAndWait();
                    return;
                }

                // 2. Validate contact number
                String contact = contactField.getText();
                if (!contact.matches("\\d{10}")) {
                    new Alert(Alert.AlertType.ERROR, "⚠ Enter a valid 10-digit contact number!").showAndWait();
                    return;
                }

                // 3. Validate age
                int age = Integer.parseInt(ageField.getText());
                if (age <= 0) {
                    new Alert(Alert.AlertType.ERROR, "⚠ Enter a valid age!").showAndWait();
                    return;
                }

                // 4. If validation passed → add/update patient
                if(existingPatient == null) {
                    Patient p = new Patient();
                    p.setName(nameField.getText());
                    p.setAge(age);
                    p.setGender(genderField.getText());
                    p.setContact(contact);
                    p.setDisease(diseaseField.getText());
                    p.setAdmitStatus(statusBox.getValue());

                    boolean success = PatientDAO.addPatient(p);
                    if(success && table != null) table.getItems().add(p);

                    if(success) {
                        new Alert(Alert.AlertType.INFORMATION, "Patient Added Successfully").showAndWait();
                        nameField.clear(); ageField.clear(); genderField.clear();
                        contactField.clear(); diseaseField.clear(); statusBox.getSelectionModel().clearSelection();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Operation failed!").showAndWait();
                    }
                } else {
                    existingPatient.setName(nameField.getText());
                    existingPatient.setAge(age);
                    existingPatient.setGender(genderField.getText());
                    existingPatient.setContact(contact);
                    existingPatient.setDisease(diseaseField.getText());
                    existingPatient.setAdmitStatus(statusBox.getValue());

                    boolean success = PatientDAO.updatePatient(existingPatient);
                    if(success && table != null) table.refresh();

                    if(success) {
                        new Alert(Alert.AlertType.INFORMATION, "Patient Updated Successfully").showAndWait();
                        stage.close();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Operation failed!").showAndWait();
                    }
                }

            } catch(Exception ex) {
                new Alert(Alert.AlertType.ERROR, "⚠ Enter valid details!").showAndWait();
            }
        });


        // Top HBox with heading center & circular buttons
        StackPane topStack = new StackPane();
        topStack.setPadding(new Insets(20));

        Label heading = new Label(existingPatient == null ? "Add New Patient" : "Edit Patient");
        heading.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                        "-fx-padding: 15 30 15 30;" +
                        "-fx-background-radius: 15;"
        );
        heading.setAlignment(Pos.CENTER);

        // Right button → Patient list
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
        listBtn.setOnAction(e -> PatientListForm.showForm());

        // Left button → Dashboard (Back arrow)
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

        // Hover animations for both buttons
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
        stage.setTitle(existingPatient == null ? "Add Patient" : "Edit Patient");
        stage.setMaximized(true);
        stage.show();
    }

    // Utility method to add hover effect
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
        try { new PatientForm().start(new Stage()); } catch(Exception e) { e.printStackTrace(); }
    }

    public static void showFormForEdit(Patient p, TableView<Patient> table) {
        try { new PatientForm(p, table).start(new Stage()); } catch(Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { launch(); }
}