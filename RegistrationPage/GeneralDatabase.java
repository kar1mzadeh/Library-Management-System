package RegistrationPage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GeneralDatabase {
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
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
            ArrayList<Object[]> list = new ArrayList<>();
            String str;
            int count=0;
            while ((str = br.readLine()) != null) {
                if(count!=0) {
                    String[] parts;
                    String[] authorBooks=str.split("\"");
                    if(authorBooks.length>1)
                    {
                        String author=(authorBooks[authorBooks.length-1].trim().length()>0) ? authorBooks[authorBooks.length-1].trim() : "Unknown";
                        if(author.contains(","))
                        {
                            author = author.replace(",", "");
                        }
                        parts=authorBooks[1].split(",");
                        String rating = parts.length > 3 ? parts[3].trim() : "No rating";
                        String review = parts.length > 4 ? parts[4].trim() : "No review";
                        for(String title: parts)
                        {
                            list.add(new Object[]{title.trim(),author,rating, review});
                        }
                    }
                    else {
                        parts = str.split(",");
                        String title = parts.length > 0 && !parts[0].trim().isEmpty() ? parts[0].trim() : "Unknown";
                        String author = parts.length > 1 ? parts[1].trim() : "Unknown";
                        String rating = parts.length > 2 ? parts[2].trim() : "No rating";
                        String review = parts.length > 3 ? parts[3].trim() : "No review";
                        list.add(new Object[]{title.trim(), author, rating, review});
                        // System.out.println(str);
                    }
                }
                count++;
            }
            br.close();
            Object[][] data = new Object[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
            return data;

        }
        catch (Exception x) {
            x.printStackTrace();
            return null;
        }


    }


}