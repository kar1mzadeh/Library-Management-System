
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
        private static final String PERSONAL_BOOKS_FILE = "csvfiles\\personalDatabaseUpdated.csv";
    
    
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
          public static boolean addBookToPersonalDB(String username, String[] bookDetails) {
            ArrayList<String[]> books = loadPersonalBooks(username);
        
            // Check for existing book by title
            boolean bookExists = books.stream().anyMatch(b -> b[0].equalsIgnoreCase(bookDetails[0]));
            if (bookExists) {
                return false; // Book already exists, do not add
            }
        
            // Add book if not already present
            if (bookDetails.length < 10) {
                String[] completeDetails = new String[10];
                System.arraycopy(bookDetails, 0, completeDetails, 0, bookDetails.length);
                Arrays.fill(completeDetails, bookDetails.length, 10, "N/A");
                completeDetails[4] = "Not Started";
                completeDetails[5] = "0";
                completeDetails[8] = "Add rating";
                completeDetails[9] = "Add review";
                books.add(completeDetails);
            } else {
                books.add(bookDetails);
            }
            savePersonalBooks(username, books);
            return true;
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
    public static ArrayList<String[]> getReviewsForBook(String bookTitle) {
    ArrayList<String[]> reviews = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(PERSONAL_BOOKS_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[1].equalsIgnoreCase(bookTitle)) { // Assuming the second part is the book title
                reviews.add(new String[] {parts[0], parts[9], parts[10]}); // username, user rating, user review
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return reviews;
}
public static String getUsernamesWhoReviewedBook(String bookTitle) {
    ArrayList<String> usernames = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("csvfiles\\personalDatabaseUpdated.csv"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[1].equalsIgnoreCase(bookTitle) && parts.length > 9 && !parts[9].equals("Add review")) { // Assuming parts[9] contains the review
                usernames.add(parts[0]);  // Assuming parts[0] contains the username
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return String.join(", ", usernames);
}

    


        
    
        // Delete a book from all users
        public static void deleteBookFromAllUsers(String[] bookDetails) {
            File file = new File(PERSONAL_BOOKS_FILE);
            ArrayList<String> updatedLines = new ArrayList<>();
        
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Split the line into components
                    String[] parts = line.split(",");
                    // Check if the current line matches the book to delete
                    if (!(parts[1].equals(bookDetails[0]) && parts[2].equals(bookDetails[1]))) {
                        // If it does not match, add it to the updated lines
                        updatedLines.add(line);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
            // Rewrite the CSV with the updated lines
            try (PrintWriter writer = new PrintWriter(new FileWriter(PERSONAL_BOOKS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }

    
     class MyPersonalTable extends JFrame {
        private static String username;
        private JTable table;
        private DefaultTableModel model;
      private JTextField statusField, startDateField, endDateField, userRatingField, userReviewField, titleField,authorField,ratingField,reviewField;
    // private JButton addButton, updateButton, deleteButton, generalDbButton;
    
        public MyPersonalTable(String username) {
            this.username = username;
    System.out.println("Received username in MyPersonalTable: " + username);
    setTitle("Personal Database - " + username);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 600);
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
            titleField = new JTextField(10);
            authorField = new JTextField(10);
            reviewField = new JTextField(10);
            ratingField = new JTextField(10);
            // Buttons
            // addButton = new JButton("Add");
            // updateButton = new JButton("Update");
            // deleteButton = new JButton("Delete");
            // generalDbButton = new JButton("General DB");
   
      
            // Layout for controls
            JPanel controlPanel = createControlPanel(); // This calls your newly defined method
            add(controlPanel, BorderLayout.SOUTH);
        controlPanel.setLayout(new GridLayout(0, 2, 10, 10));
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
            controlPanel.add(new JLabel("Title:"));
            controlPanel.add(titleField); 
            controlPanel.add(new JLabel("Author"));
            controlPanel.add(authorField); 
            controlPanel.add(new JLabel("Rating:"));
            controlPanel.add(ratingField);
             controlPanel.add(new JLabel("Review:"));
            controlPanel.add(reviewField);
            
            // controlPanel.add(addButton);
            // controlPanel.add(updateButton);
            // controlPanel.add(deleteButton);
            // controlPanel.add(generalDbButton);
        
    
    
            add(controlPanel, BorderLayout.SOUTH);
    
            loadBooks();  // Load the books into the table
            setVisible(true);
            setupTableListeners();
        }
    
        private void loadBooks() {
            ArrayList<String[]> books = PersonalDB.loadPersonalBooks(username);
            for (String[] book : books) {
                model.addRow(book);
            }
        }

        // This should be in the part of your code where you transition to the personal table view
public void openPersonalTable() {
    String currentUser = username;  // Ensure this method or variable correctly provides the non-null username
    if (currentUser != null) {
        MyPersonalTable personalTable = new MyPersonalTable(currentUser);
        personalTable.setVisible(true);
    } else {
        System.out.println("Error: Username is null when trying to open Personal Table.");
    }
}
private JPanel createControlPanel() {
    JPanel controlPanel = new JPanel(new BorderLayout());
    controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds margin around the panel

    // Search panel at the top
    JPanel searchPanel = new JPanel(new BorderLayout());
    JTextField searchField = new JTextField();
    searchField.setPreferredSize(new Dimension(200, 24)); // Set the search bar size
    searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
    searchPanel.add(searchField, BorderLayout.CENTER);

    // Button panel at the bottom
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Add spacing between buttons
    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton updateButton = new JButton("Update");
    JButton generalDbButton = new JButton("General DB");

    // Set button dimensions for uniformity
    Dimension buttonSize = new Dimension(100, 25);
    addButton.setPreferredSize(buttonSize);
    deleteButton.setPreferredSize(buttonSize);
    updateButton.setPreferredSize(buttonSize);
    generalDbButton.setPreferredSize(buttonSize);

     
    addButton.addActionListener(this::addBook);
    updateButton.addActionListener(this::updateBook);
    deleteButton.addActionListener(this::deleteBook);
    generalDbButton.addActionListener(e -> openGeneralDatabase());

    buttonPanel.add(addButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(updateButton);
    buttonPanel.add(generalDbButton);
    searchPanel.add(new JLabel("Search:"));
    searchPanel.add(searchField);
    controlPanel.add(searchPanel, BorderLayout.NORTH);

    searchField.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
            filter(searchField.getText());
        }

        public void removeUpdate(DocumentEvent e) {
            filter(searchField.getText());
        }

        public void insertUpdate(DocumentEvent e) {
            filter(searchField.getText());
        }

        private void filter(String text) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter(text));
        }
    });


    // Adding panels to the main control panel
    controlPanel.add(searchPanel, BorderLayout.NORTH);
    controlPanel.add(buttonPanel, BorderLayout.SOUTH);

    return controlPanel;
}



    
private void addBook(ActionEvent e) {
    String title = titleField.getText();
    String author = authorField.getText();
    String rating = ratingField.getText();
    String review = reviewField.getText();
    String status = "Not started";
    String timeSpent = "0";
    String startDate = "";
    String endDate = "";
    String userRating = "Add rating";
    String userReview = "Add review";

    String[] bookDetails = {title, author, rating, review, status, timeSpent, startDate, endDate, userRating, userReview};
    
    boolean addedSuccessfully = PersonalDB.addBookToPersonalDB(username, bookDetails);
    if (addedSuccessfully) {
        model.addRow(bookDetails);  // Only add to table if successfully added to DB
        clearInputFields();  // Clear input fields after adding
    } else {
        JOptionPane.showMessageDialog(this, "This book title already exists in your personal library.", "Duplicate Book Error", JOptionPane.ERROR_MESSAGE);
    }
}

        
        private void clearInputFields() {
            titleField.setText("");
            authorField.setText("");
            ratingField.setText("");
            reviewField.setText("");
        }
      
        private void setupTableListeners() {
            table.getSelectionModel().addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        loadFieldsFromSelectedRow(selectedRow);
                    }
                }
            });
        }
        private void loadFieldsFromSelectedRow(int row) {
            titleField.setText(model.getValueAt(row, 0).toString());
            authorField.setText(model.getValueAt(row, 1).toString());
            ratingField.setText(model.getValueAt(row, 2).toString());
            reviewField.setText(model.getValueAt(row, 3).toString());
            statusField.setText(model.getValueAt(row, 4).toString());
            startDateField.setText(model.getValueAt(row, 6).toString());
            endDateField.setText(model.getValueAt(row, 7).toString());
            userRatingField.setText(model.getValueAt(row, 8).toString());
            userReviewField.setText(model.getValueAt(row, 9).toString());
        }
        
    
        private void updateBook(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Ensure that fields are correctly mapped to table columns
                model.setValueAt(statusField.getText(), selectedRow, 4);
                model.setValueAt(startDateField.getText(), selectedRow, 6);
                model.setValueAt(endDateField.getText(), selectedRow, 7);
                model.setValueAt(userRatingField.getText(), selectedRow, 8);
                model.setValueAt(userReviewField.getText(), selectedRow, 9);
        
                // Update the data in the CSV (requires re-writing or better file handling)
                saveUpdatedBooks();  // Implement this to handle CSV updates
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
            System.out.println("About to open MyPersonalTable with username: " + username);
            new MyPersonalTable(username);
        }
    }
    