package com.abhiram.edupulse.model;

import java.util.HashMap;
import java.util.List;

public class Student {

    private int studentId;
    private String name;
    private String department;
    private int semester;
    private String email;
    private double attendance;

    private HashMap<String, Integer> subjectMarks;

    private List<Double> previousSemesterGPAs;

    public Student(
            int studentId,
            String name,
            String department,
            int semester,
            String email,
            double attendance,
            HashMap<String, Integer> subjectMarks,
            List<Double> previousSemesterGPAs) {

        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.semester = semester;
        this.email = email;
        this.attendance = attendance;
        this.subjectMarks = subjectMarks;
        this.previousSemesterGPAs = previousSemesterGPAs;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public String getDepartment() {
        return department;
    }

    public int getSemester() {
        return semester;
    }

    public String getEmail() {
        return email;
    }

    public double getAttendance() {
        return attendance;
    }

    public HashMap<String, Integer> getSubjectMarks() {
        return subjectMarks;
    }

    public List<Double> getPreviousSemesterGPAs() {
        return previousSemesterGPAs;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId
                + "\nName: " + name
                + "\nDepartment: " + department
                + "\nSemester: " + semester
                + "\nEmail: " + email
                + "\nAttendance: " + attendance;
    }
}
