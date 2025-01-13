package hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomPostgresRepository implements RoomRepository {

	private final Connection connection;

	public RoomPostgresRepository(String url, String dbUser, String dbpassword) throws SQLException {

		this.connection = DriverManager.getConnection(url, dbUser, dbpassword);

	}

	// for test
	public RoomPostgresRepository(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Room> findAll() {
		List<Room> rooms = new ArrayList<>();
		String sql = "SELECT room_number,room_description FROM rooms";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				rooms.add(new Room(resultSet.getString("room_number"), resultSet.getString("room_description")));
			}
		} catch (SQLException e) {
			throw new RoomRepositoryException("Error while fetching all rooms", e);
		}
		return rooms;
	}

	@Override
	public Room findById(String roomNumber) {
		String sql = "SELECT room_number,room_description FROM rooms WHERE room_number = ?";
		try {
			final PreparedStatement statement = this.connection.prepareStatement(sql);
			statement.setString(1, roomNumber);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return new Room(resultSet.getString("room_number"), resultSet.getString("room_description"));

		} catch (SQLException e) {

			throw new RoomRepositoryException("Error while fetching findById rooms", e);

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
			throw new RoomRepositoryException("Error while saving room", e);
		}

	}

	@Override
	public void delete(String roomNumber) {

		String sql = "DELETE FROM rooms WHERE room_number = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, roomNumber);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RoomRepositoryException("Error while deleting room", e);
		}
	}

}
