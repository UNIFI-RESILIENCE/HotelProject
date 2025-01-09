package hotel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(GUITestRunner.class)
public class HotelRoomE2E extends AssertJSwingJUnitTestCase { //NOSONAR
	private static Connection connection;
	private FrameFixture window;

	@SuppressWarnings("resource")
	@ClassRule
	public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
			.withDatabaseName("testdb").withUsername("testuser").withPassword("testpass");

	private static final String EXISTING_ROOM_1 = "1";
	private static final String EXISTING_ROOM_1_DESCRIPTION = "test room 1 added";

	private static final String EXISTING_ROOM_2 = "2";
	private static final String EXISTING_ROOM_2_DESCRIPTION = "test room 2 added";

	@Override
	protected void onSetUp() {
		
		// Start the container
		postgres.start();

		String jdbcUrl = postgres.getJdbcUrl();
		String username = postgres.getUsername();
		String password = postgres.getPassword();

		// Connect to the database and initialize the repository
		try {
			connection = DriverManager.getConnection(jdbcUrl, username, password);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		// Prepare the schema
		try (Statement statement = connection.createStatement()) {
			statement.execute("DROP TABLE IF EXISTS rooms");
			statement.execute(
					"CREATE TABLE rooms (room_number VARCHAR(50) PRIMARY KEY, room_description VARCHAR(1000))");
			addTestRoomToDatabase(EXISTING_ROOM_1, EXISTING_ROOM_1_DESCRIPTION);
			addTestRoomToDatabase(EXISTING_ROOM_2, EXISTING_ROOM_2_DESCRIPTION);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

		application("hotel.HotelRoomApp")
				.withArgs("--dbName=" + username, "--dbHost=" + jdbcUrl, "--dbPassword=" + password).start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Room Manager".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Override
	protected void onTearDown() {
		// Stop the container
		postgres.stop();
	}

	@Test
	@GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents()).anySatisfy(e -> assertThat(e).contains("1", "test room 1 added"))
				.anySatisfy(e -> assertThat(e).contains("2", "test room 2 added"));
	}

	@Test
	@GUITest
	public void testAddRoomSuccess() {
		// Simulate entering data for adding a new room
		window.textBox("txtRoomNumber").enterText("101");
		window.textBox("txtRoomDescription").enterText("Test room 101 description");
		window.button(JButtonMatcher.withText("Publish Room")).click();

		// Assert that the new room was successfully added to the list
		assertThat(window.list().contents())
				.anySatisfy(entry -> assertThat(entry).contains("101", "Test room 101 description"));
	}

	@Test
	@GUITest
	public void testAddRoomError() {
		// Simulate trying to add a room with duplicate room number
		window.textBox("txtRoomNumber").enterText(EXISTING_ROOM_1);
		window.textBox("txtRoomDescription").enterText(EXISTING_ROOM_1_DESCRIPTION);
		window.button(JButtonMatcher.withText("Publish Room")).click();

		// Assert that an appropriate error message is shown
		assertThat(window.label("lbDisplayStatus").text()).contains(EXISTING_ROOM_1, EXISTING_ROOM_1_DESCRIPTION);
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
