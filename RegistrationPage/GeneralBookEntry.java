//package RegistrationPage;
//
//import com.opencsv.CSVReader;
//import com.opencsv.CSVWriter;
//import com.opencsv.exceptions.CsvException;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//
//public class BookEntry {
//    String title;
//    String author;
//    double rating;
//    List<String> reviews;
//
//    public BookEntry(String title, String author) {
//        this.title = title;
//        this.author = author;
//        this.rating = 0.0;
//        this.reviews = new ArrayList<>();
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public void setRating(double rating) {
//        this.rating = rating;
//    }
//
//    public List<String> getReviews() {
//        return reviews;
//    }
//
//    public void setReviews(List<String> reviews) {
//        this.reviews = reviews;
//    }
//
//    public BookEntry(String title, String author, double rating, List<String> reviews) {
//        this.title = title;
//        this.author = author;
//        this.rating = rating;
//        this.reviews = reviews;
//    }
//
//    public static List<BookEntry> getAllEntries() {
//        List<BookEntry> books = new ArrayList<>();
//
//        try (CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv"))) {
//            String[] nextLine;
//            while ((nextLine = reader.readNext()) != null) {
//                String title = nextLine[0].isEmpty() ? "Unknown" : nextLine[0];
//                String author = nextLine[1].isEmpty() ? "Unknown" : nextLine[1];
//                double rating = nextLine.length > 2 && !nextLine[2].isEmpty() ? Double.parseDouble(nextLine[2]) : 0.0;
//                List<String> reviews = nextLine.length > 1 && !nextLine[1].isEmpty() ? Arrays.asList(nextLine[1].split(",")) : new ArrayList<>();
//
//                BookEntry book = new BookEntry(title, author, rating, reviews);
//                books.add(book);
//            }
//        } catch (IOException | CsvException e) {
//            e.printStackTrace();
//        }
//
//        return books;
//    }
//
//    public static void addEntry(BookEntry book) {
//        try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Rahman\\OneDrive - ADA University\\Desktop\\team-project-team-11\\RegistrationPage\\brodsky.csv", true))) {
//            String[] newEntry = {book.getTitle(), book.getAuthor(), String.valueOf(book.getRating()), String.valueOf(book.getReviews())};
//            writer.writeNext(newEntry);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected int getReviewCount() {
//        return reviews.size();
//    }
//}