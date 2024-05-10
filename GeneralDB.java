

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class MyGeneraltable extends JFrame {
    
    JTextField titleField, authorField, ratingField, reviewField;
    JTable table;
    DefaultTableModel defaultTableModel;
    JButton userManagerButton, personalDbButton;    
    ArrayList<Object[]> dataList = new ArrayList<>(); // To manage table data more easily
    Map<String, Integer> columnClickCount = new HashMap<>();
    private static String username;
    

    MyGeneraltable(String username) {
        this.username = username;
      //  initializeUI();
        setBounds(500, 240, 800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = { "Title", "Author", "Rating", "Review" };
        loadData(); // Load data from CSV into dataList
        

        defaultTableModel = new DefaultTableModel(dataList.toArray(new Object[0][]), columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        

        table = new JTable(defaultTableModel);
    table.setAutoCreateRowSorter(true);  // Set up the table with a row sorter
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(defaultTableModel);
    table.setRowSorter(sorter);  // Ensure this is explicitly set, even if auto create is true

    // Attaching the mouse listener to the table header for sorting
    table.getTableHeader().setFont(new Font(table.getFont().getFontName(), Font.BOLD, 16));

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
        setupTableListeners();
        setVisible(true);
    setupTable();

        
    }
  
    
    private void setupTable() {
        table.setAutoCreateRowSorter(true);
        Font headerFont = table.getTableHeader().getFont();
        Font newHeaderFont = headerFont.deriveFont(Font.BOLD, 16);
        table.getTableHeader().setFont(newHeaderFont);

        table.getTableHeader().addMouseListener(new HeaderMouseListener());
    }

    private class HeaderMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JTableHeader header = (JTableHeader) e.getSource();
            TableColumnModel columnModel = header.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            DefaultTableModel defaultTableModel = new DefaultTableModel();
            String columnName = defaultTableModel.getColumnName(column);

            int clickCount = columnClickCount.getOrDefault(columnName, 0);
            clickCount++;
            columnClickCount.put(columnName, clickCount);

            TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
            ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();

            switch (clickCount % 3) {
                case 0: // Original form
                    columnClickCount.put(columnName, 0);
                    sorter.setSortKeys(null); // Clear sorting
                    break;
                case 1: // Ascending order
                    sortKeys.add(new RowSorter.SortKey(column, SortOrder.ASCENDING));
                    break;
                case 2: // Descending order
                    sortKeys.add(new RowSorter.SortKey(column, SortOrder.DESCENDING));
                    break;
            }

            sorter.setSortKeys(sortKeys);
            sorter.sort();
        }
    }
   
    
 

    private void setupTableListeners() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    // Get the model of the table
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    
                    // Retrieve the selected row index
                    int selectedRowIndex = table.getSelectedRow();
                    
                    // Set the text fields based on the values in the selected row
                    titleField.setText(model.getValueAt(selectedRowIndex, 0).toString());
                    authorField.setText(model.getValueAt(selectedRowIndex, 1).toString());
                    ratingField.setText(model.getValueAt(selectedRowIndex, 2).toString());
                    reviewField.setText(model.getValueAt(selectedRowIndex, 3).toString());
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 3) { // Assuming the review column index is 3
                    Object bookTitle = table.getValueAt(row, 0); // Assuming the title is in column 0
                    if (bookTitle != null) {
                        showReviewDetails(bookTitle.toString());
                    }
                }
            }
        });
    }
   
    
    private void showReviewDetails(String bookTitle) {
        // Fetch reviews based on the book title
        ArrayList<String[]> reviews = PersonalDB.getReviewsForBook(bookTitle);
        JDialog reviewDialog = new JDialog(this, "Reviews for " + bookTitle, true); // Make the dialog modal
        reviewDialog.setSize(400, 400);
        reviewDialog.setLayout(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String[] review : reviews) {
            listModel.addElement(review[0] + " - " + review[1] + " - " + review[2]); // username - rating - review
        }
    
        JList<String> reviewList = new JList<>(listModel);
        reviewDialog.add(new JScrollPane(reviewList), BorderLayout.CENTER);
    
        if ("admin".equals(username)) {  // Check if the user is an admin
            JButton deleteButton = new JButton("Delete Review");
            deleteButton.addActionListener(e -> {
                if (!reviewList.isSelectionEmpty()) {
                    String selectedReview = reviewList.getSelectedValue();
                    if (deleteReview(selectedReview, bookTitle)) {
                        listModel.removeElement(selectedReview);  // Remove from list model
                        JOptionPane.showMessageDialog(reviewDialog, "Review deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(reviewDialog, "Failed to delete review.");
                    }
                }
            });
            reviewDialog.add(deleteButton, BorderLayout.SOUTH);
        }
    
        reviewDialog.setVisible(true);
    }
    private boolean deleteReview(String reviewInfo, String bookTitle) {
        String username = reviewInfo.split(" - ")[0];  // Extract username from the review info string
        return PersonalDB.deleteUserReview(username, bookTitle);
    }
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
                    parts[9] = "Deleted";  // Assuming review is at index 9
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
            
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 10, 10, 10)); 
         // Adjust layout for better field distribution
    
        // Initializing text fields
        titleField = new JTextField();
        authorField = new JTextField();
        ratingField = new JTextField();
        reviewField = new JTextField();
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }
            public void removeUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }
            public void insertUpdate(DocumentEvent e) {
                filterTable(searchField.getText());
            }
        });
    
        searchPanel.add(new JLabel("Search:"));
        controlPanel.add(searchPanel);

        controlPanel.add(searchField);
      
    
        // Adding fields and buttons to panel
        controlPanel.add(new JLabel("Title:"));
        controlPanel.add(titleField);
        controlPanel.add(new JLabel("Author:"));
        controlPanel.add(authorField);
        controlPanel.add(new JLabel("Rating:"));
        controlPanel.add(ratingField);
        controlPanel.add(new JLabel("Review:"));
        controlPanel.add(reviewField);
    

        if ("admin".equals(username)) {
            JButton addButton = new JButton("Add");
            addButton.addActionListener(this::addRow);
            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(this::updateRow);
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(this::deleteRow);
    
            controlPanel.add(addButton);
            controlPanel.add(updateButton);
            controlPanel.add(deleteButton);
        }

    
        // UserManager Button, only shown for admin
        userManagerButton = new JButton("User Manager");
        userManagerButton.addActionListener(e -> openUserManagement());
        if ("admin".equals(username)) {
            controlPanel.add(userManagerButton);
        }
        // Initialize and add other controls here...
    
        JButton addToPersonalButton = new JButton("Add to Personal Library");
        addToPersonalButton.addActionListener(e -> addToPersonalLibrary());

        personalDbButton = new JButton("Personal DB");
        personalDbButton.addActionListener(e -> openPersonalDb());
         
         // Only add this button if the user is not an admin
        if (!"admin".equals(username)) {
            controlPanel.add(addToPersonalButton);
            controlPanel.add(personalDbButton); 
        }
    
        return controlPanel;
    }
    
    private void addToPersonalLibrary() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) table.getValueAt(selectedRow, 0);
            String author = (String) table.getValueAt(selectedRow, 1);
            String rating = (String) table.getValueAt(selectedRow, 2);
            String review = (String) table.getValueAt(selectedRow, 3);
    
            String[] bookDetails = {title, author, rating, review, "Not Started", "0", "", "", "Add rating", "Add review"};
            PersonalDB.addBookToPersonalDB(username, bookDetails);
        } else {
            JOptionPane.showMessageDialog(this, "No book selected!");
        }
    }

    private void addRow(ActionEvent e) {
        String title = titleField.getText();
        String author = authorField.getText();
        String rating = ratingField.getText();
        String review = reviewField.getText();
    
        // Check if the book title already exists in the dataList
        boolean exists = dataList.stream().anyMatch(row -> row[0].equals(title));
        if (exists) {
            JOptionPane.showMessageDialog(this, "This title already exists in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop the addition process if the title exists
        }
    
        // If the title doesn't exist, add the new row to the table and dataList
        Object[] newRow = {title, author, rating, review};
        defaultTableModel.addRow(newRow); // Add to table model
        dataList.add(newRow); // Add to data list for persistence
        updateCSV(); // Update the CSV after adding the new row
    }

    private void openPersonalDb() {
        new MyPersonalTable(username).setVisible(true);
        this.dispose();
    }

    private void openUserManagement() {
        UserManagementTable userManagement = new UserManagementTable(username);
        userManagement.setVisible(true);
    }
    private void updateRow(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Object[] updatedRow = {
                titleField.getText(),
                authorField.getText(),
                ratingField.getText(),
                reviewField.getText()
            };

            for (int i = 0; i < updatedRow.length; i++) {
                defaultTableModel.setValueAt(updatedRow[i], selectedRow, i);
            }

            dataList.set(selectedRow, updatedRow); // Update the dataList
            updateCSV(); // Update CSV after modifying a row
        }
    }

    private void deleteRow(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Retrieve book details
            String title = (String) table.getValueAt(selectedRow, 0);
            String author = (String) table.getValueAt(selectedRow, 1);
    
            // Remove the book from the general database displayed in the table
            defaultTableModel.removeRow(selectedRow);
            dataList.remove(selectedRow);
    
            // Update the CSV for general database
            updateCSV();
    
            // Remove the book from all users' personal databases
            String[] bookDetails = {title, author};
            PersonalDB.deleteBookFromAllUsers(bookDetails);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
        }
    }
    private void filterTable(String searchText) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            // (?i) for case-insensitive matching
            rf = RowFilter.regexFilter("(?i)" + searchText);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        sorter.setRowFilter(rf);
    }
    

   

   private void loadData() {
    File file = new File("csvfiles\\generalDatabaseUpdated.csv");
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 4) {  // Assuming columns are Title, Author, Rating, Review
                String[] dataRow = Arrays.copyOf(parts, 4);

                // Fetch usernames who reviewed this book and the rating details
                String usernames = PersonalDB.getUsernamesWhoReviewedBook(parts[0]);  // Assuming parts[0] is the book title
                String ratingDetails = PersonalDB.getRatingDetails(parts[0]);  // Also fetch the rating details
                
                // Update the review column to show usernames if available
                dataRow[3] = usernames.isEmpty() ? "No reviews" : usernames;
                // Update the rating column to show average rating and count
                dataRow[2] = ratingDetails.isEmpty() ? "No rating" : ratingDetails;

                dataList.add(dataRow);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void loadBooks() {
        ArrayList<String[]> books = PersonalDB.loadPersonalBooks(username);
        DefaultTableModel model = new DefaultTableModel();
        model.setRowCount(0); // Clear existing rows first
        for (String[] book : books) {
            model.addRow(book); // Populate the table with new user data
        }
        table.clearSelection(); // Clear any existing selection
    }

    public void switchUser(String newUser) {
        username = newUser;
        loadBooks(); // Reload the books for the new user
        table.clearSelection(); // Clear selections
        setTitle("Personal Database - " + username); // Update window title
        // Optionally, reset any user-specific settings or UI components
    }
    private void updateCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("csvfiles\\generalDatabaseUpdated.csv"))) {
            bw.write("Title,Author,Rating,Review\n"); // Write header
            for (Object[] row : dataList) {
                bw.write(String.format("%s,%s,%s,%s\n", row[0], row[1], row[2], row[3]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new MyGeneraltable(username);
    }
}
