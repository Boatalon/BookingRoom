import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private User user;
    private Room room;
    private int duration;
    private int start;

    private static final String FILE_PATH = "bookings.txt";
    private static ArrayList<Booking> bookingList = new ArrayList<>();

    // Constructor
    public Booking(User user, Room room, int start, int duration) {
        this.user = user;
        this.room = room;
        this.start = start;
        this.duration = duration;
    }

    // Booking initialization to load data from file
    public static void initialize() {
        bookingList = loadBookings();
    }

    // Add booking with validation and save to file
    public static boolean addBooking(User user, Room room, int start, int duration) {
        if (duration <= user.getMaxHrs()) {
            if (room.getAvailable(start, duration)) {
                Booking newBooking = new Booking(user, room, start, duration);
                
                // Update room and user booking status
                for (int i = 0; i < duration; i++) {
                    room.setBooked(start + i);
                }
                user.setBookedHrs(duration);
                bookingList.add(newBooking);

                saveBookings();
                JOptionPane.showMessageDialog(null, "Booking success: " + newBooking);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Room not available for the selected time.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid duration. Exceeds user's maximum booking hours.");
        }
        return false;
    }

    @Override
    public String toString() {
        return "Room " + room.toString() + " from " + start + ":00 for " + duration + " hour(s) by " + user.getUsername();
    }

    private static void saveBookings() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(bookingList);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving bookings: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Booking> loadBookings() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ArrayList<Booking>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading bookings: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Method to return current bookings list for displaying or other purposes
    public static List<Booking> getBookingList() {
        return bookingList;
    }
}