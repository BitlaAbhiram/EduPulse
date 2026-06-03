package com.abhiram.edupulse.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.service.StudentService;
import com.abhiram.edupulse.util.GPAUtil;

public class Main {

    public static void main(String[] args) {

        StudentService service = new StudentService();

        HashMap<String, Integer> marks = new HashMap<>();

        marks.put("Java", 95);
        marks.put("DSA", 90);
        marks.put("DBMS", 88);
        marks.put("OS", 92);

        Student student = new Student(
                101,
                "Abhiram",
                "AIML",
                4,
                "abhiram@gmail.com",
                92.5,
                marks,
                new ArrayList<>()
        );

        service.addStudent(student);

        double average = GPAUtil.calculateAverage(marks);
        double gpa = GPAUtil.calculateGPA(marks);
        String grade = GPAUtil.calculateGrade(average);

        System.out.println("\n===== STUDENT REPORT =====");

        System.out.println(student);

        System.out.println("\nTotal Marks : "
                + GPAUtil.calculateTotal(marks));

        System.out.println("Average     : "
                + average);

        System.out.println("GPA         : "
                + gpa);

        System.out.println("Grade       : "
                + grade);
    }
}