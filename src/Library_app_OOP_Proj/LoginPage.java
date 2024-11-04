package Library_app_OOP_Proj;

// LoginPage.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        initializeFrame();
        createComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeFrame() {
        setTitle("Library Room Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 500));
        setResizable(false);
    }

    private void createComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 52, 52));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel titleLabel = new JLabel("LIBRARY ROOM BOOKING");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(null);

        // Login Form
        JPanel formPanel = createLoginForm();
        formPanel.setBounds(250, 50, 300, 250);
        loginPanel.add(formPanel);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        
        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 52, 52));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(200, 35));
        loginButton.addActionListener(e -> handleLogin());

        // Sign Up Link
        JButton signupButton = new JButton("Create New Account");
        signupButton.setForeground(new Color(0, 52, 52));
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.addActionListener(e -> openSignupPage());

        // Add key listeners for Enter key
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(loginButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(signupButton, gbc);

        return panel;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check admin login
        ArrayList<Admin> admins = Admin.getAdminList();
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && 
                admin.getPassword().equals(password)) {
                openManagePage(admin);
                return;
            }
        }

        // Check regular user login
        ArrayList<User> users = User.getUserList();
        for (User user : users) {
            if (user.getUsername().equals(username) && 
                user.getPassword().equals(password)) {
                openBookingPage(user);
                return;
            }
        }

        JOptionPane.showMessageDialog(this,
            "Invalid username or password",
            "Login Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void openBookingPage(User user) {
        this.dispose();
        new BookingPage(user).setVisible(true);
    }

    private void openManagePage(Admin admin) {
        this.dispose();
        new ManagePage(admin).setVisible(true);
    }

    private void openSignupPage() {
        this.dispose();
        new SignupPage().setVisible(true);
    }
}
