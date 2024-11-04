package Library_app_OOP_Proj;

//ManagePage.java
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;

public class ManagePage extends JFrame {
 private Admin admin;
 private JTabbedPane tabbedPane;
 private DefaultTableModel roomTableModel;
 private DefaultTableModel bookingTableModel;
 private JTable roomTable;
 private JTable bookingTable;
 
 private static final int FRAME_WIDTH = 1000;
 private static final int FRAME_HEIGHT = 600;
 private static final Color THEME_COLOR = new Color(0, 52, 52);

 public ManagePage(Admin admin) {
     this.admin = admin;
     initializeFrame();
     createComponents();
     refreshTables();
 }

 private void initializeFrame() {
     setTitle("Admin Management System");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
     setMinimumSize(new Dimension(800, 500));
     setLocationRelativeTo(null);
 }

 private void createComponents() {
     setLayout(new BorderLayout());
     
     // Header
     add(createHeaderPanel(), BorderLayout.NORTH);
     
     // Tabbed Pane
     tabbedPane = new JTabbedPane();
     tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
     
     tabbedPane.addTab("Room Management", createRoomPanel());
     tabbedPane.addTab("Booking Management", createBookingPanel());
     
     add(tabbedPane, BorderLayout.CENTER);
 }

 private JPanel createHeaderPanel() {
     JPanel panel = new JPanel(new BorderLayout());
     panel.setBackground(THEME_COLOR);
     panel.setPreferredSize(new Dimension(FRAME_WIDTH, 80));

     // Title
     JLabel titleLabel = new JLabel("ADMIN MANAGEMENT");
     titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
     titleLabel.setForeground(Color.WHITE);
     titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

     // Admin Info & Logout
     JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     rightPanel.setOpaque(false);
     
     JLabel adminLabel = new JLabel("Admin: " + admin.getUsername());
     adminLabel.setForeground(Color.WHITE);
     
     JButton logoutButton = new JButton("Logout");
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
     addButton.addActionListener(e -> showAddRoomDialog());
     
     JButton deleteButton = new JButton("Delete Room");
     deleteButton.addActionListener(e -> deleteSelectedRoom());
     
     JButton refreshButton = new JButton("Refresh");
     refreshButton.addActionListener(e -> refreshRoomTable());

     buttonPanel.add(addButton);
     buttonPanel.add(deleteButton);
     buttonPanel.add(refreshButton);

     // Room Table
     roomTableModel = new DefaultTableModel(
         new String[]{"Room ID", "Status"}, 
         0
     ) {
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

     // Date Filter
     JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
     JComboBox<String> dateFilter = createDateFilter();
     filterPanel.add(new JLabel("Date: "));
     filterPanel.add(dateFilter);

     // Buttons
     JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     
     JButton cancelButton = new JButton("Cancel Booking");
     cancelButton.addActionListener(e -> cancelSelectedBooking());
     
     JButton refreshButton = new JButton("Refresh");
     refreshButton.addActionListener(e -> refreshBookingTable());

     buttonPanel.add(cancelButton);
     buttonPanel.add(refreshButton);

     topPanel.add(filterPanel, BorderLayout.WEST);
     topPanel.add(buttonPanel, BorderLayout.EAST);

     // Booking Table
     bookingTableModel = new DefaultTableModel(
         new String[]{"Date", "Room", "User", "Start Time", "Duration", "Status"},
         0
     ) {
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
     String[] dates = new String[7];  // แสดง 7 วันล่วงหน้า
     LocalDate today = LocalDate.now();
     
     for (int i = 0; i < 7; i++) {
         dates[i] = today.plusDays(i).toString();
     }
     
     JComboBox<String> dateFilter = new JComboBox<>(dates);
     dateFilter.addActionListener(e -> 
         filterBookingsByDate((String)dateFilter.getSelectedItem())
     );
     return dateFilter;
 }

 private void showAddRoomDialog() {
     JDialog dialog = new JDialog(this, "Add New Room", true);
     dialog.setLayout(new GridBagLayout());
     GridBagConstraints gbc = new GridBagConstraints();
     gbc.fill = GridBagConstraints.HORIZONTAL;
     gbc.insets = new Insets(5, 5, 5, 5);

     // Room ID input
     JLabel idLabel = new JLabel("Room ID:");
     JTextField idField = new JTextField(20);

     // Buttons
     JButton addButton = new JButton("Add");
     addButton.addActionListener(e -> {
         try {
             String roomId = idField.getText().trim();
             if (roomId.isEmpty()) {
                 throw new IllegalArgumentException("Room ID cannot be empty");
             }
             admin.addRoom(roomId);
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

     String roomId = (String)roomTable.getValueAt(selectedRow, 0);

     // ตรวจสอบการจองที่ยังใช้งานอยู่
     boolean hasActiveBookings = Booking.getBookingList().stream()
         .anyMatch(b -> b.getRoom().getRoomId().equals(roomId) && 
                      b.getStatus().equals("ACTIVE"));

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
         String date = (String)bookingTable.getValueAt(selectedRow, 0);
         String roomId = (String)bookingTable.getValueAt(selectedRow, 1);
         int startTime = Integer.parseInt(
             ((String)bookingTable.getValueAt(selectedRow, 3))
                 .split(":")[0]
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
     String today = LocalDate.now().toString();
     
     for (Room room : Room.getRoomList()) {
         roomTableModel.addRow(new Object[]{
             room.getRoomId(),
             room.isAvailable(today, 9, 1) ? "Available" : "Booked"
         });
     }
 }

 private void refreshBookingTable() {
     bookingTableModel.setRowCount(0);
     
     for (Booking booking : Booking.getBookingList()) {
         if (booking.getStatus().equals("ACTIVE")) {
             bookingTableModel.addRow(new Object[]{
                 booking.getDate(),
                 booking.getRoom().getRoomId(),
                 booking.getUser().getUsername(),
                 String.format("%02d:00", booking.getStartHour()),
                 booking.getDuration() + " hours",
                 booking.getStatus()
             });
         }
     }
 }

 private void filterBookingsByDate(String date) {
     bookingTableModel.setRowCount(0);
     
     for (Booking booking : Booking.getBookingsByDate(date)) {
         bookingTableModel.addRow(new Object[]{
             booking.getDate(),
             booking.getRoom().getRoomId(),
             booking.getUser().getUsername(),
             String.format("%02d:00", booking.getStartHour()),
             booking.getDuration() + " hours",
             booking.getStatus()
         });
     }
 }

 private void logout() {
     dispose();
     new LoginPage().setVisible(true);
 }
}