package hotel;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class HotelRoomApp implements Callable<Void> {

	@Option(names = { "--dbName" }, description = "Database name")
	private String dbName = System.getenv("DB_USER");

	@Option(names = { "--dbHost" }, description = "Database Host Url")
	private String dbHost = System.getenv("DB_URL");

	@Option(names = { "--dbPassword" }, description = "Database Password")
	private String dbPassword = System.getenv("DB_PASSWORD");

	RoomPostgresRepository roomRepository;

	public static void main(String[] args) {
		new CommandLine(new HotelRoomApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {

		EventQueue.invokeLater(() -> {

			try {

				roomRepository = new RoomPostgresRepository(dbHost, dbName, dbPassword);
				HotelRoomView hotelRoomView = new HotelRoomView();
				RoomController roomController = new RoomController(hotelRoomView, roomRepository);
				hotelRoomView.setRoomController(roomController);
				hotelRoomView.setVisible(true);
				roomController.allRooms();

			} catch (Exception e) {
				
			}
		});
		return null;
	}

}
