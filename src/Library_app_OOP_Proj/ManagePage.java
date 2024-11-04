package Library_app_OOP_Proj;

// ManagePage.java
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

public class ManagePage extends JFrame {
    private Admin admin;
    private JTabbedPane tabbedPane;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel bookingTableModel;
    private JTable roomTable;
    private JTable bookingTable;

    public ManagePage(Admin admin) {
        this.admin = admin;
        initializeFrame();
        createComponents();
        refreshTables();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeFrame() {
        setTitle("Admin Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(800, 500));
    }

    private void createComponents() {
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add Tabs
        tabbedPane.addTab("Room Management", createRoomPanel());
        tabbedPane.addTab("Booking Management", createBookingPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 52, 52));
        panel.setPreferredSize(new Dimension(1000, 80));

        JLabel titleLabel = new JLabel("ADMIN MANAGEMENT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        JLabel adminLabel = new JLabel("Admin: " + admin.getUsername());
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.addActionListener(e -> logout());

        rightPanel.add(adminLabel);
        rightPanel.add(logoutButton);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Room");
        JButton deleteButton = new JButton("Delete Room");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddRoomDialog());
        deleteButton.addActionListener(e -> deleteSelectedRoom());
        refreshButton.addActionListener(e -> refreshRoomTable());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Room Table
        String[] columns = {"Room ID", "Status"};
        roomTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(roomTableModel);
        setupTable(roomTable);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> dateFilter = createDateFilter();
        filterPanel.add(new JLabel("Date: "));
        filterPanel.add(dateFilter);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton cancelButton = new JButton("Cancel Booking");
        JButton refreshButton = new JButton("Refresh");

        cancelButton.addActionListener(e -> cancelSelectedBooking());
        refreshButton.addActionListener(e -> refreshBookingTable());

        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);

        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Booking Table
        String[] columns = {"Date", "Room", "User", "Start Time", "Duration", "Status"};
        bookingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(bookingTableModel);
        setupTable(bookingTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        return panel;
    }

    private void setupTable(JTable table) {
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private JComboBox<String> createDateFilter() {
        String[] dates = new String[7];
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dates[i] = today.plusDays(i).toString();
        }
        JComboBox<String> dateFilter = new JComboBox<>(dates);
        dateFilter.addActionListener(e -> 
            filterBookingsByDate((String)dateFilter.getSelectedItem()));
        return dateFilter;
    }

    private void showAddRoomDialog() {
        JDialog dialog = new JDialog(this, "Add New Room", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel idLabel = new JLabel("Room ID:");
        JTextField idField = new JTextField(20);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                admin.addRoom(idField.getText().trim());
                refreshRoomTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Room added successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(idLabel, gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        dialog.add(addButton, gbc);
        gbc.gridy = 2;
        dialog.add(cancelButton, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete");
            return;
        }

        String roomId = (String) roomTable.getValueAt(selectedRow, 0);

        // ตรวจสอบว่ามีการจองอยู่หรือไม่
        boolean hasActiveBookings = false;
        for (Booking booking : Booking.getBookingList()) {
            if (booking.getRoom().getRoomID().equals(roomId) && 
                booking.getStatus().equals("ACTIVE")) {
                hasActiveBookings = true;
                break;
            }
        }

        if (hasActiveBookings) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete room with active bookings",
                "Delete Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete room " + roomId + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            admin.removeRoom(roomId);
            refreshRoomTable();
        }
    }

    private void cancelSelectedBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this booking?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String date = (String) bookingTable.getValueAt(selectedRow, 0);
            String roomId = (String) bookingTable.getValueAt(selectedRow, 1);
            int startTime = Integer.parseInt(
                ((String) bookingTable.getValueAt(selectedRow, 3)).split(":")[0]
            );
            
            admin.cancelBooking(roomId, date, startTime);
            refreshBookingTable();
        }
    }

    private void refreshTables() {
        refreshRoomTable();
        refreshBookingTable();
    }

    private void refreshRoomTable() {
        roomTableModel.setRowCount(0);
        ArrayList<Room> rooms = Room.getRoomList();
        String today = LocalDate.now().toString();
        
        for (Room room : rooms) {
            roomTableModel.addRow(new Object[]{
                room.getRoomID(),
                room.isAvailable(today, 9, 1) ? "Available" : "Booked"
            });
        }
    }

    private void refreshBookingTable() {
        bookingTableModel.setRowCount(0);
        ArrayList<Booking> bookings = new ArrayList<>(Booking.getBookingList());
        
        for (Booking booking : bookings) {
            if (booking.getStatus().equals("ACTIVE")) {
                bookingTableModel.addRow(new Object[]{
                    booking.getDate(),
                    booking.getRoom().getRoomID(),
                    booking.getUser().getUsername(),
                    booking.getStart() + ":00",
                    booking.getDuration() + " hours",
                    booking.getStatus()
                });
            }
        }
    }

    private void filterBookingsByDate(String date) {
        bookingTableModel.setRowCount(0);
        ArrayList<Booking> bookings = new ArrayList<>(Booking.getBookingsByDate(date));
        
        for (Booking booking : bookings) {
            bookingTableModel.addRow(new Object[]{
                booking.getDate(),
                booking.getRoom().getRoomID(),
                booking.getUser().getUsername(),
                booking.getStart() + ":00",
                booking.getDuration() + " hours",
                booking.getStatus()
            });
        }
    }

    private void logout() {
        this.dispose();
        new LoginPage().setVisible(true);
    }
}