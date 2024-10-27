import java.util.ArrayList;

public class Room {
	private String roomID;
	private boolean available[];
	
	private static ArrayList<Room> roomList = new ArrayList<Room>();
	
	public Room(String roomID) {
		boolean check = true;
		if(!roomList.isEmpty()) {
			for(Room r : roomList) {
				if(r.getRoomID().equals(roomID))
					check = false;
			}
		}
		
		if(check) {
			this.roomID = roomID;
			available = new boolean[10];
			
			for(int i=0;i<10;i++) {
				available[i] = true;
			}
			roomList.add(this);
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

	public static ArrayList<Room> getRoomList() {
		return roomList;
	}
	
	public static void removeRoom(String roomID) {
		for(int i=0; i<roomList.size(); i++) {
			if(roomList.get(i).getRoomID().equals(roomID)) {
				roomList.remove(i);
			}
		}
	}
	
	@Override
	public String toString() {
		return this.getRoomID();
	}
}
