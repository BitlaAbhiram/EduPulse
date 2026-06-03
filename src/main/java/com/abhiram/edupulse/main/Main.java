package com.abhiram.edupulse.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.abhiram.edupulse.analytics.AnalyticsService;
import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.service.StudentService;
import com.abhiram.edupulse.util.ReportGenerator;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StudentService service = new StudentService();

        while (true) {

            System.out.println("\n=================================");
            System.out.println("         EDUPULSE");
            System.out.println("=================================");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Generate Report");
            System.out.println("6. Analytics Dashboard");
            System.out.println("7. Exit");

            System.out.print("\nEnter Choice: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    addStudent(scanner, service);
                    break;

                case 2:
                    service.displayAllStudents();
                    break;

                case 3:
                    searchStudent(scanner, service);
                    break;

                case 4:
                    deleteStudent(scanner, service);
                    break;

                case 5:
                    generateReport(scanner, service);
                    break;

                case 6:
                    showAnalytics(service);
                    break;

                case 7:
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    private static void deleteStudent(
            Scanner scanner,
            StudentService service) {

        System.out.print("Enter Student ID: ");

        int id = scanner.nextInt();

        if (service.deleteStudent(id)) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }

    private static void generateReport(
            Scanner scanner,
            StudentService service) {

        System.out.print("Enter Student ID: ");

        int id = scanner.nextInt();

        Student student
                = service.findStudentById(id);

        if (student != null) {

            ReportGenerator.generateReport(student);

        } else {

            System.out.println("Student not found!");

        }
    }

    private static void showAnalytics(
            StudentService service) {

        if (service.getStudents().isEmpty()) {

            System.out.println("No student data found!");
            return;
        }

        System.out.println("\n=================================");
        System.out.println("      EDUPULSE ANALYTICS");
        System.out.println("=================================");

        System.out.println(
                "Class Average GPA : "
                + String.format("%.2f",
                        AnalyticsService.getClassAverage(
                                service.getStudents()
                        )));

        Student topper
                = AnalyticsService.getTopPerformer(
                        service.getStudents());

        System.out.println(
                "Top Performer    : "
                + topper.getName());

        System.out.println(
                "Highest GPA      : "
                + String.format("%.2f",
                        AnalyticsService.getHighestGPA(
                                service.getStudents()
                        )));

        System.out.println(
                "Total Students   : "
                + service.getStudents().size());

        System.out.println("=================================");
    }

    private static void searchStudent(
            Scanner scanner,
            StudentService service) {

        System.out.print("Enter Student ID: ");

        int id = scanner.nextInt();

        Student student = service.findStudentById(id);

        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found!");
        }
    }

    private static void addStudent(Scanner scanner, StudentService service) {

        System.out.print("Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Department: ");
        String department = scanner.nextLine();

        System.out.print("Semester: ");
        int semester = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Attendance (%): ");
        double attendance = scanner.nextDouble();

        HashMap<String, Integer> marks = new HashMap<>();

        System.out.print("Number of Subjects: ");
        int subjects = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= subjects; i++) {

            System.out.print("Subject Name: ");
            String subject = scanner.nextLine();

            System.out.print("Marks: ");
            int score = scanner.nextInt();
            scanner.nextLine();

            marks.put(subject, score);
        }

        Student student = new Student(
                id,
                name,
                department,
                semester,
                email,
                attendance,
                marks,
                new ArrayList<>()
        );

        service.addStudent(student);
    }

}
