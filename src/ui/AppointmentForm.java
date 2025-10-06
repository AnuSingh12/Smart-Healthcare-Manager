package ui;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.PatientDAO;
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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Appointment;
import model.Doctor;
import model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentForm extends Application {

    private Appointment editingAppointment;

    public AppointmentForm() { }
    public AppointmentForm(Appointment a) { this.editingAppointment = a; }

    @Override
    public void start(Stage stage) {
        // --- Form Grid ---
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

        // --- Fields ---
        ComboBox<String> patientBox = new ComboBox<>();
        ComboBox<String> doctorBox = new ComboBox<>();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Scheduled", "Completed", "Cancelled");

        patientBox.setPrefWidth(300); patientBox.setStyle(inputStyle);
        doctorBox.setPrefWidth(300); doctorBox.setStyle(inputStyle);
        datePicker.setPrefWidth(300); datePicker.setStyle(inputStyle);
        timeField.setPrefWidth(300); timeField.setStyle(inputStyle);
        statusBox.setPrefWidth(300); statusBox.setStyle(inputStyle);

        // Disable past dates
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Load patients
        ObservableList<String> patients = FXCollections.observableArrayList();
        for (Patient p : PatientDAO.getAllPatients()) {
            patients.add(p.getPatientId() + " - " + p.getName());
        }
        patientBox.setItems(patients);

        // Load doctors
        ObservableList<String> doctors = FXCollections.observableArrayList();
        for (Doctor d : DoctorDAO.getAllDoctors()) {
            doctors.add(d.getDoctorId() + " - " + d.getName());
        }
        doctorBox.setItems(doctors);

        // Prefill for edit
        if (editingAppointment != null) {
            patientBox.setValue(editingAppointment.getPatientId() + " - " +
                    PatientDAO.getPatientById(editingAppointment.getPatientId()).getName());
            doctorBox.setValue(editingAppointment.getDoctorId() + " - " +
                    DoctorDAO.getDoctorById(editingAppointment.getDoctorId()).getName());
            datePicker.setValue(editingAppointment.getAppointmentDate());
            timeField.setText(editingAppointment.getAppointmentTime().toString());
            statusBox.setValue(editingAppointment.getStatus());
        } else {
            statusBox.setValue("Scheduled");
        }

        // --- Labels ---
        String labelStyle = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;";
        Label patientLbl = new Label("Patient:"); patientLbl.setStyle(labelStyle);
        Label doctorLbl = new Label("Doctor:"); doctorLbl.setStyle(labelStyle);
        Label dateLbl = new Label("Date:"); dateLbl.setStyle(labelStyle);
        Label timeLbl = new Label("Time (HH:mm):"); timeLbl.setStyle(labelStyle);
        Label statusLbl = new Label("Status:"); statusLbl.setStyle(labelStyle);

        // Submit button
        Button submitBtn = new Button(editingAppointment == null ? "Book Appointment" : "Update Appointment");
        submitBtn.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 40 12 40;"
        );

        // Add to grid
        grid.add(patientLbl, 0, 0); grid.add(patientBox, 1, 0);
        grid.add(doctorLbl, 0, 1); grid.add(doctorBox, 1, 1);
        grid.add(dateLbl, 0, 2); grid.add(datePicker, 1, 2);
        grid.add(timeLbl, 0, 3); grid.add(timeField, 1, 3);
        grid.add(statusLbl, 0, 4); grid.add(statusBox, 1, 4);
        grid.add(submitBtn, 1, 5);

        // --- Submit logic ---
        submitBtn.setOnAction(e -> {
            try {
                int patientId = Integer.parseInt(patientBox.getValue().split(" - ")[0]);
                int doctorId = Integer.parseInt(doctorBox.getValue().split(" - ")[0]);
                LocalDate date = datePicker.getValue();
                LocalTime time = LocalTime.parse(timeField.getText());
                String status = statusBox.getValue();

                if (date.isBefore(LocalDate.now()) || 
                    (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
                    new Alert(Alert.AlertType.ERROR, "⚠ Appointment cannot be in the past!").showAndWait();
                    return;
                }

                if (editingAppointment == null) {
                    Appointment a = new Appointment(patientId, doctorId, date, time, status);
                    if (AppointmentDAO.addAppointment(a)) {
                        new Alert(Alert.AlertType.INFORMATION, "Appointment Booked Successfully").showAndWait();
                    }
                } else {
                    editingAppointment.setPatientId(patientId);
                    editingAppointment.setDoctorId(doctorId);
                    editingAppointment.setAppointmentDate(date);
                    editingAppointment.setAppointmentTime(time);
                    editingAppointment.setStatus(status);

                    if (AppointmentDAO.updateAppointment(editingAppointment)) {
                        new Alert(Alert.AlertType.INFORMATION, "Appointment Updated Successfully").showAndWait();
                    }
                }

                AppointmentListForm.refreshTable();
                stage.close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "⚠ Fill all fields correctly (Time HH:mm)").showAndWait();
                ex.printStackTrace();
            }
        });

        // --- Top StackPane with heading & buttons ---
        StackPane topStack = new StackPane();
        topStack.setPadding(new Insets(20));

        Label heading = new Label(editingAppointment == null ? "Book New Appointment" : "Edit Appointment");
        heading.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                "-fx-padding: 15 30 15 30;" +
                "-fx-background-radius: 15;"
        );
        heading.setAlignment(Pos.CENTER);

        Button backBtn = new Button("←");
        backBtn.setShape(new Circle(25));
        backBtn.setMinSize(50,50); backBtn.setMaxSize(50,50);
        backBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #896C6C; -fx-font-weight: bold; -fx-font-size: 16px;");
        backBtn.setOnAction(e -> { stage.close(); Dashboard.showForm(); });

        Button listBtn = new Button("➔");
        listBtn.setShape(new Circle(25));
        listBtn.setMinSize(50,50); listBtn.setMaxSize(50,50);
        listBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #896C6C; -fx-font-weight: bold; -fx-font-size: 16px;");
        listBtn.setOnAction(e -> AppointmentListForm.showForm());

        addHoverEffect(listBtn, -10);
        addHoverEffect(backBtn, 10);

        topStack.getChildren().addAll(heading, listBtn, backBtn);
        StackPane.setAlignment(heading, Pos.CENTER);
        StackPane.setAlignment(listBtn, Pos.CENTER_RIGHT);
        StackPane.setAlignment(backBtn, Pos.CENTER_LEFT);

        // --- Root ---
        BorderPane root = new BorderPane();
        root.setTop(topStack);
        root.setCenter(grid);
        root.setStyle("-fx-background-color: #E7D3D3;");

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle(editingAppointment == null ? "Book Appointment" : "Edit Appointment");
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

    public static void showForm() { try { new AppointmentForm().start(new Stage()); } catch(Exception e){ e.printStackTrace(); } }
    public static void showForm(Appointment a) { try { new AppointmentForm(a).start(new Stage()); } catch(Exception e){ e.printStackTrace(); } }

    public static void main(String[] args) { launch(); }
}