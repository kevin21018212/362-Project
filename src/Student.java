public class Student {
    private String studentID;
    private String name;
    private String email;
    private String address;

    // Constructors and Getter/Setters
    public Student(String studentID, String name, String email, String address) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // Methods Case
    public void enroll() {
        //To do
        System.out.println("Enroll method called for student: " + this.name);
    }
}
