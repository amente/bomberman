package bomberman.game;

import java.net.DatagramPacket;

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
	
	public GameEvent getEvent(DatagramPacket packet){	
		String message = new String(packet.getData(),packet.getOffset(),packet.getLength());
		NetworkAddress senderAddress = new NetworkAddress(packet.getSocketAddress());
		GameEvent action = getEvent(message);

		action.setSenderAddress(senderAddress);
		return action;
	}
	
	public GameEvent getEvent(String message){	
		// TODO: Parse the message according to the protocol and make an action
		
		String[] params = message.split(" ", 2);
		if(params.length < 1) { return null;}
		
		GameEvent event = null;

		// For game messages we don't need to check for player

		if (params[0].equalsIgnoreCase("Game")) {
			if (params[1].equalsIgnoreCase("JOIN")) {
				event = new GameEvent();
				event.setType(GameEvent.Type.GAMECHANGE);
				event.addParameter("CALL", "JOIN");
			} else if (params[1].equalsIgnoreCase("START")) {
				event = new GameEvent();
				event.setType(GameEvent.Type.GAMECHANGE);
				event.addParameter("CALL", "START");
			}
		}

		// For all other messages, packets are ignored if player is not in
		// the game

		if (params[0].equalsIgnoreCase("Move")) {
			if (params.length != 2) {
				return null;
			}
			event = new GameEvent();
			event.setType(GameEvent.Type.MOVE);
			event.addParameter("DIR", params[1]);

		} else if (params[0].equalsIgnoreCase("BOMB")) {
			event = new GameEvent();
			event.setType(GameEvent.Type.BOMB);

		}
		
		return event;
	}



	

}
