package bomberman.game;

import java.net.DatagramPacket;

import bomberman.game.floor.Floor;
import bomberman.game.network.NetworkAddress;

/**
 * 
 * A singleton class represents the game protocol
 *
 */
public class GameProtocol {
	
    /*Basic game protocol
	
	Movement:			
			MOVE {Dir}
				Dir = {UP,DOWN,LEFT,RIGHT}
	Bomb:
			BOMB	
			
	Game:
			Game JOIN
			Game LEAVE
			Game START_GAME
			Game END_GAME
	
	*/
	private static GameProtocol theProtocol;
	
    
    
	public static  GameProtocol getInstance(){
		if(theProtocol == null){
			theProtocol = new GameProtocol();			
		}
		return theProtocol;
	}	
	
	public GameAction getAction(DatagramPacket packet,Floor floor,boolean gameOnly){	
		// TODO: Parse the message according to the protocol and make an action
		String message = new String(packet.getData(),packet.getOffset(),packet.getLength());
		NetworkAddress senderAddress = new NetworkAddress(packet.getSocketAddress());
		String[] params = message.split(" ", 2);
		if(params.length < 1) { return null;}
		
		GameAction action = null;
		
		// For game messages we don't need to check for player
		if (gameOnly) {
			if (params[0].equalsIgnoreCase("Game")){
					if(params[1].equalsIgnoreCase("JOIN")) {
						action = new GameAction();
						action.setType(GameAction.Type.GAME);
						action.addParameter("CALL", "JOIN");
						action.setSenderAddress(senderAddress);
					}else if(params[1].equalsIgnoreCase("START")){
						action = new GameAction();
						action.setType(GameAction.Type.GAME);
						action.addParameter("CALL", "START");
						action.setSenderAddress(senderAddress);
					}					
			} 		
		
			return action;
			
		} else {
			
			// For all other messages, packets are ignored if player is not in
			// the game
			if (!senderHasJoinedGame(senderAddress, floor)) {
				return null;
			}

			if (params[0].equalsIgnoreCase("Move")) {
				if (params.length != 2) {
					return null;
				}
				action = new GameAction();
				action.setType(GameAction.Type.MOVE);
				action.addParameter("DIR", params[1]);
				

			} else if (params[0].equalsIgnoreCase("BOMB")) {
				action = new GameAction();
				action.setType(GameAction.Type.BOMB);
				
			}
			
			//Bind action to sender address
			action.setSenderAddress(senderAddress);			
			return action;
			
		}		
		
	}



	private boolean senderHasJoinedGame(NetworkAddress senderAddress, Floor floor) {
		//Extract the sender player information from the packet and check if it has already joined the game
		if(floor.getPlayer(senderAddress)==null){
			return false;
		}		
		return true;
	}	

}
