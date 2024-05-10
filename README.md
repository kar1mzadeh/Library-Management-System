[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/4zK3HDh5)


###Youtube Link
https://youtu.be/5SG_R3Q8Zuw?feature=shared 

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

### 5. ‘LoginFrame.java’
The LoginFrame.java file is a Java Swing application responsible for creating a login interface where users can enter their credentials to access the book library system.
### Components and Function
setTitle("Book Library");: This line sets the title of the JFrame window to "Book Library". The title appears at the top of the window's frame and helps users identify the purpose of the application.
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);: This line sets the default operation that occurs when the user closes the window. JFrame.EXIT_ON_CLOSE specifies that the application should exit and terminate when the window is closed. This ensures that the application stops running gracefully when the user chooses to close the window.
setSize(480, 320);: This line sets the initial size of the JFrame window to 480 pixels in width and 320 pixels in height. It determines the dimensions of the window when it first appears on the screen.
setLayout(null);: This line sets the layout manager of the JFrame to null. In Java Swing, layout managers are responsible for automatically arranging components within a container. Setting the layout manager to null means that the programmer will manually specify the size and position of each component added to the container.
setLocationRelativeTo(null);: This line sets the initial location of the JFrame window relative to the screen. Passing null as the argument centers the window on the screen horizontally and vertically, ensuring that it appears at the center of the screen when it is first displayed.
### Example of Use
The main() method initializes and displays the LoginFrame interface, allowing users to input their credentials to access the book library system.

### 6. “MainMenu.java”
“MainMenu.java” is a Swing application that serves as the main menu for accessing different functionalities of a book library system. It provides options for navigating to the general book database and the personal book database.
### Components and Function
 ImageIcon generalIcon = new ImageIcon("images\\personaldatabase.png");
    ImageIcon personalIcon = new ImageIcon("images\\personaldatabase.png");
These lines create ImageIcon objects by loading image files from the specified file paths. These images will be used to create buttons and labels in the user interface.
  BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight
(null),
        BufferedImage.TYPE_INT_ARGB);
This line creates a BufferedImage object with the same dimensions as the loaded image.
 generalButton.addActionListener(this);
  personalButton.addActionListener(this);
These lines register the MainMenu class as an action listener for the "General DB" and "Personal DB" buttons, which means that the actionPerformed method will be called when these buttons are clicked
### Example of use
The main() method initializes and displays the main menu interface, allowing users to choose between accessing the general book database or the personal book database. Upon clicking a button, the corresponding database interface is opened, providing users with access to relevant functionalities based on their selection.
### 7. ”UserManagement.java”
” UserManagement.java” represents a Java Swing application for managing user data displayed in a JTable
### Components and Function
 public static UserManagementTable userManagementTable;
Declares a static variable userManagementTable of type UserManagementTable. It seems to be intended to hold an instance of UserManagementTable but is not initialized here.

   UserManagementTable userManagementTable = new UserManagementTable("admin");
Creates a new instance of the UserManagementTable class with the username “admin” and assigns it to the local variable userManagementTable.

setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
Sets the default close operation for the frame to exit the application when closed.

DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnName);
Creates a DefaultTableModel with the data and column names.

### Example of Use

The main() method of MyGeneralTable.java initializes and displays the interface of the book database (MyGeneralTable), allowing users to interact with the database based on their roles and preferences. Users can view, filter, edit, add, update, and delete book entries as per their permissions and requirements within the application interface.


### 8.UserManagementPanel.java
UserManagementPanel.java is a Swing component designed to manage user accounts within a larger application. It provides fields for entering username and password, along with buttons for deleting a user and accessing a general database. The actions triggered by these buttons are handled internally within the component, making it a self-contained module for user management tasks.
### Components and Function
public UserManagementPanel(String username)
: Begins the constructor of the UserManagementPanel class, which takes a String parameter username.

  usernameField = new JTextField();
Initializes the usernameField variable as a new JTextField.

 generalDbButton = new JButton("GeneralDB")
Initializes the generalDbButton variable as a new JButton with the text “GeneralDB”

The next few lines add labels, text fields, and buttons to the panel, setting their positions, sizes, fonts, and adding action listeners.

### Example of use 

The main() method in UserManagementPanel initializes and displays the user management panel interface, allowing users to interact with the user management functionalities.



