package Library_app_OOP_Proj;

//Main.java
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Main {
	 private static void validateData(ArrayList<Admin> admins, ArrayList<User> users, ArrayList<Room> rooms, ArrayList<Booking> bookings) {
	     // ตรวจสอบการจองที่หมดอายุ
	     LocalDate today = LocalDate.now();
	     bookings.removeIf(booking -> {
	         LocalDate bookingDate = LocalDate.parse(booking.getDate());
	         return bookingDate.isBefore(today);
	     });

	     // ตรวจสอบการจองที่อ้างอิงข้อมูลที่ถูกลบ
	     bookings.removeIf(booking -> 
	         !rooms.contains(booking.getRoom()) || !users.contains(booking.getUser())
	     );

	     // บันทึกข้อมูลที่ทำความสะอาดแล้ว
	     Booking.saveToFile();
	     Room.saveToFile();
	     User.saveToFile();
	     Admin.saveToFile();
	}
	 
	public static void main(String[] args) {
		try {
			// สร้างโฟลเดอร์เก็บข้อมูล
			new File("data").mkdirs();

			// โหลดข้อมูลทั้งหมด
			ArrayList<Admin> admins = Admin.getAdminList();
        	ArrayList<User> users = User.getUserList();
        	ArrayList<Room> rooms = Room.getRoomList();
        	ArrayList<Booking> bookings = Booking.getBookingList();

        	// ตรวจสอบและทำความสะอาดข้อมูล
        	validateData(admins, users, rooms, bookings);
         
        	// ถ้ายังไม่มี admin ให้สร้าง admin เริ่มต้น
        	if (admins.isEmpty()) {
        		new Admin("admin", "admin123");
        	}
         
        	// เริ่มโปรแกรม
        	new LoginPage().setVisible(true);
         
		} catch (Exception e) {
			System.err.println("Error starting application: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error starting application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

}
