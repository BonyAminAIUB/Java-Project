import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class AvailableGuidesPage extends JPanel {
    private MainFrame mainFrame;
    private String travelerUsername;

    public AvailableGuidesPage(MainFrame mainFrame, String travelerUsername) {
        this.mainFrame = mainFrame;
        this.travelerUsername = travelerUsername;

        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("profile-bg.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // Center the content

        // Create a panel to hold the guide list
        JPanel guideListPanel = new JPanel();
        guideListPanel.setLayout(new BoxLayout(guideListPanel, BoxLayout.Y_AXIS));
        guideListPanel.setOpaque(false); // Make panel transparent to show background

        // Fixed width for guide panels
        int panelWidth = 600; // Adjust as needed

        // Title Label
        JLabel titleLabel = new JLabel("Available Guides", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        guideListPanel.add(titleLabel);
        guideListPanel.add(Box.createVerticalStrut(20)); // Add spacing

        // Load available guides from file and display them
        ArrayList<String[]> availableGuides = loadAvailableGuides();
        int guideNumber = 1; // For numbering the guides

        for (String[] guide : availableGuides) {
            // Check if the guide array has the expected length
            if (guide.length < 13) {
                // Skip this entry or handle it accordingly
                continue;
            }

            JPanel guidePanel = new JPanel();
            guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.Y_AXIS));
            guidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            guidePanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with transparency

            // Set fixed width for guidePanel
            guidePanel.setMaximumSize(new Dimension(panelWidth, Integer.MAX_VALUE));
            guidePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Display guide information
            JLabel guideInfo = new JLabel("<html>"
                    + "<b>" + guideNumber + ". Name: </b>" + guide[0] + "<br>"
                    + "<b>Covered Area: </b>" + guide[9] + "<br>"
                    + "<b>Cost Per Day in BDT: </b>" + guide[7] + "<br>"
                    + "<b>Experience: </b>" + guide[6] + "<br>"
                    + "</html>");
            guideInfo.setFont(new Font("Arial", Font.PLAIN, 16));
            guideInfo.setForeground(Color.BLACK);
            guideInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

            guidePanel.add(guideInfo);
            guidePanel.add(Box.createVerticalStrut(10)); // Add spacing

            // Create "Hire" button for each guide
            JButton hireButton = new JButton("Hire");
            styleButton(hireButton);
            hireButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            hireButton.addActionListener(e -> mainFrame.showHirePage(guide[3], travelerUsername)); // Pass the guide's username
            guidePanel.add(hireButton);

            // Add guidePanel to guideListPanel
            guideListPanel.add(guidePanel);

            // Add spacing between guide panels
            guideListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Increment guide number
            guideNumber++;
        }

        // Create a scroll pane for the guideListPanel
        JScrollPane scrollPane = new JScrollPane(guideListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(panelWidth + 50, 400)); // Adjust height as needed

        // Create a wrapper panel to center the scrollPane
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setOpaque(false);
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.add(scrollPane);

        // Back button to return to the traveler profile
        JButton backButton = new JButton("Back to Profile");
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            String[] userInfo = loadTravelerInfo(travelerUsername);
            if (userInfo != null) {
                mainFrame.showTravelerProfilePage(userInfo);
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving your profile information.");
            }
        });

        // Create a panel for the back button
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);

        wrapperPanel.add(Box.createVerticalStrut(20)); // Add spacing
        wrapperPanel.add(backButtonPanel);

        // Center the wrapperPanel in the backgroundPanel using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(wrapperPanel, gbc);

        // Add backgroundPanel to the main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    // Load available guides from the file
    private ArrayList<String[]> loadAvailableGuides() {
        ArrayList<String[]> guides = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("GuidesData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip blank lines
                }
                String[] guideInfo = line.split(",");
                if (guideInfo.length >= 13) { // Ensure the guide has at least the expected number of fields
                    guides.add(guideInfo);
                } else {
                    // Optionally, log or handle the malformed line
                    System.err.println("Malformed guide entry: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guides;
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
