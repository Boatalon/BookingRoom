package Library_app_OOP_Proj;

// BookingPage.java
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;

public class BookingPage extends JFrame {
    private User currentUser;
    private JTable bookingTable;
    private JComboBox<String> dateSelector;
    private DefaultTableModel tableModel;
    private static final int FIRST_HOUR = 9;
    private static final int LAST_HOUR = 20;

    public BookingPage(User user) {
        this.currentUser = user;
        initializeFrame();
        createComponents();
        refreshTables();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeFrame() {
        setTitle("Room Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(800, 500));
    }

    private void createComponents() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Controls
        JPanel topPanel = createTopPanel();
        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Booking Table
        createBookingTable();
        contentPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 52, 52));
        panel.setPreferredSize(new Dimension(1000, 100));

        // Title
        JLabel titleLabel = new JLabel("ROOM BOOKING");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // User Info and Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel("User: " + currentUser.getUsername() + 
            " (" + currentUser.getUserType() + ")   " +
            "Max Advance Days: " + currentUser.getMaxAdvanceDay() +
            "   Max Hours: " + currentUser.getMaxHrs());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Date Selector
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateSelector = createDateSelector();
        datePanel.add(new JLabel("Select Date: "));
        datePanel.add(dateSelector);

        // View Bookings Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewButton = new JButton("View My Bookings");
        viewButton.addActionListener(e -> viewMyBookings());
        buttonPanel.add(viewButton);

        panel.add(datePanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JComboBox<String> createDateSelector() {
        String[] dates = new String[currentUser.getMaxAdvanceDay() + 1];
        LocalDate today = LocalDate.now();
        for (int i = 0; i <= currentUser.getMaxAdvanceDay(); i++) {
            dates[i] = today.plusDays(i).toString();
        }
        JComboBox<String> selector = new JComboBox<>(dates);
        selector.addActionListener(e -> refreshTables());
        return selector;
    }

    private void createBookingTable() {
        // Create column headers
        String[] columns = new String[LAST_HOUR - FIRST_HOUR + 2];
        columns[0] = "Room";
        for (int i = FIRST_HOUR; i <= LAST_HOUR; i++) {
            columns[i - FIRST_HOUR + 1] = String.format("%d:00", i);
        }

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);
        setupTableAppearance();
        setupTableClickListener();
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
                
                if (value != null && value.toString().equals("•")) {
                    c.setBackground(new Color(248, 215, 218));
                    c.setForeground(new Color(114, 28, 36));
                } else if (column > 0) {
                    c.setBackground(new Color(212, 237, 218));
                    c.setForeground(new Color(21, 87, 36));
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
        bookingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookingTable.rowAtPoint(e.getPoint());
                int col = bookingTable.columnAtPoint(e.getPoint());
                if (col > 0 && tableModel.getValueAt(row, col).equals("◦")) {
                    String roomId = ((String)tableModel.getValueAt(row, 0));
                    showBookingDialog(roomId, col + FIRST_HOUR - 1);
                }
            }
        });
    }
}