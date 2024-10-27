
import java.util.ArrayList;

public class Booking {
	private User user;
	private Room room;
	private int duration;
	private int start;
	
	private static ArrayList<Booking> bookingList = new ArrayList<Booking>();

	public Booking(User user, Room room,int start,int duration) {
		if(duration <= user.getMaxHrs()) {
			if(room.getAvailable(start, duration)) {
				this.user = user;
				this.room = room;
				this.start = start;
				this.duration = duration;
				
				for(int i=0; i<duration; i++) {
					room.setBooked(start+i);
				}
				user.setBookedHrs(duration);
				bookingList.add(this);
				System.out.println("Booking success");
			}else {
				System.out.println("Room not available");
			}
		}else {
			System.out.println("Invalid duration");
		}
	}
	
	
	
}
