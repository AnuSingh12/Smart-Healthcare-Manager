//package model;
//
//public class Bill {
//    private int billId;
//    private int patientId;
//    private int doctorId;
//    private double consultFee;
//    private double testCharges;
//    private double medicineCharges;
//
//    public Bill(int patientId, int doctorId, double consultFee, double testCharges, double medicineCharges) {
//        this.patientId = patientId;
//        this.doctorId = doctorId;
//        this.consultFee = consultFee;
//        this.testCharges = testCharges;
//        this.medicineCharges = medicineCharges;
//    }
//
//    public double getTotalAmount() {
//        return consultFee + testCharges + medicineCharges;
//    }
//
//    // getters & setters
//    public int getBillId() { return billId; }
//    public void setBillId(int billId) { this.billId = billId; }
//    public int getPatientId() { return patientId; }
//    public int getDoctorId() { return doctorId; }
//    public double getConsultFee() { return consultFee; }
//    public double getTestCharges() { return testCharges; }
//    public double getMedicineCharges() { return medicineCharges; }
//}
package model;

import java.util.List;

public class Bill {
    private int billId;
    private int patientId;
    private int doctorId;
    private double consultFee;
    private List<Item> tests;
    private List<Item> medicines;

    public Bill(int patientId, int doctorId, double consultFee, List<Item> tests, List<Item> medicines) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.consultFee = consultFee;
        this.tests = tests;
        this.medicines = medicines;
    }

    public double getTotalAmount() {
        double total = consultFee;
        if(tests != null) for(Item t : tests) total += t.getPrice();
        if(medicines != null) for(Item m : medicines) total += m.getPrice();
        return total;
    }

    public double getTestCharges() {
        double total = 0;
        if(tests != null) for(Item t : tests) total += t.getPrice();
        return total;
    }

    public double getMedicineCharges() {
        double total = 0;
        if(medicines != null) for(Item m : medicines) total += m.getPrice();
        return total;
    }

    // Getters
    public int getBillId(){ return billId; }
    public void setBillId(int billId){ this.billId = billId; }
    public int getPatientId(){ return patientId; }
    public int getDoctorId(){ return doctorId; }
    public double getConsultFee(){ return consultFee; }
    public List<Item> getTests(){ return tests; }
    public List<Item> getMedicines(){ return medicines; }
}
