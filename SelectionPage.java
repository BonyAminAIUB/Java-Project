import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectionPage extends JPanel {

    public SelectionPage(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Set the background image
        ImageIcon backgroundImageIcon = new ImageIcon("travel.jpg"); // Replace with your image file
        Image backgroundImage = backgroundImageIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(new GridBagLayout()); // To center the contentPanel

        // Create a panel to hold the text and buttons
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Make it transparent to show background image

        // Add some text (header and description)
        JLabel headerLabel = new JLabel("Welcome to our Travel App!");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea descriptionText = new JTextArea(
                "Explore new destinations and connect with expert guides to make your trips unforgettable. " +
                "Whether you are a seasoned traveler or just starting your journey, we are here to make it easier and more enjoyable!"
        );
        descriptionText.setFont(new Font("Arial", Font.PLAIN, 18));
        descriptionText.setForeground(Color.WHITE);
        descriptionText.setOpaque(false);
        descriptionText.setEditable(false);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionText.setMaximumSize(new Dimension(600, 200));

        // Create buttons
        JButton registerGuideButton = new JButton("Register as Guide");
        styleButton(registerGuideButton);
        registerGuideButton.addActionListener(e -> mainFrame.showPage("GuideRegistrationPage"));

        JButton registerTravelerButton = new JButton("Register as Traveler");
        styleButton(registerTravelerButton);
        registerTravelerButton.addActionListener(e -> mainFrame.showPage("TravelerRegistrationPage"));

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> mainFrame.showPage("LoginPage"));

        // Add components to contentPanel
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(headerLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(descriptionText);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(registerGuideButton);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(registerTravelerButton);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(loginButton);
        contentPanel.add(Box.createVerticalGlue());

        // Add contentPanel to backgroundPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(contentPanel, gbc);

        // Add backgroundPanel to main panel
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(400, 50));
        button.setPreferredSize(new Dimension(400, 50));

        // Add mouse hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(64, 165, 120)); // A shade of green
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        });
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
