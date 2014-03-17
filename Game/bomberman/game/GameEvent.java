package bomberman.game;

import java.util.HashMap;

import bomberman.game.network.NetworkAddress;

public class GameEvent {
	
	public enum Type{		
		MOVE,
		BOMB, 
		GAMECHANGE,
		EXPLOSION,
		KILL,
	}
	
		
	private Type type;
	private HashMap<String,Object> parameters;	
	private NetworkAddress senderAddress = null;
	private boolean isFromServer = false;
	
	public GameEvent(){
		parameters = new HashMap<String,Object>(3);	
	}	
	
	public void addParameter(String key,Object value){
		parameters.put(key, value);
	}
	
	public Object getParameter(String key) {
		return parameters.get(key);
	}
	
	public GameEvent.Type getType(){
		return type;
	}
	
	public void setType(GameEvent.Type type){
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

	public NetworkAddress getSenderAddress(){
		return senderAddress;
	}

	public void setSenderAddress(NetworkAddress networkAddress) {
		this.senderAddress  = networkAddress;		
	}

	public void setIsFromServer(boolean b){
		isFromServer = b;
	}
	public boolean isFromServer() {		
		return isFromServer ;
	}
}
