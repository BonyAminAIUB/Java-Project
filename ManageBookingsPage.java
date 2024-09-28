// package TourGuide;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ManageBookingsPage extends JPanel {
    private String guideUsername;
    private MainFrame mainFrame;

    public ManageBookingsPage(MainFrame mainFrame, String guideUsername) {
        this.mainFrame = mainFrame;
        this.guideUsername = guideUsername;

        setLayout(new BorderLayout());

        JTextArea bookingsArea = new JTextArea(20, 40);
        bookingsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingsArea);

        // Load bookings for the guide from file and display them
        ArrayList<String> bookings = loadBookingsForGuide(guideUsername);
        for (String booking : bookings) {
            bookingsArea.append(booking + "\n");
        }

        add(new JLabel("Manage Bookings"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button to accept booking
        JButton acceptButton = new JButton("Accept Booking");
        acceptButton.addActionListener(e -> {
            String selectedDate = JOptionPane.showInputDialog("Enter the date of the booking to accept:");
            if (selectedDate != null && !selectedDate.isEmpty()) {
                manageBooking(selectedDate, "accepted");
            }
        });
        add(acceptButton, BorderLayout.SOUTH);

        // Button to reject booking
        JButton rejectButton = new JButton("Reject Booking");
        rejectButton.addActionListener(e -> {
            String selectedDate = JOptionPane.showInputDialog("Enter the date of the booking to reject:");
            if (selectedDate != null && !selectedDate.isEmpty()) {
                manageBooking(selectedDate, "rejected");
            }
        });
        add(rejectButton, BorderLayout.SOUTH);
    }

    // Load bookings for a guide from the file
    private ArrayList<String> loadBookingsForGuide(String guideUsername) {
        ArrayList<String> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Bookings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingInfo = line.split(",");
                if (bookingInfo[0].equals(guideUsername)) {
                    bookings.add("Date: " + bookingInfo[1] + ", Time: " + bookingInfo[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Accept or reject a booking based on the date
    private void manageBooking(String date, String action) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Bookings.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("Bookings_temp.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookingInfo = line.split(",");
                if (bookingInfo[0].equals(guideUsername) && bookingInfo[1].equals(date)) {
                    writer.write(bookingInfo[0] + "," + bookingInfo[1] + "," + bookingInfo[2] + "," + action);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            // Rename the temp file to replace the original file
            File originalFile = new File("Bookings.txt");
            File tempFile = new File("Bookings_temp.txt");
            if (!originalFile.delete()) {
                System.out.println("Could not delete original file.");
            }
            if (!tempFile.renameTo(originalFile)) {
                System.out.println("Could not rename temporary file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
