package com.abhiram.edupulse.service;

import java.util.ArrayList;
import java.util.List;

import com.abhiram.edupulse.model.Student;

public class StudentService {

    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Student added successfully!");
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student findStudentById(int id) {

        for (Student student : students) {
            if (student.getStudentId() == id) {
                return student;
            }
        }

        return null;
    }

    public void addAllStudents(List<Student> loadedStudents) {

        students.addAll(loadedStudents);
    }

    public void displayAllStudents() {

        if (students.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        for (Student student : students) {
            System.out.println();
            System.out.println(student);
            System.out.println("------------------------");
        }
    }

    public boolean deleteStudent(int id) {

        Student student = findStudentById(id);

        if (student != null) {
            students.remove(student);
            return true;
        }

        return false;
    }
}
