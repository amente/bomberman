package bomberman.game;

import java.io.IOException;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;

public class GameClient{
	
	
	NetworkManager networkManager;		
	private NetworkAddress serverAddress;	
	String playerName ;		
	
			
	GameClient(int serverPort,String serverAddress){
		try {		   
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
	
	
	public void sendCommand(String cmd){
		String[] lineArr = cmd.split(" ");
		if (lineArr[0].equalsIgnoreCase("Move")) {
			System.out.println("Sending Move " + lineArr[1]);
			networkManager.sendAsynchronous(cmd.trim(), serverAddress, true);
		} else if (lineArr[0].equalsIgnoreCase("Bomb")) {
			// System.out.println("Sending Bomb");
			networkManager.sendAsynchronous(cmd.trim(), serverAddress, true);
		}	
	}
	
	public NetworkManager getNetworkManager() {
		// TODO Auto-generated method stub
		return networkManager;
	}
	public void close() {
		networkManager.close();		
	}	

}
