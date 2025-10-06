//package dao;
//
//import model.Appointment;
//import util.DBConnection;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AppointmentDAO {
//
//    public static boolean addAppointment(Appointment a) {
//        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, appointment_time, status) VALUES(?,?,?,?,?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            ps.setInt(1, a.getPatientId());
//            ps.setInt(2, a.getDoctorId());
//            ps.setDate(3, Date.valueOf(a.getAppointmentDate()));
//            ps.setTime(4, Time.valueOf(a.getAppointmentTime()));
//            ps.setString(5, a.getStatus());
//            ps.executeUpdate();
//
//            // Get generated appointmentId
//            ResultSet rs = ps.getGeneratedKeys();
//            if (rs.next()) {
//                a.setAppointmentId(rs.getInt(1));
//            }
//
//            return true;
//        } catch(Exception e) {
//            System.out.println("Error adding appointment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public static boolean updateAppointment(Appointment a) {
//        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, status=? WHERE appointment_id=?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, a.getPatientId());
//            ps.setInt(2, a.getDoctorId());
//            ps.setDate(3, Date.valueOf(a.getAppointmentDate()));
//            ps.setTime(4, Time.valueOf(a.getAppointmentTime()));
//            ps.setString(5, a.getStatus());
//            ps.setInt(6, a.getAppointmentId());
//            ps.executeUpdate();
//            return true;
//        } catch(Exception e) {
//            System.out.println("Error updating appointment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public static boolean deleteAppointment(int id) {
//        String sql = "DELETE FROM appointments WHERE appointment_id=?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ps.executeUpdate();
//            return true;
//        } catch(Exception e) {
//            System.out.println("Error deleting appointment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public static List<Appointment> getAllAppointments() {
//        List<Appointment> list = new ArrayList<>();
//        String sql = "SELECT * FROM appointments";
//        try (Connection conn = DBConnection.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            while (rs.next()) {
//                Appointment a = new Appointment(
//                        rs.getInt("patient_id"),
//                        rs.getInt("doctor_id"),
//                        rs.getDate("appointment_date").toLocalDate(),
//                        rs.getTime("appointment_time").toLocalTime(),
//                        rs.getString("status")
//                );
//                a.setAppointmentId(rs.getInt("appointment_id"));
//                list.add(a);
//            }
//        } catch(Exception e) {
//            System.out.println("Error fetching appointments: " + e.getMessage());
//        }
//        return list;
//    }
//}


package dao;

import model.Appointment;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // ✅ Add new appointment
    public static boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, appointment_time, status) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setDate(3, Date.valueOf(a.getAppointmentDate()));
            ps.setTime(4, Time.valueOf(a.getAppointmentTime()));
            ps.setString(5, a.getStatus());
            ps.executeUpdate();

            // Get generated appointmentId
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setAppointmentId(rs.getInt(1));
            }

            return true;
        } catch(Exception e) {
            System.out.println("Error adding appointment: " + e.getMessage());
            return false;
        }
    }

    // ✅ Update existing appointment
    public static boolean updateAppointment(Appointment a) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, appointment_time=?, status=? WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setDate(3, Date.valueOf(a.getAppointmentDate()));
            ps.setTime(4, Time.valueOf(a.getAppointmentTime()));
            ps.setString(5, a.getStatus());
            ps.setInt(6, a.getAppointmentId());
            ps.executeUpdate();
            return true;
        } catch(Exception e) {
            System.out.println("Error updating appointment: " + e.getMessage());
            return false;
        }
    }

    // ✅ Delete appointment
    public static boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch(Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get all appointments
    public static List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Appointment a = new Appointment(
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("status")
                );
                a.setAppointmentId(rs.getInt("appointment_id"));
                list.add(a);
            }
        } catch(Exception e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
        }
        return list;
    }

    // ✅ New: Get appointment by ID
    public static Appointment getAppointmentById(int id) {
        Appointment a = null;
        String sql = "SELECT * FROM appointments WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                a = new Appointment(
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("status")
                );
                a.setAppointmentId(rs.getInt("appointment_id"));
            }
        } catch(Exception e) {
            System.out.println("Error fetching appointment by ID: " + e.getMessage());
        }
        return a;
    }
}
