package bomberman.game;


public class GameStateUpdater extends Thread {

	private boolean isStopped = false; 	
	private int numSent = 0;

	private GameResolver gameResolver;
	public GameStateUpdater(GameResolver resolver){
		super("ClientUpdater");
		this.gameResolver = resolver;			
	}
	
	@Override
	public void run(){
		// First update is the full game state
		GameStateUpdate fullUpdate = gameResolver.getFloorState();
		gameResolver.sendUpdateMessage(fullUpdate.toString());
		
		//All other updates are incremental diffs
		while(!isStopped){
			GameStateUpdate update = null;
			/*try {
				update = gameStateUpdateQueue.poll(10, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/		
			
			update = gameResolver.getUpdate();
			if(update!=null){
				System.out.println("Sent Update "+ update.getType());
				String updateMessage = update.toString();
				System.out.println("Update: "+updateMessage);
				gameResolver.sendUpdateMessage(updateMessage);
				numSent++;
			 System.out.println("Updates Sent: "+numSent);
			}
		}
		
	}
	

	public void stopGracefully(){	
		isStopped = true;			
	}
	
}
