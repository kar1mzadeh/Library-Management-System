package RegistrationPage;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;

class MyGeneraltable extends JFrame {
    JTable table;
    Object[][] data;
    DefaultTableModel defaultTableModel;
    Map<String, Integer> columnClickCount = new HashMap<>();

    MyGeneraltable() {
        setBounds(500, 240, 800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        data = getData();
        String[] columnName = { "Title", "Author", "Rating", "Review" };

        defaultTableModel = new DefaultTableModel(data, columnName) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class; // Ensure all columns are sorted as strings
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing of table cells
            }
        };

        table = new JTable(defaultTableModel);
        table.setAutoCreateRowSorter(true); // Enable row sorting

        Font headerFont = table.getTableHeader().getFont();
        Font newHeaderFont = headerFont.deriveFont(Font.BOLD, 16);
        table.getTableHeader().setFont(newHeaderFont);

        table.getTableHeader().addMouseListener(new HeaderMouseListener());

        add(new JScrollPane(table));
        pack();
        validate();
    }

    private class HeaderMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JTableHeader header = (JTableHeader) e.getSource();
            TableColumnModel columnModel = header.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            String columnName = defaultTableModel.getColumnName(column);

            int clickCount = columnClickCount.getOrDefault(columnName, 0);
            clickCount++;
            columnClickCount.put(columnName, clickCount);

            TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
            ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();

            switch (clickCount % 3) {
                case 0: // Original form
                    columnClickCount.put(columnName, 0);
                    defaultTableModel.setRowCount(0); // Clear table
                    Object[][] originalData = getData(); // Get original data
                    for (Object[] row : originalData) {
                        defaultTableModel.addRow(row); // Add rows to table
                    }
                    break;
                case 1: // Ascending order
                    sortKeys.add(new RowSorter.SortKey(column, SortOrder.ASCENDING));
                    break;
                case 2: // Descending order
                    sortKeys.add(new RowSorter.SortKey(column, SortOrder.DESCENDING));
                    break;
            }

            sorter.setSortKeys(sortKeys);
            sorter.sort();
        }
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
                            list.add(new Object[] { title.trim(), author, rating, review });
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
                        list.add(new Object[] { title.trim(), author, rating, review });
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

    public static void main(String[] args) {
        new MyGeneraltable();
    }
}
