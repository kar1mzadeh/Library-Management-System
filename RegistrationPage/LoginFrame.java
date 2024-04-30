package RegistrationPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LoginFrame extends JFrame implements ActionListener {

    public static void main(String[] args) {

        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);

    }

    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        setTitle("Book Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 320);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.GRAY);

        // Load the image
        ImageIcon imageIcon = new ImageIcon("RegistrationPage\\library.png");
        Image image = imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);
        // Create a label to display the image
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(120, 20, 60, 60);

        // Add the image label to the frame
        add(imageLabel);

        JLabel label = new JLabel("Welcome Library");
        label.setBounds(200, 30, 200, 30);
        label.setFont(new Font("Times New Roman", Font.BOLD, 18));
        label.setForeground(Color.BLACK);
        add(label);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(110, 90, 80, 30);
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        usernameLabel.setForeground(Color.BLACK);
        add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(200, 90, 165, 30);
        usernameTextField.setForeground(Color.BLACK);
        add(usernameTextField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(110, 150, 80, 30);
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        passwordLabel.setForeground(Color.BLACK);
        add(passwordLabel);

        passwordPasswordField = new JPasswordField();
        passwordPasswordField.setBounds(200, 150, 165, 30);
        passwordPasswordField.setForeground(Color.BLACK);
        add(passwordPasswordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(120, 220, 100, 45);
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        loginButton.addActionListener(this);
        loginButton.setForeground(Color.BLACK);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(270, 220, 100, 45);
        registerButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        registerButton.addActionListener(this);
        registerButton.setForeground(Color.BLACK);
        add(registerButton);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginButton) {
            String username = usernameTextField.getText();
            String password = new String(passwordPasswordField.getPassword());
            if (checkCredentials(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                dispose();
                if (username.equals("admin") && password.equals("admin")) {
                    new MyGeneraltable();
                } else {
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        } else if (e.getSource() == registerButton) {
            String username = usernameTextField.getText();
            String password = new String(passwordPasswordField.getPassword());
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful");
                dispose();
                if (username.equals("admin") && password.equals("admin")) {
                    new MyGeneraltable();
                } else {
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(this, "The Same Username is existed, You should try another username");
            }
        }
    }

    private boolean checkCredentials(String username, String password) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String password) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("users.csv"));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("users.csv", true))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return false;
                }
            }
            bufferedWriter.write(username + "," + password);
            bufferedWriter.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}