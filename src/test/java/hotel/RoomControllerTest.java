package hotel;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoomControllerTest {

	@Mock
	private RoomRepository roomRepository;
	
	@Mock
	private RoomView roomView;
	
	@InjectMocks
	private RoomController roomController;
	
	private AutoCloseable closeable;
	
	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}
	
	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
	public void testAllRooms() {
	List<Room> rooms = asList(new Room());
	when(roomRepository.findAll())
	.thenReturn(rooms);
	roomController.allRooms();
	verify(roomView).showAllRooms(rooms);
	}
	
	@Test
	public void testNewRoomWhenRoomDoesNotAlreadyExist() {
		Room room = new Room("1", "test");
		when(roomRepository.findById("1")).
		thenReturn(null);
		roomController.newRoom(room);
		InOrder inOrder = inOrder(roomRepository, roomView);
		inOrder.verify(roomRepository).save(room);
		inOrder.verify(roomView).roomAdded(room);
	}
	
	@Test
	public void testNewRoomWhenRoomAlreadyExists() {
		Room roomToAdd = new Room("1", "test");
		Room existingRoom = new Room("1", "name");
		when(roomRepository.findById("1")).
			thenReturn(existingRoom);
		roomController.newRoom(roomToAdd);
		verify(roomView)
			.showError("Room already exist 1", roomToAdd);
		verifyNoMoreInteractions(ignoreStubs(roomRepository));
	}
 
	@Test
	public void testDeleteRoomWhenRoomExists() {
		Room roomToDelete = new Room("1", "test");
		when(roomRepository.findById("1")).
			thenReturn(roomToDelete);
		roomController.deleteRoom(roomToDelete);
		InOrder inOrder = inOrder(roomRepository, roomView);
		inOrder.verify(roomRepository).delete("1");
		inOrder.verify(roomView).roomRemoved(roomToDelete);
	}

	@Test
	public void testdeleteRoomWhenRoomDoesNotExist() {
		Room room = new Room("1", "test");
		when(roomRepository.findById("1")).
			thenReturn(null);
		roomController.deleteRoom(room);
		verify(roomView)
			.showError("Room not found 1", room);
		verifyNoMoreInteractions(ignoreStubs(roomRepository));
	}
}
