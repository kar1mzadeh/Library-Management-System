
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


    public class PersonalDB {
        private static final String PERSONAL_BOOKS_FILE = "csvfiles\\personalDatabaseUpdated.csv";

        
         public static boolean deleteUserReview(String username, String bookTitle) {
        // Implementation depends on your storage setup
        // This is a simple example assuming a CSV file and immediate deletion
        ArrayList<String[]> updatedReviews = new ArrayList<>();
        boolean isDeleted = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("csvfiles\\personalDatabaseUpdated.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equalsIgnoreCase(bookTitle)) {
                    // Change the review and rating to indicate deletion
                    parts[10] = "Deleted";  // Assuming review is at index 9
                    isDeleted = true;
                }
                updatedReviews.add(parts);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    
        // Rewrite the updated reviews back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter("csvfiles\\personalDatabaseUpdated.csv"))) {
            for (String[] parts : updatedReviews) {
                writer.println(String.join(",", parts));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return isDeleted;
    }
        public static String getRatingDetails(String bookTitle) {
            
            ArrayList<Double> ratings = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(PERSONAL_BOOKS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Assuming the book title is at index 1 and the user rating is at index 8
                    if (parts[1].equalsIgnoreCase(bookTitle) && !parts[9].equals("Add rating")) {
                        try {
                            double rating = Double.parseDouble(parts[9]);
                            ratings.add(rating);
                        } catch (NumberFormatException e) {
                            // Handle case where the rating is not a number
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            if (ratings.isEmpty()) {
                return "No rating";
            } else {
                double average = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                return String.format("%.2f (%d)", average, ratings.size());
            }
        }
    
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
                Arrays.fill(completeDetails, bookDetails.length, 10, "");
                completeDetails[4] = "Not Started";
                completeDetails[5] = "0";
                completeDetails[6]="";
                completeDetails[7]="";
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
            if (parts[1].equalsIgnoreCase(bookTitle) && parts.length > 9 && !parts[10].equals("Add review")) { // Assuming parts[9] contains the review
                usernames.add(parts[0]);  // Assuming parts[0] contains the username
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return String.join(" · ", usernames);
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

        public static String fetchUserReview(String username, String bookTitle) {
            File file = new File(PERSONAL_BOOKS_FILE);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Assuming the format is Username,BookTitle,Rating,Review,...
                    if (parts.length > 3 && parts[0].equals(username) && parts[1].equals(bookTitle)) {
                        return "Rating: " + parts[2] + ", Review: " + parts[3];  // Customize as needed
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            return "No review found.";
        }
        
    }

    
     class MyPersonalTable extends JFrame {
        private static String username;
        private JTable table;
        private DefaultTableModel model;
        private JComboBox<String> statusComboBox;
        private JComboBox<String> userRatingField;
      private JTextField userReviewField, titleField,authorField,ratingField,reviewField, timeSpentField;
private JFormattedTextField startDateField, endDateField;
    
      public MyPersonalTable(String username) {
        this.username = username;
        System.out.println("Received username in MyPersonalTable: " + username);
        setTitle("Personal Database - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    
        // Table Model Setup with validation for userRating
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only the userRating and userReview columns editable
                return column == 8 || column == 9;
            }
    
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 8) { // userRating column
                    try {
                        double rating = Double.parseDouble(aValue.toString());
                        if (rating >= 1.0 && rating <= 5.0) {
                            super.setValueAt(aValue, row, column);
                        } else {
                            JOptionPane.showMessageDialog(null, "Rating must be between 1 and 5.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid input: Please enter a valid number.");
                    }
                } else {
                    super.setValueAt(aValue, row, column);
                }
            }
        };
        model.setColumnIdentifiers(new String[]{"Title", "Author", "Rating", "Review", "Status", "Time Spent", "Start Date", "End Date", "User Rating", "User Review"});
        
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] statusOptions = {"Not Started", "Ongoing", "Completed"};
        statusComboBox = new JComboBox<>(statusOptions);

    
        // Setting a JComboBox as the editor for the userRating column
        String[] validRatings = {"1", "2", "3", "4", "5"};
        JComboBox<String> ratingEditor = new JComboBox<>(validRatings);
         table.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(ratingEditor));
        userRatingField = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        TableColumn ratingColumn = table.getColumnModel().getColumn(8);
        ratingColumn.setCellEditor(new DefaultCellEditor(userRatingField));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Input fields setup
        titleField = new JTextField(10);
        authorField = new JTextField(10);
        reviewField = new JTextField(10);
        ratingField = new JTextField(10);
        TableColumn statusColumn = table.getColumnModel().getColumn(4);
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));
        // startDateField = new JFormattedTextField();
        // endDateField = new JFormattedTextField(10);
        TableColumn userRatingColumn = table.getColumnModel().getColumn(8);
      userRatingColumn.setCellEditor(new DefaultCellEditor(userRatingField));
        userReviewField = new JTextField(10);
    
        // Layout for controls
        JPanel controlPanel = createControlPanel(); // This calls your newly defined method
        add(controlPanel, BorderLayout.SOUTH);
        controlPanel.setLayout(new GridLayout(0, 2, 10, 10));
        controlPanel.add(new JLabel("Title:"));
        controlPanel.add(titleField);
        controlPanel.add(new JLabel("Author"));
        controlPanel.add(authorField);
        controlPanel.add(new JLabel("Rating:"));
        controlPanel.add(ratingField);
        controlPanel.add(new JLabel("Review:"));
        controlPanel.add(reviewField);
        controlPanel.add(new JLabel("Status:"));
        controlPanel.add(statusComboBox);
       
        controlPanel.add(new JLabel("UserRating:"));
        controlPanel.add(userRatingField);
        controlPanel.add(new JLabel("UserReview:"));
        controlPanel.add(userReviewField);

        // try {
    
            startDateField = new JFormattedTextField();
            endDateField = new JFormattedTextField();
    
            startDateField.setColumns(10);  // Set the size of the text field
            endDateField.setColumns(10);    // Set the size of the text field
    
            // Add the fields to your layout (adjust according to your actual layout setup)
            controlPanel.add(new JLabel("StartDate:"));
            controlPanel.add(startDateField);
            controlPanel.add(new JLabel("EndDate:"));
            controlPanel.add(endDateField);
          // Adjust this to add to your actual frame or panel
    
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // }
    
        add(controlPanel, BorderLayout.SOUTH);
       
       
        loadBooks();  // Load the books into the table
        setVisible(true);
        setupTableListeners();
     //   setupDateFields();
     setupDateValidation(startDateField);  
     setupDateValidation(endDateField);
     setupDateListeners();
    }
   private void setupDateValidation(JTextField dateField) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    dateField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            String text = dateField.getText();
            try {
                LocalDate date = LocalDate.parse(text, dateFormatter);
                dateField.setText(date.format(dateFormatter));  // Reformat to ensure consistent formatting
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dateField, "Invalid date format. Please use dd/MM/yyyy.", "Date Error", JOptionPane.ERROR_MESSAGE);
                dateField.setText("");
            }
        }
    });

    dateField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) && c != KeyEvent.VK_SLASH && c != KeyEvent.VK_BACK_SPACE) {
                e.consume();  // Ignore non-digit and non-slash characters
            } else {
                String currentText = dateField.getText();
                int slashCount = (int) currentText.chars().filter(ch -> ch == '/').count();
                
                if (Character.isDigit(c)) {
                    // Append slash after day and month digits if needed
                    if ((currentText.length() == 2 || currentText.length() == 5) && slashCount < 2) {
                        dateField.setText(currentText + "/" + c);
                        e.consume();
                    }
                    // Limit input length and enforce correct format
                    else if (currentText.length() >= 10) {
                        e.consume();
                    }
                } else if (c == KeyEvent.VK_SLASH && slashCount >= 2) {
                    e.consume();  // Limit to two slashes
                }
            }
        }
        
    
        @Override
        public void keyPressed(KeyEvent e) {
            // To handle backspace correctly when the cursor is after a slash
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                String text = dateField.getText();
                if (text.length() > 0 && text.charAt(text.length() - 2) == '/') {
                    dateField.setText(text.substring(0, text.length() - 1));
                }
            }
        }
    });
}
 // Assuming startDateField and endDateField are the JTextFields for start date and end date respectively
 private void validateAndCalculateDates() {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate startDate = null;
    LocalDate endDate = null;

    // Parse the dates from the input fields
    try {
        if (!startDateField.getText().isEmpty()) {
            startDate = LocalDate.parse(startDateField.getText(), dateFormatter);
        }
        if (!endDateField.getText().isEmpty()) {
            endDate = LocalDate.parse(endDateField.getText(), dateFormatter);
        }
    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Use dd/MM/yyyy.", "Date Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (startDate != null && endDate != null) {
        if (startDate.isAfter(endDate)) {
            JOptionPane.showMessageDialog(this, "Start Date cannot be after End Date.", "Date Error", JOptionPane.ERROR_MESSAGE);
            startDateField.setText(endDate.format(dateFormatter)); // Reset Start Date
            return;
        }

        // Calculate time spent in days
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.setValueAt(daysBetween, selectedRow, 5); // Assuming the Time Spent column is at index 5
        }
    }
}
private void setupDateListeners() {
    // Set up your date validation and calculation to occur on specific events
    startDateField.addFocusListener(new FocusAdapter() {
        public void focusLost(FocusEvent e) {
            validateAndCalculateDates();
        }
    });

    endDateField.addFocusListener(new FocusAdapter() {
        public void focusLost(FocusEvent e) {
            validateAndCalculateDates();
        }
    });
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
// private void setupDateFields() {
//     try {
//         MaskFormatter dateFormatter = new MaskFormatter("####-##-##");
//         dateFormatter.setPlaceholderCharacter('_');

//         // Initialize the formatted text fields with the mask formatter
//         startDateField = new JFormattedTextField(dateFormatter);
//         endDateField = new JFormattedTextField(dateFormatter);

//         setupFieldAutoAdvance(startDateField);
//         setupFieldAutoAdvance(endDateField);
//     } catch (ParseException e) {
//         e.printStackTrace();
//     }
// }

// private void setupFieldAutoAdvance(JFormattedTextField field) {
//     field.setFocusLostBehavior(JFormattedTextField.PERSIST);
    
//     // Adding a key listener to check for input completion
//     field.addKeyListener(new KeyAdapter() {
//         @Override
//         public void keyTyped(KeyEvent e) {
//             if (field.getText().matches("\\d{4}-\\d{2}-_")) {
//                 // Move focus when year and month are entered
//                 field.transferFocus();
//             } else if (field.getText().matches("\\d{4}-__-__")) {
//                 // Move focus from year to month part
//                 field.setCaretPosition(5);
//             } else if (field.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
//                 // Move focus out of field if full date is entered
//                 field.transferFocus();
//             }
//         }
//     });
// }

// private void setupDateFields() {
   
// }




    
private void addBook(ActionEvent e) {
    String title = titleField.getText();
    String author = authorField.getText();
    String rating = ratingField.getText();
    String review = reviewField.getText();
    String status = "Not started";
    String timeSpent = "0";
    String startDate = " ";
    String endDate = " ";
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
            String status = model.getValueAt(row, 4).toString();
    statusComboBox.setSelectedItem(status);
            startDateField.setText(model.getValueAt(row, 6).toString());
            endDateField.setText(model.getValueAt(row, 7).toString());
            String userRating = model.getValueAt(row, 8).toString();
            userRatingField.setSelectedItem(userRating);
            userReviewField.setText(model.getValueAt(row, 9).toString());
        }
        
    
        private void updateBook(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Ensure that fields are correctly mapped to table columns
                model.setValueAt(statusComboBox.getSelectedItem().toString(), selectedRow, 4);
                model.setValueAt(startDateField.getText(), selectedRow, 6);
                model.setValueAt(endDateField.getText(), selectedRow, 7);
                model.setValueAt(userRatingField.getSelectedItem().toString(), selectedRow, 8);
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
    