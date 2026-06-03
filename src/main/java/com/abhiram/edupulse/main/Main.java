package com.abhiram.edupulse.main;

import java.util.Scanner;

import com.abhiram.edupulse.service.StudentService;

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
            System.out.println("5. Exit");

            System.out.print("\nEnter Choice: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("Add Student - Coming Next");
                    break;

                case 2:
                    service.displayAllStudents();
                    break;

                case 3:
                    System.out.println("Search Student - Coming Next");
                    break;

                case 4:
                    System.out.println("Delete Student - Coming Next");
                    break;

                case 5:
                    System.out.println("Thank you for using EduPulse!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}