package hotel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
//import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class HotelRoomView extends JFrame implements RoomView{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtRoomNumber;
	private JTextArea txtRoomDescription;
	private JLabel lblDescription;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	/**
	 * Create the frame.
	 */
	public HotelRoomView() {

		setTitle("Room Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 636);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbRoomNumber = new JLabel("Room Number");
		lbRoomNumber.setBounds(31, 88, 149, 29);
		contentPane.add(lbRoomNumber);

		txtRoomNumber = new JTextField();
		txtRoomNumber.setBounds(31, 120, 149, 45);
		contentPane.add(txtRoomNumber);
		txtRoomNumber.setColumns(10);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(214, 58, 441, 158);
		contentPane.add(scrollPane_1);

		txtRoomDescription = new JTextArea();
		scrollPane_1.setViewportView(txtRoomDescription);
		txtRoomDescription.setRows(1000);
		txtRoomDescription.setWrapStyleWord(true);
		txtRoomDescription.setLineWrap(true);

		lblDescription = new JLabel("Description");
		lblDescription.setBounds(214, 30, 94, 29);
		contentPane.add(lblDescription);

		JSeparator separator = new JSeparator();
		separator.setBounds(31, 298, 624, 10);
		contentPane.add(separator);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 322, 624, 219);
		contentPane.add(scrollPane);

		JTextPane lbRoomDetails = new JTextPane();
		lbRoomDetails.setEditable(false);
		scrollPane.setViewportView(lbRoomDetails);

		JButton btnPublish = new JButton("Publish Room");
		btnPublish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Integer roomNumber = Integer.parseInt(txtRoomNumber.getText());

				//Double amount = Double.parseDouble(txtRoomAmount.getText());

				//String roomDescription = txtRoomDescription.getText();

			}
		});
		btnPublish.setBounds(31, 233, 624, 45);
		contentPane.add(btnPublish);
		
		JLabel lbDisplayStatus = new JLabel("");
		lbDisplayStatus.setForeground(new Color(192, 28, 40));
		lbDisplayStatus.setBackground(new Color(238, 238, 238));
		lbDisplayStatus.setBounds(31, 595, 624, 29);
		contentPane.add(lbDisplayStatus);
		
		JButton btnDelete = new JButton("Delete Room");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnDelete.setBounds(288, 553, 140, 27);
		contentPane.add(btnDelete);

	}

	@Override
	public void showAllRooms(List<Room> rooms) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message, Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomAdded(Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomRemoved(Room room) {
		// TODO Auto-generated method stub
		
	}
}
