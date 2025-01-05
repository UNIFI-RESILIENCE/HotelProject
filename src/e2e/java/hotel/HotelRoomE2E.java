package hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;


import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;

import picocli.CommandLine.Option;

@RunWith(GUITestRunner.class)
public class HotelRoomE2E extends AssertJSwingJUnitTestCase{
	private static Connection connection;
	private FrameFixture window;
	
	@SuppressWarnings("resource")
	@ClassRule
	public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine").withDatabaseName("testdb").withUsername("testuser")
			.withPassword("testpass");

	
	

 
	@Override
	protected void onSetUp() {
		// TODO Auto-generated method stub
		// Start the container
		postgres.start();
		
		 String jdbcUrl = postgres.getJdbcUrl();
		String username = postgres.getUsername();
		 String password = postgres.getPassword();

		
		// Connect to the database and initialize the repository
		try {
			connection = DriverManager.getConnection(jdbcUrl, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Prepare the schema
		try (Statement statement = connection.createStatement()) {
			statement.execute("DROP TABLE IF EXISTS rooms");
			statement.execute(
					"CREATE TABLE rooms (room_number VARCHAR(50) PRIMARY KEY, room_description VARCHAR(1000))");
			addTestRoomToDatabase("1", "test room 1 added");
			addTestRoomToDatabase("2", "test room 2 added");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		application("hotel.HotelRoomApp").withArgs(
				"--dbName=" + username,
				"--dbHost=" + jdbcUrl,
				"--dbPassword=" + password
			).start();
		
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Room Manager".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		
//		window = new FrameFixture(robot(), hotelRoomView);
//		window.show(); // shows the frame to test

	}


	@Override
	protected void onTearDown() {
		// Stop the container
		postgres.stop();
	}
	
	@Test @GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
	assertThat(window.list().contents())
	.anySatisfy(e -> assertThat(e).contains("1", "test room 1 added"))
	.anySatisfy(e -> assertThat(e).contains("2", "test room 2 added"));
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
 