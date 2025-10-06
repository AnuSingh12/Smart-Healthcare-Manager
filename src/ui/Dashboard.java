package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {
        showForm(stage);
    }

    // Static method to open Dashboard from anywhere
    public static void showForm() {
        try {
            new Dashboard().start(new Stage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Core Dashboard UI logic
    private void showForm(Stage stage) {
        // Overall grid
        GridPane grid = new GridPane();
        grid.setHgap(30); 
        grid.setVgap(30); 
        grid.setPadding(new Insets(20)); 
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: E7D3D3;"); // background behind boxes

        // Buttons
        Button patientBtn = new Button("Add Patient");
        Button patientListBtn = new Button("View Patients");
        Button doctorBtn = new Button("Add Doctor");
        Button doctorListBtn = new Button("View Doctors");
        Button appointmentBtn = new Button("Book Appointment");
        Button appointmentListBtn = new Button("View Appointments");
        Button billingBtn = new Button("Generate Bill");

        // Uniform button size
        Button[] buttons = {patientBtn, patientListBtn, doctorBtn, doctorListBtn, appointmentBtn, appointmentListBtn};
        for (Button b : buttons) {
            b.setMinWidth(220);  
            b.setMinHeight(150); 
            b.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 15;"
            );
        }

        // Billing button with a distinct gradient
        billingBtn.setMinWidth(200);
        billingBtn.setMinHeight(100);
        billingBtn.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 20;" +
            "-fx-background-color: linear-gradient(to right, DCC5B2, D9A299);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 20;"
        );

        // Wrap buttons in VBox
        VBox[] buttonBoxes = new VBox[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            VBox box = new VBox(buttons[i]);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(5));
            box.setBackground(new Background(new BackgroundFill(Color.web("#ffffff00"), new CornerRadii(20), Insets.EMPTY)));
            buttonBoxes[i] = box;
        }

        grid.add(buttonBoxes[0], 0, 0);
        grid.add(buttonBoxes[1], 1, 0);
        grid.add(buttonBoxes[2], 0, 1);
        grid.add(buttonBoxes[3], 1, 1);
        grid.add(buttonBoxes[4], 0, 2);
        grid.add(buttonBoxes[5], 1, 2);

        // Button Actions
        patientBtn.setOnAction(e -> PatientForm.showForm());
        patientListBtn.setOnAction(e -> PatientListForm.showForm());
        doctorBtn.setOnAction(e -> DoctorForm.showForm());
        doctorListBtn.setOnAction(e -> DoctorListForm.showForm());
        appointmentBtn.setOnAction(e -> AppointmentForm.showForm());
        appointmentListBtn.setOnAction(e -> AppointmentListForm.showForm());
        billingBtn.setOnAction(e -> {
            BillingForm billingForm = new BillingForm();
            billingForm.start(new Stage());
        });

        Label heading = new Label("🏥 Smart HealthCare Manager");
        heading.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );

        HBox headingBox = new HBox(heading);
        headingBox.setAlignment(Pos.CENTER);
        headingBox.setPadding(new Insets(20));
        headingBox.setBackground(new Background(
            new BackgroundFill(Color.web("#896C6C"), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        headingBox.setMaxWidth(Double.MAX_VALUE);

        // Billing box (footer style)
        VBox billingBox = new VBox(billingBtn);
        billingBox.setAlignment(Pos.CENTER);
        billingBox.setPadding(new Insets(20));
        billingBox.setBackground(new Background(
            new BackgroundFill(Color.web("#896C6C"), new CornerRadii(20), Insets.EMPTY)
        ));

        // Main layout → BorderPane
        BorderPane root = new BorderPane();
        root.setTop(headingBox);
        root.setCenter(grid);
        root.setBottom(billingBox);
        BorderPane.setAlignment(billingBox, Pos.CENTER);

        Scene scene = new Scene(root, 650, 800);
        stage.setScene(scene);
        stage.setTitle("Hospital Management Dashboard");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}