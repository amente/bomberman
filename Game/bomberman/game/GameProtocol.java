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
	
	
	
	public GameAction getAction(String message){	
		System.out.println("Handling message: " + message);
		return new GameAction();
	}
	
	

}
