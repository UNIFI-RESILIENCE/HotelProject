package hotel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import org.junit.Before;
import org.junit.Test;

public class HotelDaoTest {

	private HotelDao hotelDao;
	private Connection mockConnection;
	private Statement mockStatement;
	private PreparedStatement mockPreparedStatement;
	private ResultSet mockResultSet;

	@Before
	public void setUp() throws SQLException {
		// Spy on the real HotelDao object because I don't want to mock all the methods
		// within the HotelDao object
		hotelDao = spy(new HotelDao());

		// Mock the database connection and other related objects
		mockConnection = mock(Connection.class);
		mockStatement = mock(Statement.class);
		mockPreparedStatement = mock(PreparedStatement.class);
		mockResultSet = mock(ResultSet.class);

		// Setup behavior for getConnection method to return a mocked connection
		doReturn(mockConnection).when(hotelDao).getConnection();

		// Mock the Statement and ResultSet for SQL queries
		when(mockConnection.createStatement()).thenReturn(mockStatement);
		when(mockStatement.executeQuery(HotelDao.MAX_ROOM_ID_SQL)).thenReturn(mockResultSet);

		// Mock the prepareStatement behavior
		when(mockConnection.prepareStatement(HotelDao.INSERT_ROOM_SQL)).thenReturn(mockPreparedStatement);
	}

	@Test
	public void testCreateRoomNumberWithValidInitialNumber() {
		int initialRoomNumber = 0;
		int expectedRoomNumber = 1;
		assertEquals(expectedRoomNumber, hotelDao.createRoomNumber(initialRoomNumber));
	}

	@Test
	public void testHotelRoomNumberIsNegativeThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> hotelDao.createRoomNumber(-5));
		assertEquals("Negative Room Number: -5", e.getMessage());
	}

	@Test
	public void testHotelRoomNumberIsZeroThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> hotelDao.createRoomNumber(-1));
		assertEquals("Negative Room Number: -1", e.getMessage());
	}

	@Test
	public void testHotelRoomDescriptionIsEmptyThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
				() -> hotelDao.prepareDescription(""));
		assertEquals("Description cannot be empty", e.getMessage());
	}

	@Test
	public void testHotelRoomDescriptionIsNotOnlyNumbersThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
				() -> hotelDao.prepareDescription("1"));
		assertEquals("Description cannot be numbers only", e.getMessage());
	}

	@Test
	public void testPrepareDescriptionWithValidDescription() {
		String validDescription = "A cozy room";
		String result = hotelDao.prepareDescription(validDescription);
		assertEquals(validDescription.trim(), result);
	}

	@Test
	public void testPublishWithValidDescription() throws SQLException {
		// setup
		String validDescription = "Spacious room with a beautiful view";
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getInt(1)).thenReturn(1); // return 1 for success
		when(mockPreparedStatement.executeUpdate()).thenReturn(1);

		// exercise
		String result = hotelDao.publish(validDescription);

		// verify
		assertNotNull(result);
		assertTrue(result.contains("Room:"));
		assertTrue(result.contains("Cost:"));
		assertTrue(result.contains("Details"));
	}

	@Test
	public void testPublishWithNoRoomSaved() throws SQLException {
		// setup
		String validDescription = "No record saved to db";
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getInt(1)).thenReturn(1);
		when(mockPreparedStatement.executeUpdate()).thenReturn(0); // return 0 for failure

		// exercise
		String result = hotelDao.publish(validDescription);

		// verify
		assertNull(result);
	}

	@Test
	public void testPublishWithNullDescription() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> hotelDao.publish(null));
		assertEquals("Description cannot be empty", e.getMessage());
	}

	@Test
	public void testGetMaxId_withExistingRecords() throws SQLException {
		// setup
		when(mockResultSet.next()).thenReturn(true);
		when(mockResultSet.getInt(1)).thenReturn(5); // Simulate the max ID being 5
		// exercise
		int maxId = hotelDao.getMaxId(mockConnection);
		// verify
		assertEquals(5, maxId);
	}

	@Test
	public void testGetMaxIdWithNoRecords() throws SQLException {
		// setup
		when(mockResultSet.next()).thenReturn(false);
		// exercise
		int maxId = hotelDao.getMaxId(mockConnection);
		// verify
		assertEquals(0, maxId);
	}

	@Test
	public void testIsNumericWithNumericInput() {
		assertTrue(HotelDao.isNumeric("12345"));
	}

	@Test
	public void testIsNumericWithNonNumericInput() {
		assertFalse(HotelDao.isNumeric("Room123"));
	}

	@Test
	public void testIsNumericWithEmptyInput() {
		assertFalse(HotelDao.isNumeric(""));
	}

}
