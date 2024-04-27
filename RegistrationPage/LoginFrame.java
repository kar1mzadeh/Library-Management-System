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

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;


    public LoginFrame() {
        setTitle("Book library login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 320);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome our library");
        label.setBounds(160, 30, 200, 30);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(110, 90, 80, 30);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 90, 165, 30);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(110, 150, 80, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 150, 165, 30);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(120, 220, 100, 45);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(this);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(270, 220, 100, 45);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(this);
        add(registerButton);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (checkCredentials(username, password) ) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                dispose();
                if(username.equals("admin") && password.equals("admin")){
                    new MyGeneraltable();
                }
                else{
                    OptionDatabase optionDatabase = new OptionDatabase();
                    optionDatabase.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        } else if (e.getSource() == registerButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful");
                dispose();
                if(username.equals("admin") && password.equals("admin")){
                    new MyGeneraltable();
                }
                else{
                    OptionDatabase optionDatabase = new OptionDatabase();
                    optionDatabase.setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Because Of Same UserName,Failed To Registration ");
            }
        }
    }

    private boolean checkCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
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
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("users.csv", true))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return false;
                }
            }
            bw.write(username + "," + password);
            bw.newLine();
            return true;
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}