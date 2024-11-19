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
        // Spy on the real HotelDao object
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
    public void testCreateRoomNumber_withValidInitialNumber() {
        // Test valid room number creation
        int initialRoomNumber = 0;
        int expectedRoomNumber = 1;
        assertEquals(expectedRoomNumber, hotelDao.createRoomNumber(initialRoomNumber));
    }
	
	@Test
	public void testHotelRoomNumberIsNegativeThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> hotelDao.createRoomNumber(-5));
		assertEquals("Negative Room Number: -5",e.getMessage());
	}
	
	@Test
	public void testHotelRoomNumberIsZeroThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> hotelDao.createRoomNumber(-1));
		assertEquals("Negative Room Number: -1",e.getMessage());
	}

    @Test
    public void testPrepareDescription_withValidDescription() {
        String validDescription = "A cozy room";
        String result = hotelDao.prepareDescription(validDescription);
        assertEquals(validDescription.trim(), result);
    }

	@Test
	public void testHotelRoomDescriptionIsEmptyThrows() {
		//String prepareDescription = room.prepareDescription("");
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()->hotelDao.prepareDescription(""));
		assertEquals("Description cannot be empty", e.getMessage());
	}

	@Test
	public void testHotelRoomDescriptionIsNotOnlyNumbersThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()->hotelDao.prepareDescription("1"));
		assertEquals("Description cannot be numbers only", e.getMessage());
	}

    @Test
    public void testPublish_withValidDescription() throws SQLException {
        String validDescription = "Spacious room with a beautiful view";

        // Mock ResultSet to simulate getting a max ID from the database
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Returning a sample max ID

        // Mock the PreparedStatement's executeUpdate method to return 1 (success)
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Test the publish method
        String result = hotelDao.publish(validDescription);

        // Assert the format of the published room
        assertNotNull(result);
        assertTrue(result.contains("Room:"));
        assertTrue(result.contains("Cost:"));
        assertTrue(result.contains("Details"));

        
    }

    @Test
    public void testPublish_withNoRoomSaved() throws SQLException {
        String validDescription = "Room with no details";

        // Mock ResultSet to simulate getting a max ID from the database
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Returning a sample max ID

        // Mock the PreparedStatement's executeUpdate method to return 0 (failure)
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        // Test the publish method
        String result = hotelDao.publish(validDescription);
       
        // Assert the result is null since no room was saved
        assertNull(result);
        
    }

    @Test
    public void testPublish_withNullDescription() throws SQLException {
        // Test null description should throw IllegalArgumentException
        try {
            hotelDao.publish(null);
            //fail("Expected IllegalArgumentException for null description"); 
        } catch (IllegalArgumentException e) {
            // Check the exception message
            assertEquals("Description cannot be empty", e.getMessage());
        }
    }


    @Test
    public void testGetMaxId_withExistingRecords() throws SQLException {
        // Mock ResultSet to simulate the case where there is at least one record
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(5); // Simulate the max ID being 5

        int maxId = hotelDao.getMaxId(mockConnection);

        assertEquals(5, maxId);
    }

    @Test
    public void testGetMaxId_withNoRecords() throws SQLException {
        // Mock ResultSet to simulate the case where there are no records
        when(mockResultSet.next()).thenReturn(false);

        int maxId = hotelDao.getMaxId(mockConnection);

        assertEquals(0, maxId);
    }

    @Test
    public void testIsNumeric_withNumericInput() {
        assertTrue(HotelDao.isNumeric("12345"));
    }

    @Test
    public void testIsNumeric_withNonNumericInput() {
        assertFalse(HotelDao.isNumeric("Room123"));
    }

    @Test
    public void testIsNumeric_withEmptyInput() {
        assertFalse(HotelDao.isNumeric(""));
    }
    
//    @Test
//    public void testPrintStatement() throws SQLException {
//        // Capture the current System.out
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        // Call the method that prints to the console
//        hotelDao.publish("d");
//
//        // Assert that the output is what you expect
//        assertEquals("No record saved to db", outContent.toString().trim());
//
//        // Reset System.out to its original state
//        System.setOut(System.out);
//    }
    
    
}







