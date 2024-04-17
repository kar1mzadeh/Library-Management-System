package RegistrationPage;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static RegistrationPage.PersonalBookEntry.getPersonalBookEntries;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    public CSVWriter writer;
    private List<BookEntry> generalDatabase;
    private List<PersonalBookEntry> personalDatabase;
    private JButton addBookButton;
    private JButton displayLibraryButton;
    private JButton userReviewButton;



    public LoginFrame() {

        generalDatabase = new ArrayList<>();
        personalDatabase = new ArrayList<>();

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 20, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 165, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 165, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(20, 100, 80, 25);
        loginButton.addActionListener(this);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(120, 100, 100, 25);
        registerButton.addActionListener(this);
        add(registerButton);

        addBookButton = new JButton("Add Book");
        addBookButton.setBounds(20, 130, 120, 25);
        addBookButton.addActionListener(this);
        addBookButton.setVisible(false);
        add(addBookButton);

        displayLibraryButton = new JButton("Display Library");
        displayLibraryButton.setBounds(150, 130, 120, 25);
        displayLibraryButton.addActionListener(this);
        displayLibraryButton.setVisible(false);
        add(displayLibraryButton);

        userReviewButton = new JButton("Show User Review");
        userReviewButton.setBounds(20, 160, 250, 25);
        userReviewButton.addActionListener(this);
        userReviewButton.setVisible(false);
        add(userReviewButton);


        try {
            writer = new CSVWriter(new FileWriter("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\users.csv", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToPersonalLibrary(String title, String author) {
        try {
            // Read the General Database CSV file
            CSVReader generalReader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
            List<String[]> generalLines = generalReader.readAll();
            generalReader.close();

            // Read the Personal Database CSV file
            CSVReader personalReader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
            List<String[]> personalLines = personalReader.readAll();
            personalReader.close();

            // Check if the book is already in the Personal Database
            for (String[] line : personalLines) {
                if (line[0].equals(title) && line[1].equals(author)) {
                    JOptionPane.showMessageDialog(this, "Book is already in your Personal Library");
                    return;
                }
            }

            // Find the book in the General Database
            for (String[] line : generalLines) {
                if (line[0].equals(title) && line[1].equals(author)) {
                    // Add the book to the Personal Database
                    String[] newLine = {title, author, "No rating", "No review", "Not started", "0", "", "", "", "", "", ""};
                    CSVWriter writer = new CSVWriter(new FileWriter("personal_database.csv", true));
                    writer.writeNext(newLine);
                    writer.flush();
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Book added to your Personal Library");
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Book not found in General Database");

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding book to Personal Library");
        }
    }


    private void displayPersonalLibrary() {
        try {
            // Read the Personal Database CSV file
            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
            List<String[]> lines = reader.readAll();
            reader.close();

            // Display the Personal Library in a dialog box
            StringBuilder sb = new StringBuilder();
            sb.append("Title\tAuthor\tRating\tReviews\tStatus\tTime spent\tStart Date\tEnd Date\tUserRating\tUserReview\n");
            for (String[] line : lines) {
                sb.append(line[0]).append("\t")
                        .append(line[1]).append("\t")
                        .append(line[2]).append("\t")
                        .append(line[3]).append("\t")
                        .append(line[4]).append("\t")
                        .append(line[5]).append("\t")
                        .append(line[6]).append("\t")
                        .append(line[7]).append("\t")
                        .append(line[8]).append("\t")
                        .append(line[9]).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Personal Library", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying Personal Library");
        }
    }


    private void showUserReview(String username) {
        try {
            // Read the General Database CSV file
            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
            List<String[]> lines = reader.readAll();
            reader.close();

            // Search for reviews by the specified user
            StringBuilder sb = new StringBuilder();
            sb.append("Title\tAuthor\tUser Review\n");
            for (String[] line : lines) {
                String reviews = line[3];
                String[] reviewers = reviews.split(",");
                for (String reviewer : reviewers) {
                    if (reviewer.trim().equals(username)) {
                        sb.append(line[0]).append("\t")
                                .append(line[1]).append("\t")
                                .append(line[4]).append("\n");
                        break; // Found a review by the user, no need to check other reviewers
                    }
                }
            }

            // Display the reviews in a dialog box
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Reviews by " + username, JOptionPane.PLAIN_MESSAGE);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error showing reviews for " + username);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // Handle login logic here
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("admin")) {
                JOptionPane.showMessageDialog(this, "Login as admin Successful");
                openDatabasePage(username);
            } else if (checkCredentials(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                openDatabasePage(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        } else if (e.getSource() == registerButton) {
            // Handle registration logic here
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Register");
            }
        } else if (e.getSource() == addBookButton) {
            String title = JOptionPane.showInputDialog(this, "Enter the title of the book:");
            String author = JOptionPane.showInputDialog(this, "Enter the author of the book:");
            addToPersonalLibrary(title, author);
        } else if (e.getSource() == displayLibraryButton) {
            displayPersonalLibrary();
        } else if (e.getSource() == userReviewButton) {
            String username = JOptionPane.showInputDialog(this, "Enter the username:");
            showUserReview(username);
        }
    }

    private void openDatabasePage(String username) {
        JFrame databaseFrame = new JFrame("Database Page");
        databaseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseFrame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        try {
            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
            List<String[]> lines = reader.readAll();
            String[] columnNames = {"Author", "Title"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (String[] line : lines) {
                model.addRow(line);
            }
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            databaseFrame.add(scrollPane);
        } catch (IOException | CsvException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading brodsky.csv");
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        if (username.equals("admin")) {
            // Admin view - display BookEntry
            String[] columnNames = {"Title", "Author", "Rating", "Reviews"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            // Populate the table with data from your BookEntry class
            for (BookEntry entry : BookEntry.getAllEntries()) {
                model.addRow(new Object[]{entry.getTitle(), entry.getAuthor(), entry.getRating(), entry.getReviews().size()});
            }
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            tabbedPane.addTab("Book Entry", scrollPane);

            JButton addEntryButton = new JButton("Add Entry");
            addEntryButton.addActionListener(e -> {
                // Implement add entry logic here
                String title = JOptionPane.showInputDialog(this, "Enter the title of the book:");
                String author = JOptionPane.showInputDialog(this, "Enter the author of the book:");
                // Add the new entry to your BookEntry class
                BookEntry.addEntry(new BookEntry(title, author));
                // Update the table
                model.addRow(new Object[]{title, author, "-", "-"});
            });
            panel.add(addEntryButton, BorderLayout.NORTH);
        }


        // User view - display PersonalBookEntry

        String[] userColumnNames = {"Title", "Author", "Rating", "Status"};
        DefaultTableModel userModel = new DefaultTableModel(userColumnNames, 0);
        List<PersonalBookEntry> personalEntries = getPersonalBookEntries(username);

        // Populate the table with data from your PersonalBookEntry class
        for (PersonalBookEntry entry : personalEntries ) {
            userModel.addRow(new Object[]{entry.getTitle(), entry.getAuthor(), entry.getRating(), entry.getStatus()});
        }
        JTable userTable = new JTable(userModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        tabbedPane.addTab("Personal Entries", userScrollPane);

        panel.add(tabbedPane, BorderLayout.CENTER);

        databaseFrame.add(panel);
        databaseFrame.setVisible(true);
        setVisible(false); // Hide the login frame
    }




    private boolean checkCredentials(String username, String password) {
        try {
            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\users.csv"));
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                if (line[0].equals(username) && line[1].equals(password)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean registerUser(String username, String password) {
        if (username.length() < 4 || password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Username and password must be at least 4 characters long");
            return false;
        }
        // Password strength check using regular expression
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and contain at least one digit, " +
                    "one lowercase letter, one uppercase letter, and one special character.");
            return false;
        }

        try {
            CSVReader reader = new CSVReader(new FileReader("users.csv"));
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                if (line[0].equals(username)) {
                    reader.close();
                    return false; // Username already exists
                }
            }
            reader.close();
            String[] newLine = {username, password};
            writer.writeNext(newLine);
            writer.flush();
            return true;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
}


