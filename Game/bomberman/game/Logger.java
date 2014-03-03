package bomberman.game;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Logger implements Runnable {
	private List<String> log;
	private static int logNumber = 0;
	private int logNum;
	private PrintWriter writer;
	private Semaphore addOperations;
	private boolean running;
	private static int lineNumber = 0;
	
	public Logger() {
		log = new ArrayList<String>();
		logNum = logNumber++;
		try {
			writer = new PrintWriter("log-" + logNum + ".txt", "UTF-8");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		addOperations = new Semaphore(0);
		running = true;
		
	}
	
	public void stopLogging() {
		writer.close();
		running = false;
		addOperations.release();
	}

	public void addToLog(String s) {
		synchronized(log) {
			log.add(s);
		}
		addOperations.release();
	}
	
	public void run() {
		while(running){
			try {
				addOperations.acquire();
			} catch (InterruptedException e) { e.printStackTrace(); }
			
			synchronized(log) {
				for(String s : log) {
					if (s == null) { return; }
					writer.write(lineNumber++ + "- " + s + "\n");
				}
				log.clear();
			}
		}
	}

}
