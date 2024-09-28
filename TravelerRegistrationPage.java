import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Scanner;

public class TravelerRegistrationPage extends JPanel {
    private JTextField nameField, numberField, emailField, usernameField, addressField, detailsField, areaField, languageField;
    private JPasswordField passwordField;
    private MainFrame mainFrame;

    public TravelerRegistrationPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Set the background image with a yellow theme
        ImageIcon backgroundImageIcon = new ImageIcon("reg-traveler.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // To center the form panel

        // Create a panel to hold the form components
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(600, 750)); // Adjusted size to accommodate labels and increased height
        formPanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with some transparency
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15); // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Title Label
        JLabel titleLabel = new JLabel("Traveler Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(64, 64, 64)); // Dark gray color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset gridwidth after title

        // Name Label and Field
        gbc.gridy = 1;
        addLabelAndField(formPanel, gbc, "Full Name:", nameField = createStyledTextField());

        // Phone Number Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Phone Number:", numberField = createStyledTextField());

        // Email Address Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Email Address:", emailField = createStyledTextField());

        // Username Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Username:", usernameField = createStyledTextField());

        // Password Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Password:", passwordField = createStyledPasswordField());

        // Address Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Address:", addressField = createStyledTextField());

        // Details Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Details:", detailsField = createStyledTextField());

        // Covered Area Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Covered Area:", areaField = createStyledTextField());

        // Preferred Language Label and Field
        gbc.gridy++;
        addLabelAndField(formPanel, gbc, "Preferred Language:", languageField = createStyledTextField());

        // Register and Back Buttons
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(e -> registerTraveler());

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> mainFrame.showPage("SelectionPage"));

        // Button Panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false); // Transparent background
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);

        // Add formPanel to the backgroundPanel
        backgroundPanel.add(formPanel);

        // Add backgroundPanel to the main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(64, 64, 64)); // Dark gray color
        gbc.gridx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        styleTextField(textField);
        // Increase input field height
        textField.setPreferredSize(new Dimension(250, 35)); // Adjust height as needed
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField);
        // Increase input field height
        passwordField.setPreferredSize(new Dimension(250, 35)); // Adjust height as needed
        return passwordField;
    }

    private void styleTextField(JTextComponent textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
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
        // Ensure the text field is empty
        textField.setText("");
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(64, 64, 64)); // Dark gray color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 20, 10, 20));

        // Add mouse hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 204, 0)); // Yellow color on hover
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(64, 64, 64)); // Original color
            }
        });
    }

    // Method to handle traveler registration logic
    private void registerTraveler() {
        String name = nameField.getText().trim();
        String number = numberField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String address = addressField.getText().trim();
        String details = detailsField.getText().trim();
        String area = areaField.getText().trim();
        String language = languageField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if any of the fields are empty
        StringBuilder emptyFields = new StringBuilder();

        if (name.isEmpty()) emptyFields.append("• Full Name\n");
        if (number.isEmpty()) emptyFields.append("• Phone Number\n");
        if (email.isEmpty()) emptyFields.append("• Email Address\n");
        if (username.isEmpty()) emptyFields.append("• Username\n");
        if (password.isEmpty()) emptyFields.append("• Password\n");
        if (address.isEmpty()) emptyFields.append("• Address\n");
        if (details.isEmpty()) emptyFields.append("• Details\n");
        if (area.isEmpty()) emptyFields.append("• Covered Area\n");
        if (language.isEmpty()) emptyFields.append("• Preferred Language\n");

        if (emptyFields.length() > 0) {
            JOptionPane.showMessageDialog(this, "Please fill the following fields:\n" + emptyFields.toString());
            return;
        }

        // Check for duplicate username in TravelersData.txt and GuidesData.txt
        if (isUsernameTaken(username, "TravelersData.txt", "GuidesData.txt")) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another username.");
            return;
        }

        // Save traveler information to TravelersData.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("TravelersData.txt", true))) {
            writer.write(String.join(",", name, number, email, username, password, address, details, area, language));
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Registration Successful!");

            // Clear all fields after successful registration
            clearFields();

            // Optionally, navigate to the login page
            mainFrame.showPage("LoginPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to clear all input fields
    private void clearFields() {
        nameField.setText("");
        numberField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        addressField.setText("");
        detailsField.setText("");
        areaField.setText("");
        languageField.setText("");
    }

    // Method to check if a username already exists in the files
    private boolean isUsernameTaken(String username, String filePath1, String filePath2) {
        return isUsernameInFile(username, filePath1) || isUsernameInFile(username, filePath2);
    }

    // Helper method to check if username exists in a specific file
    private boolean isUsernameInFile(String username, String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] userInfo = scanner.nextLine().split(",");
                if (userInfo.length > 3 && userInfo[3].equals(username)) { // Assuming username is the 4th field (index 3)
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
