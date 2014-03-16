package bomberman.game;

import java.util.Map;
import java.util.TreeMap;

import bomberman.game.floor.Bomb;
import bomberman.game.floor.FloorObject;
import bomberman.game.floor.Movable.MovementType;

/*
 *The game state update consists of sequential board state updates
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
 *  	BOMB {STATUS} X_LOC Y_LOC  
 *  
 *			 STATUS : - {NEW,EXPLODE}
 */
public class GameStateUpdate {
	
	UpdateType type;
	private Map<String,String> parameters;
	
	public GameStateUpdate(UpdateType t){
		this.type = t;
		parameters = new TreeMap<String,String>();
	}
	
	public GameStateUpdate(String updateString){
		parameters = new TreeMap<String,String>();
		String[] tokens = updateString.split(" ");
		
		String stype = tokens[0];
		if(stype.equalsIgnoreCase(UpdateType.NEW.name())){
			type = UpdateType.NEW;
			parameters.put("OBJECT_NAME",tokens[1]);
			parameters.put("OBJECT_TYPE", tokens[2]);			
			parameters.put("X_LOC",tokens[3]);
			parameters.put("Y_LOC",tokens[4]);
		}else if(stype.equalsIgnoreCase(UpdateType.MOVE.name())){
			type = UpdateType.MOVE;
			parameters.put("DIR", tokens[1]);
			parameters.put("OBJECT_NAME",tokens[2]);
			parameters.put("X_LOC",tokens[3]);
			parameters.put("Y_LOC",tokens[4]);
		}else if(stype.equalsIgnoreCase(UpdateType.EXPLODEBOMB.name())){
			type = UpdateType.EXPLODEBOMB;	
			parameters.put("OBJECT_NAME",tokens[1]);
			parameters.put("X_LOC",tokens[2]);
			parameters.put("Y_LOC",tokens[3]);
		}else if(stype.equalsIgnoreCase(UpdateType.DEL.name())){
			type = UpdateType.DEL;
			parameters.put("OBJECT_NAME", tokens[1]);			
			parameters.put("OBJECT_TYPE",tokens[2]);		
		}					
		
	}
	
	public static GameStateUpdate makeUpdateForAddObject(FloorObject o){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.NEW);
		update.addParameter("OBJECT_TYPE", o.getType());
		update.addParameter("OBJECT_NAME", o.getName());
		update.addParameter("X_LOC", ""+o.getX());
		update.addParameter("Y_LOC", ""+o.getY());
		return update;		
	}
	
	
	public static GameStateUpdate makeUpdateForMove(FloorObject o,MovementType dir){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.MOVE);
		update.addParameter("DIR",dir.name());
		update.addParameter("OBJECT_NAME", o.getName());
		update.addParameter("X_LOC", ""+o.getX());
		update.addParameter("Y_LOC", ""+o.getY());
		return update;		
	}
	
		
	public static GameStateUpdate makeUpdateForExplodeBomb(Bomb b){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.EXPLODEBOMB);
		update.addParameter("OBJECT_NAME", b.getName());
		update.addParameter("X_LOC", ""+b.getX());
		update.addParameter("Y_LOC", ""+b.getY());
		return update;		
	}
	
	public static GameStateUpdate makeUpdateForRemoveObject(FloorObject o){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.DEL);
		update.addParameter("OBJECT_NAME", o.getName());
		update.addParameter("OBJECT_TYPE", o.getType()); 				
		return update;		
	}
	
	
	
	public void addParameter(String key,String value){
		parameters.put(key, value);
	}
	
	public String getParameter(String key){
		return parameters.get(key);
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
		EXPLODEBOMB
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
