


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UserManagementPanel extends JFrame implements ActionListener {
    private JPanel panel;
    private JTextField usernameField, passwordField;
    private JButton deleteButton, generalDbButton;
    String username;

    public UserManagementPanel(String username) {
        this.username = username;
        panel = new JPanel();
        panel.setLayout(null);
        usernameField = new JTextField();
        passwordField = new JTextField();
        deleteButton = new JButton("Delete User");
        generalDbButton = new JButton("GeneralDB");

        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(40, 390, 150, 30);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(usernameLabel);
        usernameField.setBounds(140, 390, 200, 30);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(40, 430, 150, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(passwordLabel);
        passwordField.setBounds(140, 430, 200, 30);
        panel.add(passwordField);

        generalDbButton.setBounds(110, 665, 150, 50);
        generalDbButton.setFont(new Font("Arial", Font.BOLD, 14));
        generalDbButton.addActionListener(this);
        panel.add(generalDbButton);

        deleteButton.setBounds(110, 620, 150, 35);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.addActionListener(this);
        panel.add(deleteButton);
    }

    JPanel getPanel() {
        return panel;
    }

    public void setUsernameField(String text) {
        usernameField.setText(text);
    }

    public void setPasswordField(String text) {
        passwordField.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            String selectedUsername = usernameField.getText();
            if (!selectedUsername.isEmpty()) {
                UserManagementTable userManagementTable = new UserManagementTable(selectedUsername);
                userManagementTable.deleteUser(selectedUsername);
                this.dispose();
            }
        } else if (e.getSource() == generalDbButton) {
            this.dispose();
           MyGeneraltable generalTable = new MyGeneraltable("admin");
            generalTable.setVisible(true); // Make sure to set the visibility of your General DB table
            this.dispose();   
        }
    }
}
