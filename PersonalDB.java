import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;


    public class PersonalDB {
        private static final String PERSONAL_BOOKS_FILE = "csvfiles\\personalDatabaseUpdated.csv";

        
         public static boolean deleteUserReview(String username, String bookTitle) {
        // Implementation depends on your storage setup
        // This is a simple example assuming a CSV file and immediate deletion
        ArrayList<String[]> updatedReviews = new ArrayList<>();
        boolean isDeleted = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("csvfiles\\personalDatabaseUpdated.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equalsIgnoreCase(bookTitle)) {
                    // Change the review and rating to indicate deletion
                    parts[10] = "Deleted";  // Assuming review is at index 9
                    isDeleted = true;
                }
                updatedReviews.add(parts);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    
        // Rewrite the updated reviews back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter("csvfiles\\personalDatabaseUpdated.csv"))) {
            for (String[] parts : updatedReviews) {
                writer.println(String.join(",", parts));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return isDeleted;
    }
        public static String getRatingDetails(String bookTitle) {
            
            ArrayList<Double> ratings = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(PERSONAL_BOOKS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Assuming the book title is at index 1 and the user rating is at index 8
                    if (parts[1].equalsIgnoreCase(bookTitle) && !parts[9].equals("Add rating")) {
                        try {
                            double rating = Double.parseDouble(parts[9]);
                            ratings.add(rating);
                        } catch (NumberFormatException e) {
                            // Handle case where the rating is not a number
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            if (ratings.isEmpty()) {
                return "No rating";
            } else {
                double average = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                return String.format("%.2f (%d)", average, ratings.size());
            }
        }
    
        public static ArrayList<String[]> loadPersonalBooks(String username) {
            ArrayList<String[]> books = new ArrayList<>();
            File file = new File(PERSONAL_BOOKS_FILE);
            if (!file.exists()) {
                System.out.println("The file " + PERSONAL_BOOKS_FILE + " does not exist.");
                return books;  // Return empty list if file doesn't exist
            }
        
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 1 && parts[0].equals(username)) {
                        books.add(Arrays.copyOfRange(parts, 1, parts.length));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file for " + username + ": " + e.getMessage());
            }
            return books;
        }
        
          // Add a book to the personal database with default values for certain fields
          public static boolean addBookToPersonalDB(String username, String[] bookDetails) {
            ArrayList<String[]> books = loadPersonalBooks(username);
        
            // Check for existing book by title
            boolean bookExists = books.stream().anyMatch(b -> b[0].equalsIgnoreCase(bookDetails[0]));
            if (bookExists) {
                return false; // Book already exists, do not add
            }
        
            // Add book if not already present
            if (bookDetails.length < 10) {
                String[] completeDetails = new String[10];
                System.arraycopy(bookDetails, 0, completeDetails, 0, bookDetails.length);
                Arrays.fill(completeDetails, bookDetails.length, 10, "");
                completeDetails[4] = "Not Started";
                completeDetails[5] = "0";
                completeDetails[6]="";
                completeDetails[7]="";
                completeDetails[8] = "Add rating";
                completeDetails[9] = "Add review";
                books.add(completeDetails);
            } else {
                books.add(bookDetails);
            }
            savePersonalBooks(username, books);
            return true;
        }
        
        
    public static void savePersonalBooks(String username, ArrayList<String[]> books) {
        // Read all lines, filter out this user's previous entries, and append new ones
        ArrayList<String> allLines = new ArrayList<>();
        File file = new File(PERSONAL_BOOKS_FILE);
    
        try {
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith(username + ",")) { // Keep lines of other users
                            allLines.add(line);
                        }
                    }
                }
            }
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (String line : allLines) {
                    writer.println(line);
                }
                for (String[] book : books) {
                    writer.println(username + "," + String.join(",", book));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String[]> getReviewsForBook(String bookTitle) {
    ArrayList<String[]> reviews = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(PERSONAL_BOOKS_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[1].equalsIgnoreCase(bookTitle)) { // Assuming the second part is the book title
                reviews.add(new String[] {parts[0], parts[9], parts[10]}); // username, user rating, user review
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return reviews;
}
public static String getUsernamesWhoReviewedBook(String bookTitle) {
    ArrayList<String> usernames = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("csvfiles\\personalDatabaseUpdated.csv"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[1].equalsIgnoreCase(bookTitle) && parts.length > 9 && !parts[10].equals("Add review")) { // Assuming parts[9] contains the review
                usernames.add(parts[0]);  // Assuming parts[0] contains the username
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return String.join(" · ", usernames);
}    
        // Delete a book from all users
        public static void deleteBookFromAllUsers(String[] bookDetails) {
            File file = new File(PERSONAL_BOOKS_FILE);
            ArrayList<String> updatedLines = new ArrayList<>();
        
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Split the line into components
                    String[] parts = line.split(",");
                    // Check if the current line matches the book to delete
                    if (!(parts[1].equals(bookDetails[0]) && parts[2].equals(bookDetails[1]))) {
                        // If it does not match, add it to the updated lines
                        updatedLines.add(line);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
            // Rewrite the CSV with the updated lines
            try (PrintWriter writer = new PrintWriter(new FileWriter(PERSONAL_BOOKS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public static String fetchUserReview(String username, String bookTitle) {
            File file = new File(PERSONAL_BOOKS_FILE);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Assuming the format is Username,BookTitle,Rating,Review,...
                    if (parts.length > 3 && parts[0].equals(username) && parts[1].equals(bookTitle)) {
                        return "Rating: " + parts[2] + ", Review: " + parts[3];  // Customize as needed
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            return "No review found.";
        }
        
    }

    

    