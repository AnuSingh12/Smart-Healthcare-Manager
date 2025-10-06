package dao;

import model.Doctor;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // Add Doctor (INSERT)
    public static boolean addDoctor(Doctor d) {
        String sql = "INSERT INTO doctors(name, specialization, fees, availability, contact) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setDouble(3, d.getFees());
            ps.setString(4, d.getAvailability());
            ps.setString(5, d.getContact());

            // Debug: print what we are inserting
            System.out.println("Inserting Doctor: " + d.getName() + ", Contact: " + d.getContact());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor added successfully.");
                return true;
            } else {
                System.out.println("Doctor not added. No rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error adding doctor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Update Doctor
    public static boolean updateDoctor(Doctor d) {
        String sql = "UPDATE doctors SET name=?, specialization=?, fees=?, availability=?, contact=? WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setDouble(3, d.getFees());
            ps.setString(4, d.getAvailability());
            ps.setString(5, d.getContact());
            ps.setInt(6, d.getDoctorId());

            System.out.println("Updating Doctor ID " + d.getDoctorId() + " with Contact: " + d.getContact());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor updated successfully.");
                return true;
            } else {
                System.out.println("Update failed. No rows affected.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error updating doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Delete Doctor by ID
    public static boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor deleted successfully.");
                return true;
            } else {
                System.out.println("Delete failed. No rows affected.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error deleting doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get All Doctors (SELECT)
    public static List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setFees(rs.getDouble("fees"));
                d.setAvailability(rs.getString("availability"));
                d.setContact(rs.getString("contact"));
                list.add(d);
            }

        } catch (SQLException e) {
            System.out.println("SQL Error fetching doctors: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error fetching doctors: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Get Doctor by ID
    public static Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setFees(rs.getDouble("fees"));
                d.setAvailability(rs.getString("availability"));
                d.setContact(rs.getString("contact"));
                return d;
            }

        } catch (SQLException e) {
            System.out.println("SQL Error fetching doctor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error fetching doctor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
