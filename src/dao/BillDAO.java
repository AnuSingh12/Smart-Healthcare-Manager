package dao;

import model.Bill;
import util.DBConnection;
import java.sql.*;
import model.Item;
import java.util.ArrayList;
import java.util.List;


public class BillDAO {

    // Insert Bill (Option 1: total_amount GENERATED in DB)
    public static boolean addBill(Bill b) {
        // total_amount ko INSERT query se remove kiya
        String sql = "INSERT INTO bills(patient_id, doctor_id, consultation_fee, test_charges, medicine_charges) VALUES (?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, b.getPatientId());
            ps.setInt(2, b.getDoctorId());
            ps.setDouble(3, b.getConsultFee());
            ps.setDouble(4, b.getTestCharges());
            ps.setDouble(5, b.getMedicineCharges());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    b.setBillId(rs.getInt(1));  // auto-generated bill_id
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error adding bill: " + e.getMessage());
        }
        return false;
    }

    // Fetch Bill by ID
    public static Bill getBillById(int billId) {
        String sql = "SELECT * FROM bills WHERE bill_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // DB me sirf charges hai, names nahi stored -> Items list empty kar do
                Bill b = new Bill(
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getDouble("consultation_fee"),
                    new ArrayList<Item>(),   // tests list empty
                    new ArrayList<Item>()    // medicines list empty
                );
                b.setBillId(rs.getInt("bill_id"));
                return b;
            }
        } catch (Exception e) {
            System.out.println("Error fetching bill: " + e.getMessage());
        }
        return null;
    }


}


