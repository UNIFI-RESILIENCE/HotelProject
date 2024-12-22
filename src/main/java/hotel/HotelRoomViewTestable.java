package hotel;

import javax.swing.JButton;
import javax.swing.JList;

public interface HotelRoomViewTestable {
    JList<Room> getLstDisplayRooms();
    JButton getBtnDelete();
    JButton getBtnPublish();
}
