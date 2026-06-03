package com.abhiram.edupulse.util;

import java.util.Map;

import com.abhiram.edupulse.model.Student;

public class ReportGenerator {

    public static void generateReport(Student student) {

        System.out.println("\n=================================");
        System.out.println("       STUDENT REPORT");
        System.out.println("=================================");

        System.out.println("Student ID : " + student.getStudentId());
        System.out.println("Name       : " + student.getName());
        System.out.println("Department : " + student.getDepartment());
        System.out.println("Semester   : " + student.getSemester());

        System.out.println("\nSubject Marks:");

        for(Map.Entry<String,Integer> entry :
                student.getSubjectMarks().entrySet()) {

            System.out.printf("%-15s %d%n",
                    entry.getKey(),
                    entry.getValue());
        }

        double average =
                GPAUtil.calculateAverage(student.getSubjectMarks());

        double gpa =
                GPAUtil.calculateGPA(student.getSubjectMarks());

        String grade =
                GPAUtil.calculateGrade(average);

        System.out.println("\n---------------------------------");

        System.out.println("Total Marks : "
                + GPAUtil.calculateTotal(student.getSubjectMarks()));

        System.out.println("Average     : "
                + String.format("%.2f", average));

        System.out.println("GPA         : "
                + String.format("%.2f", gpa));

        System.out.println("Grade       : "
                + grade);

        System.out.println("=================================");
    }
}