import javax.swing.*;
import java.awt.*;

public class TravelerProfilePage extends JPanel {
    private String username;
    private MainFrame mainFrame;

    public TravelerProfilePage(MainFrame mainFrame, String[] userInfo) {
        this.mainFrame = mainFrame;
        this.username = userInfo[3]; // Assuming username is the 4th field (index 3)

        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("profile-bg.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);

        // Create a panel to hold the profile information
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with transparency
        profilePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title Label
        JLabel titleLabel = new JLabel("Traveler Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK); // Set text color to black
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        profilePanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        // Display user information
        gbc.gridy++;
        addLabelAndValue(profilePanel, gbc, "Name:", userInfo[0]);

        gbc.gridy++;
        addLabelAndValue(profilePanel, gbc, "Phone Number:", userInfo[1]);

        gbc.gridy++;
        addLabelAndValue(profilePanel, gbc, "Email:", userInfo[2]);

        gbc.gridy++;
        addLabelAndValue(profilePanel, gbc, "Username:", userInfo[3]);

        gbc.gridy++;
        addLabelAndValue(profilePanel, gbc, "Address:", userInfo[5]); // Adjust index if necessary

        // Buttons Panel
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton changePasswordButton = new JButton("Change Password");
        styleButton(changePasswordButton);
        changePasswordButton.addActionListener(e -> mainFrame.showChangePasswordPage(username, "TravelersData.txt", "Traveler"));
        buttonPanel.add(changePasswordButton);

        JButton allGuidesButton = new JButton("See All Guides");
        styleButton(allGuidesButton);
        allGuidesButton.addActionListener(e -> mainFrame.showAvailableGuidesPage(username));
        buttonPanel.add(allGuidesButton);

        JButton myBookingsButton = new JButton("See My Bookings");
        styleButton(myBookingsButton);
        myBookingsButton.addActionListener(e -> mainFrame.showSeeBookingTravelerPage(username));
        buttonPanel.add(myBookingsButton);

        JButton logoutButton = new JButton("Log Out");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> mainFrame.showPage("LoginPage"));
        buttonPanel.add(logoutButton);

        profilePanel.add(buttonPanel, gbc);

        // Add profilePanel to backgroundPanel
        backgroundPanel.add(profilePanel);

        // Add backgroundPanel to the main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void addLabelAndValue(JPanel panel, GridBagConstraints gbc, String labelText, String valueText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.BLACK); // Set text color to black
        gbc.gridx = 0;
        panel.add(label, gbc);

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Arial", Font.PLAIN, 18));
        value.setForeground(Color.BLACK); // Set text color to black
        gbc.gridx = 1;
        panel.add(value, gbc);
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
            setLayout(new GridBagLayout()); // Center the profile panel
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image scaled to fit the panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
