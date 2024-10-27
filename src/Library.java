

public class Library {

	public static void main(String[] args) {
		User kuyPoos = new Lecterer("B1234","inwza007");
		Room S2206 = new Room("S2206");
		
		new Booking(kuyPoos,S2206,0,2);
		new Booking(kuyPoos,S2206,2,1);

	}

}
