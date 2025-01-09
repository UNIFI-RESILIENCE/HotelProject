package hotel;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class HotelRoomView extends JFrame implements RoomView, HotelRoomViewTestable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtRoomNumber;
	private JTextArea txtRoomDescription;
	private JLabel lblDescription;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane1;

	private transient RoomController roomController;

	private JButton btnPublish;
	
	private JButton btnDelete;
	
	private JList<Room> lstDisplayRooms;

	private JLabel lbDisplayStatus;

	private DefaultListModel<Room> listRoomsModel;


	DefaultListModel<Room> getListRoomModel() {
		return listRoomsModel;
	}

	public void setRoomController(RoomController roomController) {

		this.roomController = roomController;
	}
	
	/**
	 * Create the frame.
	 */
	public HotelRoomView() {
		setTitle("Room Manager");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setBounds(100, 100, 692, 636);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbRoomNumber = new JLabel("Room Number");
		lbRoomNumber.setName("lbRoomNumber");
		lbRoomNumber.setBounds(31, 88, 149, 29);
		contentPane.add(lbRoomNumber);

		txtRoomNumber = new JTextField();
		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnPublish.setEnabled(
						!txtRoomNumber.getText().trim().isEmpty() && !txtRoomDescription.getText().trim().isEmpty());
			}
		};
		txtRoomNumber.setName("txtRoomNumber");
		txtRoomNumber.setBounds(31, 120, 149, 45);
		txtRoomNumber.addKeyListener(btnAddEnabler);
		contentPane.add(txtRoomNumber);
		txtRoomNumber.setColumns(10);

		scrollPane1 = new JScrollPane();
		scrollPane1.setName("scrollPane1");
		scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane1.setBounds(214, 58, 441, 158);
		contentPane.add(scrollPane1);

		txtRoomDescription = new JTextArea();
		txtRoomDescription.setName("txtRoomDescription");
		txtRoomDescription.setRows(1000);
		txtRoomDescription.setWrapStyleWord(true);
		txtRoomDescription.setLineWrap(true);
		txtRoomDescription.addKeyListener(btnAddEnabler);
		scrollPane1.setViewportView(txtRoomDescription);

		lblDescription = new JLabel("Description");
		lblDescription.setName("lblDescription");
		lblDescription.setBounds(214, 30, 94, 29);
		contentPane.add(lblDescription);

		scrollPane = new JScrollPane();
		scrollPane.setName("scrollPane");
		scrollPane.setBounds(31, 322, 624, 219);
		contentPane.add(scrollPane);

		btnDelete = new JButton("Delete Room");
		btnDelete.setName("btnDelete");
		btnDelete.setEnabled(false);
		btnDelete.setBounds(288, 553, 140, 27);
		btnDelete.addActionListener(e -> 
			roomController.deleteRoom(lstDisplayRooms.getSelectedValue())
		);
		contentPane.add(btnDelete);

		listRoomsModel = new DefaultListModel<>();
		lstDisplayRooms = new JList<>(listRoomsModel);
		lstDisplayRooms.setName("lstDisplayRooms");
		lstDisplayRooms.addListSelectionListener(e -> 
			btnDelete.setEnabled(lstDisplayRooms.getSelectedIndex() != -1)
		);
		lstDisplayRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(lstDisplayRooms);

		btnPublish = new JButton("Publish Room");
		btnPublish.setEnabled(false);
		btnPublish.setName("btnPublish");
		btnPublish.addActionListener(e -> 
			roomController.newRoom(new Room(txtRoomNumber.getText(), txtRoomDescription.getText()))
		);

		btnPublish.setBounds(31, 233, 624, 45);
		contentPane.add(btnPublish);

		lbDisplayStatus = new JLabel("");
		lbDisplayStatus.setName("lbDisplayStatus");
		lbDisplayStatus.setForeground(new Color(192, 28, 40));
		lbDisplayStatus.setBackground(new Color(238, 238, 238));
		lbDisplayStatus.setBounds(31, 595, 624, 29);
		contentPane.add(lbDisplayStatus);
	}

	@Override
	public void showAllRooms(List<Room> rooms) {
		rooms.stream().forEach(listRoomsModel::addElement);
	}

	@Override
	public void showError(String message, Room room) {
		SwingUtilities.invokeLater(() -> lbDisplayStatus.setText(message + ": " + room));
	}

	@Override
	public void roomAdded(Room room) {
		SwingUtilities.invokeLater(() -> {
			listRoomsModel.addElement(room);
			resetErrorLabel();
		});
	}

	@Override
	public void roomRemoved(Room room) {
		listRoomsModel.removeElement(room);
		resetErrorLabel();
	}

	private void resetErrorLabel() {
		lbDisplayStatus.setText(" ");
	}

	@Override
	public JList<Room> getLstDisplayRooms() {
		return lstDisplayRooms;
	}

	@Override
	public JButton getBtnDelete() {
		return btnDelete;
	}

	@Override
	public JButton getBtnPublish() {
		return btnPublish;
	}

}
