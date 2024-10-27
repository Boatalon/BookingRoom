
public class Admin {
	private String name;
	private String password;
	
	public Admin(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public void addRoom(String roomID) {
		new Room(roomID);
	}
	
	public void removeRoom(String roomID) {
		Room.removeRoom(roomID);
	}
	
	public void showRoom() {
		for(Room r : Room.getRoomList()) {
			System.out.print(r.getRoomID()+" ");
		}
		System.out.println("");
	}
	
}
