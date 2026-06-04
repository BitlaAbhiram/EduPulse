package com.abhiram.edupulse.gui;

import java.util.ArrayList;
import java.util.HashMap;

import com.abhiram.edupulse.analytics.AnalyticsService;
import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.service.StudentService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EduPulseApp extends Application {

    private StudentService studentService = new StudentService();

    @Override
    public void start(Stage stage) {

        Label title = new Label("EduPulse");
        title.setStyle(
                "-fx-font-size: 32px;"
                + "-fx-font-weight: bold;"
        );
        title.setFont(new Font(28));

        Label subtitle
                = new Label("Student Performance Analytics");

        Button addStudentBtn
                = new Button("Add Student");

        addStudentBtn.setOnAction(e
                -> openAddStudentWindow());

        Button viewStudentsBtn
                = new Button("View Students");

        viewStudentsBtn.setOnAction(e
                -> openStudentsTable());

        Button analyticsBtn
                = new Button("Analytics");

        analyticsBtn.setOnAction(
                e -> openAnalyticsWindow());

        Button exitBtn
                = new Button("Exit");

        exitBtn.setOnAction(e -> stage.close());

        VBox root = new VBox(
                15,
                title,
                subtitle,
                addStudentBtn,
                viewStudentsBtn,
                analyticsBtn,
                exitBtn
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene
                = new Scene(root, 700, 500);

        stage.setTitle("EduPulse Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void openAnalyticsWindow() {

        Stage stage = new Stage();

        int totalStudents
                = studentService.getStudents().size();

        double averageGPA
                = AnalyticsService.getClassAverage(
                        studentService.getStudents());

        double highestGPA
                = AnalyticsService.getHighestGPA(
                        studentService.getStudents());

        String topperName = "N/A";

        Student topper
                = AnalyticsService.getTopPerformer(
                        studentService.getStudents());

        if (topper != null) {
            topperName = topper.getName();
        }

        Label title
                = new Label("EduPulse Analytics");

        title.setStyle(
                "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
        );

        Label total
                = new Label("Total Students : "
                        + totalStudents);

        Label avg
                = new Label("Average GPA : "
                        + String.format("%.2f",
                                averageGPA));

        Label highest
                = new Label("Highest GPA : "
                        + String.format("%.2f",
                                highestGPA));

        Label topperLabel
                = new Label("Top Performer : "
                        + topperName);

        VBox root
                = new VBox(
                        15,
                        title,
                        total,
                        avg,
                        highest,
                        topperLabel
                );

        root.setPadding(
                new Insets(20));

        Scene scene
                = new Scene(root, 450, 300);

        stage.setTitle("Analytics");

        stage.setScene(scene);

        stage.show();
    }

    private void openStudentsTable() {

        Stage stage = new Stage();

        TableView<Student> table
                = new TableView<>();

        TableColumn<Student, Integer> idColumn
                = new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentId"));

        TableColumn<Student, String> nameColumn
                = new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Student, String> deptColumn
                = new TableColumn<>("Department");

        deptColumn.setCellValueFactory(
                new PropertyValueFactory<>("department"));

        TableColumn<Student, Integer> semColumn
                = new TableColumn<>("Semester");

        semColumn.setCellValueFactory(
                new PropertyValueFactory<>("semester"));

        TableColumn<Student, Double> attendanceColumn
                = new TableColumn<>("Attendance");

        attendanceColumn.setCellValueFactory(
                new PropertyValueFactory<>("attendance"));

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                deptColumn,
                semColumn,
                attendanceColumn
        );

        table.getItems().addAll(
                studentService.getStudents()
        );

        VBox root
                = new VBox(table);

        Scene scene
                = new Scene(root, 700, 400);

        stage.setTitle("Students");

        stage.setScene(scene);

        stage.show();
    }

    private void openAddStudentWindow() {

        Stage stage = new Stage();

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField deptField = new TextField();
        TextField semField = new TextField();
        TextField emailField = new TextField();
        TextField attendanceField = new TextField();

        Button saveBtn
                = new Button("Save Student");

        saveBtn.setOnAction(event -> {

            try {

                Student student
                        = new Student(
                                Integer.parseInt(
                                        idField.getText()),
                                nameField.getText(),
                                deptField.getText(),
                                Integer.parseInt(
                                        semField.getText()),
                                emailField.getText(),
                                Double.parseDouble(
                                        attendanceField.getText()),
                                new HashMap<>(),
                                new ArrayList<>()
                        );

                studentService.addStudent(student);

                Alert alert
                        = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle("Success");

                alert.setHeaderText(null);

                alert.setContentText(
                        "Student Added Successfully!");

                alert.showAndWait();

                stage.close();

            } catch (Exception ex) {

                Alert alert
                        = new Alert(
                                Alert.AlertType.ERROR);

                alert.setTitle("Error");

                alert.setHeaderText(null);

                alert.setContentText(
                        "Please enter valid data.");

                alert.showAndWait();
            }
        });

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Student ID"), 0, 0);
        grid.add(idField, 1, 0);

        grid.add(new Label("Name"), 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Department"), 0, 2);
        grid.add(deptField, 1, 2);

        grid.add(new Label("Semester"), 0, 3);
        grid.add(semField, 1, 3);

        grid.add(new Label("Email"), 0, 4);
        grid.add(emailField, 1, 4);

        grid.add(new Label("Attendance"), 0, 5);
        grid.add(attendanceField, 1, 5);

        grid.add(saveBtn, 1, 6);

        Scene scene
                = new Scene(grid, 450, 350);

        stage.setTitle("Add Student");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
