package RegistrationPage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class GeneralDB {
    public static  void main (String []args){
        new MyGeneraltable();
    }

}

class MyGeneraltable extends JFrame {
    JTable table;
    Object[][] data;
    MyGeneraltable(){
        setBounds(500,240,800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        data = getData();
        String [] columnName={"Title","Author","Rating","Review"};


        DefaultTableModel defaultTableModel=new DefaultTableModel(data,columnName);
        table=new JTable(defaultTableModel);

        Font headerFont = table.getTableHeader().getFont();
        Font newHeaderFont = headerFont.deriveFont(Font.BOLD, 16);
        table.getTableHeader().setFont(newHeaderFont);

        add(new JScrollPane(table));
        pack();
        validate();
            
    }
    Object[][] getData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("RegistrationPage\\brodsky.csv"));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("generalDatabaseUpdated.csv"));
            bufferedWriter.write("Title" + "Author" + "Rating" + "Review");
            ArrayList<Object[]> list = new ArrayList<>();
            String str;
            int count = 0;
            while ((str = bufferedReader.readLine()) != null) {
                if (count != 0) {
                    String[] parts;
                    String[] authorBooks = str.split("\"");
                    if (authorBooks.length > 1) {
                        int last = authorBooks.length - 1;
                        String author = (authorBooks[last].trim().length() > 0) ? authorBooks[last].trim() : "Unknown";
                        if (author.contains(",")) {
                            author = author.replace(",", "");

                        }
                        parts = authorBooks[1].split(",");
                        String rating = "No rating";
                        String review = "No review";
                        for (String title : parts) {
                            title = title.replace("[", "");
                            title = title.replace("]", "");
                            list.add(new Object[]{title.trim(), author, rating, review});
                            bufferedWriter.write(title.trim() + "," + author + "," + rating + "," + review);
                            bufferedWriter.newLine();

                        }
                    } else {
                        parts = str.split(",");
                        String title = parts.length > 0 && !parts[0].trim().isEmpty() ? parts[0].trim() : "Unknown";
                        String author = parts.length > 1 ? parts[1].trim() : "Unknown";
                        String rating = "No rating";
                        String review = "No review";
                        title = title.replace("[", "");
                        title = title.replace("]", "");
                        list.add(new Object[]{title.trim(), author, rating, review});
                        bufferedWriter.write(title.trim() + "," + author + "," + rating + "," + review);
                        bufferedWriter.newLine();
                    }

                }
                count++;

            }
            bufferedReader.close();
            bufferedWriter.close();
            Object[][] data = new Object[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
            return data;

        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }
    }


}