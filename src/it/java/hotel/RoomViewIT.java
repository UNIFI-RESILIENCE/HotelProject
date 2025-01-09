package hotel;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(GUITestRunner.class)
public class RoomViewIT extends AssertJSwingJUnitTestCase {

	private RoomController roomController;
	private HotelRoomView hotelRoomView;
	private RoomPostgresRepository roomRepository;
	private static Connection connection;
	private FrameFixture window;

	private static PostgreSQLContainer<?> postgres;

	@SuppressWarnings("resource")
	@BeforeClass
	public static void setupServer() {
		postgres = new PostgreSQLContainer<>("postgres:16-alpine").withDatabaseName("testdb").withUsername("testuser")
				.withPassword("testpass");

	}

	@Override
	protected void onSetUp() throws Exception {

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
			statement.execute(
					"CREATE TABLE rooms (room_number VARCHAR(50) PRIMARY KEY, room_description VARCHAR(1000))");
		}

		GuiActionRunner.execute(() -> {
			hotelRoomView = new HotelRoomView();
			roomController = new RoomController(hotelRoomView, roomRepository);
			hotelRoomView.setRoomController(roomController);
			return hotelRoomView;
		});
		window = new FrameFixture(robot(), hotelRoomView);
		window.show(); // shows the frame to test

	}

	@AfterClass
	public static void shutdownServer() throws Exception {

		if (connection != null) {
			connection.close();
		}

	}

	@Override
	protected void onTearDown() {
		// Stop the container
		postgres.stop();
	}

	@Test
	@GUITest
	public void testAllRooms() {
		// Arrange: Use the repository to add rooms to the database
		Room room1 = new Room("101", "Deluxe Suite");
		Room room2 = new Room("102", "Standard Room");
		roomRepository.save(room1);
		roomRepository.save(room2);
		// Act: Use the controller's allRooms method
		GuiActionRunner.execute(() -> roomController.allRooms());
		// Assert: Verify that the view's list is populated correctly
		assertThat(window.list("lstDisplayRooms").contents()).containsExactly(room1.toString(), room2.toString());
	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		// Use the UI to add a new room
		window.textBox("txtRoomNumber").enterText("101");
		window.textBox("txtRoomDescription").enterText("Deluxe Suite");
		GuiActionRunner.execute(() -> hotelRoomView.getBtnPublish().doClick());

		// Verify that the room is correctly added to the list
		assertThat(window.list("lstDisplayRooms").contents())
				.containsExactly(new Room("101", "Deluxe Suite").toString());
	}

	@Test
	@GUITest
	public void testAddButtonError() {
		// Pre-populate the repository with a room
		roomRepository.save(new Room("101", "Existing Room"));
		// Try to add a room with a duplicate ID using the UI
		window.textBox("txtRoomNumber").enterText("101");
		window.textBox("txtRoomDescription").enterText("Existing Room");
		GuiActionRunner.execute(() -> hotelRoomView.getBtnPublish().doClick());

		// Verify that the list has not changed
		assertThat(window.list("lstDisplayRooms").contents()).isEmpty();
		// Verify that the error message is displayed
		window.label("lbDisplayStatus").requireText("Room already exist 101: " + new Room("101", "Existing Room"));
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		// Use the controller to populate the view's list

		GuiActionRunner.execute(() -> roomController.newRoom(new Room("101", "To Be Removed")));

		// Select the room from the list and delete it
		GuiActionRunner.execute(() -> {
			hotelRoomView.getLstDisplayRooms().setSelectedIndex(0);
			hotelRoomView.getBtnDelete().doClick();
		});

		// Verify that the room has been removed from the list
		assertThat(window.list("lstDisplayRooms").contents()).isEmpty();
	}

	@Test
	@GUITest
	public void testDeleteButtonError() {
		// Manually add a room to the list that is not in the database
		Room room = new Room("999", "Non-Existent Room");
		GuiActionRunner.execute(() -> hotelRoomView.getListRoomModel().addElement(room));

		// Attempt to delete the non-existent room
		GuiActionRunner.execute(() -> {
			hotelRoomView.getLstDisplayRooms().setSelectedIndex(0);
			hotelRoomView.getBtnDelete().doClick();
		});

		// Verify that the room remains in the list
		assertThat(window.list("lstDisplayRooms").contents()).containsExactly(room.toString());

		// Verify that the error message is displayed
		window.label("lbDisplayStatus").requireText("Room not found 999: " + room);
	}

	@Test
	public void testAddRoom() {
		// Use the UI to add a new room
		window.textBox("txtRoomNumber").enterText("101");
		window.textBox("txtRoomDescription").enterText("Deluxe Suite");
		GuiActionRunner.execute(() -> hotelRoomView.getBtnPublish().doClick());

		// Verify that the room has been added to the repository
		assertThat(roomRepository.findById("101")).isEqualTo(new Room("101", "Deluxe Suite"));
	}

	@Test
	public void testDeleteRoom() {
		// Add a room to the repository for the test
		roomRepository.save(new Room("202", "Existing Room"));

		// Use the controller to make the room appear in the GUI list
		GuiActionRunner.execute(() -> roomController.allRooms());

		// Select the room and delete it

		GuiActionRunner.execute(() -> {
			hotelRoomView.getLstDisplayRooms().setSelectedIndex(0);
			hotelRoomView.getBtnDelete().doClick();
		});

		// Verify that the room has been removed from the repository
		assertThat(roomRepository.findById("202")).isNull();
	}

}