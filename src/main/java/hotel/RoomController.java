package hotel;

public class RoomController {
	
	private RoomView roomView;
	private RoomRepository roomRepository;

	public RoomController(RoomView roomView, RoomRepository roomRepository) {
		this.roomView = roomView;
		this.roomRepository = roomRepository;
	}
	 
	public void allRooms() {
		roomView.showAllRooms(roomRepository.findAll());
	}
	
	public void newRoom(Room room) {
		Room existingRoom  = roomRepository.findById(room.getId());
		
		if (existingRoom != null ) {
			roomView.showError("Room already exist " + room.getId(), room);
			return ;
		}
		roomRepository.save(room);
		roomView.roomAdded(room);
	}
	
	public void deleteRoom(Room room) {
		
		if (roomRepository.findById(room.getId()) == null ) {
			roomView.showError("Room not found " + room.getId(), room);
			return;
		}
		roomRepository.delete(room.getId());
		roomView.roomRemoved(room);
	}

}
