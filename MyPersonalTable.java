
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class MyPersonalTable extends JFrame {
    private static String username;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> userRatingField;
  private JTextField userReviewField, titleField,authorField,ratingField,reviewField, timeSpentField;
private JFormattedTextField startDateField, endDateField;
Map<String, Integer> columnClickCount = new HashMap<>();
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
 setupTable();
 setupStatusListener();

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
private void setupStatusListener() {
    statusComboBox.addActionListener(e -> {
        String selectedStatus = (String) statusComboBox.getSelectedItem();
        switch (selectedStatus) {
            case "Not Started":
                // Disable both StartDate and EndDate fields
                startDateField.setEnabled(false);
                endDateField.setEnabled(false);
                startDateField.setText("");
                endDateField.setText("");
                break;
            case "Ongoing":
                // Enable StartDate but disable EndDate
                startDateField.setEnabled(true);
                endDateField.setEnabled(false);
                endDateField.setText("Not Finished");
                break;
            case "Completed":
                // Enable both StartDate and EndDate fields
                startDateField.setEnabled(true);
                endDateField.setEnabled(true);
                break;
        }
    });
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
        statusComboBox.setSelectedItem(model.getValueAt(row, 4).toString());

        startDateField.setText(model.getValueAt(row, 6).toString());
        endDateField.setText(model.getValueAt(row, 7).toString());
        updateDateFieldsBasedOnStatus((String) statusComboBox.getSelectedItem());
        String userRating = model.getValueAt(row, 8).toString();
        userRatingField.setSelectedItem(userRating);
        userReviewField.setText(model.getValueAt(row, 9).toString());
    }
    private void updateDateFieldsBasedOnStatus(String status) {
        switch (status) {
            case "Not Started":
                startDateField.setEnabled(false);
                endDateField.setEnabled(false);
                startDateField.setText("Not Started");
                endDateField.setText("Not Started");
                break;
            case "Ongoing":
                startDateField.setEnabled(true);
                endDateField.setEnabled(false);
                endDateField.setText("Not Finished");
                break;
            case "Completed":
                startDateField.setEnabled(true);
                endDateField.setEnabled(true);
                break;
        }
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
