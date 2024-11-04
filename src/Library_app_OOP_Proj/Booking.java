package Library_app_OOP_Proj;

//Booking.java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Booking {
 private User user;
 private Room room;
 private String date;
 private int startHour;
 private int duration;
 private String status;
 private static ArrayList<Booking> bookingList = new ArrayList<>();

 static {
     bookingList = FileHandler.loadBookings();
 }

 public Booking(User user, Room room, String date, int startHour, int duration) {
     validateBooking(user, room, date, startHour, duration);
     
     this.user = user;
     this.room = room;
     this.date = date;
     this.startHour = startHour;
     this.duration = duration;
     this.status = "ACTIVE";

     room.addBooking(date, startHour, duration);
     bookingList.add(this);
     FileHandler.saveBookings(bookingList);
 }

 private void validateBooking(User user, Room room, String date, int startHour, int duration) {
     // ตรวจสอบวันที่จอง
     LocalDate bookingDate = LocalDate.parse(date);
     LocalDate today = LocalDate.now();
     long daysAhead = ChronoUnit.DAYS.between(today, bookingDate);
     
     if (daysAhead < 0) {
         throw new IllegalArgumentException("Cannot book past dates");
     }
     
     if (daysAhead > user.getMaxAdvanceDays()) {
         throw new IllegalArgumentException(
             "Cannot book more than " + user.getMaxAdvanceDays() + " days ahead"
         );
     }

     // ตรวจสอบจำนวนชั่วโมงที่จอง
     if (duration > user.getMaxHours()) {
         throw new IllegalArgumentException(
             "Cannot book more than " + user.getMaxHours() + " hours"
         );
     }

     // ตรวจสอบจำนวนชั่วโมงรวมในวันนั้น
     int totalHours = getTotalBookedHours(user, date);
     if (totalHours + duration > user.getMaxHours()) {
         throw new IllegalArgumentException(
             "Would exceed maximum " + user.getMaxHours() + " hours per day"
         );
     }

     // ตรวจสอบว่าห้องว่าง
     if (!room.isAvailable(date, startHour, duration)) {
         throw new IllegalArgumentException("Room is not available for selected time");
     }
 }

 private int getTotalBookedHours(User user, String date) {
     return bookingList.stream()
         .filter(b -> b.user.equals(user) && 
                     b.date.equals(date) && 
                     b.status.equals("ACTIVE"))
         .mapToInt(b -> b.duration)
         .sum();
 }

 public void cancel() {
     if (status.equals("ACTIVE")) {
         status = "CANCELLED";
         room.cancelBooking(date, startHour, duration);
         FileHandler.saveBookings(bookingList);
     }
 }

 public static ArrayList<Booking> getActiveBookings() {
     ArrayList<Booking> active = new ArrayList<>();
     for (Booking b : bookingList) {
         if (b.status.equals("ACTIVE")) {
             active.add(b);
         }
     }
     return active;
 }

 public static ArrayList<Booking> getBookingsByDate(String date) {
     ArrayList<Booking> dateBookings = new ArrayList<>();
     for (Booking b : bookingList) {
         if (b.date.equals(date) && b.status.equals("ACTIVE")) {
             dateBookings.add(b);
         }
     }
     return dateBookings;
 }

 // Getters
 public User getUser() { return user; }
 public Room getRoom() { return room; }
 public String getDate() { return date; }
 public int getStartHour() { return startHour; }
 public int getDuration() { return duration; }
 public String getStatus() { return status; }
 public static ArrayList<Booking> getBookingList() { return bookingList; }


}
