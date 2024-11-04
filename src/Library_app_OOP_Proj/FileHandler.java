package Library_app_OOP_Proj;

//FileHandler.java
import java.io.*;
import java.util.*;

import Library_app_OOP_Proj.Room.TimeSlot;

import java.nio.file.*;

public class FileHandler {
	private static final String DATA_DIR = "data";
	private static final String ROOMS_FILE = DATA_DIR + "/rooms.txt";
	private static final String BOOKINGS_FILE = DATA_DIR + "/bookings.txt";
	private static final String USERS_FILE = DATA_DIR + "/users.txt";
	private static final String ADMINS_FILE = DATA_DIR + "/admins.txt";

	// สร้างโฟลเดอร์และไฟล์เริ่มต้น
	static {
		try {
			// สร้างโฟลเดอร์ data
			Files.createDirectories(Paths.get(DATA_DIR));
         
			// สร้างไฟล์ถ้ายังไม่มี
			createFileIfNotExists(ROOMS_FILE);
			createFileIfNotExists(BOOKINGS_FILE);
			createFileIfNotExists(USERS_FILE);
			createFileIfNotExists(ADMINS_FILE);
         
		} catch (IOException e) {
			System.err.println("Error initializing data files: " + e.getMessage());
		}
	}

	// utility method สำหรับสร้างไฟล์
	private static void createFileIfNotExists(String filePath) throws IOException {
    	File file = new File(filePath);
    	if (!file.exists()) {
    		file.createNewFile();
    		// เขียนข้อมูลเริ่มต้นถ้าเป็นไฟล์ admins (สร้าง admin เริ่มต้น)
    		if (filePath.equals(ADMINS_FILE)) {
    			try (PrintWriter writer = new PrintWriter(file)) {
    				writer.println("admin,admin123");
    			}
    		}
    	}
	}

	// บันทึกข้อมูลห้อง
	public static void saveRooms(ArrayList<Room> rooms) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(ROOMS_FILE))) {
			for (Room room : rooms) {
				writer.println(room.getRoomID());
				for (TimeSlot slot : room.getBookings()) {
					writer.println(slot.getDate() + "," + 
                              Arrays.toString(slot.getTimeSlots()));
				}
				writer.println("---");
			}
		} catch (IOException e) {
			System.err.println("Error saving rooms: " + e.getMessage());
		}
	}

	// โหลดข้อมูลห้อง
	public static ArrayList<Room> loadRooms() {
		ArrayList<Room> rooms = new ArrayList<>();
		try {
			// สร้างไฟล์ถ้ายังไม่มี
			createFileIfNotExists(ROOMS_FILE);
         
			try (BufferedReader reader = new BufferedReader(new FileReader(ROOMS_FILE))) {
				String line;
				Room currentRoom = null;
				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty()) continue;  // ข้ามบรรทัดว่าง
                 
					if (line.equals("---")) {
						currentRoom = null;
						continue;
					}
					if (currentRoom == null) {
						currentRoom = new Room(line.trim());
						rooms.add(currentRoom);
					} else {
						String[] parts = line.split(",");
						String date = parts[0];
                    	boolean[] slots = parseTimeSlots(parts[1]);
                    	currentRoom.addTimeSlot(date, slots);
                	}
				}
			}
		} catch (IOException e) {
			System.err.println("Error loading rooms: " + e.getMessage());
		}
		return rooms;
	}

	// บันทึกข้อมูลการจอง
	public static void saveBookings(ArrayList<Booking> bookings) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
			for (Booking booking : bookings) {
				writer.println(String.format("%s,%s,%s,%d,%d,%s", booking.getUser().getUsername(), booking.getRoom().getRoomID(), booking.getDate(), booking.getStart(), booking.getDuration(), booking.getStatus()));
			}
		} catch (IOException e) {
			System.err.println("Error saving bookings: " + e.getMessage());
		}
	}

	// โหลดข้อมูลการจอง
	public static ArrayList<Booking> loadBookings() {
		ArrayList<Booking> bookings = new ArrayList<>();
		try {
			createFileIfNotExists(BOOKINGS_FILE);
         
			try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty()) continue;
                 
					String[] parts = line.split(",");
					User user = findUser(parts[0]);
					Room room = findRoom(parts[1]);
					if (user != null && room != null) {
						new Booking(user, room, parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error loading bookings: " + e.getMessage());
		}
		return bookings;
	}

	// บันทึกข้อมูลผู้ใช้
	public static void saveUsers(ArrayList<User> users) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
			for (User user : users) {
				writer.println(String.format("%s,%s,%s,%d,%d", user.getUsername(), user.getPassword(), user.getUserType(), user.getMaxAdvanceDay(), user.getMaxHrs()));
			}
		} catch (IOException e) {
			System.err.println("Error saving users: " + e.getMessage());
		}
	}

	// โหลดข้อมูลผู้ใช้
	public static ArrayList<User> loadUsers() {
		ArrayList<User> users = new ArrayList<>();
		try {
			createFileIfNotExists(USERS_FILE);
         
			try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty()) continue;
                 
					String[] parts = line.split(",");
					switch (parts[2]) {
						case "STUDENT":
							users.add(new Student(parts[0], parts[1]));
							break;
						case "LECTURER":
                        	users.add(new Lecturer(parts[0], parts[1]));
                        	break;
						case "GENERAL":
							users.add(new General(parts[0], parts[1]));
							break;
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error loading users: " + e.getMessage());
		}
		return users;
	}

	// บันทึกข้อมูล admin
	public static void saveAdmins(ArrayList<Admin> admins) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(ADMINS_FILE))) {
			for (Admin admin : admins) {
				writer.println(admin.getUsername() + "," + admin.getPassword());
			}
		} catch (IOException e) {
			System.err.println("Error saving admins: " + e.getMessage());
		}
	}

	// โหลดข้อมูล admin
	public static ArrayList<Admin> loadAdmins() {
		ArrayList<Admin> admins = new ArrayList<>();
    	try {
    		createFileIfNotExists(ADMINS_FILE);
         
    		try (BufferedReader reader = new BufferedReader(new FileReader(ADMINS_FILE))) {
    			String line;
    			while ((line = reader.readLine()) != null) {
    				if (line.trim().isEmpty()) continue;
                 
    				String[] parts = line.split(",");
    				admins.add(new Admin(parts[0], parts[1]));
    			}
    		}
    	} catch (IOException e) {
    		System.err.println("Error loading admins: " + e.getMessage());
    	}
    	return admins;
	}

	// Utility methods
	private static boolean[] parseTimeSlots(String str) {
		String[] parts = str.substring(1, str.length() - 1).split(", ");
		boolean[] slots = new boolean[24];
		for (int i = 0; i < parts.length; i++) {
			slots[i] = Boolean.parseBoolean(parts[i]);
		}
		return slots;
	}

	private static User findUser(String username) {
		return User.getUserList().stream()
				.filter(u -> u.getUsername().equals(username))
				.findFirst()
				.orElse(null);
	}

	private static Room findRoom(String roomId) {
		return Room.getRoomList().stream()
				.filter(r -> r.getRoomID().equals(roomId))
				.findFirst()
				.orElse(null);
	}
}