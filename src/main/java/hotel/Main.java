package hotel;

import java.sql.SQLException;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
public class Main {
	
	//private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		LOGGER.info("App started");
		//HotelRoom room = new HotelRoom();
//		String roomDescription = room.prepareDescription("new room at 109");
		
		//room.publish("saved from main");
		 
//		LOGGER.info("App terminated");
		  
		HotelDao room = new HotelDao();
		
		try {
			room.publish("saved from main");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
 