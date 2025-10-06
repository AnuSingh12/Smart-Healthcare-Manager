package model;

public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String disease;
    private String admitStatus;
    
    @Override
    public String toString() {
        return name;  // patient ka naam return karega
    }


    // Constructor
    public Patient() {}

    public Patient(String name, int age, String gender, String contact, String disease, String admitStatus) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.disease = disease;
        this.admitStatus = admitStatus;
    }

    // Getters & Setters
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getAdmitStatus() { return admitStatus; }
    public void setAdmitStatus(String admitStatus) { this.admitStatus = admitStatus; }
}
