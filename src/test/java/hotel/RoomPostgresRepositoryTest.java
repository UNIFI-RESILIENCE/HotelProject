package hotel;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RoomPostgresRepositoryTest {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/testdb"; // Adjust the URL
    private static final String USER = "postgres"; // Replace with your username
    private static final String PASSWORD = "/Pass@098/"; // Replace with your password

    private Connection connection;
    private RoomPostgresRepository roomRepository;

    @Before
    public void setup() throws Exception {
        // Connect to the PostgreSQL database
        connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        roomRepository = new RoomPostgresRepository(JDBC_URL, USER, PASSWORD);

        // Clean up and prepare the schema
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS rooms");
            statement.execute("CREATE TABLE rooms (room_number VARCHAR(50) PRIMARY KEY, room_description VARCHAR(1000))");
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
    
    public void testSaveRoom() throws Exception {
        // Arrange
        Room room = new Room("1", "John Doe");

        // Act
        roomRepository.save(room);
        Room fetchedRoom = roomRepository.findById("1");

        // Assert
        assertThat(fetchedRoom).isNotNull();
        assertThat(fetchedRoom.getDescription()).isEqualTo("John Doe");
        assertThat(fetchedRoom.getId()).isEqualTo("1");
    }

    @Test
    public void testGetAllRooms() throws Exception {
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
        assertThat(roomRepository.findAll())
        .containsExactly(
        new Room("1", "test1"),
        new Room("2", "test2"));
    }
    
    @Test
    public void testSave() {
        // Arrange
        Room room = new Room("1", "Deluxe Room");

        // Act
        roomRepository.save(room);
        Room fetchedRoom = roomRepository.findById("1");

        // Assert
        assertThat(fetchedRoom)
            .isNotNull()
            .extracting(Room::getId, Room::getDescription)
            .containsExactly("1", "Deluxe Room");
    }

    @Test
    public void testFindById() {
        // Arrange
        addTestRoomToDatabase("1", "Deluxe Room");

        // Act
        Room fetchedRoom = roomRepository.findById("1");

        // Assert
        assertThat(fetchedRoom)
            .isNotNull()
            .extracting(Room::getId, Room::getDescription)
            .containsExactly("1", "Deluxe Room");
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


    private void addTestRoomToDatabase(String id, String description) {
		String sql = "INSERT INTO rooms ( room_number, room_description) VALUES ( ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setString(2, description);
            statement.executeUpdate();
            System.out.println(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
