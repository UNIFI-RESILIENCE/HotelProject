package hotel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomPostgresRepository implements RoomRepository {
	private String dbUser = System.getenv("DB_USER") ;
	private String url = System.getenv("DB_URL") ;
	private String dbPassword = System.getenv("DB_PASSWORD") ;
	
	private Connection connection;
	
	public RoomPostgresRepository() {
		try {
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
	}
	
	//for test
	public RoomPostgresRepository(String url, String dbUser, String dbpassword) {
		try {
			connection = DriverManager.getConnection(url, dbUser, dbpassword);
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
	}

	@Override
	public List<Room> findAll() {
		  List<Room> rooms = new ArrayList<>();
	        String sql = "SELECT * FROM rooms";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            ResultSet resultSet = statement.executeQuery();
	            while (resultSet.next()) {
	                rooms.add(new Room(
	                    resultSet.getString("room_number"),
	                    resultSet.getString("room_description")
	                ));
	            }
	            System.err.println(resultSet);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        System.out.println(rooms);
	        return rooms;
	}

	@Override
	public Room findById(String room_number) {
		String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room_number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                    resultSet.getString("room_number"),
                    resultSet.getString("room_description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public void save(Room room) {
		String sql = "INSERT INTO rooms ( room_number, room_description) VALUES ( ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getId());
            statement.setString(2, room.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void delete(String room_number) {
		
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room_number);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

}
