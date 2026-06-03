package com.abhiram.edupulse.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.service.StudentService;

public class Main {

    public static void main(String[] args) {

        StudentService service = new StudentService();

        HashMap<String, Integer> marks = new HashMap<>();

        marks.put("Java", 95);
        marks.put("DSA", 90);
        marks.put("DBMS", 88);

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

        System.out.println("\nAll Students:");

        for(Student s : service.getAllStudents()) {
            System.out.println(s);
        }
    }
}