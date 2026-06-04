package com.abhiram.edupulse.gui;

import java.util.ArrayList;
import java.util.HashMap;

import com.abhiram.edupulse.analytics.AnalyticsService;
import com.abhiram.edupulse.model.Student;
import com.abhiram.edupulse.service.StudentService;
import com.abhiram.edupulse.storage.FileStorageService;
import com.abhiram.edupulse.util.GPAUtil;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EduPulseApp extends Application {

    // ── Colour palette ────────────────────────────────────────────────────────
    private static final String BG_DARK        = "#0F172A";
    private static final String BG_DARK_HOVER  = "#1E293B";
    private static final String ACCENT         = "#6366F1";
    private static final String ACCENT_LIGHT   = "#818CF8";
    private static final String SUCCESS        = "#10B981";
    private static final String DANGER         = "#EF4444";
    private static final String WARNING        = "#F59E0B";
    private static final String BG_MAIN        = "#F1F5F9";
    private static final String BG_CARD        = "#FFFFFF";
    private static final String TEXT_PRIMARY   = "#0F172A";
    private static final String TEXT_SECONDARY = "#64748B";
    private static final String TEXT_LIGHT     = "#94A3B8";
    private static final String BORDER         = "#E2E8F0";

    private StudentService       studentService = new StudentService();
    private ObservableList<Student> studentObsList;
    private StackPane            contentArea;
    private Button               activeSidebarBtn = null;

    @Override
    public void start(Stage stage) {
        studentService.addAllStudents(FileStorageService.loadStudents());
        studentObsList = FXCollections.observableArrayList(studentService.getStudents());

        VBox     sidebar = buildSidebar();
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:" + BG_MAIN + ";");
        showDashboard();

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("EduPulse — Student Performance Analytics");
        stage.setScene(scene);
        stage.setMinWidth(950);
        stage.setMinHeight(600);
        stage.show();

        stage.setOnCloseRequest(e ->
                FileStorageService.saveStudents(studentService.getStudents()));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ═════════════════════════════════════════════════════════════════════════

    private VBox buildSidebar() {
        Label logo      = new Label("Edu");
        logo.setStyle("-fx-font-size:26px;-fx-font-weight:bold;-fx-text-fill:#FFFFFF;");
        Label logoPulse = new Label("Pulse");
        logoPulse.setStyle("-fx-font-size:26px;-fx-font-weight:bold;-fx-text-fill:" + ACCENT_LIGHT + ";");
        HBox brand = new HBox(0, logo, logoPulse);
        brand.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("Performance Analytics");
        tagline.setStyle("-fx-font-size:11px;-fx-text-fill:" + TEXT_LIGHT + ";");

        VBox brandBox = new VBox(4, brand, tagline);
        brandBox.setPadding(new Insets(28, 20, 24, 20));

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color:#1E293B;");
        divider.setMaxWidth(Double.MAX_VALUE);

        Label navLabel = new Label("NAVIGATION");
        navLabel.setStyle("-fx-font-size:10px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_LIGHT + ";-fx-padding:16 20 6 20;");

        Button dashBtn   = sidebarButton("🏠   Dashboard");
        Button studBtn   = sidebarButton("👥   Students");
        Button addBtn    = sidebarButton("➕   Add Student");
        Button deleteBtn = sidebarButton("🗑   Delete Student");
        Button analytBtn = sidebarButton("📊   Analytics");

        dashBtn.setOnAction(e   -> { setActive(dashBtn);   showDashboard();          });
        studBtn.setOnAction(e   -> { setActive(studBtn);   showStudentsPage();       });
        addBtn.setOnAction(e    -> { setActive(addBtn);    showAddStudentPage();     });
        deleteBtn.setOnAction(e -> { setActive(deleteBtn); showDeleteStudentPage();  });
        analytBtn.setOnAction(e -> { setActive(analytBtn); showAnalyticsPage();     });

        setActive(dashBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button exitBtn = new Button("⏻   Exit");
        exitBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:" + TEXT_SECONDARY
                + ";-fx-font-size:13px;-fx-padding:10 20 10 20;-fx-cursor:hand;-fx-alignment:CENTER_LEFT;");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setOnAction(e -> {
            FileStorageService.saveStudents(studentService.getStudents());
            javafx.application.Platform.exit();
        });
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(
                "-fx-background-color:" + BG_DARK_HOVER + ";-fx-text-fill:#FFFFFF;"
                + "-fx-font-size:13px;-fx-padding:10 20 10 20;-fx-cursor:hand;-fx-alignment:CENTER_LEFT;"));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(
                "-fx-background-color:transparent;-fx-text-fill:" + TEXT_SECONDARY
                + ";-fx-font-size:13px;-fx-padding:10 20 10 20;-fx-cursor:hand;-fx-alignment:CENTER_LEFT;"));

        VBox sidebar = new VBox(brandBox, divider, navLabel,
                dashBtn, studBtn, addBtn, deleteBtn, analytBtn,
                spacer, exitBtn);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color:" + BG_DARK + ";");
        return sidebar;
    }

    private Button sidebarButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(46);
        btn.setStyle(inactiveSidebarStyle());
        btn.setOnMouseEntered(e -> { if (btn != activeSidebarBtn) btn.setStyle(hoverSidebarStyle()); });
        btn.setOnMouseExited(e  -> { if (btn != activeSidebarBtn) btn.setStyle(inactiveSidebarStyle()); });
        return btn;
    }

    private void setActive(Button btn) {
        if (activeSidebarBtn != null) activeSidebarBtn.setStyle(inactiveSidebarStyle());
        activeSidebarBtn = btn;
        btn.setStyle(activeSidebarStyle());
    }

    private String activeSidebarStyle() {
        return "-fx-background-color:" + ACCENT + ";-fx-text-fill:#FFFFFF;"
                + "-fx-font-size:13px;-fx-font-weight:bold;"
                + "-fx-padding:10 20 10 20;-fx-cursor:hand;-fx-alignment:CENTER_LEFT;";
    }
    private String inactiveSidebarStyle() {
        return "-fx-background-color:transparent;-fx-text-fill:" + TEXT_SECONDARY + ";"
                + "-fx-font-size:13px;-fx-padding:10 20 10 20;-fx-cursor:hand;-fx-alignment:CENTER_LEFT;";
    }
    private String hoverSidebarStyle() {
        return "-fx-background-color:" + BG_DARK_HOVER + ";-fx-text-fill:#FFFFFF;"
                + "-fx-font-size:13px;-fx-padding:10 20 10 20;-fx-cursor:hand;-fx-alignment:CENTER_LEFT;";
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE: DASHBOARD
    // ═════════════════════════════════════════════════════════════════════════

    private void showDashboard() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(32, 36, 32, 36));
        page.setStyle("-fx-background-color:" + BG_MAIN + ";");

        int     total     = studentService.getStudents().size();
        double  avgGPA    = AnalyticsService.getClassAverage(studentService.getStudents());
        double  highGPA   = AnalyticsService.getHighestGPA(studentService.getStudents());
        Student topper    = AnalyticsService.getTopPerformer(studentService.getStudents());
        String  topperName = (topper != null) ? topper.getName() : "N/A";

        HBox statCards = new HBox(20,
                statCard("Total Students", String.valueOf(total),          "👥", ACCENT),
                statCard("Class Avg GPA",  String.format("%.2f", avgGPA),  "📈", SUCCESS),
                statCard("Highest GPA",    String.format("%.2f", highGPA), "🏆", WARNING),
                statCard("Top Performer",  topperName,                     "⭐", DANGER)
        );
        for (javafx.scene.Node n : statCards.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);

        TableView<Student> table = buildStudentTable(false);
        table.setMaxHeight(300);

        VBox tableCard = card(new VBox(12, sectionTitle("Recent Students"), scrollableTable(table)));

        page.getChildren().addAll(pageTitle("Dashboard"),
                pageSubtitle("Overview of your institution's performance"),
                statCards, tableCard);

        contentArea.getChildren().setAll(wrapScroll(page));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE: STUDENTS LIST
    // ═════════════════════════════════════════════════════════════════════════

    private void showStudentsPage() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(32, 36, 32, 36));
        page.setStyle("-fx-background-color:" + BG_MAIN + ";");

        TableView<Student> table = buildStudentTable(true);
        table.setPrefHeight(500);

        VBox tableCard = card(new VBox(12,
                sectionTitle("Student Records"), scrollableTable(table)));
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        page.getChildren().addAll(
                pageTitle("All Students"),
                pageSubtitle(studentService.getStudents().size() + " students enrolled"),
                tableCard);

        contentArea.getChildren().setAll(wrapScroll(page));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE: ADD STUDENT  (fixed layout)
    // ═════════════════════════════════════════════════════════════════════════

    private void showAddStudentPage() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(32, 36, 32, 36));
        page.setStyle("-fx-background-color:" + BG_MAIN + ";");

        // ── Fields ───────────────────────────────────────────────────────────
        TextField idField         = styledTF("e.g. 101");
        TextField nameField       = styledTF("Full name");
        TextField deptField       = styledTF("e.g. CSE");
        TextField semField        = styledTF("1 – 8");
        TextField emailField      = styledTF("student@email.com");
        TextField attendanceField = styledTF("0 – 100");
        TextField javaField       = styledTF("0 – 100");
        TextField dsaField        = styledTF("0 – 100");
        TextField dbmsField       = styledTF("0 – 100");
        TextField osField         = styledTF("0 – 100");

        // ── Custom subjects ───────────────────────────────────────────────────
        VBox customSubjectsBox = new VBox(8);
        ArrayList<TextField> customNames  = new ArrayList<>();
        ArrayList<TextField> customMarks  = new ArrayList<>();
        ArrayList<HBox>      customRows   = new ArrayList<>();

        Button addCustomBtn = accentButton("+ Add Custom Subject", WARNING);
        addCustomBtn.setOnAction(e -> {
            TextField sn = styledTF("Subject name");
            TextField sm = styledTF("Marks (0-100)");
            Button del   = iconButton("✕ Remove", DANGER);
            HBox row     = new HBox(12, sn, sm, del);
            row.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(sn, Priority.ALWAYS);
            HBox.setHgrow(sm, Priority.ALWAYS);
            del.setOnAction(ev -> {
                int i = customRows.indexOf(row);
                if (i >= 0) {
                    customRows.remove(i); customNames.remove(i); customMarks.remove(i);
                    customSubjectsBox.getChildren().remove(row);
                }
            });
            customRows.add(row); customNames.add(sn); customMarks.add(sm);
            customSubjectsBox.getChildren().add(row);
        });

        // ── Save button ───────────────────────────────────────────────────────
        Button saveBtn = accentButton("💾  Save Student", ACCENT);
        saveBtn.setPrefWidth(200);

        saveBtn.setOnAction(event -> {
            try {
                HashMap<String, Integer> marks = new HashMap<>();
                marks.put("Java", parseMarks(javaField.getText(), "Java"));
                marks.put("DSA",  parseMarks(dsaField.getText(),  "DSA"));
                marks.put("DBMS", parseMarks(dbmsField.getText(), "DBMS"));
                marks.put("OS",   parseMarks(osField.getText(),   "OS"));
                for (int i = 0; i < customNames.size(); i++) {
                    String sn = customNames.get(i).getText().trim();
                    if (sn.isEmpty()) throw new IllegalArgumentException("Custom subject name cannot be empty.");
                    marks.put(sn, parseMarks(customMarks.get(i).getText(), sn));
                }
                Student s = new Student(
                        Integer.parseInt(idField.getText().trim()),
                        nameField.getText().trim(), deptField.getText().trim(),
                        Integer.parseInt(semField.getText().trim()),
                        emailField.getText().trim(),
                        Double.parseDouble(attendanceField.getText().trim()),
                        marks, new ArrayList<>());
                studentService.addStudent(s);
                studentObsList.setAll(studentService.getStudents());
                showSuccess("Student Added", s.getName() + " enrolled successfully.");
            } catch (NumberFormatException ex) {
                showError("Invalid Input", "Please enter valid numeric values for ID, Semester, Attendance, and all Marks fields.");
            } catch (IllegalArgumentException ex) {
                showError("Validation Error", ex.getMessage());
            }
        });

        // ── Grid with explicit 50/50 column constraints ───────────────────────
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(16);
        grid.setPadding(new Insets(4, 0, 4, 0));

        // Two equal columns — THIS is the fix
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setFillWidth(true);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setFillWidth(true);
        grid.getColumnConstraints().addAll(col1, col2);

        int r = 0;

        // Section: Student Information
        grid.add(formSectionHeader("Student Information"), 0, r++, 2, 1);
        grid.add(formField("Student ID",     idField),         0, r);
        grid.add(formField("Name",           nameField),       1, r++);
        grid.add(formField("Department",     deptField),       0, r);
        grid.add(formField("Semester",       semField),        1, r++);
        grid.add(formField("Email",          emailField),      0, r);
        grid.add(formField("Attendance (%)", attendanceField), 1, r++);

        // Section: Fixed Subjects
        grid.add(formSectionHeader("Fixed Subject Marks (out of 100)"), 0, r++, 2, 1);
        grid.add(formField("Java",  javaField),  0, r);
        grid.add(formField("DSA",   dsaField),   1, r++);
        grid.add(formField("DBMS",  dbmsField),  0, r);
        grid.add(formField("OS",    osField),    1, r++);

        // Section: Custom Subjects
        grid.add(formSectionHeader("Custom Subjects (optional)"), 0, r++, 2, 1);
        grid.add(addCustomBtn,      0, r++, 2, 1);
        grid.add(customSubjectsBox, 0, r++, 2, 1);

        // Save
        HBox saveRow = new HBox(saveBtn);
        saveRow.setPadding(new Insets(8, 0, 0, 0));
        grid.add(saveRow, 0, r, 2, 1);

        VBox formCard = card(new VBox(16, sectionTitle("Enrolment Form"), grid));

        page.getChildren().addAll(
                pageTitle("Add New Student"),
                pageSubtitle("Fill in the details below to enrol a new student"),
                formCard);

        contentArea.getChildren().setAll(wrapScroll(page));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE: DELETE STUDENT
    // ═════════════════════════════════════════════════════════════════════════

    private void showDeleteStudentPage() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(32, 36, 32, 36));
        page.setStyle("-fx-background-color:" + BG_MAIN + ";");

        // ── Search by ID ──────────────────────────────────────────────────────
        TextField searchField = styledTF("Enter Student ID");
        searchField.setPrefWidth(220);
        Button searchBtn = accentButton("🔍  Search", ACCENT);

        HBox searchRow = new HBox(12, searchField, searchBtn);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        VBox resultBox = new VBox(14);
        resultBox.setVisible(false);
        resultBox.setManaged(false);

        Label resultLabel = new Label();

        VBox studentDetailCard = new VBox(10);
        studentDetailCard.setStyle(
                "-fx-background-color:#FFF7ED;-fx-border-color:" + WARNING + ";"
                + "-fx-border-width:1;-fx-border-radius:8;"
                + "-fx-background-radius:8;-fx-padding:16;");

        Button confirmDeleteBtn = accentButton("🗑  Confirm Delete", DANGER);
        confirmDeleteBtn.setPrefWidth(200);

        searchBtn.setOnAction(e -> {
            String txt = searchField.getText().trim();
            try {
                int     id    = Integer.parseInt(txt);
                Student found = studentService.findStudentById(id);
                if (found == null) {
                    resultLabel.setText("❌  No student found with ID: " + id);
                    resultLabel.setStyle("-fx-font-size:13px;-fx-text-fill:" + DANGER + ";");
                    resultBox.getChildren().setAll(resultLabel);
                    studentDetailCard.setVisible(false); studentDetailCard.setManaged(false);
                    confirmDeleteBtn.setVisible(false);  confirmDeleteBtn.setManaged(false);
                } else {
                    resultLabel.setText("Student found — review before deleting:");
                    resultLabel.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_PRIMARY + ";");
                    studentDetailCard.getChildren().setAll(
                            detailRow("ID",         String.valueOf(found.getStudentId())),
                            detailRow("Name",       found.getName()),
                            detailRow("Department", found.getDepartment()),
                            detailRow("Semester",   String.valueOf(found.getSemester())),
                            detailRow("Email",      found.getEmail()),
                            detailRow("Attendance", found.getAttendance() + "%"),
                            detailRow("GPA",        String.format("%.2f",
                                    GPAUtil.calculateGPA(found.getSubjectMarks())))
                    );
                    studentDetailCard.setVisible(true); studentDetailCard.setManaged(true);
                    confirmDeleteBtn.setVisible(true);  confirmDeleteBtn.setManaged(true);
                    resultBox.getChildren().setAll(resultLabel, studentDetailCard, confirmDeleteBtn);
                    confirmDeleteBtn.setOnAction(ev -> {
                        Alert dlg = new Alert(Alert.AlertType.CONFIRMATION);
                        dlg.setTitle("Confirm Deletion");
                        dlg.setHeaderText("Delete " + found.getName() + "?");
                        dlg.setContentText("This action cannot be undone.");
                        styleDialog(dlg);
                        dlg.showAndWait().ifPresent(type -> {
                            if (type == ButtonType.OK) {
                                studentService.deleteStudent(found.getStudentId());
                                studentObsList.setAll(studentService.getStudents());
                                searchField.clear();
                                resultBox.setVisible(false); resultBox.setManaged(false);
                                showSuccess("Deleted", found.getName() + " removed.");
                            }
                        });
                    });
                }
                resultBox.setVisible(true); resultBox.setManaged(true);
            } catch (NumberFormatException ex) {
                showError("Invalid ID", "Please enter a numeric Student ID.");
            }
        });

        VBox searchCard = card(new VBox(16, sectionTitle("Find Student"), searchRow, resultBox));

        // ── Table with inline delete ──────────────────────────────────────────
        TableView<Student> table = buildStudentTable(true);
        table.setPrefHeight(300);

        Label hint = new Label("Click Delete on any row to remove that student.");
        hint.setStyle("-fx-font-size:12px;-fx-text-fill:" + TEXT_SECONDARY + ";");

        VBox tableCard = card(new VBox(10,
                sectionTitle("Delete from Table"), hint, scrollableTable(table)));

        page.getChildren().addAll(
                pageTitle("Delete Student"),
                pageSubtitle("Search by ID or click Delete in the table below"),
                searchCard, tableCard);

        contentArea.getChildren().setAll(wrapScroll(page));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE: ANALYTICS
    // ═════════════════════════════════════════════════════════════════════════

    private void showAnalyticsPage() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(32, 36, 32, 36));
        page.setStyle("-fx-background-color:" + BG_MAIN + ";");

        int     total  = studentService.getStudents().size();
        double  avgGPA = AnalyticsService.getClassAverage(studentService.getStudents());
        double  hi     = AnalyticsService.getHighestGPA(studentService.getStudents());
        Student topper = AnalyticsService.getTopPerformer(studentService.getStudents());

        HBox statRow = new HBox(20,
                statCard("Total Students", String.valueOf(total),        "👥", ACCENT),
                statCard("Average GPA",    String.format("%.2f", avgGPA),"📈", SUCCESS),
                statCard("Highest GPA",    String.format("%.2f", hi),    "🏆", WARNING),
                statCard("Top Performer",  topper != null ? topper.getName() : "N/A", "⭐", DANGER)
        );
        for (javafx.scene.Node n : statRow.getChildren()) HBox.setHgrow(n, Priority.ALWAYS);

        // GPA table
        TableView<Student> gpaTable = new TableView<>(studentObsList);
        gpaTable.setStyle(tableStyle());
        gpaTable.setPrefHeight(400);

        TableColumn<Student, Integer> idCol  = tableColumn("ID",         "studentId",  70);
        TableColumn<Student, String>  nmCol  = tableColumn("Name",       "name",       160);
        TableColumn<Student, String>  dpCol  = tableColumn("Department", "department", 130);
        TableColumn<Student, Double>  atCol  = tableColumn("Attendance", "attendance", 110);

        TableColumn<Student, String> gpaCol = new TableColumn<>("GPA");
        gpaCol.setMinWidth(90);
        gpaCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("%.2f", GPAUtil.calculateGPA(cd.getValue().getSubjectMarks()))));
        gpaCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                double v = Double.parseDouble(item);
                String c = v >= 8 ? SUCCESS : v >= 6 ? WARNING : DANGER;
                setStyle("-fx-text-fill:" + c + ";-fx-font-weight:bold;-fx-font-size:13px;");
            }
        });

        TableColumn<Student, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setMinWidth(80);
        gradeCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        GPAUtil.calculateGrade(GPAUtil.calculateAverage(cd.getValue().getSubjectMarks()))));
        gradeCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                String bg = (item.equals("O") || item.equals("A+")) ? SUCCESS
                          : (item.equals("A") || item.equals("B"))  ? WARNING : DANGER;
                setStyle("-fx-background-color:" + bg + "1A;-fx-text-fill:" + bg
                        + ";-fx-font-weight:bold;-fx-font-size:12px;"
                        + "-fx-padding:3 8 3 8;-fx-background-radius:4;");
            }
        });

        gpaTable.getColumns().addAll(idCol, nmCol, dpCol, atCol, gpaCol, gradeCol);

        VBox gpaCard = card(new VBox(12,
                sectionTitle("Student GPA Breakdown"), scrollableTable(gpaTable)));

        page.getChildren().addAll(
                pageTitle("Analytics"),
                pageSubtitle("Performance insights across all students"),
                statRow, gpaCard);

        contentArea.getChildren().setAll(wrapScroll(page));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  TABLE BUILDER
    // ═════════════════════════════════════════════════════════════════════════

    private TableView<Student> buildStudentTable(boolean withDelete) {
        TableView<Student> table = new TableView<>(studentObsList);
        table.setStyle(tableStyle());
        table.setPlaceholder(new Label("No students enrolled yet."));
        // Do NOT use CONSTRAINED_RESIZE_POLICY — let columns be their natural width
        // so horizontal scroll can work properly

        table.getColumns().addAll(
                tableColumn("ID",          "studentId",  70),
                tableColumn("Name",        "name",      150),
                tableColumn("Department",  "department",130),
                tableColumn("Semester",    "semester",   90),
                tableColumn("Email",       "email",     200),
                tableColumn("Attendance",  "attendance", 110)
        );

        if (withDelete) {
            TableColumn<Student, Void> delCol = new TableColumn<>("Action");
            delCol.setMinWidth(110);
            delCol.setCellFactory(col -> new TableCell<>() {
                private final Button btn = iconButton("🗑 Delete", DANGER);
                {
                    btn.setOnAction(e -> {
                        Student s = getTableView().getItems().get(getIndex());
                        Alert dlg = new Alert(Alert.AlertType.CONFIRMATION);
                        dlg.setTitle("Delete Student");
                        dlg.setHeaderText("Delete " + s.getName() + "?");
                        dlg.setContentText("This will permanently remove the record.");
                        styleDialog(dlg);
                        dlg.showAndWait().ifPresent(t -> {
                            if (t == ButtonType.OK) {
                                studentService.deleteStudent(s.getStudentId());
                                studentObsList.setAll(studentService.getStudents());
                            }
                        });
                    });
                }
                @Override protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            });
            table.getColumns().add(delCol);
        }
        return table;
    }

    /**
     * Wraps a TableView in a horizontal+vertical ScrollPane so columns
     * can always be scrolled left-to-right.
     */
    private ScrollPane scrollableTable(TableView<Student> table) {
        ScrollPane sp = new ScrollPane(table);
        sp.setFitToWidth(true);   // fills width when there's room
        sp.setFitToHeight(false); // let table dictate its own height
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color:transparent;"
                + "-fx-background:transparent;"
                + "-fx-border-color:transparent;");
        // Ensure the table itself has a minimum width wider than its container
        // so horizontal scroll actually activates when needed
        table.setMinWidth(750);
        return sp;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private ScrollPane wrapScroll(VBox page) {
        ScrollPane sp = new ScrollPane(page);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:" + BG_MAIN + ";-fx-background:" + BG_MAIN + ";");
        return sp;
    }

    private VBox statCard(String label, String value, String icon, String color) {
        Label iconLbl  = new Label(icon);
        iconLbl.setStyle("-fx-font-size:28px;");
        Label valueLbl = new Label(value);
        valueLbl.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_PRIMARY + ";");
        valueLbl.setWrapText(true);
        Label labelLbl = new Label(label);
        labelLbl.setStyle("-fx-font-size:12px;-fx-text-fill:" + TEXT_SECONDARY + ";");
        Region bar = new Region();
        bar.setPrefHeight(4); bar.setPrefWidth(40);
        bar.setStyle("-fx-background-color:" + color + ";-fx-background-radius:2;");
        VBox card = new VBox(6, iconLbl, valueLbl, labelLbl, bar);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color:" + BG_CARD + ";-fx-background-radius:12;"
                + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),10,0,0,2);");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private VBox card(javafx.scene.Node content) {
        VBox c = new VBox(content);
        c.setStyle("-fx-background-color:" + BG_CARD + ";-fx-background-radius:12;"
                + "-fx-padding:20;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),12,0,0,2);");
        return c;
    }

    private Label pageTitle(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_PRIMARY + ";");
        return l;
    }
    private Label pageSubtitle(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:14px;-fx-text-fill:" + TEXT_SECONDARY + ";");
        return l;
    }
    private Label sectionTitle(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_PRIMARY + ";");
        return l;
    }
    private Label formSectionHeader(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-font-size:11px;-fx-font-weight:bold;-fx-text-fill:" + ACCENT
                + ";-fx-padding:10 0 2 0;");
        return l;
    }

    /** Label + field stacked vertically, fills the grid cell width. */
    private VBox formField(String labelText, TextField field) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_SECONDARY + ";");
        field.setMaxWidth(Double.MAX_VALUE);      // stretch to full cell width
        GridPane.setFillWidth(field, true);
        VBox box = new VBox(5, lbl, field);
        GridPane.setFillWidth(box, true);
        GridPane.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private TextField styledTF(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        String base = "-fx-background-color:#F8FAFC;-fx-border-color:" + BORDER
                + ";-fx-border-width:1;-fx-border-radius:8;-fx-background-radius:8;"
                + "-fx-padding:9 12 9 12;-fx-font-size:13px;";
        String focus = "-fx-background-color:#FFFFFF;-fx-border-color:" + ACCENT
                + ";-fx-border-width:1.5;-fx-border-radius:8;-fx-background-radius:8;"
                + "-fx-padding:9 12 9 12;-fx-font-size:13px;";
        tf.setStyle(base);
        tf.focusedProperty().addListener((obs, o, n) -> tf.setStyle(n ? focus : base));
        return tf;
    }

    private Button accentButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;"
                + "-fx-font-weight:bold;-fx-font-size:13px;"
                + "-fx-padding:10 22 10 22;-fx-background-radius:8;-fx-cursor:hand;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.88));
        btn.setOnMouseExited(e  -> btn.setOpacity(1.0));
        return btn;
    }

    private Button iconButton(String text, String color) {
        Button btn = new Button(text);
        String base = "-fx-background-color:" + color + "1A;-fx-text-fill:" + color
                + ";-fx-font-size:12px;-fx-font-weight:bold;"
                + "-fx-padding:5 12 5 12;-fx-background-radius:6;-fx-cursor:hand;";
        String hover = "-fx-background-color:" + color + ";-fx-text-fill:white;"
                + "-fx-font-size:12px;-fx-font-weight:bold;"
                + "-fx-padding:5 12 5 12;-fx-background-radius:6;-fx-cursor:hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private HBox detailRow(String key, String value) {
        Label k = new Label(key + ":");
        k.setStyle("-fx-font-weight:bold;-fx-text-fill:" + TEXT_SECONDARY + ";-fx-min-width:110;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill:" + TEXT_PRIMARY + ";-fx-font-size:13px;");
        return new HBox(8, k, v);
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<Student, T> tableColumn(String title, String prop, double minW) {
        TableColumn<Student, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setMinWidth(minW);
        col.setStyle("-fx-font-size:13px;");
        return col;
    }

    private String tableStyle() {
        return "-fx-background-color:" + BG_CARD + ";-fx-border-color:" + BORDER
                + ";-fx-border-radius:8;-fx-background-radius:8;-fx-font-size:13px;";
    }

    private int parseMarks(String text, String subject) {
        int m = Integer.parseInt(text.trim());
        if (m < 0 || m > 100)
            throw new IllegalArgumentException(subject + " marks must be 0 – 100.");
        return m;
    }

    private void showSuccess(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        styleDialog(a); a.showAndWait();
    }
    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        styleDialog(a); a.showAndWait();
    }
    private void styleDialog(Alert a) {
        a.getDialogPane().setStyle("-fx-background-color:" + BG_CARD + ";-fx-font-size:13px;");
    }

    public static void main(String[] args) { launch(); }
}