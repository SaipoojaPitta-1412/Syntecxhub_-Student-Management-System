import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Serializable allows objects to be saved directly to a file
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private int age;
    private String course;

    public Student(int id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCourse() { return course; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setCourse(String course) { this.course = course; }

    public void displayStudent() {
        System.out.printf("%-10d %-20s %-10d %-20s%n", id, name, age, course);
    }
}

public class StudentManagementSystem {

    private static final String FILE_NAME = "students.dat";
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load existing data from file at startup
        loadDataFromFile();

        while (true) {
            System.out.println("\n========================================");
            System.out.println("       STUDENT MANAGEMENT SYSTEM        ");
            System.out.println("========================================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.println("========================================");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    saveDataToFile();
                    System.out.println("\nData saved. Thank you for using Student Management System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
            }
        }
    }

    // Load ArrayList from local .dat file
    @SuppressWarnings("unchecked")
    private static void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Notice: Initializing new student dataset.");
        }
    }

    // Save ArrayList to local .dat file
    private static void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    static void addStudent() {
        System.out.println("\n------------- ADD STUDENT -------------");

        int id = readInt("Enter Student ID: ");

        if (findStudent(id) != null) {
            System.out.println("Error: Student ID already exists!");
            return;
        }

        String name = readString("Enter Student Name: ");
        int age = readInt("Enter Student Age: ");

        if (age <= 0 || age >= 100) {
            System.out.println("Invalid age! Please enter an age between 1 and 100.");
            return;
        }

        String course = readString("Enter Course: ");

        students.add(new Student(id, name, age, course));
        saveDataToFile();
        System.out.println("Student added successfully!");
    }

    static void viewStudents() {
        System.out.println("\n------------- STUDENT RECORDS -------------");

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.printf("%-10s %-20s %-10s %-20s%n", "ID", "Name", "Age", "Course");
        System.out.println("------------------------------------------------------------");

        for (Student student : students) {
            student.displayStudent();
        }
    }

    static void updateStudent() {
        System.out.println("\n------------- UPDATE STUDENT -------------");

        int id = readInt("Enter Student ID to update: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        String name = readString("Enter New Name: ");
        int age = readInt("Enter New Age: ");

        if (age <= 0 || age >= 100) {
            System.out.println("Invalid age!");
            return;
        }

        String course = readString("Enter New Course: ");

        student.setName(name);
        student.setAge(age);
        student.setCourse(course);

        saveDataToFile();
        System.out.println("Student updated successfully!");
    }

    static void deleteStudent() {
        System.out.println("\n------------- DELETE STUDENT -------------");

        int id = readInt("Enter Student ID to delete: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        students.remove(student);
        saveDataToFile();
        System.out.println("Student deleted successfully!");
    }

    static Student findStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    static String readString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty!");
        }
    }
}