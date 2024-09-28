import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SeeBookingTravelerPage extends JPanel {
    private MainFrame mainFrame;
    private String travelerUsername;

    public SeeBookingTravelerPage(MainFrame mainFrame, String travelerUsername) {
        this.mainFrame = mainFrame;
        this.travelerUsername = travelerUsername;

        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("profile-bg.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // Center the content

        // Create a panel to hold the booking list
        JPanel bookingListPanel = new JPanel();
        bookingListPanel.setLayout(new BoxLayout(bookingListPanel, BoxLayout.Y_AXIS));
        bookingListPanel.setOpaque(false); // Make panel transparent to show background

        // Fixed width for booking panels
        int panelWidth = 600; // Adjust as needed

        // Title Label
        JLabel titleLabel = new JLabel("My Bookings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bookingListPanel.add(titleLabel);
        bookingListPanel.add(Box.createVerticalStrut(20)); // Add spacing

        // Load available bookings for the specified traveler
        ArrayList<String[]> bookings = loadAvailableTravelerBookings();
        int bookingNumber = 1; // For numbering the bookings

        for (String[] booking : bookings) {
            JPanel bookingPanel = new JPanel();
            bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
            bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding between bookings
            bookingPanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with transparency

            // Set fixed width for bookingPanel
            bookingPanel.setMaximumSize(new Dimension(panelWidth, Integer.MAX_VALUE));
            bookingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Display booking information with bold labels
            JLabel bookingInfo = new JLabel("<html>"
                    + "<b>" + bookingNumber + ". Guide: </b>" + booking[0] + "<br>"
                    + "<b>Booked Date: </b>" + booking[2] + "<br>"
                    + "<b>Transaction ID: </b>" + booking[3] + "<br>"
                    + "<b>Status: </b>" + booking[4] + "</html>");
            bookingInfo.setFont(new Font("Arial", Font.PLAIN, 16));
            bookingInfo.setForeground(Color.BLACK);
            bookingInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

            bookingPanel.add(bookingInfo);
            bookingPanel.add(Box.createVerticalStrut(10)); // Add spacing

            // Add bookingPanel to bookingListPanel
            bookingListPanel.add(bookingPanel);

            // Add spacing between booking panels
            bookingListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            bookingNumber++; // Increment booking number for the next entry
        }

        // Create a scroll pane for the bookingListPanel
        JScrollPane scrollPane = new JScrollPane(bookingListPanel);
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

    // Load available bookings from the file, filtering by traveler username
    private ArrayList<String[]> loadAvailableTravelerBookings() {
        ArrayList<String[]> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingInfo = line.split(",");
                if (bookingInfo.length > 1 && this.travelerUsername.equalsIgnoreCase(bookingInfo[1].trim())) {
                    bookings.add(bookingInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
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
