package RegistrationPage;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class PersonalDB {
    public static void main(String[] args) {
        new MyPersonalTable();

    }

    static class MyPersonalTable extends MyGeneraltable {
        MyPersonalTable() {
            setVisible(true);
            setTitle("Personal Database");
            data = getPersonalData();
            String[] columnName = { "Title", "Author", "Rating", "Review", "Status", "Time spent", "Start Date",
                    "End Date", "User Rating", "User Review" };
            DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnName);
            table.setModel(defaultTableModel);

        }

        Object[][] getPersonalData() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("users.csv"));
                ArrayList<Object[]> list = new ArrayList<>();
                String str;
                int count = 0;
                while ((str = bufferedReader.readLine()) != null) {

                    String[] parts = str.split(",");
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].contains(",")) {
                            parts[i].replace(",", "");
                        }
                    }
                    String author = parts[1];
                    String title = parts[0];
                    String rating = parts[2];
                    String review = parts[3];
                    String status = "Not started";
                    String timeSpent = "Unknown";
                    String startDate = "";
                    String endDate = "";
                    String userRating = "Add rating";
                    String userReview = "Add review";

                    list.add(new Object[] { title.trim(), author, rating, review, status, timeSpent, startDate, endDate,
                            userRating, userReview });

                }
                count++;
                bufferedReader.close();
                Object[][] data = new Object[list.size()][10];
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
}