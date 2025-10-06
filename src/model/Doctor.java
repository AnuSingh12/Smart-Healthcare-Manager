package model;

public class Doctor {
    private int doctorId;
    private String name;
    private String specialization;
    private double fees;
    private String availability;
    private String contact;
    
    @Override
    public String toString() {
        return name + " (" + specialization + ")";  // doctor ka naam + specialization show karega
    }


    // Constructor
    public Doctor() {}

    public Doctor(String name, String specialization, double fees, String availability, String contact) {
        this.name = name;
        this.specialization = specialization;
        this.fees = fees;
        this.availability = availability;
        this.contact = contact;
    }

    // Getters & Setters
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    
    public String getContact() { return contact;}
    public void setContact(String contact) {this.contact = contact;}
}
