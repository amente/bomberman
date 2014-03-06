package bomberman.game;

import java.net.DatagramPacket;

import bomberman.game.floor.Floor;
import bomberman.game.network.PacketProcessor;

public class JoinResolver implements PacketProcessor {

	private GameResolver gameResolver;
	private GameServer gameServer;
	public JoinResolver(GameResolver gameResolver,GameServer gameServer){
		this.gameResolver = gameResolver;
		this.gameServer = gameServer;
	}
	
	@Override
	public String process(DatagramPacket packet) {		
		if(packet!=null){
			Floor gameFloor = gameResolver.getGameFloor();
			GameAction action = GameProtocol.getInstance().getAction(packet,gameFloor,true);			
			if(action == null){return null;}
			
						
			if(action.getType() == GameAction.Type.GAME){
				
				String param = action.getParameter("CALL");
				
				if(param.equalsIgnoreCase("JOIN")){
					
					String playerName = gameFloor.addPlayer(packet.getSocketAddress());
					if(playerName!=null){
						System.out.println(playerName+ " Joined");	
						return playerName;
					}  
					
				}else if(param.equalsIgnoreCase("START")){					
					if(action.getPlayer()!=null && action.getPlayer()==gameFloor.getHostPlayer()){
						gameServer.broadCastStartGame(gameFloor.getAddressOfAllPlayers());
					}					
				}				
			}
						
		}		
		return null;
	}
	
	
}
