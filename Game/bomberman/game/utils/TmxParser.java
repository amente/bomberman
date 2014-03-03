package bomberman.game.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bomberman.game.floor.Block;
import bomberman.game.floor.Door;
import bomberman.game.floor.Floor;
import bomberman.game.floor.FloorObject;
import bomberman.game.floor.Player;

/**
 * Parses the XML map file '.tmx' and constructs the floor grid 
 *
 */
public class TmxParser {

		
	public static FloorObject[][] parse(File file,Floor floor){
		
		FloorObject[][] grid = null;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Document doc = null;
		if (dBuilder != null) {
			try {
				doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (doc != null) {

			doc.getDocumentElement().normalize();
			Element map = doc.getDocumentElement();
			
			int width = Integer.parseInt(map.getAttribute("width"));	
			int height = Integer.parseInt(map.getAttribute("height"));
			
						
			Node main_layer = map.getElementsByTagName("layer").item(0);
			Node data = ((Element)main_layer).getElementsByTagName("data").item(0);
			
			//Process tilesets
			Node tile_set = map.getElementsByTagName("tileset").item(0);
			
			HashMap<Integer,String> tileSet = new HashMap<Integer,String>();
			
			int firstGid = Integer.parseInt(((Element)tile_set).getAttribute("firstgid"));
			
			NodeList master_tiles = ((Element)tile_set).getElementsByTagName("tile");
			
			for(int i=0; i<master_tiles.getLength();i++){
				
				int id= Integer.parseInt(((Element)master_tiles.item(i)).getAttribute("id"))+firstGid;
				Node props = ((Element)master_tiles.item(i)).getElementsByTagName("properties").item(0);
				String name = ((Element)((Element)props).getElementsByTagName("property").item(0)).getAttribute("value");	
				tileSet.put(id,name);		
				
			}
			
			grid = new FloorObject[width][height];
			
			NodeList grid_tiles = ((Element)data).getElementsByTagName("tile");
			
			
			int gridY = 0;
			
			for(int i=0; i<grid_tiles.getLength();i++){
				
				if(i==height-1){gridY++;}			
				
				Element tile = (Element)grid_tiles.item(i);

				
				int gId = Integer.parseInt(tile.getAttribute("gid"));							
				
				String tileType = tileSet.get(gId);
				
				if(tileType.equalsIgnoreCase("block")){					
					grid[i%width][gridY] = new Block(floor);				
					
				}else if(tileType.equalsIgnoreCase("door")){
					grid[i%width][gridY] = new Door(floor);					
				} else if (tileType.equalsIgnoreCase("player")){
					String playerName = tile.getAttribute("player_name");
				
					Player player = new Player(floor, playerName);
					
					grid[i%width][gridY] = player;
					floor.players.add(player);
				} 
				
			}
			

		}
		
		return grid;
		
		
	}	
	
}
