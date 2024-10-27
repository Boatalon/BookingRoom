

public class Room {
	private String roomID;
	private boolean available[];
	
	public Room(String roomID) {
		this.roomID = roomID;
		available = new boolean[10];
		
		for(int i=0;i<10;i++) {
			available[i] = true;
		}
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	
	public boolean getAvailable(int index, int duration) {
		for(int i=0; i<duration; i++) {
			if(available[index+i] == false)
				return false;
		}
		return true;
	}
	
	public void setBooked(int index) {
		available[index] = false;
	}
	
}
