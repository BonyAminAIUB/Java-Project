import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class ChangePasswordPage extends JPanel {
    private String username;
    private String filePath;
    private String userType;  // Could be "Guide" or "Traveler"
    private MainFrame mainFrame;

    public ChangePasswordPage(MainFrame mainFrame, String username, String filePath, String userType) {
        this.mainFrame = mainFrame;
        this.username = username;
        this.filePath = filePath;
        this.userType = userType;  // Initialize user type (Guide or Traveler)

        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("profile-bg.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // Center the content

        // Create a panel to hold the form components
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 224, 200)); // Light yellow with transparency
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Change Your Password", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Current Password
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(currentPasswordLabel, gbc);

        JPasswordField currentPasswordField = new JPasswordField();
        currentPasswordField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(currentPasswordField, gbc);

        gbc.gridy++;

        // New Password
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        newPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(newPasswordLabel, gbc);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);

        gbc.gridy++;

        // Confirm New Password
        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        confirmPasswordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        formPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridy++;

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // Change Password Button
        JButton changePasswordButton = new JButton("Update Password");
        styleButton(changePasswordButton);
        changePasswordButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Check if current password is correct
            if (validateCurrentPassword(username, currentPassword)) {
                // Check if new password matches confirmation
                if (newPassword.equals(confirmPassword)) {
                    // Update the password
                    updatePasswordInFile(username, newPassword);
                    JOptionPane.showMessageDialog(this, "Password updated successfully!");
                    mainFrame.showPage("LoginPage");  // Go back to login page after password change
                } else {
                    JOptionPane.showMessageDialog(this, "New passwords do not match.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.");
            }
        });
        buttonPanel.add(changePasswordButton);

        // Back Button
        JButton backButton = new JButton("Back to Profile");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            if (userType.equalsIgnoreCase("Traveler")) {
                String[] userInfo = loadTravelerInfo(username);
                if (userInfo != null) {
                    mainFrame.showTravelerProfilePage(userInfo);
                } else {
                    JOptionPane.showMessageDialog(this, "Error retrieving your profile information.");
                }
            } else if (userType.equalsIgnoreCase("Guide")) {
                String[] userInfo = loadGuideInfo(username);
                if (userInfo != null) {
                    mainFrame.showGuideProfilePage(userInfo);
                } else {
                    JOptionPane.showMessageDialog(this, "Error retrieving your profile information.");
                }
            }
        });
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


    // Method to validate the current password
    private boolean validateCurrentPassword(String username, String currentPassword) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] userInfo = scanner.nextLine().split(",");
                if (userInfo.length > 4 && userInfo[3].equals(username) && userInfo[4].equals(currentPassword)) {  // Assuming password is in the 5th field
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update the password in the file
    private void updatePasswordInFile(String username, String newPassword) {
        File inputFile = new File(filePath);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] userInfo = currentLine.split(",");
                if (userInfo.length > 4 && userInfo[3].equals(username)) {  // Update password
                    userInfo[4] = newPassword;
                    writer.write(String.join(",", userInfo));
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace original file with the updated temp file
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file to original file name.");
            }
        } else {
            System.out.println("Could not delete original file.");
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
