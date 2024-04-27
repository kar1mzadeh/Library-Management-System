package RegistrationPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionDatabase extends JFrame implements ActionListener {
    public static void main(String[] args) {
        OptionDatabase optionDatabase = new OptionDatabase();
        optionDatabase.setVisible(true);

    }


    JButton generalButton;
    JButton personalButton;
    OptionDatabase(){
        setTitle("Option Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        generalButton=new JButton("General DB");
        generalButton.setBounds(50, 100, 160, 25);
        generalButton.setFont(new Font("Arial", Font.BOLD, 14));

        generalButton.addActionListener(this);
        generalButton.setFocusPainted(false);
        add(generalButton);

        personalButton=new JButton("Personal DB");
        personalButton.setBounds(230, 100, 160, 25);
        personalButton.setFont(new Font("Arial", Font.BOLD, 14));
        personalButton.addActionListener(this);
        add(personalButton);



    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == generalButton){
            new MyGeneraltable();
        }
        else if  (e.getSource() == personalButton){
            new PersonalDatabase.MyPersonalTable();
        }



    }

}