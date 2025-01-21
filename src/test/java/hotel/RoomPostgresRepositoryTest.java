package hotel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.VerificationCollector;
import org.testcontainers.containers.PostgreSQLContainer;

public class RoomPostgresRepositoryTest {

	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();
	private Connection connection;
	private RoomPostgresRepository roomRepository;

	@SuppressWarnings("resource")
	public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
			.withDatabaseName("testdb").withUsername("testuser").withPassword("testpass");

	@Before
	public void setup() throws Exception {

		postgres.start();

		String jdbcUrl = postgres.getJdbcUrl();
		String username = postgres.getUsername();
		String password = postgres.getPassword();
		// Connect to the PostgreSQL database
		connection = DriverManager.getConnection(jdbcUrl, username, password);
		roomRepository = new RoomPostgresRepository(jdbcUrl, username, password);

		// Clean up and prepare the schema
		try (Statement statement = connection.createStatement()) {
			statement.execute("DROP TABLE IF EXISTS rooms");
			statement.execute(
					"CREATE TABLE rooms (room_number VARCHAR(50) PRIMARY KEY, room_description VARCHAR(1000))");
		}
	}

	@After
	public void tearDown() throws Exception {
		// Clean up after each test
		try (Statement statement = connection.createStatement()) {
			statement.execute("DROP TABLE IF EXISTS rooms");
		}
		if (connection != null) {
			connection.close();
		}
	}

	@Test
	public void testSaveRoom() {
		// Arrange
		Room room = new Room("1", "John Doe");

		// Act
		roomRepository.save(room);
		Room fetchedRoom = roomRepository.findById("1");

		// Assert
		assertThat(fetchedRoom).isNotNull();
		assertThat(fetchedRoom.getDescription()).isEqualTo("John Doe");
		assertThat(fetchedRoom.getId()).isEqualTo("1");
		assertThat(fetchedRoom).isNotNull().extracting(Room::getId, Room::getDescription).containsExactly("1",
				"John Doe");
	}

	@Test
	public void testGetAllRooms() {
		// Arrange
		roomRepository.save(new Room("1", "Alice"));
		roomRepository.save(new Room("2", "Bob"));

		// Act
		List<Room> rooms = roomRepository.findAll();

		// Assert
		assertThat(rooms).hasSize(2);
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(roomRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestRoomToDatabase("1", "test1");
		addTestRoomToDatabase("2", "test2");
		assertThat(roomRepository.findAll()).containsExactly(new Room("1", "test1"), new Room("2", "test2"));
	}

	@Test
	public void testFindById() {
		// Arrange
		addTestRoomToDatabase("1", "Deluxe Room");

		// Act
		Room fetchedRoom = roomRepository.findById("1");

		// Assert
		assertThat(fetchedRoom).isNotNull().extracting(Room::getId, Room::getDescription).containsExactly("1",
				"Deluxe Room");
	}

	@Test
	public void testDelete() {
		// Arrange
		addTestRoomToDatabase("1", "Deluxe Room");

		// Act
		roomRepository.delete("1");
		Room fetchedRoom = roomRepository.findById("1");

		// Assert
		assertThat(fetchedRoom).isNull();
	}

	@Test
	public void testFindAllThrowsException() throws Exception {
		// Arrange
		Connection mockConnection = Mockito.mock(Connection.class);
		Mockito.when(mockConnection.prepareStatement(Mockito.anyString()))
				.thenThrow(new SQLException("Test SQL Exception"));

		RoomPostgresRepository repo = new RoomPostgresRepository(mockConnection);

		// Act & Assert
		assertThrows(RuntimeException.class, repo::findAll);

		Mockito.verify(mockConnection, Mockito.times(1)).prepareStatement(Mockito.anyString());
	}

	@Test
	public void testSaveThrowsException() throws Exception {
		// Arrange
		Connection mockConnection = Mockito.mock(Connection.class);
		PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
		Mockito.doThrow(new SQLException("Test SQL Exception")).when(mockStatement).executeUpdate();

		RoomPostgresRepository repo = new RoomPostgresRepository(mockConnection);
		Room room = new Room("1", "Test Room");

		// Act & Assert
		assertThrows(null, RuntimeException.class, () -> repo.save(room));

		Mockito.verify(mockConnection, Mockito.times(1)).prepareStatement(Mockito.anyString());
		Mockito.verify(mockStatement, Mockito.times(1)).setString(1, "1");
		Mockito.verify(mockStatement, Mockito.times(1)).setString(2, "Test Room");
		Mockito.verify(mockStatement, Mockito.times(1)).executeUpdate();
	}

	@Test
	public void testDeleteThrowsException() throws Exception {
		// Arrange
		Connection mockConnection = Mockito.mock(Connection.class);
		PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
		Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
		Mockito.doThrow(new SQLException("Test SQL Exception")).when(mockStatement).executeUpdate();

		RoomPostgresRepository repo = new RoomPostgresRepository(mockConnection);

		// Act & Assert
		assertThrows(RuntimeException.class, () -> repo.delete("1"));

		Mockito.verify(mockConnection, Mockito.times(1)).prepareStatement(Mockito.anyString());
		Mockito.verify(mockStatement, Mockito.times(1)).setString(1, "1");
		Mockito.verify(mockStatement, Mockito.times(1)).executeUpdate();
	}

	@Test
	public void testFindByIdReturnsNull() {
		// Arrange
		addTestRoomToDatabase("1", "Deluxe Room");

		// Act
		Room fetchedRoom = roomRepository.findById("-1");

		// Assert
		assertThat(fetchedRoom).isNull();

	}

	@Test
	public void testCatchBlockTriggeredBySQLException() throws SQLException {
		// Arrange
		Connection mockConnection = mock(Connection.class);
		PreparedStatement mockStatement = mock(PreparedStatement.class);

		// Simulate incorrect SQL causing an SQLException
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
		when(mockStatement.executeQuery()).thenThrow(new SQLException("Simulated SQL Exception"));

		RoomPostgresRepository repository = new RoomPostgresRepository(mockConnection);

		// Act
		assertThrows(RoomRepositoryException.class, () -> repository.findById("1R"));
	}

	@Test
	public void testFindByIdthrows() throws SQLException {
		// Arrange
		addTestRoomToDatabase("1", "Deluxe Room");
		try (Connection connectionx = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(),
				postgres.getPassword());) {
			connectionx.close();
			RoomPostgresRepository repository = new RoomPostgresRepository(connectionx);
			// Assert
			assertThrows(RoomRepositoryException.class, () -> repository.findById("1R"));

		}

	}

	private void addTestRoomToDatabase(String id, String description) {
		String sql = "INSERT INTO rooms ( room_number, room_description) VALUES ( ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, id);
			statement.setString(2, description);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
