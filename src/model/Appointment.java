package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;

    public Appointment() {}

    public Appointment(int patientId, int doctorId, LocalDate appointmentDate, LocalTime appointmentTime, String status) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Getters & Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Alias for UI/DAO compatibility
    public int getId() { return appointmentId; }
    public void setId(int id) { this.appointmentId = id; }
}
