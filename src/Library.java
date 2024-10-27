

public class Library {

	public static void main(String[] args) {	
		User p1 = new Student("possza","1234");
		Admin A1 = new Admin("A111","inwza007");
		
		A1.addRoom("CHAO NI MA");
		A1.addRoom("ABC");
		A1.addRoom("123");
				
		A1.showRoom();
		new Booking(p1,Room.getRoomList().get(0),1,2);
		
		for(Booking B : Booking.getBookingList()) {
			System.out.println(B);
		}
	}

}
