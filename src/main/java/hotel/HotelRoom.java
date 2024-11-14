package hotel;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HotelRoom {
	
	//private static final Logger LOGGER = LogManager.getLogger(HotelRoom.class);
	private String dbuser = "postgres";
	private String url = "jdbc:postgresql://localhost:5432/hoteldb";
	private String dbpassword = "/Pass@098/";
	
	int _INITIAL_ROOM_NUMBER_ = 0;
	double amount = 300.00;
	private int roomNumber;
	private String prepareDetails;
	int maxId;
	
	
	public HotelRoom() {
		
		this.createRoomNumber(_INITIAL_ROOM_NUMBER_);
		
	}

	public int createRoomNumber( int initialRoomNumber) {
		// TODO Auto-generated method stub
		roomNumber = initialRoomNumber + 1;
		
		if (roomNumber <=0 ) {
			throw new IllegalArgumentException("Negative Room Number: " + initialRoomNumber);
		}
		return roomNumber;
	}

	public String prepareDescription(String roomDescription) {
		// check that the description is not empty or contain or empty spaces
		if (roomDescription.isBlank() || roomDescription == null ) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		
		boolean isNumericOnly = isNumeric(roomDescription);
		if (isNumericOnly  ) {
			throw new IllegalArgumentException("Description cannot be numbers only");
		}
		roomDescription = roomDescription.trim();
		
		//LOGGER.info("Description prepared to be saved to database " + roomDescription);
		
		return roomDescription;
	}
	
	public Connection conn (String url, String dbuser, String dbpassword) {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, dbuser, dbpassword);
		} catch (SQLException e) {
			throw new IllegalAccessError(e.getMessage());
		}
		return conn;
	}

	
	public String publish(String description) {

		String publishedRoom = null;
		Connection connect = conn(this.url, this.dbuser, this.dbpassword);

		String maxRoomIdSql = "SELECT MAX(id) FROM rooms";
		
		try {
			if(connect != null) {
				
	            Statement stmt = connect.createStatement();
	            ResultSet rs = stmt.executeQuery(maxRoomIdSql);
	            if (rs.next()) {
	                maxId = rs.getInt(1);
	            } else {
	                System.out.println("No records found.");
	            }
			}
		} catch (SQLException e) {
			
			throw new IllegalAccessError("ERROR: relation does not exist");
		}
		roomNumber =  this.createRoomNumber(maxId);
		

		String sql = "INSERT INTO rooms (id, room_number, room_description, room_amount) VALUES ((SELECT MAX(id) FROM rooms) + 1, ?, ?, ?)";

		try {

			if (connect != null) {
												
				// Statement statement = conn.createStatement();
				PreparedStatement preparedStatement = connect.prepareStatement(sql);
				preparedStatement.setInt(1, roomNumber);
				preparedStatement.setString(2, description);
				preparedStatement.setDouble(3, this.amount);
				int rowSaved = preparedStatement.executeUpdate();

				if (rowSaved == 1) {

					System.out.println("Record saved to the db");
					prepareDetails = "Room: " + this.roomNumber + ". <br/>" + " Cost: " + this.amount + ". <br/>"
							+ "  Details " + this.prepareDescription(description) + "";

					publishedRoom = prepareDetails;

				} else {
					System.out.println("No record saved to db");
				}

				preparedStatement.close();
				connect.close();

			} 
		} catch (SQLException e) {
			throw new IllegalAccessError("ERROR: relation does not exist");
		}

		return publishedRoom;
	}

	 

	public static boolean isNumeric(String str) {
	    return Pattern.matches("[0-9]+", str);
	}
	
}
