import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class HireGuidePage extends JPanel {
    private String guideUsername;
    private String travelerUsername;
    private MainFrame mainFrame;
    private JTextField dateField;
    private JTextField transactionIdField;

    public HireGuidePage(MainFrame mainFrame, String guideUsername, String travelerUsername) {
        this.mainFrame = mainFrame;
        this.guideUsername = guideUsername;
        this.travelerUsername = travelerUsername;

        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("profile-bg.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // Center the form panel

        // Load guide information
        String[] guideInfo = loadGuideInfo(guideUsername);
        String bankName = "";
        String accountNumber = "";

        if (guideInfo != null && guideInfo.length > 12) {
            bankName = guideInfo[12]; // Bank name is at index 12
            accountNumber = guideInfo[11]; // Account number is at index 11
        }

        // Create a panel to hold the form components
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with transparency
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Hire Guide", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK); // Set text color to black
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Selected Guide
        JLabel guideLabel = new JLabel("Selected Guide:");
        guideLabel.setFont(new Font("Arial", Font.BOLD, 18));
        guideLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(guideLabel, gbc);

        JLabel guideNameLabel = new JLabel(guideUsername);
        guideNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        guideNameLabel.setForeground(Color.BLACK);
        gbc.gridx = 1;
        formPanel.add(guideNameLabel, gbc);

        gbc.gridy++;

        // Bank Name
        JLabel bankNameLabel = new JLabel("Bank Name:");
        bankNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bankNameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(bankNameLabel, gbc);

        JLabel bankNameValueLabel = new JLabel(bankName);
        bankNameValueLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        bankNameValueLabel.setForeground(Color.BLACK);
        gbc.gridx = 1;
        formPanel.add(bankNameValueLabel, gbc);

        gbc.gridy++;

        // Account Number
        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        accountNumberLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(accountNumberLabel, gbc);

        JLabel accountNumberValueLabel = new JLabel(accountNumber);
        accountNumberValueLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        accountNumberValueLabel.setForeground(Color.BLACK);
        gbc.gridx = 1;
        formPanel.add(accountNumberValueLabel, gbc);

        gbc.gridy++;

        // Booking Date
        JLabel dateLabel = new JLabel("Booking Date (yyyy-mm-dd):");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dateLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(dateLabel, gbc);

        dateField = new JTextField();
        dateField.setFont(new Font("Arial", Font.PLAIN, 18));
        dateField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridy++;

        // Transaction ID
        JLabel transactionIdLabel = new JLabel("Transaction ID:");
        transactionIdLabel.setFont(new Font("Arial", Font.BOLD, 18));
        transactionIdLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(transactionIdLabel, gbc);

        transactionIdField = new JTextField();
        transactionIdField.setFont(new Font("Arial", Font.PLAIN, 18));
        transactionIdField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(transactionIdField, gbc);

        gbc.gridy++;

        // Note Label 1
        JLabel noteLabel1 = new JLabel("<html><span style='color:red;'>Please make a transaction to the mentioned bank and account number, and fill in the Transaction ID field correctly.</span></html>");
        noteLabel1.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(noteLabel1, gbc);

        gbc.gridy++;

        // Note Label 2
        JLabel noteLabel2 = new JLabel("<html><span style='color:red;'>If the guide rejects your booking, they will contact you to return the payment manually.</span></html>");
        noteLabel2.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        formPanel.add(noteLabel2, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton confirmButton = new JButton("Confirm Booking");
        styleButton(confirmButton);
        confirmButton.addActionListener(e -> confirmBooking());
        buttonPanel.add(confirmButton);

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> mainFrame.showAvailableGuidesPage(travelerUsername));
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add formPanel to backgroundPanel
        backgroundPanel.add(formPanel);

        // Add backgroundPanel to the main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    // Confirm booking method
    private void confirmBooking() {
        String date = dateField.getText().trim();
        String transactionId = transactionIdField.getText().trim();

        if (date.isEmpty() || transactionId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in the date and Transaction ID.");
            return;
        }

        // Save the booking information to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Bookings.txt", true))) {
            writer.write(guideUsername + "," + travelerUsername + "," + date + "," + transactionId + "," + "Pending by Guide");
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Booking confirmed! Waiting for guide approval.");
            // Clear input fields after booking
            dateField.setText("");
            transactionIdField.setText("");
            // Load traveler information and navigate back to the profile page
            String[] userInfo = loadTravelerInfo(travelerUsername);
            if (userInfo != null) {
                mainFrame.showTravelerProfilePage(userInfo);
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving your profile information.");
                mainFrame.showPage("LoginPage"); // Navigate to login page as fallback
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load traveler info based on username
    private String[] loadTravelerInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("TravelersData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 3 && userInfo[3].equals(username)) { // Assuming username is at index 3
                    return userInfo;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to load guide info based on username
    private String[] loadGuideInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("GuidesData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 3 && userInfo[3].equals(username)) { // Assuming username is at index 3
                    return userInfo;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(64, 64, 64)); // Dark gray color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 204, 0)); // Yellow color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(64, 64, 64)); // Original color
            }
        });
    }

    // Custom JPanel to draw the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
            setLayout(new GridBagLayout()); // Center the content
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image scaled to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
