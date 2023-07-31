import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Student class to represent individual students
class Student {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

// StudentManagementSystem class to manage the collection of students
class StudentManagementSystem {
    private List<Student> students;
    private File dataFile; // The file to store student data

    public StudentManagementSystem() {
        students = new ArrayList<>();
        dataFile = new File("students.txt");
        loadStudents(); // Load students from the file when the system is initialized
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    // Method to load students from the file
    private void loadStudents() {
        if (dataFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String name = parts[0].trim();
                        int rollNumber = Integer.parseInt(parts[1].trim());
                        String grade = parts[2].trim();
                        Student student = new Student(name, rollNumber, grade);
                        students.add(student);
                    }
                }
            } catch (IOException e) {
                // Error occurred while reading the file
                // You can handle the exception according to your requirement
                e.printStackTrace();
            }
        }
    }

    // Method to save students to the file
    public void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
                writer.newLine();
            }
        } catch (IOException e) {
            // Error occurred while writing the file
            // You can handle the exception according to your requirement
            e.printStackTrace();
        }
    }

    // Additional methods for searching, removing, and editing students
    public void editStudent(Student oldStudent, Student newStudent) {
        int index = students.indexOf(oldStudent);
        if (index != -1) {
            students.set(index, newStudent);
        }
    }
}


// StudentManagementGUI class to create the GUI for interacting with the student management system
class StudentManagementGUI extends JFrame {
    private StudentManagementSystem managementSystem;

    // GUI components...
    private JTextField nameField;
    private JTextField rollNumberField;
    private JTextField gradeField;
    private JTextArea outputArea;

    public StudentManagementGUI() {
        setTitle("Student Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        managementSystem = new StudentManagementSystem();

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField(10);
        inputPanel.add(rollNumberField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField(5);
        inputPanel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        inputPanel.add(addButton);

        JButton displayButton = new JButton("Display All Students");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudents();
            }
        });
        inputPanel.add(displayButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(10,30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add padding to the input panel
        javax.swing.border.EmptyBorder inputPanelPadding = new javax.swing.border.EmptyBorder(30, 30, 30, 30);
        inputPanel.setBorder(inputPanelPadding);

        // Add padding to the output area
        javax.swing.border.EmptyBorder outputAreaPadding = new javax.swing.border.EmptyBorder(30, 30, 30, 30);
        outputArea.setBorder(outputAreaPadding);

        JButton removeButton = new JButton("Remove Student");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStudent();
            }
        });
        inputPanel.add(removeButton);

        JButton searchButton = new JButton("Search Student");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        inputPanel.add(searchButton);

        JButton editButton = new JButton("Edit Student");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });
        inputPanel.add(editButton);
    }
    

    // Method to add a student
    private void addStudent() {
        // Implementation for adding a student
        // Use the managementSystem to add the student to the collection
        String name = nameField.getText();
        String rollNumberStr = rollNumberField.getText();
        String grade = gradeField.getText();

        if (name.isEmpty() || rollNumberStr.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollNumberStr);
            Student student = new Student(name, rollNumber, grade);
            managementSystem.addStudent(student);
            outputArea.append("Student added: " + student.toString() + "\n");
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Roll Number. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to display all students
    private void displayStudents() {
        // Implementation for displaying all students
        // Use the managementSystem to get all students and show them in the outputArea
        List<Student> students = managementSystem.getAllStudents();
        outputArea.setText("");
        if (students.isEmpty()) {
            outputArea.append("No students found.\n");
        } else {
            for (Student student : students) {
                outputArea.append(student.toString() + "\n");
            }
        }
    }

   private void removeStudent() {
        String rollNumberStr = rollNumberField.getText();
        if (rollNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Roll Number of the student to remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollNumberStr);
            Student studentToRemove = managementSystem.searchStudent(rollNumber);
            if (studentToRemove != null) {
                managementSystem.removeStudent(studentToRemove);
                outputArea.append("Student removed: " + studentToRemove.toString() + "\n");
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this, "Student not found with Roll Number: " + rollNumber, "Student Not Found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Roll Number. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudent() {
        String rollNumberStr = rollNumberField.getText();
        if (rollNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Roll Number of the student to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollNumberStr);
            Student student = managementSystem.searchStudent(rollNumber);
            if (student != null) {
                outputArea.setText("Student found: " + student.toString() + "\n");
            } else {
                outputArea.setText("Student not found with Roll Number: " + rollNumber + "\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Roll Number. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editStudent() {
        String rollNumberStr = rollNumberField.getText();
        if (rollNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Roll Number of the student to edit.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollNumberStr);
            Student studentToEdit = managementSystem.searchStudent(rollNumber);
            if (studentToEdit != null) {
                String name = nameField.getText();
                String grade = gradeField.getText();
                if (!name.isEmpty() && !grade.isEmpty()) {
                    studentToEdit.setName(name);
                    studentToEdit.setGrade(grade);
                    managementSystem.editStudent(studentToEdit, studentToEdit);
                    outputArea.append("Student edited: " + studentToEdit.toString() + "\n");
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill all the fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Student not found with Roll Number: " + rollNumber, "Student Not Found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Roll Number. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
    }

    // Getter for the StudentManagementSystem
    public StudentManagementSystem getManagementSystem() {
        return managementSystem;
    }
}

public class mainn {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentManagementGUI gui = new StudentManagementGUI();
                gui.setVisible(true);

                // Save students to the file when the application is closed
                gui.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        gui.getManagementSystem().saveStudents();
                    }
                });
            }
        });
    }
}

