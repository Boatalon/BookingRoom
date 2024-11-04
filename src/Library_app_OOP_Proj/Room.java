package Library_app_OOP_Proj;

//Room.java
import java.util.ArrayList;
import java.util.Arrays;

public class Room {
	private String roomId;
    private ArrayList<TimeSlot> bookings;
    private static ArrayList<Room> roomList = new ArrayList<>();

    static {
        roomList = FileHandler.loadRooms();
    }

    public Room(String roomId) {
        if (isRoomIDAvailable(roomId)) {
            this.roomId = roomId;
            this.bookings = new ArrayList<>();
            roomList.add(this);
            FileHandler.saveRooms(roomList);
        } else {
            throw new IllegalArgumentException("Room ID already exists");
        }
    }
    
    private Room(String roomId, boolean isLoading) {
        this.roomId = roomId;
        this.bookings = new ArrayList<>();
        if (!isLoading) {
            roomList.add(this);
            FileHandler.saveRooms(roomList);
        }
    }

	public static void removeRoom(String roomId) {
	    Room roomToRemove = null;
	    for (Room room : roomList) {
	        if (room.getRoomId().equals(roomId)) {
	            roomToRemove = room;
	            break;
	        }
	    }
	    if (roomToRemove != null) {
	        roomList.remove(roomToRemove);
	        FileHandler.saveRooms(roomList);
	    }
	}

	private boolean isRoomIDAvailable(String roomId) {
        for (Room room : roomList) {
            if (room.roomId.equals(roomId)) {
                return false;
            }
        }
        return true;
    }
	
	public static Room createFromFile(String roomId) {
        Room room = new Room(roomId, true);
        roomList.add(room);
        return room;
    }

	public boolean isAvailable(String date, int startHour, int duration) {
	   TimeSlot slot = findTimeSlot(date);
	   if (slot == null) return true;

	   for (int hour = startHour; hour < startHour + duration; hour++) {
	       if (slot.isTimeBooked(hour)) return false;
	   }
	   return true;
	}

	public void addBooking(String date, int startHour, int duration) {
        TimeSlot slot = findOrCreateTimeSlot(date);
        for (int hour = startHour; hour < startHour + duration; hour++) {
            slot.bookTime(hour);
        }
        FileHandler.saveRooms(roomList);  // บันทึกข้อมูลทันทีที่มีการจอง
    }

	 public void cancelBooking(String date, int startHour, int duration) {
	        TimeSlot slot = findTimeSlot(date);
	        if (slot != null) {
	            for (int hour = startHour; hour < startHour + duration; hour++) {
	                slot.cancelTime(hour);
	            }
	            FileHandler.saveRooms(roomList);  // บันทึกข้อมูลทันทีที่มีการยกเลิก
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

	// TimeSlot inner class
		public static class TimeSlot {
			private String date;
			private boolean[] timeSlots; // 24 ชั่วโมง

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

			public boolean[] getTimeSlots() {  // เพิ่ม getter สำหรับ timeSlots
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


	// Getters
	public String getRoomId() { 
		return roomId; 
	}
	public ArrayList<TimeSlot> getBookings() {
		return bookings; 
	}
	public static ArrayList<Room> getRoomList() {
		return roomList;
	}
	
}
