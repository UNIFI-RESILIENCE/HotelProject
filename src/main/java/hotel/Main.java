package hotel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		
		HotelRoomView roomGUI = new HotelRoomView();
		roomGUI.setVisible(true);
		
		LOGGER.info("App started");
 
		//HotelDao room = new HotelDao();

		// try {
		// 	room.publish("saved from main");
		// } catch (SQLException e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }
		
		RoomPostgresRepository repository = new RoomPostgresRepository();
		// Save a room
		Room room = new Room("200", "John Doe");
		repository.save(room);
		
		// Retrieve a room
		Room retrievedRoom = repository.findById("1");
		System.out.println(retrievedRoom);
		
		// Get all students
		List<Room> allRooms = repository.findAll();
		System.out.println(allRooms);
		
		// Delete a room
		repository.delete("1");
		LOGGER.info("App terminated");
		
		
		 
	}

}
