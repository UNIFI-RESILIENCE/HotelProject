package hotel;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class RoomPostgresRepositoryTestContainersIT {

    @SuppressWarnings("resource")
	@ClassRule
    public static final PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
 
    private Connection connection;
    private RoomPostgresRepository roomRepository;

    @Before
    public void setUp() throws Exception {
        // Start the container
        postgres.start();

        // Retrieve dynamic database connection details
        String jdbcUrl = postgres.getJdbcUrl();
        String username = postgres.getUsername();
        String password = postgres.getPassword();

        // Connect to the database and initialize the repository
        connection = DriverManager.getConnection(jdbcUrl, username, password);
        roomRepository = new RoomPostgresRepository(jdbcUrl, username, password);

        // Prepare the schema
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS rooms");
            statement.execute("CREATE TABLE rooms (room_number VARCHAR(50) PRIMARY KEY, room_description VARCHAR(1000))");
        }
    }

    @After
    public void tearDown() throws Exception {
        // Clean up the database
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS rooms");
        }
        if (connection != null) {
            connection.close();
        }

        // Stop the container
        postgres.stop();
    }

    @Test
    public void testFindById() {
        // Arrange
        Room room = new Room("1", "Luxury Suite");
        roomRepository.save(room);

        // Act
        Room fetchedRoom = roomRepository.findById("1");

        // Assert
        assertThat(fetchedRoom)
            .isNotNull()
            .extracting(Room::getId, Room::getDescription)
            .containsExactly("1", "Luxury Suite");
    }

    @Test
    public void testSaveAndFindById() {
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
    public void testFindAll() {
        // Arrange
        roomRepository.save(new Room("1", "Deluxe Room"));
        roomRepository.save(new Room("2", "Standard Room"));

        // Act
        var rooms = roomRepository.findAll();

        // Assert
        assertThat(rooms)
            .hasSize(2)
            .containsExactlyInAnyOrder(
                new Room("1", "Deluxe Room"),
                new Room("2", "Standard Room")
            );
    }

    @Test
    public void testDelete() {
        // Arrange
        roomRepository.save(new Room("1", "Deluxe Room"));

        // Act
        roomRepository.delete("1");
        Room fetchedRoom = roomRepository.findById("1");

        // Assert
        assertThat(fetchedRoom).isNull();
    }
}
