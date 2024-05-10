
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UserManagement {
    public static UserManagementTable userManagementTable;

    public static void main(String[] args) {
        UserManagementTable userManagementTable = new UserManagementTable("admin");
        userManagementTable.setLocationRelativeTo(null); // Center on screen
        userManagementTable.setVisible(true);
    }
}

class UserManagementTable extends JFrame {
    private JTable table;
    private Object[][] data;
    private String username;
    private UserManagementPanel userManagementPanel;

    public UserManagementTable(String username) {
        this.username = username;
        setBounds(500, 240, 800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        data = getData();
        String[] columnName = {"Username", "Password"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnName);
        table = new JTable(defaultTableModel);

        Font headerFont = table.getTableHeader().getFont();
        Font newHeaderFont = headerFont.deriveFont(Font.BOLD, 14);
        table.getTableHeader().setFont(newHeaderFont);

        userManagementPanel = new UserManagementPanel(username);
        userManagementPanel.getPanel().setPreferredSize(new Dimension(400, getHeight()));

        add(userManagementPanel.getPanel(), BorderLayout.EAST);

        add(new JScrollPane(table));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    userManagementPanel.setUsernameField((String) table.getValueAt(selectedRow, 0));
                    userManagementPanel.setPasswordField((String) table.getValueAt(selectedRow, 1));
                 
                }
            }
        });
    }

    Object[][] getData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("csvfiles\\users.csv"));
            ArrayList<Object[]> list = new ArrayList<>();
            String str;
            while ((str = br.readLine()) != null) {
                String[] parts = str.split(",");
                if (parts.length > 0) {
                    String username = parts[0];
                    String password = parts[1];
                    list.add(new Object[]{username.trim(), password});
                }
            }
            br.close();

            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
            return data;

        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }
    }

    public void deleteUser(String username) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(username)) {
                model.removeRow(i);
                break;
            }

        }

      //  PersonalDB.deleteUsersPersonalLibrary(username);
        saveDataToFile(model);
        if (userManagementPanel != null) {
            userManagementPanel.dispose(); // Dispose the panel if it exists
        }
    }

    private void saveDataToFile(DefaultTableModel model) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("csvfiles\\users.csv"));
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    sb.append(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) {
                        sb.append(",");
                    }
                }
                writer.println(sb.toString());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}