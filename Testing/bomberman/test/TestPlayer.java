package bomberman.test;

import java.io.IOException;
import java.util.ArrayList;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;

/**
 * 
 * A Test Player
 *   Can send Join messages, Start messages and a list of game commands using its own socket
 *
 */
public class TestPlayer extends Thread{
	
	NetworkManager networkManager;		
	private NetworkAddress serverAddress;
	private ArrayList<String> commands;
	String playerName ;		
	
			
	TestPlayer(ArrayList<String> commands,int serverPort,String serverAddress){
		try {
		    this.commands = commands;
		    networkManager = new NetworkManager();
		    this.serverAddress = new NetworkAddress(serverAddress,serverPort);			    			  
		} catch (IOException e) {
		    e.printStackTrace();
		}			
					
	}		
	
	
	public String sendJoin(){	
		System.out.println("Sending Join Message...");
		playerName = networkManager.sendSynchronous("Game Join",serverAddress,null,3,5000,false);		
		return playerName;
	}
	
	public void sendStartGame(){			
		networkManager.sendAsynchronous("Game Start",serverAddress,false);			
	}		
	
	@Override
	public void run(){			
		
		// Send game commands
		for(String cmd: commands){
			
			String[] lineArr = cmd.split(" ");			
			if(lineArr[0].equalsIgnoreCase("Move")){						
					System.out.println("Sending Move "+lineArr[1]);
					networkManager.sendAsynchronous(cmd.trim(),serverAddress,true);					
			}else if(lineArr[0].equalsIgnoreCase("Bomb")){
				//System.out.println("Sending Bomb");
				networkManager.sendAsynchronous(cmd.trim(),serverAddress,true);
			}
			

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		while(!isInterrupted());
		
		networkManager.close();
	}

	public NetworkManager getNetworkManager() {
		// TODO Auto-generated method stub
		return networkManager;
	}		

}