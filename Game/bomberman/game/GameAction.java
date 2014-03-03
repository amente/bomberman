package bomberman.game;

import java.util.HashMap;

public class GameAction {
	
	public enum Type{		
		JOIN_LEAVE,
		MOVE,
		BOMB, 
		GAME		
	}
	
		
	private Type type;
	private HashMap<String,String> parameters;
	
	public GameAction(){
		parameters = new HashMap<String,String>(3);	
	}	
	
	public void addParameter(String key,String value){
		parameters.put(key, value);
	}
	
	public String getParameter(String key) {
		return parameters.get(key);
	}
	
	public GameAction.Type getType(){
		return type;
	}
	
	public void setType(GameAction.Type type){
		this.type = type; 
	}

	public String toString() {
		String s = "ACTION: " + type.toString()
					+ " | WITH PARAMETERS: ";
		
		for(String key : parameters.keySet()) {
			s += key + ": " + parameters.get(key) + " | ";
		}
		
		return s;
	}
}
