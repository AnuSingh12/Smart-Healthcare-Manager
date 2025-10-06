package dao;

import model.Patient;
import util.DBConnection;
import java.sql.*;

public class PatientDAO {

    // Add Patient (INSERT)
    public static boolean addPatient(Patient p) {
        String sql = "INSERT INTO patients(name, age, gender, contact, disease, admit_status) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setString(4, p.getContact());
            ps.setString(5, p.getDisease());         // add disease
            ps.setString(6, p.getAdmitStatus());     // add admit_status

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }

    // Update Patient
    public static boolean updatePatient(Patient p) {
        String sql = "UPDATE patients SET name=?, age=?, gender=?, contact=?, disease=?, admit_status=? WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getAge());
            ps.setString(3, p.getGender());
            ps.setString(4, p.getContact());
            ps.setString(5, p.getDisease());      // update disease
            ps.setString(6, p.getAdmitStatus());  // update status
            ps.setInt(7, p.getPatientId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Patient
    public static boolean deletePatient(int patientId) {
        String sql = "DELETE FROM patients WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            return ps.executeUpdate() > 0;
        } catch(Exception e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    // Get All Patients
    public static java.util.List<Patient> getAllPatients() {
        java.util.List<Patient> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setContact(rs.getString("contact"));
                p.setDisease(rs.getString("disease"));           // fetch disease
                p.setAdmitStatus(rs.getString("admit_status"));  // fetch status
                list.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error fetching patients: " + e.getMessage());
        }
        return list;
    }
 // Get Patient by ID
    public static Patient getPatientById(int patientId) {
        String sql = "SELECT * FROM patients WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setContact(rs.getString("contact"));
                p.setDisease(rs.getString("disease"));
                p.setAdmitStatus(rs.getString("admit_status"));
                return p;
            }
        } catch (Exception e) {
            System.out.println("Error fetching patient by ID: " + e.getMessage());
        }
        return null;
    }
}
