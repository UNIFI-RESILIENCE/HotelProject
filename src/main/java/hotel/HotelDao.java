package hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class HotelDao {

    static final String MAX_ROOM_ID_SQL = "SELECT MAX(id) FROM rooms";
    static final String INSERT_ROOM_SQL = "INSERT INTO rooms (id, room_number, room_description, room_amount) VALUES ( (SELECT MAX(id) FROM rooms) + 1, ?, ?, ?)";

    private String dbUser = "postgres";
    private String url = "jdbc:postgresql://localhost:5432/hoteldb";
    private String dbPassword = "/Pass@098/";

    private int initialRoomNumber = 0;
    private double amount = 300.00;
    private int roomNumber;
    private String prepareDetails;
    private int maxId;

    public HotelDao() {
        this.createRoomNumber(initialRoomNumber);
    }

    public int createRoomNumber(int initialRoomNumber) {
        roomNumber = initialRoomNumber + 1;
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Negative Room Number: " + initialRoomNumber);
        }
        return roomNumber;
    }

    public String prepareDescription(String roomDescription) {
        if (roomDescription.isBlank() || roomDescription == null) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        boolean isNumericOnly = isNumeric(roomDescription);
        if (isNumericOnly) {
            throw new IllegalArgumentException("Description cannot be numbers only");
        }
        roomDescription = roomDescription.trim();
        return roomDescription;
    }

    public String publish(String description) throws SQLException {
        String publishedRoom = null;

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
 
        try (Connection connection = getConnection()) {
            maxId = getMaxId(connection);
            roomNumber = this.createRoomNumber(maxId);

            int rowsSaved = insertRoom(connection, description);

            if (rowsSaved == 1) {
                prepareDetails = "Room: " + this.roomNumber + ".<br/>" + " Cost: " + this.amount + ".<br/>" + " Details " + this.prepareDescription(description) + "";
                publishedRoom = prepareDetails;
            } else {
            	prepareDetails = "No record saved to db";
            }
        }

        return publishedRoom;
    }


    int getMaxId(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(MAX_ROOM_ID_SQL);
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0; // Assuming no records initially
        }
    }

    private int insertRoom(Connection connection, String description) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROOM_SQL);
        preparedStatement.setInt(1, roomNumber);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, this.amount);
        return preparedStatement.executeUpdate();
    }
  
    // This method is used for testing purposes to inject a mock connection
    protected Connection getConnection() throws SQLException {
        // In production, use DriverManager or a connection pool to get a connection
        return DriverManager.getConnection(url, dbUser, dbPassword);
    }

    public static boolean isNumeric(String str) {
        return Pattern.matches("[0-9]+", str);
    }
}