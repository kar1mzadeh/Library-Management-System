package RegistrationPage;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class PersonalDatabase {
    public static void main(String[] args) {
        new MyPersonalTable();
        
    }

    static class MyPersonalTable extends MyGeneraltable {
        MyPersonalTable() {
            setTitle("Personal Database");
            Object[][] personalData = getPersonalData();
            String[] columnName = {"Title", "Author", "Rating", "Review", "Status", "Time spent", "Start Date", "End Date", "User Rating", "User Review"};

            DefaultTableModel defaultTableModel = new DefaultTableModel(mergeData(getData(), personalData), columnName);
            table.setModel(defaultTableModel);
            
        }


        Object[][] getPersonalData() {
            try {
                BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\team-project-team-11\\RegistrationPage\\brodsky.csv"));
                ArrayList<Object[]> list = new ArrayList<>();
                String str;
                int count = 0;
                while ((str = br.readLine()) != null) {
                    if (count != 0) {
 
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
                            String status = parts.length > 5 && !parts[5].trim().isEmpty() ? parts[5].trim() : "Not started";
                            String timeSpent = parts.length > 6 && !parts[6].trim().isEmpty() ? parts[6].trim() : "Unknown";
                            String startDate = parts.length > 7 && !parts[7].trim().isEmpty() ? parts[7].trim() : "";
                            String endDate = parts.length > 8 && !parts[8].trim().isEmpty() ? parts[8].trim() : "";
                            String userRating = parts.length > 9 && !parts[9].trim().isEmpty() ? parts[9].trim() : "Add rating";
                            String userReview = parts.length > 10 && !parts[10].trim().isEmpty() ? parts[10].trim() : "Add review";
                            for(String title: parts)
                            {
                                list.add(new Object[]{title.trim(),author,rating, review,status, timeSpent, startDate, endDate, userRating, userReview});
                            }

                        }

                    }
                    count++;
                }
                br.close();
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



        Object[][] mergeData(Object[][] generalData, Object[][] personalData) {
            Object[][] mergedData = new Object[generalData.length + personalData.length][10];
            int index = 0;
            for (Object[] row : personalData) {
                mergedData[index++] = row;
            }
            return mergedData;
        }




    }


}