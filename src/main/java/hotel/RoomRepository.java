package hotel;

import java.util.List;

public interface RoomRepository {
	public List<Room> findAll();

	public Room findById(String id);

	public void save(Room room);

	public void delete(String id);
}
