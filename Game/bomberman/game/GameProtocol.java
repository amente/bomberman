package bomberman.game;

/**
 * 
 * A singleton class represents the game protocol
 *
 */
public class GameProtocol {
	
    /*Basic game protocol
	
	Join/Leave
		Join game:
			JOIN PLAYER_NAME
		Leave game:
			LEAVE PLAYER_NAME
	
	Movement:			
			MOVE {Dir} PLAYER_NAME
				Dir = {UP,DOWN,LEFT,RIGHT}
	Bomb:
			BOMB PLAYER_NAME				
	
	*/
	private static GameProtocol theProtocol;
	
    
    
	public static  GameProtocol getInstance(){
		if(theProtocol == null){
			theProtocol = new GameProtocol();			
		}
		return theProtocol;
	}
	
	
	
	public GameAction getAction(String message){	
		// TODO: Parse the message according to the protocol and make an action
		System.out.println("Handling message: " + message);
		
		String[] params = message.split(" ", 3);
		if(params.length < 2 ) { return null;}
		
		
		GameAction action = null;
		
		
		if(params[0].equals("Move")){	
			if(params.length!=3) { return null;}
			action = new GameAction();
			action.setType(GameAction.Type.MOVE);			
			action.addParameter("DIR", params[1]);
			action.addParameter("PLAYER",params[2]);						
			
			
		}else if(params[0].equals("BOMB")){
			action = new GameAction();
			action.setType(GameAction.Type.BOMB);			
			action.addParameter("PLAYER",params[0]);
			
		}else if(params[0].equals("Join") || params[0].equals("LEAVE")){
			action = new GameAction();
			action.setType(GameAction.Type.JOIN_LEAVE);			
			action.addParameter("TYPE",params[0]);
			action.addParameter("PLAYER",params[1]);
			
		} else if (params[0].equals("Game")) {
			if (params.length != 2) { return null; }
			
			action = new GameAction();
			action.setType(GameAction.Type.GAME);			
			action.addParameter("TYPE",params[0]);

			if (params[1].trim().equals("END_GAME")) {
				action.addParameter("CALL", params[1].trim());
			}
		}

		
		return action;
	}
	
	

}
