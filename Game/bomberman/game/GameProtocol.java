package bomberman.game;

/**
 * 
 * A singleton class represents the game protocol
 *
 */
public class GameProtocol {
	
    private static GameProtocol theProtocol;
	
	public static  GameProtocol getInstance(){
		if(theProtocol == null){
			theProtocol = new GameProtocol();			
		}
		return theProtocol;
	}
	
	
	
	public GameAction getAction(byte[] message){	
		// TODO: Parse the message according to the protocol and make an action
		return new GameAction();
	}
	
	

}
