package hotel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class HotelRoomViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private HotelRoomView hotelRoomView;

	private AutoCloseable closeable;

	@Mock
	private RoomController roomController;

	@Mock
	private RoomRepository roomRepository;

	@Override
	protected void onSetUp() {
		// System.setProperty("java.awt.headless", "true");
		closeable = MockitoAnnotations.openMocks(this);
		Mockito.verifyNoMoreInteractions(roomController);
		// RoomPostgresRepository roomRepository = new RoomPostgresRepository();
		// roomController = new RoomController(hotelRoomView, roomRepository);
		GuiActionRunner.execute(() -> {
			hotelRoomView = new HotelRoomView();
			// roomController = new RoomController(hotelRoomView, null);
			hotelRoomView.setRoomController(roomController);
			// System.err.println(roomController.getClass().getDeclaredFields());
			return hotelRoomView;
		});
		window = new FrameFixture(robot(), hotelRoomView);
		window.show(); // shows the frame to test

		// to implement
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

//	@Test
//    public void printMockedMethods() {
//        // Initialize mocks
//        MockitoAnnotations.openMocks(this);
//
//        // Get the mocked class
//        Class<?> clazz = roomController.getClass();
//
//        // Print methods
//        System.out.println("Methods in the mocked class:");
//        for (Method method : clazz.getMethods()) {
//            System.out.println(method.getName());
//        }
//    }

	@Test
	@GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("Room Number"));
		window.textBox("txtRoomNumber").requireEnabled();
		window.label(JLabelMatcher.withText("Description"));
		window.textBox("txtRoomDescription").requireEnabled();
		window.scrollPane("scrollPane").isEnabled();
		window.scrollPane("scrollPane_1").isEnabled();
		window.button(JButtonMatcher.withText("Publish Room")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete Room")).requireDisabled();
		window.list("lstDisplayRooms");
		window.label(JLabelMatcher.withName("lbDisplayStatus"));

	}

	@Test
	public void testWhenIdAndRoomAreNonEmptyThenAddButtonShouldBeEnabled() {
		window.textBox("txtRoomNumber").enterText("1");
		window.textBox("txtRoomDescription").enterText("test");
		window.button(JButtonMatcher.withName("btnPublish")).requireEnabled();
	}

	@Test
	public void testWhenEitherIdOrNameAreBlankThenAddButtonShouldBeDisabled() {
		JTextComponentFixture txtRoomNumber = window.textBox("txtRoomNumber");
		JTextComponentFixture txtRoomDescription = window.textBox("txtRoomDescription");
		txtRoomNumber.enterText("1");
		txtRoomDescription.enterText(" ");
		window.button(JButtonMatcher.withName("btnPublish")).requireDisabled();
		txtRoomNumber.setText("");
		txtRoomDescription.setText("");
		txtRoomNumber.enterText(" ");
		txtRoomDescription.enterText("test");
		window.button(JButtonMatcher.withName("btnPublish")).requireDisabled();
	}

	@Test
	public void testShowAllRoomsShouldAddRoomDescriptionsToTheList() {
		Room room1 = new Room("1", "test1");
		Room room2 = new Room("2", "test2");
		GuiActionRunner.execute(() -> hotelRoomView.showAllRooms(Arrays.asList(room1, room2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(room1.toString(), room2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		// Arrange: Create a test room
		Room room = new Room("101", "Test Room");

		// Act: Call the showError method with a test message and room
		GuiActionRunner.execute(() -> hotelRoomView.showError("error message", room));

		// Assert: Verify the error label shows the correct error message
		window.label("lbDisplayStatus").requireText("error message: " + room);
	}

	@Test
	public void testRoomRemovedShouldRemoveTheRoomFromTheListAndResetTheErrorLabel() {
		// Arrange: Set up initial room list
		Room room1 = new Room("101", "Test Room 1");
		Room room2 = new Room("102", "Test Room 2");

		GuiActionRunner.execute(() -> {
			DefaultListModel<Room> listRoomsModel = hotelRoomView.getListRoomModel();
			listRoomsModel.addElement(room1);
			listRoomsModel.addElement(room2);
		});

		// Act: Remove a room
		GuiActionRunner.execute(() -> hotelRoomView.roomRemoved(new Room("101", "Test Room 1")));

		// Assert: Verify the list and error label are updated
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(room2.toString());

		window.label("lbDisplayStatus").requireText(" ");
	}

	@Test
	public void testRoomAddedShouldAddTheRoomToTheListAndResetTheErrorLabel() {
		// Arrange: Create a test room
		Room room = new Room("101", "Test Room");

		// Act: Add the room to the list
		GuiActionRunner.execute(() -> hotelRoomView.roomAdded(new Room("101", "Test Room")));

		// Assert: Verify the list contains the added room
		String[] listContents = window.list("lstDisplayRooms").contents();
		assertThat(listContents).containsExactly(room.toString());
		// Verify the error label is reset
		window.label("lbDisplayStatus").requireText(" ");
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenARoomIsSelected() {
		Room room1 = new Room("50", "test");
		GuiActionRunner.execute(() -> {
			hotelRoomView.getListRoomModel().addElement(room1);
		});
		// JListFixture list = window.list("lstDisplayRooms");
		GuiActionRunner.execute(() -> hotelRoomView.getLstDisplayRooms().setSelectedIndex(0));
		// System.err.println(list.selectItem(0).valueAt(0));
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Room"));
		deleteButton.requireEnabled();
		window.list("lstDisplayRooms").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testAddButtonShouldDelegateToHotelRoomControllerNewRoom() {
		window.textBox("txtRoomNumber").enterText("101");
		window.textBox("txtRoomDescription").enterText("Test Room");
		// GuiActionRunner.execute(() -> hotelRoomView);
		// window.button(JButtonMatcher.withText("Publish Room")).click();
		GuiActionRunner.execute(() -> hotelRoomView.getBtnPublish().doClick());
		verify(roomController).newRoom(new Room("101", "Test Room"));
	}

	@Test
	public void testDeleteButtonShouldDelegateToHotelRoomControllerDeleteRoom() {
		// Arrange: Create and populate room list
		Room room1 = new Room("101", "Test Room 1");
		Room room2 = new Room("102", "Test Room 2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Room> listRoomsModel = hotelRoomView.getListRoomModel();
			listRoomsModel.addElement(room1);
			listRoomsModel.addElement(room2);
		});
		GuiActionRunner.execute(() -> hotelRoomView.getLstDisplayRooms().setSelectedIndex(1));
		assertThat(window.list("lstDisplayRooms").selection()).containsExactly(room2.toString());
		GuiActionRunner.execute(() -> hotelRoomView.getBtnDelete().doClick());
		// window.button(JButtonMatcher.withText("Delete
		// Room")).requireEnabled().click();
		verify(roomController).deleteRoom(room2);
	}

}
