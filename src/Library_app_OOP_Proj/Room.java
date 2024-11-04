package Library_app_OOP_Proj;

//Room.java
import java.util.ArrayList;
import java.util.Arrays;

public class Room {
	private String roomID;
	private ArrayList<TimeSlot> bookings;
	private static ArrayList<Room> roomList = new ArrayList<>();
 
	static {
		roomList = FileHandler.loadRooms();
	}

	public Room(String roomID) {
		if (isRoomIDUnique(roomID)) {
			this.roomID = roomID;
			this.bookings = new ArrayList<>();
			roomList.add(this);
			saveToFile();
		} else {
			throw new IllegalArgumentException("Room ID already exists");
		}
	}

	private boolean isRoomIDUnique(String roomID) {
		return roomList.stream().noneMatch(r -> r.getRoomID().equals(roomID));
	}

	public static void removeRoom(String roomID) {
		roomList.removeIf(r -> r.getRoomID().equals(roomID));
		saveToFile();
	}

	public boolean isAvailable(String date, int start, int duration) {
		TimeSlot targetSlot = findTimeSlot(date);
		if (targetSlot == null) return true;
     
		for (int i = 0; i < duration; i++) {
			if (targetSlot.isTimeBooked(start + i)) {
				return false;
			}
		}
		return true;
	}

	public void addBooking(String date, int start, int duration) {
		TimeSlot slot = findOrCreateTimeSlot(date);
		for (int i = 0; i < duration; i++) {
			slot.bookTime(start + i);
		}
		saveToFile();
	}

	public void cancelBooking(String date, int start, int duration) {
		TimeSlot slot = findTimeSlot(date);
		if (slot != null) {
			for (int i = 0; i < duration; i++) {
				slot.cancelTime(start + i);
			}
			saveToFile();
		}
	}

	private TimeSlot findTimeSlot(String date) {
		for (TimeSlot slot : bookings) {
			if (slot.getDate().equals(date)) {
				return slot;
			}
		}
		return null;
	}

	private TimeSlot findOrCreateTimeSlot(String date) {
		TimeSlot slot = findTimeSlot(date);
		if (slot == null) {
			slot = new TimeSlot(date);
			bookings.add(slot);
		}
		return slot;
	}

	public void addTimeSlot(String date, boolean[] slots) {
		TimeSlot slot = new TimeSlot(date, slots);
		bookings.add(slot);
	}

	public static void saveToFile() {
		FileHandler.saveRooms(roomList);
	}

	public String getRoomID() {
		return roomID;
	}
	public ArrayList<TimeSlot> getBookings() {
		return bookings; 
	}
	public static ArrayList<Room> getRoomList() {
		return roomList; 
	}

	// TimeSlot inner class
	public static class TimeSlot {
		private String date;
		private boolean[] timeSlots;

		public TimeSlot(String date) {
			this.date = date;
			this.timeSlots = new boolean[24];
        	Arrays.fill(timeSlots, false);
		}

		public TimeSlot(String date, boolean[] slots) {
			this.date = date;
			this.timeSlots = slots;
		}

		public String getDate() {	
			return date;
		}
		public boolean[] getTimeSlots() {
			return timeSlots; 
		}
		public boolean isTimeBooked(int hour) {
			return timeSlots[hour]; 
		}
		public void bookTime(int hour) {
			timeSlots[hour] = true; 
		}
		public void cancelTime(int hour) { 
			timeSlots[hour] = false; 
		}
	}


}