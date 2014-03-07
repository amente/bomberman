package bomberman.game;

public class ClientUpdateProtocol {
	
	/*
	 *The client update protocol consists of sequential board state updates
	 * 
	 *  A message is defined as
	 *  
	 *  UpdateNumber/UpdateSize/Update1/Update2/ ... 
	 *
	 *  An Update is defined as follows
	 *  
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
	
	private static ClientUpdateProtocol theProtocol;
	
    
    
	public static  ClientUpdateProtocol getInstance(){
		if(theProtocol == null){
			theProtocol = new ClientUpdateProtocol();			
		}
		return theProtocol;
	}
	
	
	public String getUpdateString(ClientUpdate[] updates){
		return "";				
	}	

}
