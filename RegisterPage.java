
    
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

public class RegisterPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField, passwordAgainField;
    private JButton registerButton;
    private JButton loginButton;

    public RegisterPage() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setExtendedState(JFrame.MAXIMIZED_BOTH); // Set JFrame to fullscreen
        setSize(480, 400);
        setLayout(null);
        setLocationRelativeTo(null); // Center the frame on the screen
        getContentPane().setBackground(Color.GRAY);

          // Load the image
    ImageIcon imageIcon = new ImageIcon("images\\library.png");
    Image image = imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
    ImageIcon scaledIcon = new ImageIcon(image);
    // Create a label to display the image
    JLabel imageLabel = new JLabel(scaledIcon);
    imageLabel.setBounds(120, 20, 60, 60);

    // Add the image label to the frame
    add(imageLabel);
    
        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setBounds(190, 30, 200, 30);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel);
    
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(110, 90, 80, 30);
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        usernameLabel.setForeground(Color.BLACK);
        add(usernameLabel);
    
        usernameField = new JTextField();
        usernameField.setBounds(200, 90, 165, 30);
        usernameField.setForeground(Color.BLACK);
        add(usernameField);
    
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(110, 150, 80, 30);
        passwordLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        passwordLabel.setForeground(Color.BLACK);
        add(passwordLabel);
    
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 150, 165, 30);
        passwordField.setForeground(Color.BLACK);
        add(passwordField);
    
        JLabel passwordAgainLabel = new JLabel("Password Again:");
        passwordAgainLabel.setBounds(80, 210, 120, 30);
        passwordAgainLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        passwordAgainLabel.setForeground(Color.BLACK);
        add(passwordAgainLabel);
    
        passwordAgainField = new JPasswordField();
        passwordAgainField.setBounds(200, 210, 165, 30);
        passwordAgainField.setForeground(Color.BLACK);
        add(passwordAgainField);
    
        registerButton = new JButton("Register");
        registerButton.setBounds(80, 270, 100, 45);
        registerButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        registerButton.addActionListener(this);
        registerButton.setForeground(Color.BLACK);
        add(registerButton);
    
        loginButton = new JButton("Back to Login");
        loginButton.setBounds(230, 270, 150, 45);
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        loginButton.addActionListener(this);
        loginButton.setForeground(Color.BLACK);
        add(loginButton);
    
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String username;
        String password;
        String passwordAgain;
        if(e.getSource()==registerButton)
        {
            username = usernameField.getText().toLowerCase();
            password = new String(passwordField.getPassword());
            passwordAgain=new String(passwordAgainField.getPassword());
            if(password.trim().equals(passwordAgain.trim()))
            {
                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration Successful");
                    dispose();
                    if (username.equals("admin") && password.equals("admin")) {
                    new MyGeneraltable(username);
                    } else {
                        MainMenu mainMenu = new MainMenu(username);
                        mainMenu.setVisible(true);
                    }
                } 
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Make sure that password matches to the password repetition");
            }
        }
        else if(e.getSource()==loginButton)
        {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        }
    }
    

    private boolean registerUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
            return false;
        }

        // Validate strong password with regex
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (!password.matches(passwordRegex)) {
            JOptionPane.showMessageDialog(this, "Password must contain at least 8 characters, including at least one digit, one uppercase letter, one lowercase letter, one special character(@#$%^&+=!), and no whitespaces.");
            return false;
        }

        // Proceed with user registration
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("csvfiles\\users.csv"));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("csvfiles\\users.csv", true))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    JOptionPane.showMessageDialog(this,"Make sure that password matches to the password repetition.");
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

    public static void main(String[] args) {
        RegisterPage registrationPage = new RegisterPage();
        registrationPage.setVisible(true);
    }
}


