

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainFrame mainFrame;

    public LoginPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("login.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // To center the form

        // Create a panel to hold the form components
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(600, 800)); // Adjusted size
        formPanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow background with transparency
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Title Label
        JLabel titleLabel = new JLabel("Login to Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(64, 64, 64)); // Dark gray color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset gridwidth for the rest of the components

        // Username Label
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setForeground(Color.BLACK);
        formPanel.add(usernameLabel, gbc);

        // Username Field
        gbc.gridy = 2;
        usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(Color.BLACK);
        formPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridy = 4;
        passwordField = createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> login());

        // Back Button
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> mainFrame.showPage("SelectionPage"));

        // Button Panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false); // Transparent background
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add formPanel to the backgroundPanel
        backgroundPanel.add(formPanel);

        // Add backgroundPanel to the main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        styleTextField(textField);
        textField.setPreferredSize(new Dimension(350, 40)); // Increased width and height
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setPreferredSize(new Dimension(350, 40)); // Increased width and height
        return passwordField;
    }

    private void styleTextField(JTextComponent textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setForeground(Color.BLACK);
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        textField.setBackground(new Color(255, 255, 240)); // Light yellow background
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(new Color(64, 64, 64), 1));
            }

            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(64, 64, 64)); // Dark gray color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(255, 204, 0)); // Yellow color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(64, 64, 64)); // Original color
            }
        });

        // Remove extra space around the text
        button.setMargin(new Insets(10, 20, 10, 20));
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check GuidesData.txt first
        String[] guideInfo = checkCredentials(username, password, "GuidesData.txt");
        if (guideInfo != null) {
            // Clear input fields after login
            clearFields();
            mainFrame.showGuideProfilePage(guideInfo);
            return;
        }

        // Check TravelersData.txt
        String[] travelerInfo = checkCredentials(username, password, "TravelersData.txt");
        if (travelerInfo != null) {
            // Clear input fields after login
            clearFields();
            mainFrame.showTravelerProfilePage(travelerInfo);
            return;
        }

        JOptionPane.showMessageDialog(this, "Invalid username or password.");
        // Clear input fields even after unsuccessful login (optional)
        clearFields();
    }

    // Method to clear all input fields
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    // Helper method to check credentials in a file
    private String[] checkCredentials(String username, String password, String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] userInfo = scanner.nextLine().split(",");
                if (userInfo.length > 4 && userInfo[3].equals(username) && userInfo[4].equals(password)) {
                    return userInfo; // Return the user info if credentials match
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Custom JPanel to draw the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
            setPreferredSize(new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image scaled to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
