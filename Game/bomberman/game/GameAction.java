package bomberman.game;

import java.util.HashMap;

public class GameAction {
	
	public enum Type{		
		JOIN_LEAVE,
		MOVE,
		BOMB		
		
	}
	
	public enum MovementType{		
		UP,
		DOWN,
		LEFT,
		RIGHT		
	}
	
	
	
	private Type type;
	private HashMap<String,String> parameters;
	
	public GameAction(){
		parameters = new HashMap<String,String>(3);	
	}	
	
	public void addParameter(String key,String value){
		parameters.put(key, value);
	}
	
	
	public GameAction.Type getType(){
		return type;
	}
	
	public void setType(GameAction.Type type){
		this.type = type; 
	}

}
