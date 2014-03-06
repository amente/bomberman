package bomberman.game;

import java.net.DatagramPacket;

import bomberman.game.floor.Floor;

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
	
	public GameAction getAction(DatagramPacket packet,Floor floor,boolean joinOnly){	
		// TODO: Parse the message according to the protocol and make an action
		String message = new String(packet.getData(),packet.getOffset(),packet.getLength());		
		String[] params = message.split(" ", 2);
		if(params.length < 1) { return null;}
		
		GameAction action = null;
		
		// For join only messages we don't need to check for player
		if (joinOnly) {
			if (params[0].equalsIgnoreCase("Game")
					&& params[1].equalsIgnoreCase("JOIN")) {
				action = new GameAction();
				action.setType(GameAction.Type.GAME);
				action.addParameter("CALL", "JOIN");
			} else {
				return null;
			}
		} else {

			// For all other messages, packets are ignored if player is not in
			// the game
			if (!senderHasJoinedGame(packet, floor)) {
				return null;
			}

			if (params[0].equalsIgnoreCase("Move")) {
				if (params.length != 2) {
					return null;
				}
				action = new GameAction();
				action.setType(GameAction.Type.MOVE);
				action.addParameter("DIR", params[1]);
				action.addParameter("PLAYER", params[2]);

			} else if (params[0].equalsIgnoreCase("BOMB")) {
				action = new GameAction();
				action.setType(GameAction.Type.BOMB);
				action.addParameter("PLAYER", params[0]);

			} else if (params[0].equalsIgnoreCase("Game")) {
				if (params.length != 2) {
					return null;
				}

				action = new GameAction();
				action.setType(GameAction.Type.GAME);
				action.addParameter("TYPE", params[0]);

				if (params[1].trim().equals("END_GAME")) {
					action.addParameter("CALL", params[1].trim());
				}
			}
		}
		
		//Bind action to player 
		action.setPlayer(floor.getPlayer(packet.getSocketAddress()));
		return action;
	}



	private boolean senderHasJoinedGame(DatagramPacket packet, Floor floor) {
		//Extract the sender player information from the packet and check if it has already joined the game
		if(floor.getPlayer(packet.getSocketAddress())==null){
			return false;
		}		
		return true;
	}	

}
