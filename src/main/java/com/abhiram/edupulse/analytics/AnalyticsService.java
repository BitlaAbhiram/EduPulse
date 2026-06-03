package com.abhiram.edupulse.analytics;

import java.util.List;

import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.util.GPAUtil;

public class AnalyticsService {

    public static double getClassAverage(List<Student> students) {

        if (students.isEmpty()) return 0;

        double total = 0;

        for(Student student : students) {

            total += GPAUtil.calculateGPA(
                    student.getSubjectMarks()
            );
        }

        return total / students.size();
    }

    public static Student getTopPerformer(
            List<Student> students) {

        if(students.isEmpty()) return null;

        Student topper = students.get(0);

        for(Student student : students) {

            if(GPAUtil.calculateGPA(
                    student.getSubjectMarks())
                    >
                    GPAUtil.calculateGPA(
                            topper.getSubjectMarks())) {

                topper = student;
            }
        }

        return topper;
    }

    public static double getHighestGPA(
            List<Student> students) {

        if(students.isEmpty()) return 0;

        double highest = 0;

        for(Student student : students) {

            double gpa =
                    GPAUtil.calculateGPA(
                            student.getSubjectMarks());

            if(gpa > highest) {
                highest = gpa;
            }
        }

        return highest;
    }
}