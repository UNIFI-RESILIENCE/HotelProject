package hotel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.EventQueue;
import java.util.List;

public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		
		LOGGER.info("App started");
		EventQueue.invokeLater(() -> {
			try {
		RoomPostgresRepository roomRepository = new RoomPostgresRepository();
		HotelRoomView hotelRoomView =  new HotelRoomView();
		RoomController roomController = new RoomController(hotelRoomView, roomRepository);
		hotelRoomView.setRoomController(roomController);
		hotelRoomView.setVisible(true);
		roomController.allRooms();
		
		} catch (Exception e) {
			e.printStackTrace();
			}
		});

		LOGGER.info("App terminated");
			 
	}

}
