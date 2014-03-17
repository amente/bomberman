package bomberman.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Start {

	private JFrame frmBomberman;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
					window.frmBomberman.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBomberman = new JFrame();
		frmBomberman.getContentPane().setBackground(Color.DARK_GRAY);
		frmBomberman.setIconImage(Toolkit.getDefaultToolkit().getImage("Resources/icon.png"));
		frmBomberman.setTitle("Bomberman");
		frmBomberman.setBounds(100, 100, 600, 350);
		frmBomberman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmBomberman.setJMenuBar(menuBar);
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent arg0) {
			}
			public void menuDeselected(MenuEvent arg0) {
			}
			public void menuSelected(MenuEvent arg0) {
				new About().setVisible(true);
			}
		});
		menuBar.add(mnAbout);
		frmBomberman.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Host Game");
		btnNewButton.setBounds(218, 32, 214, 45);
		frmBomberman.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Join Game");
		btnNewButton_1.setBounds(372, 141, 214, 45);
		frmBomberman.getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(47, 147, 293, 33);
		frmBomberman.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblServerIpAddress = new JLabel("Server IP Address:Port");
		lblServerIpAddress.setForeground(Color.WHITE);
		lblServerIpAddress.setBounds(47, 107, 181, 28);
		frmBomberman.getContentPane().add(lblServerIpAddress);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(47, 38, 129, 33);
		frmBomberman.getContentPane().add(textField_1);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setForeground(Color.WHITE);
		lblPort.setBounds(47, 12, 181, 28);
		frmBomberman.getContentPane().add(lblPort);
	}
}
