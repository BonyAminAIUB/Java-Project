import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel contentPanel = new JPanel();

    public MainFrame() {
        setTitle("Tour Guide App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPanel.setLayout(cardLayout);

        // Add different panels (pages)
        contentPanel.add(new SelectionPage(this), "SelectionPage");
        contentPanel.add(new GuideRegistrationPage(this), "GuideRegistrationPage");
        contentPanel.add(new TravelerRegistrationPage(this), "TravelerRegistrationPage");
        contentPanel.add(new LoginPage(this), "LoginPage");


        add(contentPanel);
        cardLayout.show(contentPanel, "SelectionPage"); // Show the initial page
    }

    // Method to switch between panels
    public void showPage(String page) {
        cardLayout.show(contentPanel, page);
    }

    // Show guide profile page after successful login
    // public void showGuideProfilePage(String[] userInfo) {
    //     GuideProfilePage guideProfilePage = new GuideProfilePage(this, userInfo);
    //     contentPanel.add(guideProfilePage, "GuideProfilePage");
    //     cardLayout.show(contentPanel, "GuideProfilePage");
    // }

    // Show traveler profile page after successful login
    // public void showTravelerProfilePage(String[] userInfo) {
    //     TravelerProfilePage travelerProfilePage = new TravelerProfilePage(this, userInfo);
    //     contentPanel.add(travelerProfilePage, "TravelerProfilePage");
    //     cardLayout.show(contentPanel, "TravelerProfilePage");
    // }

 // Overloaded method: This version only takes username, and assumes isTraveler is null (optional)
/// Show SeeBookingTravelerPage using CardLayout
public void showSeeBookingTravelerPage(String travelerUsername) {
    SeeBookingTravelerPage seeBookingTravelerPage = new SeeBookingTravelerPage(this, travelerUsername);
    
    // Add the page to the contentPanel if it's not already added
    contentPanel.add(seeBookingTravelerPage, "SeeBookingTravelerPage");
    cardLayout.show(contentPanel, "SeeBookingTravelerPage");
}

// Ensure Back to Profile works via CardLayout
public void showProfilePage(String username, Boolean isTraveler) {
    System.err.println("---");
    System.err.println(username);

    // If isTraveler is explicitly true, load the Traveler profile
    if (isTraveler != null && isTraveler) {
        System.err.println("Traveler Profile");

        String[] userInfo = getUserInfo(username, "TravelersData.txt");
        showTravelerProfilePage(userInfo); // Show traveler profile
    } 
    // If isTraveler is false or null, assume the user is a guide
    else {
        System.err.println("Guide Profile");

        String[] userInfo = getUserInfo(username, "GuidesData.txt");
        showGuideProfilePage(userInfo); // Show guide profile
    }
}

// Example of showing the guide profile using CardLayout
public void showGuideProfilePage(String[] userInfo) {
    GuideProfilePage guideProfilePage = new GuideProfilePage(this, userInfo);
    
    // Add guide profile page to the content panel only if not added yet
    contentPanel.add(guideProfilePage, "GuideProfilePage");
    cardLayout.show(contentPanel, "GuideProfilePage");
}

// Example of showing the traveler profile using CardLayout
public void showTravelerProfilePage(String[] userInfo) {
    TravelerProfilePage travelerProfilePage = new TravelerProfilePage(this, userInfo);
    
    // Add traveler profile page to the content panel only if not added yet
    contentPanel.add(travelerProfilePage, "TravelerProfilePage");
    cardLayout.show(contentPanel, "TravelerProfilePage");
}

    

    // Show hire guide page
    public void showHirePage(String guideUsername,String travelerUsername) {
        HireGuidePage hireGuidePage = new HireGuidePage(this, guideUsername,travelerUsername);
        contentPanel.add(hireGuidePage, "HireGuidePage");
        cardLayout.show(contentPanel, "HireGuidePage");
    }

    // Add this method to MainFrame
    public void showAvailableGuidesPage(String travelerUsername) {
        AvailableGuidesPage availableGuidesPage = new AvailableGuidesPage(this,travelerUsername);
        contentPanel.add(availableGuidesPage, "AvailableGuidesPage");
        cardLayout.show(contentPanel, "AvailableGuidesPage");
    }
  
    public void showSeeBookingGuidePage(String guideUsername) {
        SeeBookingGuidePage seeBookingGuidePage = new SeeBookingGuidePage(this, guideUsername);
        contentPanel.add(seeBookingGuidePage, "SeeBookingGuidePage");
        cardLayout.show(contentPanel, "SeeBookingGuidePage");
    }
    
  
    // public void showSeeBookingTravelerPage(String travelerUsername) {
    //     SeeBookingTravelerPage seeBookingTravelerPage = new SeeBookingTravelerPage(this, travelerUsername);
    //     setContentPane(seeBookingTravelerPage);
    //     revalidate();
    // }
    // Show change password page for guide or traveler
    public void showChangePasswordPage(String username, String filePath,String user) {
        ChangePasswordPage changePasswordPage = new ChangePasswordPage(this, username, filePath,user);
        contentPanel.add(changePasswordPage, "ChangePasswordPage");
        cardLayout.show(contentPanel, "ChangePasswordPage");
    }

    
    

    // Helper method to get user info (You may replace with actual logic)
    private String[] getUserInfo(String username, String filePath) {
        // Fetch user information from the file based on username
        return new String[] { "Name", "Phone", "Email", "Username", "Address" }; // Placeholder
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
