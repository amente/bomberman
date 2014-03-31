package bomberman.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import bomberman.game.Application;
import bomberman.game.GameClient;
import bomberman.game.GameResolver;

public class Start {

	private JFrame frmBomberman;
	private JTextField textIPAddress;
	private JTextField textPort;
	
	private GameClient client;
	private GameResolver server;
	private boolean isHost = false;
	
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
		
		JButton btnHostGame = new JButton("Host Game");
		btnHostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hostGameButtonClicked();
			}
		});
		btnHostGame.setBounds(218, 32, 172, 45);
		frmBomberman.getContentPane().add(btnHostGame);
		
		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				joinGameButtonClicked();
			}
		});
		btnJoinGame.setBounds(360, 141, 214, 45);
		frmBomberman.getContentPane().add(btnJoinGame);
		
		textIPAddress = new JTextField();
		textIPAddress.setBounds(47, 147, 293, 33);
		frmBomberman.getContentPane().add(textIPAddress);
		textIPAddress.setColumns(10);
		
		JLabel lblServerIpAddress = new JLabel("Server IP Address:Port");
		lblServerIpAddress.setForeground(Color.WHITE);
		lblServerIpAddress.setBounds(47, 107, 181, 28);
		frmBomberman.getContentPane().add(lblServerIpAddress);
		
		textPort = new JTextField();
		textPort.setColumns(10);
		textPort.setBounds(47, 38, 129, 33);
		frmBomberman.getContentPane().add(textPort);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setForeground(Color.WHITE);
		lblPort.setBounds(47, 12, 181, 28);
		frmBomberman.getContentPane().add(lblPort);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 230, 564, 49);
		frmBomberman.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblGameStatusTxt = new JLabel("Game Status: ");
		lblGameStatusTxt.setBounds(20, 24, 68, 14);
		panel.add(lblGameStatusTxt);
		
		JLabel lblGameStatusMsg = new JLabel("");
		lblGameStatusMsg.setBounds(90, 24, 464, 14);
		panel.add(lblGameStatusMsg);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButtonClicked();
			}
		});
		btnStartGame.setBounds(402, 32, 172, 45);
		frmBomberman.getContentPane().add(btnStartGame);
	}

	protected void startButtonClicked() {
		// TODO Auto-generated method stub
		if(isHost){
			if(client!=null){
				client.sendStartGame();
			}
		}
	}

	private void joinGameButtonClicked() {
		// TODO Auto-generated method stub
	    String[] addrString = textIPAddress.getText().split(":");
	    int port  = Integer.parseInt(addrString[1]);
	    client =  Application.startClient(addrString[0], port);
	    isHost = false;	    
	}

	private void hostGameButtonClicked() {
		// TODO Auto-generated method stub
		int port = Integer.parseInt(textPort.getText());
		server  = Application.startServer(port);
		client  = Application.startClient("127.0.0.1", port);
		isHost = true;
	}	
}
