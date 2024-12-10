package hotel;

import org.junit.Test;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class HotelRoomViewTest extends AssertJSwingJUnitTestCase{
	
	private FrameFixture window;
	
	private HotelRoomView hotelRoomView;
	
	@Override
	protected void onSetUp() {
		
		GuiActionRunner.execute(() -> {
			hotelRoomView = new HotelRoomView();
			return hotelRoomView;
			});
			window = new FrameFixture(robot(), hotelRoomView);
			window.show(); // shows the frame to test
	// to implement
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
		
		
		window.textBox("lbRoomDetails").requireEnabled();
		window.label(JLabelMatcher.withName("lbDisplayStatus"));	
		
		
	}

	
	

}
