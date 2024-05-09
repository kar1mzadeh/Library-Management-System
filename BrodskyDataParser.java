

import java.io.*;
import java.util.*;
import javax.swing.*;

public class BrodskyDataParser {
    private static final String CSV_FILE_PATH = "csvfiles\\brodsky.csv";

    Object[][] getData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH));
            BufferedWriter bw = new BufferedWriter(new FileWriter("csvfiles\\generalDatabaseUpdated.csv"));
            bw.write("Title" + "Author" + "Rating" + "Review");
            ArrayList<Object[]> list = new ArrayList<>();
            String str;
            int count = 0;
            while ((str = br.readLine()) != null) {
                if (count != 0) {
                    String[] parts;
                    String[] authorBooks = str.split("\"");
                    if (authorBooks.length > 1) {
                        String author = (authorBooks[authorBooks.length - 1].trim().length() > 0) ? authorBooks[authorBooks.length - 1].trim() : "Unknown";
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
                            bw.write(title.trim() + "," + author + "," + rating + "," + review);
                            bw.newLine();

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
                        bw.write(title.trim() + "," + author + "," + rating + "," + review);
                        bw.newLine();
                        System.out.println(str);
                    }

                }
                count++;

            }
            br.close();
            bw.close();
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


    public static void main(String[] args) {
        BrodskyDataParser parser = new BrodskyDataParser();
        Object[][] data = parser.getData();
        // Assume you have some method to handle the display of this data in your UI
        System.out.println("Data loaded successfully.");
    }
}

