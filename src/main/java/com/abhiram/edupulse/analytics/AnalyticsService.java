package com.abhiram.edupulse.analytics;

import java.util.List;

import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.util.GPAUtil;

public class AnalyticsService {

    public static double getClassAverage(List<Student> students) {

        if (students.isEmpty()) {
            return 0;
        }

        double totalAverage = 0;

        for (Student student : students) {
            totalAverage += GPAUtil.calculateAverage(
                    student.getSubjectMarks()
            );
        }

        return totalAverage / students.size();
    }

    public static Student getTopPerformer(List<Student> students) {

        if (students.isEmpty()) {
            return null;
        }

        Student topper = students.get(0);

        for (Student student : students) {

            double currentGPA =
                    GPAUtil.calculateGPA(student.getSubjectMarks());

            double topperGPA =
                    GPAUtil.calculateGPA(topper.getSubjectMarks());

            if (currentGPA > topperGPA) {
                topper = student;
            }
        }

        return topper;
    }
}
