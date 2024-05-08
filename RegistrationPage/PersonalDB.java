package RegistrationPage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



import java.awt.*;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.stream.Collectors;

    public class PersonalDB {
        private static final String PERSONAL_BOOKS_FILE = "personalDatabaseUpdated.csv";
    
    
        public static ArrayList<String[]> loadPersonalBooks(String username) {
            ArrayList<String[]> books = new ArrayList<>();
            File file = new File(PERSONAL_BOOKS_FILE);
            if (!file.exists()) {
                System.out.println("The file " + PERSONAL_BOOKS_FILE + " does not exist.");
                return books;  // Return empty list if file doesn't exist
            }
        
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 1 && parts[0].equals(username)) {
                        books.add(Arrays.copyOfRange(parts, 1, parts.length));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file for " + username + ": " + e.getMessage());
            }
            return books;
        }
        
          // Add a book to the personal database with default values for certain fields
    public static void addBookToPersonalDB(String username, String[] bookDetails) {
        ArrayList<String[]> books = loadPersonalBooks(username);
        if (bookDetails.length < 10) { // Ensure array has 10 elements
            String[] completeDetails = new String[10];
            System.arraycopy(bookDetails, 0, completeDetails, 0, bookDetails.length);
            Arrays.fill(completeDetails, bookDetails.length, 10, "N/A"); // Fill empty slots with "N/A"
            completeDetails[4] = "Not Started"; // Default status
            completeDetails[5] = "0"; // Default time spent
            completeDetails[8] = "Add rating"; // Default user rating
            completeDetails[9] = "Add review"; // Default user review
            books.add(completeDetails);
        } else {
            books.add(bookDetails);
        }
        savePersonalBooks(username, books);
    }
    public static void savePersonalBooks(String username, ArrayList<String[]> books) {
        // Read all lines, filter out this user's previous entries, and append new ones
        ArrayList<String> allLines = new ArrayList<>();
        File file = new File(PERSONAL_BOOKS_FILE);
    
        try {
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith(username + ",")) { // Keep lines of other users
                            allLines.add(line);
                        }
                    }
                }
            }
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (String line : allLines) {
                    writer.println(line);
                }
                for (String[] book : books) {
                    writer.println(username + "," + String.join(",", book));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    
        // Delete a book from all users
        public static void deleteBookFromAllUsers(String[] bookDetails) {
            try {
                ArrayList<String> updatedLines = new ArrayList<>();
                File file = new File(PERSONAL_BOOKS_FILE);
    
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        updatedLines = (ArrayList<String>) reader.lines().filter(line -> {
                            String[] parts = line.split(",");
                            // Keep the line unless it matches the book to be deleted
                            return !(parts[1].equals(bookDetails[0]) && parts[2].equals(bookDetails[1]));
                        }).collect(Collectors.toList());
                    }
    
                    // Rewrite the file with the updated lines
                    try (PrintWriter writer = new PrintWriter(new FileWriter(PERSONAL_BOOKS_FILE))) {
                        for (String line : updatedLines) {
                            writer.println(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
     class MyPersonalTable extends JFrame {
        private static String username;
        private JTable table;
        private DefaultTableModel model;
      private JTextField statusField, startDateField, endDateField, userRatingField, userReviewField, titleField,authorField,ratingField,reviewField;
    private JButton addButton, updateButton, deleteButton, generalDbButton;
    
        public MyPersonalTable(String username) {
            this.username = username;
            setTitle("Personal Database - " + username);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 500);
            setLocationRelativeTo(null);

            
    
            // Table Model Setup
            model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Title", "Author", "Rating", "Review", "Status", "Time Spent", "Start Date", "End Date", "User Rating", "User Review"});
            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
    
            // Input fields
            statusField = new JTextField(10);
            startDateField = new JTextField(10);
            endDateField = new JTextField(10);
            userRatingField = new JTextField(10);
            userReviewField = new JTextField(10);

            // Buttons
            addButton = new JButton("Add");
            updateButton = new JButton("Update");
            deleteButton = new JButton("Delete");
            generalDbButton = new JButton("General DB");
    
            addButton.addActionListener(this::addBook);
            updateButton.addActionListener(this::updateBook);
            deleteButton.addActionListener(this::deleteBook);
            generalDbButton.addActionListener(e -> openGeneralDatabase());
    
            // Layout for controls
            JPanel controlPanel = new JPanel();
            controlPanel.add(new JLabel("Status:"));
            controlPanel.add(statusField);
            controlPanel.add(new JLabel("StartDate:"));
            controlPanel.add(startDateField);
            controlPanel.add(new JLabel("EndDate:"));
            controlPanel.add(endDateField);
            controlPanel.add(new JLabel("UserRating:"));
            controlPanel.add(userRatingField);
            controlPanel.add(new JLabel("UserReview:"));
            controlPanel.add(userReviewField);
            controlPanel.add(addButton);
            controlPanel.add(updateButton);
            controlPanel.add(deleteButton);
            controlPanel.add(generalDbButton);
    
            add(controlPanel, BorderLayout.SOUTH);
    
            loadBooks();  // Load the books into the table
            setVisible(true);
        }
    
        private void loadBooks() {
            ArrayList<String[]> books = PersonalDB.loadPersonalBooks(username);
            for (String[] book : books) {
                model.addRow(book);
            }
        }
    
        private void addBook(ActionEvent e) {
            String title = titleField.getText();
            String author = authorField.getText();
            String rating = ratingField.getText();
            String review = reviewField.getText();
            String status = "Not started";  // default status
            String timeSpent = "0";  // default time spent
            String startDate = "";  // default start date
            String endDate = "";  // default end date
            String userRating = "Add rating";  // default user rating
            String userReview = "Add review";  // default user review
        
            String[] bookDetails = {title, author, rating, review, status, timeSpent, startDate, endDate, userRating, userReview};
            model.addRow(bookDetails);  // Add to table
            PersonalDB.addBookToPersonalDB(username, bookDetails);  // Persist to CSV
            clearInputFields();  // Optional: clear input fields after adding
        }
        
        private void clearInputFields() {
            titleField.setText("");
            authorField.setText("");
            ratingField.setText("");
            reviewField.setText("");
        }
        
    
        private void updateBook(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                model.setValueAt(titleField.getText(), selectedRow, 0);
                model.setValueAt(authorField.getText(), selectedRow, 1);
                model.setValueAt(ratingField.getText(), selectedRow, 2);
                model.setValueAt(reviewField.getText(), selectedRow, 3);
                // Assume other fields might also be updated similarly
        
                // Update the data in the CSV (requires re-writing or better file handling)
                saveUpdatedBooks();  // You would need to implement this method
            }
        }
        
    
        private void deleteBook(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                model.removeRow(selectedRow);  // Remove from table
                // Remove from CSV: Could be complex as it requires modifying the file
                saveUpdatedBooks();  // Assumes re-writing the CSV with remaining data
            }
        }
        
        private void saveUpdatedBooks() {
            ArrayList<String[]> allBooks = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String[] row = new String[model.getColumnCount()];
                for (int j = 0; j < model.getColumnCount(); j++) {
                    row[j] = model.getValueAt(i, j).toString();
                }
                allBooks.add(row);
            }
            PersonalDB.savePersonalBooks(username, allBooks);  // Method to overwrite CSV with new data
        }
        
    
        private void openGeneralDatabase() {
            new MyGeneraltable(username).setVisible(true);
        }
    
        public static void main(String[] args) {
            new MyPersonalTable(username); // Test with a hypothetical user
        }
    }
    