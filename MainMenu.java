

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenu extends JFrame implements ActionListener {

  JButton generalButton;
  JButton personalButton;
  private static String username;

  public static void main(String[] args) {
    MainMenu mainMenu = new MainMenu(username);
    mainMenu.setVisible(true);
  }

  MainMenu(String username) {
    this.username=username;
    setTitle("Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(450, 300);
    setLayout(null);
    setLocationRelativeTo(null);
    getContentPane().setBackground(Color.GRAY);

    // Load images (assuming the images are in the same directory as the Java class)
    ImageIcon generalIcon = new ImageIcon("images\\personaldatabase.png");
    ImageIcon personalIcon = new ImageIcon("images\\personaldatabase.png");

    Image img = generalIcon.getImage();

    // Create a BufferedImage to modify the image
    BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    g2d.drawImage(img, 0, 0, null);
    g2d.dispose();

    // Modify the color to red
    for (int y = 0; y < bufferedImage.getHeight(); y++) {
      for (int x = 0; x < bufferedImage.getWidth(); x++) {
        int rgb = bufferedImage.getRGB(x, y);
        int alpha = (rgb >> 24) & 0xff;
        // int red = (rgb >> 16) & 0xff;
        // int green = (rgb >> 8) & 0xff;
        // int blue = rgb & 0xff;
        int newRGB = (alpha << 24) | (255 << 16) | (0 << 8) | 0;
        bufferedImage.setRGB(x, y, newRGB);
      }
    }

    // Create a new ImageIcon with the modified image
    ImageIcon generalIconRed = new ImageIcon(bufferedImage);

    // Create labels for the images
    JLabel generalLabel = new JLabel();
    generalLabel.setIcon(generalIconRed);
    generalLabel.setBounds(10, 20, 210, 210);
    add(generalLabel);

    JLabel personalLabel = new JLabel();
    personalLabel.setIcon(personalIcon);
    personalLabel.setBounds(230, 20, 210, 210);
    add(personalLabel);

    // create button of general and personal database
    generalButton = new JButton("General DB");
    generalButton.setBounds(10, 200, 200, 25);
    generalButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
    generalButton.addActionListener(this);
    generalButton.setFocusPainted(false);
    generalButton.setForeground(Color.BLACK);
    add(generalButton);

    personalButton = new JButton("Personal DB");
    personalButton.setBounds(230, 200, 200, 25);
    personalButton.setFont(new Font("TImes New Roman", Font.BOLD, 15));
    personalButton.addActionListener(this);
    personalButton.setFocusPainted(false);
    personalButton.setForeground(Color.BLACK);
    add(personalButton);
  }

  @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == generalButton){
            new MyGeneraltable(username);
        }
        else if  (e.getSource() == personalButton)
        {
    
            new MyPersonalTable(username);
        
        }
    }
}
