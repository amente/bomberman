package bomberman.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bomberman.game.Application;
import bomberman.game.Player;
import bomberman.game.UDPWrapper;

public class TestDriver {
	public static void main(String args[]) {
		if (args.length != 2) {
			System.out.println("usage: java TestDriver <ip-address> <port>");
			System.exit(1);
		}
		

	
		int port = Integer.parseInt(args[1]);
		String address = args[0];

		
		UDPWrapper udpWrapper = new UDPWrapper();
		
		/*udpWrapper.sendSynchronous(
			Player.Controls.START_GAME + Application.MESSAGE_DELIMITER,
			port, 
			address
		);*/
		
		List<String> readLines = new ArrayList<String>();
		File file = new File("res/test_driver.txt");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;

		    while ((text = reader.readLine()) != null) {
		    	readLines.add(text);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {}
		}
		
		boolean started = false;
		for(String line : readLines) {
			String[] lineArr = line.split(" ");
			
			if (lineArr[1].equals("START_GAME") || lineArr[0].equals("Join")){
				udpWrapper.sendSynchronous(line, port, address);
				if (lineArr[1].equals("START_GAME")) {
					started = true;
				}
			} else {
				if (!started) { continue; }
				udpWrapper.sendAsynchronous(line, port, address);
			}
		}
		
	}
}
