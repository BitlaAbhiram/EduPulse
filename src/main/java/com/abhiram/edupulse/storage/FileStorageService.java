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

                writer.write(
                        student.getStudentId() + ","
                        + student.getName() + ","
                        + student.getDepartment() + ","
                        + student.getSemester() + ","
                        + student.getEmail() + ","
                        + student.getAttendance()
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

                String[] data = line.split(",");

                Student student = new Student(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        Integer.parseInt(data[3]),
                        data[4],
                        Double.parseDouble(data[5]),
                        new HashMap<>(),
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
