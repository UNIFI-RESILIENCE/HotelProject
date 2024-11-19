package hotel;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Main {
	
	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LOGGER.info("App started");
		  
		HotelDao room = new HotelDao();
		
		try {
			room.publish("saved from main");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("App terminated");
	}

}
 