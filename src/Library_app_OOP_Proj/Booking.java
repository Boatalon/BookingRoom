package Library_app_OOP_Proj;


//Booking.java
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

public class Booking {
	private User user;
	private Room room;
	private String date;
	private int start;
	private int duration;
	private String status;
 
	private static ArrayList<Booking> bookingList = new ArrayList<>();
 
	static {
		bookingList = FileHandler.loadBookings();
	}

	public Booking(User user, Room room, String date, int start, int duration) {
		if (validateBooking(user, room, date, start, duration)) {
			this.user = user;
			this.room = room;
			this.date = date;
			this.start = start;
			this.duration = duration;
			this.status = "ACTIVE";
         
			room.addBooking(date, start, duration);
			user.addBookedHrs(duration);
			bookingList.add(this);
			saveToFile();
		}
	}

	private boolean validateBooking(User user, Room room, String date, int start, int duration) {
		// ตรวจสอบจำนวนวันที่จองล่วงหน้า
		LocalDate bookingDate = LocalDate.parse(date);
		LocalDate today = LocalDate.now();
		long daysInAdvance = ChronoUnit.DAYS.between(today, bookingDate);
     
		if (daysInAdvance < 0) {
			throw new IllegalArgumentException("Cannot book for past dates");
		}
     
		if (daysInAdvance > user.getMaxAdvanceDay()) {
			throw new IllegalArgumentException("Cannot book more than " + 
					user.getMaxAdvanceDay() + " days in advance");
		}

		// ตรวจสอบจำนวนชั่วโมงต่อวัน
		if (duration > user.getMaxHrs()) {
			throw new IllegalArgumentException("Cannot book more than " + 
					user.getMaxHrs() + " hours per day");
		}

		// ตรวจสอบจำนวนชั่วโมงที่จองในวันนั้น
		int bookedHoursForDay = 0;
		for (Booking b : bookingList) {
			if (b.getUser().equals(user) && 
					b.getDate().equals(date) && 
					b.getStatus().equals("ACTIVE")) {
				bookedHoursForDay += b.getDuration();
			}
		}
     
		if (bookedHoursForDay + duration > user.getMaxHrs()) {
			throw new IllegalArgumentException("Would exceed maximum hours per day");
		}

		// ตรวจสอบว่าห้องว่างไหม
		if (!room.isAvailable(date, start, duration)) {
			throw new IllegalArgumentException("Room not available for selected time");
		}

		return true;
	}

	public void cancelBooking() {
		if (status.equals("ACTIVE")) {
			status = "CANCELLED";
			room.cancelBooking(date, start, duration);
			user.reduceBookedHrs(duration);
			saveToFile();
		}
	}

	public static void saveToFile() {
			FileHandler.saveBookings(bookingList);
	}

	public static ArrayList<Booking> getBookingsByDate(String date) {
		ArrayList<Booking> dateBookings = new ArrayList<>();
		for (Booking b : bookingList) {
			if (b.getDate().equals(date) && b.getStatus().equals("ACTIVE")) {
				dateBookings.add(b);
			}
		}
		return dateBookings;
	}

	public static ArrayList<Booking> getActiveBookingsByRoom(Room room) {
		ArrayList<Booking> result = new ArrayList<>();
		for (Booking b : bookingList) {
			if (b.room.equals(room) && b.status.equals("ACTIVE")) {
				result.add(b);
			}
		}
		return result;
	}

	public static ArrayList<Booking> getActiveBookingsByUser(User user) {
		ArrayList<Booking> result = new ArrayList<>();
		for (Booking b : bookingList) {
			if (b.user.equals(user) && b.status.equals("ACTIVE")) {
				result.add(b);
			}
		}	
		return result;
	}

	// Getters
	public User getUser() {
		return user;
	}
	public Room getRoom() {
		return room; 
	}
	public String getDate() {
		return date; 
	}
	public int getStart() {
		return start; 
	}
	public int getDuration() {
		return duration;
	}
	public String getStatus() { 
		return status; 
	}
	public static ArrayList<Booking> getBookingList() {
		return bookingList; 
	}

}
