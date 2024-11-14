package hotel;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HotelTests {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private HotelRoom room;
	int initialValue = 0;
	

	@Before
	public void setUp() throws Exception {
		room = new HotelRoom();
	}

	
	@Test
	public void testHotelRoomNumberIncreases(){
		
		//HotelRoom room = new HotelRoom();
		 int roomNum = room.createRoomNumber(initialValue);
		 assertEquals(1, roomNum);
		
	}
	
	@Test
	public void testHotelRoomNumberIsNegativeThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> room.createRoomNumber(-5));
		assertEquals("Negative Room Number: -5",e.getMessage());
	}
	
	@Test
	public void testHotelRoomNumberIsZeroThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> room.createRoomNumber(-1));
		assertEquals("Negative Room Number: -1",e.getMessage());
	}
	
	
	
	@Test
	public void testHotelRoomDescriptionIsEmptyThrows() {
		//String prepareDescription = room.prepareDescription("");
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()->room.prepareDescription(""));
		assertEquals("Description cannot be empty", e.getMessage());
	}
	
	@Test
	public void testHotelRoomDescriptionIsNotOnlyNumbersThrows() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()->room.prepareDescription("1"));
		assertEquals("Description cannot be numbers only", e.getMessage());
	}

	@Test
	public void testHotelRoomDescriptionReturnsCorrectDescription() {
		assertEquals( "Hotel Room 1",room.prepareDescription("        Hotel Room 1         "));
	}
	
	@Test
	public void testDatabaseConnectionErrorThrows() {
		IllegalAccessError e = assertThrows(IllegalAccessError.class, ()->room.conn("jdbc:postgresql://localhost:5432/hoteldb", "postgres", "/Pass@098/s"));
		System.out.println("from test :" + e.getMessage());
		assertEquals("FATAL: password authentication failed for user \"postgres\"", e.getMessage());
	}
	
	
//	@Test
//	public void testDatabaseConnectionConnected() {
//		Connection newConnect = room.conn("jdbc:postgresql://localhost:5432/hoteldb", "postgres",  "/Pass@098/");
//		assertEquals("org.postgresql.jdbc.PgConnection@68f1b17f",newConnect);
//	}
	
//	@Test
//	public void testRoomPublishThrowsSqlError() {
//		String sqlerror = room.publish("ss");
//		assertEquals("ERROR: relation does not exist", sqlerror);
//	}
	
	
	
}
