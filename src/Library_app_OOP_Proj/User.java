package Library_app_OOP_Proj;

//User.java
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
 
	protected int maxAdvanceDay;  // จำนวนวันที่จองล่วงหน้าได้
	protected int maxHrs;         // จำนวนชั่วโมงที่จองได้ต่อวัน
	protected int bookedHrs;
	protected String username;
	protected String password;
	protected String userType;
 
	private static ArrayList<User> userList = new ArrayList<>();
 
	static {
		userList = FileHandler.loadUsers();
	}
 
	public User(String username, String password, String userType) {
		if (isUsernameUnique(username)) {
			this.username = username;
			this.password = password;
			this.userType = userType;
			this.bookedHrs = 0;
         
			switch (userType) {
				case "STUDENT":
					this.maxAdvanceDay = 1;  // นักศึกษาจองล่วงหน้าได้ 1 วัน
					this.maxHrs = 3;         // จองได้ 3 ชั่วโมงต่อวัน
					break;
				case "LECTURER":
					this.maxAdvanceDay = 7;  // อาจารย์จองล่วงหน้าได้ 7 วัน
					this.maxHrs = 5;         // จองได้ 5 ชั่วโมงต่อวัน
					break;
				case "GENERAL":
					this.maxAdvanceDay = 1;  // บุคคลทั่วไปจองล่วงหน้าได้ 1 วัน
					this.maxHrs = 3;         // จองได้ 3 ชั่วโมงต่อวัน
					break;
			}
         
			userList.add(this);
			saveToFile();
		} else {
			throw new IllegalArgumentException("Username already exists");
		}
	}

	private boolean isUsernameUnique(String username) {
		return userList.stream().noneMatch(u -> u.getUsername().equals(username));
	}

	public void addBookedHrs(int duration) {
		this.bookedHrs += duration;
		saveToFile();
	}

	public void reduceBookedHrs(int duration) {
		this.bookedHrs -= duration;
		if (this.bookedHrs < 0) this.bookedHrs = 0;
		saveToFile();
	}

	public static void saveToFile() {
		FileHandler.saveUsers(userList);
	}

	// Getters
	public int getMaxAdvanceDay() { 
		return maxAdvanceDay;
	}
	public int getMaxHrs() {
		return maxHrs; 
	}
	public int getBookedHrs() {
		return bookedHrs;
	}
	public String getUsername() {
		return username; 
	}
	public String getUserType() {
		return userType;
	}
	public String getPassword() {
		return password; 
	}
	public static ArrayList<User> getUserList() {
		return userList; 
	}
}
