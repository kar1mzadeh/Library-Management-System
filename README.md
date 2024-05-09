[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/4zK3HDh5)

## Overview of files

### 1.`BrodskyDataParser.java`

This file (`BrodskyDataParser.java`) handles CSV data from a specific file (`brodsky.csv`) and prepares it for use in the app.

#### Components and Function
- **getData():** Reads data from `brodsky.csv`, processes it, and returns a two-dimensional array (`Object[][]`) containing the parsed data. Each array row represents a book entry with details like title, author, rating, and review.
- **CSV File Handling:** The `getData()` method reads the CSV file line by line using `BufferedReader`, processes each line to extract book details, and writes these details to another CSV file (`generalDatabaseUpdated.csv`) using` BufferedWriter` for potential future use.

#### Example of Use
The `main()` method in this class shows how to use BrodskyDataParser to retrieve and handle book data.



### 2.`RegisterPage.java`

This file (`RegisterPage.java`) carries out the registration function of the app using Java Swing for the user interface.

#### Components and Function
- **User Interface (UI) Design:** The `RegisterPage` class extends `JFrame` and creates a registration form using Swing components like `JLabel`, `JTextField`, `JPasswordField`, and `JButton`.
- **Action Handling (`actionPerformed()`):** Implements the `ActionListener` interface to manage button clicks (`Register` and `Back to Login`). The `actionPerformed()` method handles user registration by validating input and calling `registerUser()`.
- **User Registration (`registerUser()`):** Validates user input (username and password) against specified criteria (non-empty, strong password requirements) and checks for existing username duplicates in a CSV file (`users.csv`). If valid, it adds the new user's details to the file.

#### Example of Use
The `main()` method sets up and displays the registration form (`RegisterPage`) for user interaction.



### 3.1.`PersonalIDB.java`

This (`PersonalDB.java`) file contains utility methods to help with storage and retrieval of personal book data in a CSV file (`personalDatabaseUpdated.csv`).

#### Components and Function
- **`loadPersonalBooks(String username)`:** Loads personal book entries added by a user from the CSV file.
- **`addBookToPersonalDB(String username, String[] bookDetails)`:** Adds a book entry to a user's personal database. Default values are used to handle missing fields in the input array.
- **`savePersonalBooks(String username, ArrayList<String[]> books)`:** Saves the personal book data to the CSV file. 
- **`getReviewsForBook(String bookTitle)`:** Fetch reviews (username, user rating, user review) for a specific book title from the personal book database. 
- **`getUsernamesWhoReviewedBook(String bookTitle)`:** Get usernames of users who reviewed a specific book title. 

#### Example of Use
This class provides methods to interact with personal book data, such as loading, adding, saving, and retrieving reviews.



### 3.2.`MyPersonalTable.java`

This (`MyPersonalTable.java`) file is a GUI implementation using Swing components that helps in adding, updating, and deleting personal book entries.

#### Components and Function
- **Table Display (`JTable`)**: Displays personal book entries in a `JTable` with columns for title, author, rating, review, status, time spent, start date, end date, user rating, and user review.
- **User Interaction**: Allows users to add, update, and delete personal book entries interactively within the GUI.
- **Input Fields (`JTextField`)**: Provides input fields for editing book details and interacting with the personal book database.
- **Control Panel (`JPanel`)**: Includes buttons for adding, updating, deleting books, and navigating to the general book database.

#### Example of Use
The `main()` method creates the personalized book management interface (`MyPersonalTable`) for the logged-in user. 



### 4.`GeneralIDB.java` (`MyGeneralTable.java`)

`MyGeneraltable.java` file is a Java Swing application that displays some general book database in a `JTable` and allows interacting with database by adding,updating,deleting operations.

#### Components and Function
- **Initialization:** Sets up a `JFrame` window to display the book database and loads book data from a CSV file (`generalDatabaseUpdated.csv`) into a `JTable` using a `DefaultTableModel`.
- **Table Configuration:** Configures the table with sorting functionality by column headers, allowing users to sort data in ascending or descending order.
Also, implements selection listeners to populate text fields with details of the selected book.
- **User Interaction:** Provides text fields (`JTextField`) for filtering and editing book details such as title, author, rating, and reviews and implements buttons (`JButton`) for adding, updating, and deleting book entries from the database.
- **User Role handling:** Differentiates functionality based on user roles (e.g., "admin" and regular users) by displaying specific buttons and actions accordingly. 

#### Example of Use
The `main()` method initializes and displays the `MyGeneraltable` interface, allowing users to interact with the book database based on their role and preferences.