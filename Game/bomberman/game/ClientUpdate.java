package bomberman.game;

import java.util.HashMap;

/*
 *The client update consists of sequential board state updates
 * 
 *  A message is defined as
 *  
 *  UpdateNumber/UpdateSize/Update1/Update2/ ... 
 *
 *  An Update is defined as follows
 *  
 *  Initial Update:
 *  	LOAD {MAP}
 *  		MAP:- {.TMX file stream}  
 *  Game Status Update:
 *  	GAME {STATUS}
 *           STATUS :- {START,STOP}
 *          
 *  Object Addition Updates:
 *  	NEW OBJECT_TYPE OBJECT_NAME X_LOC Y_LOC
 *  
 *  Object Removal Updates:
 *  	DEL OBJECT_NAME X_LOC Y_LOC	
 *    
 *  Movement Updates:
 *      MOVE OBJECT_NAME X_LOC Y_LOC
 *  
 *  Bomb Explosion Updates:
 *  
 *  	BOMB POWER X_LOC Y_LOC  
 *  
 *
 */
public class ClientUpdate {
	
	UpdateType type;
	private HashMap<String,String> parameters;
	
	public ClientUpdate(UpdateType t){
		this.type = t;
		parameters = new HashMap<String,String>(3);
	}
	
	public void addParameter(String key,String value){
		parameters.put(key, value);
	}
	
	public UpdateType getType(){
		return type;
	}
	
	public enum UpdateType{	
		LOAD,
		NEW,
		GAME, 
		DEL,
		MOVE,
		BOMB
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();		
		sb.append(type.name());
		sb.append(" ");
		for(String param: parameters.values()){
			sb.append(param);
			sb.append(" ");
		}
		
		return sb.toString();		
	}

}
