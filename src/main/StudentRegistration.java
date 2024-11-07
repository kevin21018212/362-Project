package main;

import java.util.ArrayList;
import java.util.List;

public class StudentRegistration {
    private String fullName;
    private String dateOfBirth;
    private String contactInfo;
    private String address;
    private String programOfStudy;
    private String academicTerm;
    private List<String> documents;

    public StudentRegistration(String fullName, String dateOfBirth, String contactInfo,
                               String address, String programOfStudy, String academicTerm) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.address = address;
        this.programOfStudy = programOfStudy;
        this.academicTerm = academicTerm;
        this.documents = new ArrayList<>();
    }

    public boolean validateInformation() {
        return !fullName.isEmpty() &&
                !dateOfBirth.isEmpty() &&
                !contactInfo.isEmpty() &&
                !address.isEmpty() &&
                !programOfStudy.isEmpty() &&
                !academicTerm.isEmpty();
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getContactInfo() { return contactInfo; }
    public String getAddress() { return address; }
    public String getProgramOfStudy() { return programOfStudy; }
    public String getAcademicTerm() { return academicTerm; }
}