import java.util.ArrayList;

public class User {
	protected int maxDay;
	protected int maxHrs;
	protected int bookedHrs;
	protected String username;
	protected String password;
	
	private static ArrayList<User> userList = new ArrayList<User>();
	
	public User(String username, String password) {
		boolean check = true;
		if(!userList.isEmpty()) {
			for(User u : userList) {
				if(u.getUsername().equals(username)) {
					System.out.println("this username already used !!");
					check = false;
				}
			}
		}
		if(check) {
				this.username = username;
				this.password = password;
				bookedHrs = 0;
				userList.add(this);
				System.out.println("Sign Up success !!");
		}
	}
	
	public int getMaxDay() {
		return maxDay;
	}
	public void setMaxDay(int maxDay) {
		this.maxDay = maxDay;
	}
	public int getMaxHrs() {
		return maxHrs;
	}
	public void setMaxHrs(int maxHrs) {
		this.maxHrs = maxHrs;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}

	public int getBookedHrs() {
		return bookedHrs;
	}

	public void setBookedHrs(int duration) {
		this.bookedHrs += duration;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static ArrayList<User> getUserList() {
		return userList;
	}

	public static void setUserList(ArrayList<User> userList) {
		User.userList = userList;
	}
	
}
