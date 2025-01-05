package hotel;

import java.awt.EventQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {

		LOGGER.info("App started");
		EventQueue.invokeLater(() -> {

			try {

				RoomPostgresRepository roomRepository;
				if (args.length > 0) {
					System.err.println("using argument for db");
					String dbName = args[0];
					String dbHost = args[1];
					String dbPassword = args[2];
					roomRepository = new RoomPostgresRepository(dbHost, dbName, dbPassword);
				} else {
					roomRepository = new RoomPostgresRepository();
				}
				HotelRoomView hotelRoomView = new HotelRoomView();
				RoomController roomController = new RoomController(hotelRoomView, roomRepository);
				hotelRoomView.setRoomController(roomController);
				hotelRoomView.setVisible(true);
				roomController.allRooms();
				System.err.println("using default for db");

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		LOGGER.info("App terminated");

	}

}
