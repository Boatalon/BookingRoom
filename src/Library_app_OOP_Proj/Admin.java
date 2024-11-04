package Library_app_OOP_Proj;

//Admin.java
import java.util.ArrayList;

public class Admin {
	private String username;
	private String password;
	private static ArrayList<Admin> adminList = new ArrayList<>();
 
	static {
		adminList = FileHandler.loadAdmins();
	}
 
	public Admin(String username, String password) {
		if (isUsernameUnique(username)) {
			this.username = username;
			this.password = password;
			adminList.add(this);
			saveToFile();
		} else {
			throw new IllegalArgumentException("Admin username already exists");
   	 	}
	}

	private boolean isUsernameUnique(String username) {
		return adminList.stream().noneMatch(a -> a.getUsername().equals(username));
	}

	public void addRoom(String roomID) {
		new Room(roomID);
	}
 
	public void removeRoom(String roomID) {
		Room.removeRoom(roomID);
	}

	public void cancelBooking(String roomID, String date, int startTime) {
		for (Booking b : Booking.getBookingList()) {
			if (b.getRoom().getRoomID().equals(roomID) && 
				b.getDate().equals(date) && 
				b.getStart() == startTime) {
				b.cancelBooking();
				break;
			}
		}
	}

	public static void saveToFile() {
		FileHandler.saveAdmins(adminList);
	}

	// Getters
	public String getUsername() {
		return username; 
	}
	public String getPassword() {
		return password; 
	}
	public static ArrayList<Admin> getAdminList() {
		return adminList; 
	}

	
}