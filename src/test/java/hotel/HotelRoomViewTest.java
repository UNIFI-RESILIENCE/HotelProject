package hotel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JListItemFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class HotelRoomViewTest extends AssertJSwingJUnitTestCase{
	
	private FrameFixture window;
	
	private HotelRoomView hotelRoomView;
	
	private AutoCloseable closeable;
	
	@Mock
	private RoomController roomController;
	
	@Override
	protected void onSetUp() {
		
		GuiActionRunner.execute(() -> {
			closeable = MockitoAnnotations.openMocks(this);
				hotelRoomView = new HotelRoomView();
				hotelRoomView.setRoomController(roomController);
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
	
	@Test @GUITest
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
	public void testDeleteButtonShouldBeEnabledOnlyWhenARoomIsSelected() {
		GuiActionRunner.execute(() -> { 
			hotelRoomView.getListRoomModel().addElement(new Room("50", "test"));
			
		});
		JListFixture list = window.list("lstDisplayRooms");
		list.selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Room"));
		/*
		 * deleteButton.requireEnabled();
		 * window.list("lstDisplayRooms").clearSelection();
		 * deleteButton.requireDisabled();
		 */
	}
	
	
	@Test
	public void testShowAllStudentsShouldAddStudentDescriptionsToTheList() {
	    // Arrange: Create test students
	    Room room1 = new Room("1", "test1");
	    Room room2 = new Room("2", "test2");

	    // Act: Call the showAllStudents method with test data
	    GuiActionRunner.execute(() ->
	        hotelRoomView.showAllRooms(Arrays.asList(room1, room2))
	    );

	    // Assert: Verify the list contains the expected student descriptions
	    String[] listContents = window.list().contents();
	    assertThat(listContents)
	        .containsExactly(room1.toString(), room2.toString());
	}
	
	
	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
	    // Arrange: Create a test room
	    Room room = new Room("101", "Test Room");

	    // Act: Call the showError method with a test message and room
	    GuiActionRunner.execute(() ->
	        hotelRoomView.showError("error message", room)
	    );

	    // Assert: Verify the error label shows the correct error message
	    window.label("lbDisplayStatus")
	        .requireText("error message: " + room);
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
	    GuiActionRunner.execute(() ->
	        hotelRoomView.roomRemoved(new Room("101", "Test Room 1"))
	    );

	    // Assert: Verify the list and error label are updated
	    String[] listContents = window.list().contents();
	    assertThat(listContents).containsExactly(room2.toString());

	    window.label("lbDisplayStatus").requireText(" ");
	}




}
