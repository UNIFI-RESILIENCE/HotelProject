package hotel;

import java.util.List;

public interface RoomView {
	
	void showAllRooms(List<Room> rooms);
	void showError(String message, Room room);
	void roomAdded(Room room);
	void roomRemoved(Room room);

}
