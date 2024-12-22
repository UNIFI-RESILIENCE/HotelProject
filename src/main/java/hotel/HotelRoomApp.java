package hotel;

import java.awt.EventQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.core.tools.picocli.CommandLine.Command;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class HotelRoomApp implements Callable<Void>{
	
	@Option(names = {"--dbName"} , description = "Database name")
	private String dbName = "";
	
	@Option (names = {"--dbHost"} , description = "Database Host Url" )
	private String dbHost = "";
	
	@Option(names = {"--dbPassword" }, description = "Database Password" )
	private String dbPassword = "";
	
	RoomPostgresRepository roomRepository;
	
	public static void main (String[] args) {
		new CommandLine(new HotelRoomApp()).execute(args);
	}
	@Override
	public Void call() throws Exception {
		// TODO Auto-generated method stub

		EventQueue.invokeLater(() -> {
		
			try {
				
				
//				if (args.length > 0 ) {
//					System.err.println("using argument for db");
//					String dbName = args[0];
//					String dbHost = args[1];
//					String dbPassword = args[2];
//					roomRepository = new RoomPostgresRepository(dbHost,dbName, dbPassword);
//				}else {
//					roomRepository = new RoomPostgresRepository();
//				}
				roomRepository = new RoomPostgresRepository(dbHost,dbName, dbPassword);
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
		return null;
	}
	
	
}

