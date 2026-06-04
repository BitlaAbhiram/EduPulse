package com.abhiram.edupulse.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.abhiram.edupulse.model.Student;

public class FileStorageService {

    private static final String FILE_PATH
            = "data/students.txt";

    public static void saveStudents(
            List<Student> students) {

        try {

            File directory = new File("data");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            BufferedWriter writer
                    = new BufferedWriter(
                            new FileWriter(FILE_PATH));

            for (Student student : students) {

                // Build subject marks string: subject1=marks1;subject2=marks2;...
                StringBuilder marksBuilder = new StringBuilder();
                HashMap<String, Integer> marks = student.getSubjectMarks();
                if (marks != null && !marks.isEmpty()) {
                    for (java.util.Map.Entry<String, Integer> entry : marks.entrySet()) {
                        marksBuilder.append(entry.getKey())
                                    .append("=")
                                    .append(entry.getValue())
                                    .append(";");
                    }
                    // Remove trailing semicolon
                    if (marksBuilder.length() > 0) {
                        marksBuilder.setLength(marksBuilder.length() - 1);
                    }
                }

                writer.write(
                        student.getStudentId() + ","
                        + student.getName() + ","
                        + student.getDepartment() + ","
                        + student.getSemester() + ","
                        + student.getEmail() + ","
                        + student.getAttendance() + ","
                        + marksBuilder.toString()
                );

                writer.newLine();
            }

            writer.close();

            System.out.println(
                    "Students saved successfully!");

            System.out.println(
                    "Saved to: "
                    + new File(FILE_PATH).getAbsolutePath()
            );

        } catch (IOException e) {

            System.out.println(
                    "Error saving students!");

            e.printStackTrace();
        }
    }

    public static List<Student> loadStudents() {

        List<Student> students = new ArrayList<>();

        try {

            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return students;
            }

            BufferedReader reader
                    = new BufferedReader(
                            new FileReader(FILE_PATH));

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", 7);

                HashMap<String, Integer> marks = new HashMap<>();

                // data[6] is the subject marks section (optional)
                if (data.length >= 7 && !data[6].isEmpty()) {
                    String[] subjectEntries = data[6].split(";");
                    for (String entry : subjectEntries) {
                        String[] kv = entry.split("=");
                        if (kv.length == 2) {
                            try {
                                marks.put(kv[0].trim(),
                                        Integer.parseInt(kv[1].trim()));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }

                Student student = new Student(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        Integer.parseInt(data[3]),
                        data[4],
                        Double.parseDouble(data[5]),
                        marks,
                        new ArrayList<>()
                );

                students.add(student);
            }

            reader.close();

            System.out.println(
                    students.size()
                    + " students loaded successfully!");

        } catch (Exception e) {

            e.printStackTrace();
        }

        return students;
    }
}