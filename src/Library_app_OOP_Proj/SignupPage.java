package Library_app_OOP_Proj;

// SignupPage.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignupPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> userTypeCombo;

    public SignupPage() {
        initializeFrame();
        createComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeFrame() {
        setTitle("Library Room Booking System - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 500));
        setResizable(false);
    }

    private void createComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 52, 52));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel titleLabel = new JLabel("CREATE NEW ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Signup Panel
        JPanel signupPanel = new JPanel();
        signupPanel.setBackground(Color.WHITE);
        signupPanel.setLayout(null);

        // Signup Form
        JPanel formPanel = createSignupForm();
        formPanel.setBounds(250, 30, 300, 300);
        signupPanel.add(formPanel);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(signupPanel, BorderLayout.CENTER);
    }

    private JPanel createSignupForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // User Type Selection
        JLabel typeLabel = new JLabel("Register as:");
        userTypeCombo = new JComboBox<>(new String[]{"Student", "Lecturer", "General"});
        
        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        
        // Password Fields
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 52, 52));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> handleSignup());

        // Back Button
        JButton backButton = new JButton("Back to Login");
        backButton.setForeground(new Color(0, 52, 52));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(e -> openLoginPage());

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(typeLabel, gbc);

        gbc.gridy = 1;
        panel.add(userTypeCombo, gbc);

        gbc.gridy = 2;
        panel.add(usernameLabel, gbc);

        gbc.gridy = 3;
        panel.add(usernameField, gbc);

        gbc.gridy = 4;
        panel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        panel.add(passwordField, gbc);

        gbc.gridy = 6;
        panel.add(confirmLabel, gbc);

        gbc.gridy = 7;
        panel.add(confirmPasswordField, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(registerButton, gbc);

        gbc.gridy = 9;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(backButton, gbc);

        return panel;
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            switch (userType) {
                case "Student":
                    new Student(username, password);
                    break;
                case "Lecturer":
                    new Lecturer(username, password);
                    break;
                case "General":
                    new General(username, password);
                    break;
            }

            JOptionPane.showMessageDialog(this,
                "Registration successful! Please login.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            openLoginPage();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLoginPage() {
        this.dispose();
        new LoginPage().setVisible(true);
    }
}