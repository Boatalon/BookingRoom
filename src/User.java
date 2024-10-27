

public class User {
	protected int maxDay;
	protected int maxHrs;
	protected int bookedHrs;
	protected String username;
	protected String password;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		bookedHrs = 0;
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
	

}
