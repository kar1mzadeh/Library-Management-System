package RegistrationPage;

import javax.swing.*;
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
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(new Font(table.getFont().getFontName(), Font.BOLD, 16));
        table.getTableHeader().addMouseListener(new HeaderMouseListener());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
        setupTableListeners();

        setVisible(true);
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
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 5, 10, 10)); 
         // Adjust layout for better field distribution
    
        // Initializing text fields
        titleField = new JTextField();
        authorField = new JTextField();
        ratingField = new JTextField();
        reviewField = new JTextField();
    
        // Adding fields and buttons to panel
        controlPanel.add(new JLabel("Title:"));
        controlPanel.add(titleField);
        controlPanel.add(new JLabel("Author:"));
        controlPanel.add(authorField);
        controlPanel.add(new JLabel("Rating:"));
        controlPanel.add(ratingField);
        controlPanel.add(new JLabel("Review:"));
        controlPanel.add(reviewField);
    
        // Button for adding rows
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this::addRow);
    
        // Button for updating rows
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this::updateRow);
    
        // Button for deleting rows
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this::deleteRow);
    
        // UserManager Button, only shown for admin
        userManagerButton = new JButton("User Manager");
        userManagerButton.addActionListener(e -> openUserManagement());
        if ("admin".equals(username)) {
            controlPanel.add(userManagerButton);
        }
    
        controlPanel.add(addButton);
        controlPanel.add(updateButton);
        controlPanel.add(deleteButton);
    

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
    
            String[] bookDetails = {title, author, rating, review, "Not Started", "0", "N/A", "N/A", "Add rating", "Add review"};
            PersonalDB.addBookToPersonalDB(username, bookDetails);
        } else {
            JOptionPane.showMessageDialog(this, "No book selected!");
        }
    }

    private void addRow(ActionEvent e) {
        Object[] newRow = {
            titleField.getText(),
            authorField.getText(),
            ratingField.getText(),
            reviewField.getText()
        };
        defaultTableModel.addRow(newRow);
        dataList.add(newRow);
        updateCSV(); // Update CSV after adding a new row
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
    

   

    private void loadData() {
        File file = new File("generalDatabaseUpdated.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { // Skip the header or any improperly formatted lines
                    dataList.add(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("generalDatabaseUpdated.csv"))) {
            bw.write("Title,Author,Rating,Review\n"); // Write header
            for (Object[] row : dataList) {
                bw.write(String.format("%s,%s,%s,%s\n", row[0], row[1], row[2], row[3]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    

    private class HeaderMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int column = table.columnAtPoint(e.getPoint());
            String columnName = table.getColumnName(column);
            int clickCount = columnClickCount.getOrDefault(columnName, 0);
            clickCount = (clickCount + 1) % 3; // Cycles through 0, 1, 2
            columnClickCount.put(columnName, clickCount);
            TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
            if (clickCount == 0) {
                sorter.setSortKeys(null); // Unsorted
            } else {
                sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column, clickCount == 1 ? SortOrder.ASCENDING : SortOrder.DESCENDING)));
            }
            sorter.sort();
        }
    }

    public static void main(String[] args) {
        new MyGeneraltable(username);
    }
}
