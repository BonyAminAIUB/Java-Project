// SeeBookingGuidePage.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class SeeBookingGuidePage extends JPanel {
    private MainFrame mainFrame;
    private String guideUsername;

    public SeeBookingGuidePage(MainFrame mainFrame, String guideUsername) {
        this.mainFrame = mainFrame;
        this.guideUsername = guideUsername;

        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("profile-bg.jpg");
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
        JLabel titleLabel = new JLabel("Bookings from Travelers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bookingListPanel.add(titleLabel);
        bookingListPanel.add(Box.createVerticalStrut(20)); // Add spacing

        // Load available bookings for the guide
        ArrayList<String[]> bookings = loadAvailableBookings();

        // Iterate over bookings and display them
        for (String[] booking : bookings) {
            JPanel bookingPanel = new JPanel();
            bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
            bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding
            bookingPanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with transparency

            // Set fixed width for bookingPanel
            bookingPanel.setMaximumSize(new Dimension(panelWidth, Integer.MAX_VALUE));
            bookingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Get traveler username from booking
            String travelerUsername = booking[1].trim();

            // Load traveler info
            String[] travelerInfo = loadTravelerInfo(travelerUsername);
            String travelerName = travelerUsername; // Default to username if name not found
            String travelerPhoneNumber = "N/A"; // Default if phone number not found

            if (travelerInfo != null && travelerInfo.length > 1) {
                travelerName = travelerInfo[0].trim(); // Assuming name is at index 0
                travelerPhoneNumber = travelerInfo[1].trim(); // Assuming phone number is at index 1
            }

            // Display booking information with bold labels
            JLabel bookingInfo = new JLabel("<html>"
                    + "<b>Traveler Name: </b>" + travelerName + "<br>"
                    + "<b>Phone Number: </b>" + travelerPhoneNumber + "<br>"
                    + "<b>Booked Date: </b>" + booking[2].trim() + "<br>"
                    + "<b>Transaction ID: </b>" + booking[3].trim() + "<br>"
                    + "<b>Status: </b>" + booking[4].trim() + "</html>");
            bookingInfo.setFont(new Font("Arial", Font.PLAIN, 16));
            bookingInfo.setForeground(Color.BLACK);
            bookingInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

            bookingPanel.add(bookingInfo);
            bookingPanel.add(Box.createVerticalStrut(10)); // Add spacing

            // Buttons Panel
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonsPanel.setOpaque(false);

            // Confirm Button
            JButton confirmButton = new JButton("Confirm");
            styleButton(confirmButton);
            confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!booking[4].trim().equals("Pending by Guide")) {
                confirmButton.setEnabled(false);
            }

            // Reject Button
            JButton rejectButton = new JButton("Reject");
            styleButton(rejectButton);
            rejectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!booking[4].trim().equals("Pending by Guide")) {
                rejectButton.setEnabled(false);
            }

            // Clone booking details to use in action listeners
            final String[] bookingDetails = booking.clone();

            // Confirm action
            confirmButton.addActionListener(e -> {
                updateBookingStatus(bookingDetails, "Approved by Guide");
                reloadPage();
            });

            // Reject action
            rejectButton.addActionListener(e -> {
                updateBookingStatus(bookingDetails, "Cancelled by Guide");
                reloadPage();
            });

            // Add buttons to panel
            buttonsPanel.add(confirmButton);
            buttonsPanel.add(rejectButton);

            bookingPanel.add(buttonsPanel);

            // Add bookingPanel to bookingListPanel
            bookingListPanel.add(bookingPanel);

            // Add spacing between booking panels
            bookingListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Create a scroll pane for the bookingListPanel
        JScrollPane scrollPane = new JScrollPane(bookingListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Adjust scrolling speed
        scrollPane.setPreferredSize(new Dimension(panelWidth + 50, 400)); // Adjust height as needed

        // Create a wrapper panel to center the scrollPane
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setOpaque(false);
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.add(scrollPane);

        // Back button to return to the guide profile
        JButton backButton = new JButton("Back to Profile");
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            String[] guideInfo = loadGuideInfo(guideUsername);
            if (guideInfo != null) {
                mainFrame.showGuideProfilePage(guideInfo);
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
        GridBagConstraints gbcWrapper = new GridBagConstraints();
        gbcWrapper.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(wrapperPanel, gbcWrapper);

        // Add backgroundPanel to the main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    // Load traveler info based on username
    private String[] loadTravelerInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("TravelersData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 3 && userInfo[3].trim().equalsIgnoreCase(username.trim())) { // Assuming username is at index 3
                    return userInfo;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Load available bookings for this guide
    private ArrayList<String[]> loadAvailableBookings() {
        ArrayList<String[]> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingInfo = line.split(",");
                if (bookingInfo.length > 4 && this.guideUsername.equalsIgnoreCase(bookingInfo[0].trim())) {
                    bookings.add(bookingInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Update the booking status based on booking details
    private void updateBookingStatus(String[] targetBooking, String newStatus) {
        File bookingsFile = new File("Bookings.txt");
        File tempFile = new File("TempBookings.txt");
        boolean bookingFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(bookingsFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] booking = line.split(",");
                if (booking.length > 4 &&
                        booking[0].trim().equals(targetBooking[0].trim()) &&
                        booking[1].trim().equals(targetBooking[1].trim()) &&
                        booking[2].trim().equals(targetBooking[2].trim()) &&
                        booking[3].trim().equals(targetBooking[3].trim())) {
                    // Update the status
                    booking[4] = newStatus;
                    bookingFound = true;
                }
                String bookingLine = String.join(",", booking);
                writer.write(bookingLine);
                writer.newLine();
            }

            if (!bookingFound) {
                JOptionPane.showMessageDialog(this, "Booking not found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the updated file
        if (bookingsFile.delete()) {
            if (!tempFile.renameTo(bookingsFile)) {
                JOptionPane.showMessageDialog(this, "Error renaming temporary file.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting original bookings file.");
        }
    }

    // Reload the page to reflect changes
    private void reloadPage() {
        mainFrame.showSeeBookingGuidePage(guideUsername);
    }

    // Load guide info based on username
    private String[] loadGuideInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("GuidesData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] guideInfo = line.split(",");
                if (guideInfo.length > 3 && guideInfo[3].trim().equalsIgnoreCase(username.trim())) { // Assuming username is at index 3
                    return guideInfo;
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
