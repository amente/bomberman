package bomberman.game;

import java.net.DatagramPacket;

import bomberman.game.floor.Floor;
import bomberman.game.network.NetworkAddress;
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
			GameEvent action = GameProtocol.getInstance().getEvent(packet);			
			if(action == null){return null;}
			
						
			if(action.getType() == GameEvent.Type.GAMECHANGE){
				
				String param = (String)(action.getParameter("CALL"));
				
				if(param.equalsIgnoreCase("JOIN")){
					
					String playerName = gameFloor.addPlayer(new NetworkAddress(packet.getSocketAddress()));
					if(playerName!=null){
						System.out.println(playerName+ " Joined");	
						return playerName;
					}  
					
				}else if(param.equalsIgnoreCase("START")){					
					if(gameFloor.getHostPlayer().getAddress().equals(action.getSenderAddress())){
						gameServer.setGameStarted(true);
						gameServer.broadCastStartGame(gameFloor.getAddressOfAllPlayers());
					}					
				}				
			}
						
		}		
		return null;
	}
	
	
}
