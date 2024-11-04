package Library_app_OOP_Proj;

//BookingPage.java
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingPage extends JFrame {
 private User currentUser;
 private JTable bookingTable;
 private JComboBox<String> dateSelector;
 private DefaultTableModel tableModel;
 
 private static final int FIRST_HOUR = 9;  // เริ่มเวลา 9:00
 private static final int LAST_HOUR = 20;  // ถึงเวลา 20:00
 private static final int FRAME_WIDTH = 1000;
 private static final int FRAME_HEIGHT = 600;
 private static final Color THEME_COLOR = new Color(0, 52, 52);

 public BookingPage(User user) {
     this.currentUser = user;
     initializeFrame();
     createComponents();
     refreshBookingTable();
 }

 private void initializeFrame() {
     setTitle("Room Booking System");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
     setMinimumSize(new Dimension(800, 500));
     setLocationRelativeTo(null);
 }

 private void createComponents() {
     // Main layout
     setLayout(new BorderLayout(10, 10));
     
     // Header
     add(createHeaderPanel(), BorderLayout.NORTH);
     
     // Content Panel
     JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
     contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
     
     // Controls Panel
     contentPanel.add(createControlsPanel(), BorderLayout.NORTH);
     
     // Booking Table
     contentPanel.add(createBookingTablePanel(), BorderLayout.CENTER);
     
     add(contentPanel, BorderLayout.CENTER);
 }

 private JPanel createHeaderPanel() {
     JPanel panel = new JPanel(new BorderLayout());
     panel.setBackground(THEME_COLOR);
     panel.setPreferredSize(new Dimension(FRAME_WIDTH, 100));

     // Title
     JLabel titleLabel = new JLabel("ROOM BOOKING");
     titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
     titleLabel.setForeground(Color.WHITE);
     titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

     // User Info Panel
     JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     userPanel.setOpaque(false);

     String userInfo = String.format("User: %s (%s)   Max Days: %d   Max Hours: %d",
         currentUser.getUsername(),
         currentUser.getUserType(),
         currentUser.getMaxAdvanceDays(),
         currentUser.getMaxHours()
     );
     JLabel userLabel = new JLabel(userInfo);
     userLabel.setForeground(Color.WHITE);
     
     JButton logoutButton = new JButton("Logout");
     logoutButton.addActionListener(e -> logout());

     userPanel.add(userLabel);
     userPanel.add(logoutButton);

     panel.add(titleLabel, BorderLayout.WEST);
     panel.add(userPanel, BorderLayout.EAST);

     return panel;
 }

 private JPanel createControlsPanel() {
     JPanel panel = new JPanel(new BorderLayout());

     // Date Selection
     JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
     dateSelector = createDateSelector();
     datePanel.add(new JLabel("Select Date: "));
     datePanel.add(dateSelector);

     // Buttons
     JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     JButton viewButton = new JButton("My Bookings");
     viewButton.addActionListener(e -> showMyBookings());
     buttonPanel.add(viewButton);

     panel.add(datePanel, BorderLayout.WEST);
     panel.add(buttonPanel, BorderLayout.EAST);

     return panel;
 }

 private JPanel createBookingTablePanel() {
	    JPanel panel = new JPanel(new BorderLayout());

	    // Create table model
	    String[] columns = new String[LAST_HOUR - FIRST_HOUR + 2];
	    columns[0] = "Room";
	    for (int i = FIRST_HOUR; i <= LAST_HOUR; i++) {
	        columns[i - FIRST_HOUR + 1] = String.format("%02d:00", i);
	    }

	    tableModel = new DefaultTableModel(columns, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };

	    // Create and setup table
	    bookingTable = new JTable(tableModel);
	    setupTableAppearance();
	    setupTableClickListener();

	    // Add table to scroll pane, then add to panel
	    JScrollPane scrollPane = new JScrollPane(bookingTable);
	    panel.add(scrollPane, BorderLayout.CENTER);

	    return panel;
	}

 private JComboBox<String> createDateSelector() {
     String[] dates = new String[currentUser.getMaxAdvanceDays() + 1];
     LocalDate today = LocalDate.now();
     
     for (int i = 0; i <= currentUser.getMaxAdvanceDays(); i++) {
         dates[i] = today.plusDays(i).toString();
     }
     
     JComboBox<String> selector = new JComboBox<>(dates);
     selector.addActionListener(e -> refreshBookingTable());
     return selector;
 }

 private void setupTableAppearance() {
     bookingTable.setRowHeight(40);
     bookingTable.getTableHeader().setReorderingAllowed(false);
     bookingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

     // Set column widths
     bookingTable.getColumnModel().getColumn(0).setPreferredWidth(150);
     for (int i = 1; i < bookingTable.getColumnCount(); i++) {
         bookingTable.getColumnModel().getColumn(i).setPreferredWidth(60);
     }

     // Custom cell renderer
     bookingTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value,
                 boolean isSelected, boolean hasFocus, int row, int column) {
             Component c = super.getTableCellRendererComponent(
                 table, value, isSelected, hasFocus, row, column);
             
             if (column > 0) {
                 boolean isBooked = value != null && value.toString().equals("•");
                 c.setBackground(isBooked ? new Color(248, 215, 218) : new Color(212, 237, 218));
                 c.setForeground(isBooked ? new Color(114, 28, 36) : new Color(21, 87, 36));
             } else {
                 c.setBackground(Color.WHITE);
                 c.setForeground(Color.BLACK);
             }
             
             ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
             return c;
         }
     });
 }

 private void setupTableClickListener() {
     bookingTable.addMouseListener(new java.awt.event.MouseAdapter() {
         @Override
         public void mouseClicked(java.awt.event.MouseEvent e) {
             int row = bookingTable.rowAtPoint(e.getPoint());
             int col = bookingTable.columnAtPoint(e.getPoint());
             
             if (col > 0 && tableModel.getValueAt(row, col).equals("◦")) {
                 String roomId = (String)tableModel.getValueAt(row, 0);
                 showBookingDialog(roomId, col + FIRST_HOUR - 1);
             }
         }
     });
 }

 private void showBookingDialog(String roomId, int hour) {
     Room room = findRoom(roomId);
     if (room == null) return;

     JDialog dialog = new JDialog(this, "Book Room", true);
     dialog.setLayout(new GridLayout(4, 2, 5, 5));

     // Dialog components
     dialog.add(new JLabel("Room:"));
     dialog.add(new JLabel(roomId));
     
     dialog.add(new JLabel("Start Time:"));
     dialog.add(new JLabel(hour + ":00"));
     
     dialog.add(new JLabel("Duration (hours):"));
     Integer[] durations = {1, 2, 3};
     JComboBox<Integer> durationCombo = new JComboBox<>(durations);
     dialog.add(durationCombo);

     // Buttons
     JButton bookButton = new JButton("Book");
     bookButton.addActionListener(e -> {
         try {
             bookRoom(room, hour, (Integer)durationCombo.getSelectedItem());
             dialog.dispose();
         } catch (Exception ex) {
             JOptionPane.showMessageDialog(dialog, 
                 ex.getMessage(), 
                 "Booking Error",
                 JOptionPane.ERROR_MESSAGE);
         }
     });

     JButton cancelButton = new JButton("Cancel");
     cancelButton.addActionListener(e -> dialog.dispose());

     dialog.add(bookButton);
     dialog.add(cancelButton);

     dialog.pack();
     dialog.setLocationRelativeTo(this);
     dialog.setVisible(true);
 }

 private void bookRoom(Room room, int hour, int duration) {
     try {
         new Booking(
             currentUser,
             room,
             (String)dateSelector.getSelectedItem(),
             hour,
             duration
         );
         refreshBookingTable();
         JOptionPane.showMessageDialog(this, "Booking successful!");
     } catch (Exception e) {
         throw new IllegalArgumentException("Booking failed: " + e.getMessage());
     }
 }

 private void showMyBookings() {
     JDialog dialog = new JDialog(this, "My Bookings", true);
     dialog.setLayout(new BorderLayout());
     
     // Create table
     String[] columns = {"Date", "Room", "Start Time", "Duration", "Status"};
     DefaultTableModel model = new DefaultTableModel(columns, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
             return false;
         }
     };

     ArrayList<Booking> myBookings = new ArrayList<>();
     for (Booking b : Booking.getBookingList()) {
         if (b.getUser().equals(currentUser) && b.getStatus().equals("ACTIVE")) {
             myBookings.add(b);
         }
     }

     for (Booking b : myBookings) {
         model.addRow(new Object[]{
             b.getDate(),
             b.getRoom().getRoomId(),
             String.format("%02d:00", b.getStartHour()),
             b.getDuration() + " hour(s)",
             b.getStatus()
         });
     }

     JTable bookingsTable = new JTable(model);
     bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

     // Buttons
     JPanel buttonPanel = new JPanel();
     JButton cancelButton = new JButton("Cancel Selected");
     cancelButton.addActionListener(e -> {
         int row = bookingsTable.getSelectedRow();
         if (row != -1) {
             cancelBooking(myBookings.get(row), dialog);
         } else {
             JOptionPane.showMessageDialog(dialog,
                 "Please select a booking to cancel",
                 "No Selection",
                 JOptionPane.WARNING_MESSAGE);
         }
     });

     JButton closeButton = new JButton("Close");
     closeButton.addActionListener(e -> dialog.dispose());

     buttonPanel.add(cancelButton);
     buttonPanel.add(closeButton);

     dialog.add(new JScrollPane(bookingsTable), BorderLayout.CENTER);
     dialog.add(buttonPanel, BorderLayout.SOUTH);

     dialog.setSize(600, 400);
     dialog.setLocationRelativeTo(this);
     dialog.setVisible(true);
 }

 private void cancelBooking(Booking booking, JDialog parentDialog) {
     int confirm = JOptionPane.showConfirmDialog(parentDialog,
         "Are you sure you want to cancel this booking?",
         "Confirm Cancellation",
         JOptionPane.YES_NO_OPTION);
         
     if (confirm == JOptionPane.YES_OPTION) {
         booking.cancel();
         refreshBookingTable();
         parentDialog.dispose();
         JOptionPane.showMessageDialog(this, "Booking cancelled successfully!");
     }
 }

 private void refreshBookingTable() {
     tableModel.setRowCount(0);
     String selectedDate = (String)dateSelector.getSelectedItem();

     for (Room room : Room.getRoomList()) {
         Object[] rowData = new Object[LAST_HOUR - FIRST_HOUR + 2];
         rowData[0] = room.getRoomId();
         
         for (int hour = FIRST_HOUR; hour <= LAST_HOUR; hour++) {
             rowData[hour - FIRST_HOUR + 1] = 
                 room.isAvailable(selectedDate, hour, 1) ? "◦" : "•";
         }
         
         tableModel.addRow(rowData);
     }
 }

 private Room findRoom(String roomId) {
     return Room.getRoomList().stream()
         .filter(r -> r.getRoomId().equals(roomId))
         .findFirst()
         .orElse(null);
 }

 private void logout() {
     dispose();
     new LoginPage().setVisible(true);
 }
}