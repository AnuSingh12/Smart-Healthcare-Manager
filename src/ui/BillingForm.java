package ui;

import dao.BillDAO;
import dao.PatientDAO;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import model.Bill;
import model.Patient;
import model.Doctor;
import model.Appointment;
import model.Item;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Node;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillingForm {

    public void start(Stage stage) {
        // ===== Heading =====
        Label heading = new Label("Generate Bill");
        heading.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                        "-fx-padding: 15 30 15 30;" +
                        "-fx-background-radius: 15;"
        );
        heading.setAlignment(Pos.CENTER);
        HBox topBox = new HBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));

        // ===== Main Grid =====
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        grid.setVgap(25);
        grid.setHgap(20);
        grid.setAlignment(Pos.CENTER);

        String inputStyle = "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #896C6C;" +
                "-fx-border-width: 2;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10 14 10 14;" +
                "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;";

        // ===== Declare final fields for lambdas =====
        final VBox testsBox = new VBox(5);
        final VBox medsBox = new VBox(5);
        final TextField consultField = new TextField();
        final TextField totalField = new TextField();

        // ===== Appointment ID Input =====
        Label apptLbl = new Label("Enter Appointment ID:"); 
        apptLbl.setFont(Font.font(16));
        TextField apptField = new TextField(); 
        apptField.setPrefWidth(300); 
        apptField.setStyle(inputStyle);

        // ===== Patient Selection =====
        Label patientLbl = new Label("Select Patient:"); 
        patientLbl.setFont(Font.font(16));
        ComboBox<Patient> patientBox = new ComboBox<>();
        patientBox.getItems().addAll(PatientDAO.getAllPatients()); 
        patientBox.setPrefWidth(300); 
        patientBox.setStyle(inputStyle);

        // Patient info box
        Label ageLabel = new Label("Age: -");
        Label genderLabel = new Label("Gender: -");
        Label diseaseLabel = new Label("Disease: -");
        HBox patientInfoBox = new HBox(20, ageLabel, genderLabel, diseaseLabel);
        patientInfoBox.setPadding(new Insets(10));
        patientInfoBox.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(5), Insets.EMPTY)));
        patientInfoBox.setAlignment(Pos.CENTER_LEFT);

        patientBox.setOnAction(e -> {
            Patient p = patientBox.getValue();
            if (p != null) {
                ageLabel.setText("Age: " + p.getAge());
                genderLabel.setText("Gender: " + p.getGender());
                diseaseLabel.setText("Disease: " + p.getDisease());
            } else {
                ageLabel.setText("Age: -");
                genderLabel.setText("Gender: -");
                diseaseLabel.setText("Disease: -");
            }
        });

        // ===== Doctor Selection =====
        Label doctorLbl = new Label("Select Doctor:"); 
        doctorLbl.setFont(Font.font(16));
        ComboBox<Doctor> doctorBox = new ComboBox<>(); 
        doctorBox.getItems().addAll(DoctorDAO.getAllDoctors()); 
        doctorBox.setPrefWidth(300); 
        doctorBox.setStyle(inputStyle);

        // ===== Charges Fields =====
        Label consultLbl = new Label("Consultation Fee:"); consultLbl.setFont(Font.font(16));
        Label totalLbl = new Label("Total Amount:"); totalLbl.setFont(Font.font(16));

        consultField.setEditable(false); 
        consultField.setPrefWidth(300); 
        consultField.setStyle(inputStyle);

        totalField.setEditable(false); 
        totalField.setPrefWidth(300);
        totalField.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-font-weight: bold; -fx-background-color: #e0ffe0; -fx-font-size:16px;");

        // ===== Tests Section =====
        Label testsLabel = new Label("Tests:"); testsLabel.setFont(Font.font(16));
        Button addTestBtn = new Button("Add Test");
        addTestBtn.setOnAction(e -> {
            HBox row = new HBox(5);
            TextField nameField = new TextField();
            nameField.setPromptText("Test Name");
            nameField.setPrefWidth(200);
            TextField priceField = new TextField();
            priceField.setPromptText("Price");
            priceField.setPrefWidth(100);

            // Real-time total update
            priceField.textProperty().addListener((obs, oldVal, newVal) -> {
                updateTotal(consultField, testsBox, medsBox, totalField);
            });

            row.getChildren().addAll(nameField, priceField);
            testsBox.getChildren().add(row);
        });

        // ===== Medicines Section =====
        Label medsLabel = new Label("Medicines:"); medsLabel.setFont(Font.font(16));
        Button addMedBtn = new Button("Add Medicine");
        addMedBtn.setOnAction(e -> {
            HBox row = new HBox(5);
            TextField nameField = new TextField();
            nameField.setPromptText("Medicine Name");
            nameField.setPrefWidth(200);
            TextField priceField = new TextField();
            priceField.setPromptText("Price");
            priceField.setPrefWidth(100);

            // Real-time total update
            priceField.textProperty().addListener((obs, oldVal, newVal) -> {
                updateTotal(consultField, testsBox, medsBox, totalField);
            });

            row.getChildren().addAll(nameField, priceField);
            medsBox.getChildren().add(row);
        });

        // ===== Add to Grid =====
        grid.add(apptLbl, 0, 0); grid.add(apptField, 1, 0);
        grid.add(patientLbl, 0, 1); grid.add(patientBox, 1, 1);
        grid.add(patientInfoBox, 1, 2);
        grid.add(doctorLbl, 0, 3); grid.add(doctorBox, 1, 3);
        HBox consultLabelBox = new HBox(consultLbl);
        consultLabelBox.setAlignment(Pos.CENTER_LEFT); // label left aligned
        consultLabelBox.setPadding(new Insets(0, 0, 0, -5)); // adjust left padding as needed
        grid.add(consultLabelBox, 0, 4);
        grid.add(consultField, 1, 4);
        grid.add(testsLabel, 0, 5); grid.add(testsBox, 1, 5); grid.add(addTestBtn, 2, 5);
        grid.add(medsLabel, 0, 6); grid.add(medsBox, 1, 6); grid.add(addMedBtn, 2, 6);
        grid.add(totalLbl, 0, 7); grid.add(totalField, 1, 7);

        GridPane.setHalignment(consultLbl, HPos.RIGHT);
        GridPane.setHalignment(totalLbl, HPos.RIGHT);

        // ===== Doctor Event =====
        doctorBox.setOnAction(e -> {
            Doctor d = doctorBox.getValue();
            if(d != null){
                consultField.setText(String.valueOf(d.getFees()));
                updateTotal(consultField, testsBox, medsBox, totalField);
            } else {
                consultField.clear();
                updateTotal(consultField, testsBox, medsBox, totalField);
            }
        });

        // ===== Appointment Auto-Fill =====
        apptField.textProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.isEmpty()){
                patientBox.getSelectionModel().clearSelection();
                doctorBox.getSelectionModel().clearSelection();
                consultField.clear();
                updateTotal(consultField, testsBox, medsBox, totalField);
                ageLabel.setText("Age: -"); genderLabel.setText("Gender: -"); diseaseLabel.setText("Disease: -");
                return;
            }
            try{
                int apptId = Integer.parseInt(newVal.trim());
                Appointment appt = AppointmentDAO.getAppointmentById(apptId);
                if(appt != null){
                    Patient p = PatientDAO.getPatientById(appt.getPatientId());
                    Doctor d = DoctorDAO.getDoctorById(appt.getDoctorId());
                    if(p != null){ patientBox.setValue(p); ageLabel.setText("Age: " + p.getAge()); genderLabel.setText("Gender: " + p.getGender()); diseaseLabel.setText("Disease: " + p.getDisease()); }
                    if(d != null){ doctorBox.setValue(d); consultField.setText(String.valueOf(d.getFees())); updateTotal(consultField, testsBox, medsBox, totalField); }
                } else {
                    patientBox.getSelectionModel().clearSelection();
                    doctorBox.getSelectionModel().clearSelection();
                    consultField.clear();
                    updateTotal(consultField, testsBox, medsBox, totalField);
                }
            } catch(NumberFormatException ex){}
        });

        // ===== Generate Button =====
        Button saveBtn = new Button("Generate Bill + PDF");
        saveBtn.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-radius: 25;" +
                        "-fx-background-color: linear-gradient(to right, #896C6C, #E5BEB5);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 40 12 40;"
        );

        saveBtn.setOnAction(e -> {
            try{
                Patient p = patientBox.getValue();
                Doctor d = doctorBox.getValue();
                if(p==null || d==null){ showAlert("Error","Select both patient and doctor!"); return; }

                double consultFee = parseDoubleSafe(consultField);

                List<Item> tests = new ArrayList<>();
                for(Node node : testsBox.getChildren()){
                    if(node instanceof HBox){
                        HBox row = (HBox) node;
                        TextField nameField = (TextField) row.getChildren().get(0);
                        TextField priceField = (TextField) row.getChildren().get(1);
                        double price = parseDoubleSafe(priceField);
                        if(!nameField.getText().isEmpty()) tests.add(new Item(nameField.getText(), price));
                    }
                }

                List<Item> medicines = new ArrayList<>();
                for(Node node : medsBox.getChildren()){
                    if(node instanceof HBox){
                        HBox row = (HBox) node;
                        TextField nameField = (TextField) row.getChildren().get(0);
                        TextField priceField = (TextField) row.getChildren().get(1);
                        double price = parseDoubleSafe(priceField);
                        if(!nameField.getText().isEmpty()) medicines.add(new Item(nameField.getText(), price));
                    }
                }

                Bill bill = new Bill(p.getPatientId(), d.getDoctorId(), consultFee, tests, medicines);

                if(BillDAO.addBill(bill)){
                    generatePDF(bill,p,d);
                    showAlert("Success", String.format("Bill generated & PDF saved!\nBill ID: %d\nTotal: ₹%.2f", bill.getBillId(), bill.getTotalAmount()));
                    // Reset fields
                    apptField.clear(); patientBox.getSelectionModel().clearSelection(); doctorBox.getSelectionModel().clearSelection();
                    consultField.clear(); testsBox.getChildren().clear(); medsBox.getChildren().clear(); totalField.clear();
                    ageLabel.setText("Age: -"); genderLabel.setText("Gender: -"); diseaseLabel.setText("Disease: -");
                } else { showAlert("Error","Failed to generate bill!"); }

            } catch(Exception ex){ showAlert("Error","Invalid input! "+ex.getMessage()); ex.printStackTrace(); }
        });

        grid.add(saveBtn, 1, 8);

        // ===== Layout =====
        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(grid);
        root.setStyle("-fx-background-color: #E7D3D3;");

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Generate Bill");
        stage.setMaximized(true);
        stage.show();
    }

    // ===== Helper Methods =====
    private void updateTotal(TextField consult, VBox testsBox, VBox medsBox, TextField total) {
        double sum = parseDoubleSafe(consult);
        for(Node node : testsBox.getChildren()){
            if(node instanceof HBox){
                HBox row = (HBox) node;
                sum += parseDoubleSafe((TextField) row.getChildren().get(1));
            }
        }
        for(Node node : medsBox.getChildren()){
            if(node instanceof HBox){
                HBox row = (HBox) node;
                sum += parseDoubleSafe((TextField) row.getChildren().get(1));
            }
        }
        total.setText(String.format("%.2f", sum));
    }

    private double parseDoubleSafe(TextField tf){
        try { return Double.parseDouble(tf.getText().isEmpty() ? "0" : tf.getText()); }
        catch(Exception e){ return 0; }
    }

    private void showAlert(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void generatePDF(Bill bill, Patient p, Doctor d){
        try{
            File folder = new File("HospitalManagement/Bills");
            if(!folder.exists()) folder.mkdirs();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = folder.getAbsolutePath() + "/" + p.getName().replaceAll("\\s+","_") +
                    "_Bill_" + bill.getBillId() + "_" + timestamp + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Paragraph title = new Paragraph("Hospital Billing Receipt",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLUE));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Generated On: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
            document.add(new Paragraph("-----------------------------------------------------"));

            document.add(new Paragraph("Patient Details"));
            document.add(new Paragraph("Name: " + p.getName()));
            document.add(new Paragraph("Age: " + p.getAge()));
            document.add(new Paragraph("Gender: " + p.getGender()));
            document.add(new Paragraph("Disease: " + p.getDisease()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Doctor Details"));
            document.add(new Paragraph("Name: " + d.getName()));
            document.add(new Paragraph("Specialization: " + d.getSpecialization()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.addCell("Description");
            table.addCell("Amount (₹)");

            // Consultation
            table.addCell("Consultation Fee");
            table.addCell(String.valueOf(bill.getConsultFee()));

            // Tests
            for(Item t : bill.getTests()){
                table.addCell("Test: " + t.getName());
                table.addCell(String.valueOf(t.getPrice()));
            }

            // Medicines
            for(Item m : bill.getMedicines()){
                table.addCell("Medicine: " + m.getName());
                table.addCell(String.valueOf(m.getPrice()));
            }

            // Total
            table.addCell("Total Amount");
            table.addCell(String.valueOf(bill.getTotalAmount()));

            document.add(table);
            document.close();
            System.out.println("PDF saved at: " + fileName);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
