package bomberman.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 *Runs an automated test by reading from test set up file
 *
 */
public class TestDriver {
	
	
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.println("usage: java TestDriver <serverAddress> <serverPort> <testFilePath>");
			System.exit(1);
		}			
       		
	    String serverAddress = args[0];
		int serverPort = Integer.parseInt(args[1]);
		String testFilePath = args[2];
		
		ArrayList<ArrayList<String>> playerCommands = new ArrayList<ArrayList<String>>();	
		
		int numPlayers= 0;
		
		// Read test file 		
		File testFile = new File(testFilePath);
		BufferedReader reader;		
		try {
			reader = new BufferedReader(new FileReader(testFile));			
			numPlayers = Integer.parseInt(reader.readLine());
			
			ArrayList<String> commands;
			for (int i=0;i<numPlayers;i++){
				reader.readLine() ; //Read START
				String nextLine = reader.readLine();
				commands = new ArrayList<String>();
				while(nextLine!=null && !nextLine.equalsIgnoreCase("END")){				
					commands.add(nextLine);					
					nextLine = reader.readLine();
				}
				playerCommands.add(commands);
			}
			
			reader.close();
		} catch (NumberFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.exit(1);
		}
		
		ArrayList<TestPlayer> testPlayers = new ArrayList<TestPlayer>();
		
		// Send Join Messages to the server for each player		
		for(int i=0;i<numPlayers;i++){
			
			TestPlayer player = new TestPlayer(playerCommands.get(i),serverPort,serverAddress);
			
			String playerID = player.sendJoin();
			if(playerID != null){
				System.out.println(playerID+ " Joining Game Success");
				testPlayers.add(player);
			}else{
				System.out.println("player joining game failed");
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Let each player join one by one
		}
				
		
		// Let the first player send start game	
		System.out.println("Sending Start Message");
		testPlayers.get(0).sendStartGame();
		
		
		//Start the spectator GUI		
		TestSpectator p1GUI = new TestSpectator(testPlayers.get(0));		
		Thread guiThread = p1GUI.start();
		
		
		
		// Let all players start sending their commands		
		for(TestPlayer player: testPlayers){
			player.start();							
		}
		
		// Join All Players
		
		for(TestPlayer player: testPlayers){
			try {
				player.join();				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							
		}		
		
		//Join The GUI Thread
		
		try {
			guiThread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

