package RegistrationPage;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Personal Database entry
public class PersonalBookEntry extends BookEntry {
    String status;
    int timeSpent;
    LocalDate startDate;
    LocalDate endDate;
    int userRating;
    String userReview;

    public PersonalBookEntry(String title, String author, double rating, List<String> reviews, String status, int timeSpent, LocalDate startDate, LocalDate endDate, int userRating, String userReview) {
        super(title, author, rating, reviews);
        this.status = status;
        this.timeSpent = timeSpent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userRating = userRating;
        this.userReview = userReview;
    }
    public List<BookEntry> getAllEntities(String username) {

        return readPersonalEntries(username);
    }

    private List<BookEntry> readPersonalEntries(String username){
        List<BookEntry> personalEntries = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(username + "C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String title = nextLine[0];
                String author = nextLine[1];
                double rating = Double.parseDouble(nextLine[2]);
                List<String> reviews = Arrays.asList(nextLine[3].split(","));
                String status = nextLine[4];
                int timeSpent = Integer.parseInt(nextLine[5]);
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now();
                int userRating = Integer.parseInt(nextLine[8]);
                String userReview = nextLine[9];


                PersonalBookEntry book = new PersonalBookEntry(title, author, rating, reviews,status,timeSpent,startDate,endDate,userRating,userReview);
                personalEntries.add(book);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return personalEntries;
    }
   static List<PersonalBookEntry> getPersonalBookEntries(String username) {
        List<PersonalBookEntry> personalEntries = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(username + "C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String title = nextLine[0];
                String author = nextLine[1];
                double rating = Double.parseDouble(nextLine[2]);
                List<String> reviews = Arrays.asList(nextLine[3].split(","));

                String status = ""; // You need to decide how to set the status, timeSpent, startDate, endDate, userRating, and userReview
                int timeSpent = 0;
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now();
                int userRating = 0;
                String userReview = "";

                PersonalBookEntry entry = new PersonalBookEntry(title, author, rating, reviews, status, timeSpent, startDate, endDate, userRating, userReview);
                personalEntries.add(entry);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return personalEntries;
    }

    private void writePersonalEntries(String username, List<PersonalBookEntry> personalEntries) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(username + "C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"))) {
            for (PersonalBookEntry book : personalEntries) {
                String[] line = {book.getTitle(), book.getAuthor(), String.valueOf(book.getRating()), String.join(",", book.getReviews()), book.getStatus(), String.valueOf(book.getTimeSpent()), String.valueOf(book.getStartDate()), String.valueOf(book.getEndDate()), String.valueOf(book.getUserRating()), book.getUserReview()};
                writer.writeNext(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }
}
