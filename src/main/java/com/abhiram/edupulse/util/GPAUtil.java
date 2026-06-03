package com.abhiram.edupulse.util;

import java.util.Map;

public class GPAUtil {

    public static int calculateTotal(Map<String, Integer> marks) {

        int total = 0;

        for (int score : marks.values()) {
            total += score;
        }

        return total;
    }

    public static double calculateAverage(Map<String, Integer> marks) {

        if (marks.isEmpty()) {
            return 0;
        }

        return (double) calculateTotal(marks) / marks.size();
    }

    public static double calculateGPA(Map<String, Integer> marks) {

        double avg = calculateAverage(marks);

        return Math.round((avg / 10.0) * 100.0) / 100.0;
    }

    public static String calculateGrade(double average) {

        if (average >= 90) return "O";
        if (average >= 80) return "A+";
        if (average >= 70) return "A";
        if (average >= 60) return "B";
        if (average >= 50) return "C";

        return "F";
    }
}
