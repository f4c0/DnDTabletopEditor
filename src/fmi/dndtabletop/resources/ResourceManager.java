package fmi.dndtabletop.resources;

import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResourceManager {

	private static final String RESOURCES_FILE = "/fmi/dndtabletop/resources/ResourceDescriptor.xml";
	private static ResourceManager m_singleton;

	private String ResTextureTileBaseFolder;
	private HashMap<Long, TileResource> m_textTileMap;
	
	private String ResObjectsBaseFolder;
	private HashMap<Long, MovableResource> m_objectsMap;
	


	private ResourceManager()
	{
		m_textTileMap = new HashMap<Long, TileResource>();
		m_objectsMap = new HashMap<Long, MovableResource>();
		try {
			
			InputStream is = getClass().getResourceAsStream(RESOURCES_FILE);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();			
			Document doc = dBuilder.parse(is);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("TileTexture");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					ResTextureTileBaseFolder = ((Element)(eElement.getParentNode())).getAttribute("dir");
					String name = eElement.getAttribute("name");
					String fileName = eElement.getAttribute("file");
					String animatedFlag = eElement.getAttribute("animated");
					
					if(animatedFlag != null)
					{
						if(animatedFlag.equals("true"))
						{
							name = name + " (anim\u00E9)";
						}
					}

					TileResource tile = new TileResource(name, ResTextureTileBaseFolder, fileName);
					m_textTileMap.put(tile.getId(), tile);
				}
			}
			
			nList = doc.getElementsByTagName("Object");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					ResObjectsBaseFolder = ((Element)(eElement.getParentNode())).getAttribute("dir");
					String name = eElement.getAttribute("name");
					String fileName = eElement.getAttribute("file");
					String animatedFlag = eElement.getAttribute("animated");
					
					if(animatedFlag != null)
					{
						if(animatedFlag.equals("true"))
						{
							name = name + " (anim\u00E9)";
						}
					}

					MovableResource tile = new MovableResource(name, ResObjectsBaseFolder, fileName);
					m_objectsMap.put(tile.getId(), tile);
				}
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erreur RessourceManager!\n"+e.toString(), "Exception", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static ResourceManager getInstance()
	{
		if(m_singleton == null)
		{
			m_singleton = new ResourceManager();
		}

		return m_singleton;
	}
	
	public Resource getTextureTile(long id)
	{
		TileResource res = m_textTileMap.get(id);
		if(res == null)
		{
			JOptionPane.showMessageDialog(null, "Erreur RessourceManager!\nResource id "+id+" inconnu !", "Exception", JOptionPane.ERROR_MESSAGE);
		}
			
		return res;
	}
	
	public MovableResource getObject(long id)
	{
		MovableResource res = m_objectsMap.get(id);
		if(res == null)
		{
			JOptionPane.showMessageDialog(null, "Erreur RessourceManager!\nMovableResource id "+id+" inconnu !", "Exception", JOptionPane.ERROR_MESSAGE);
		}
			
		return res;
	}
	
	public HashMap<Long, TileResource> getTextureTileMap()
	{
		return m_textTileMap;
	}
	
	public HashMap<Long, MovableResource> getObjectMap()
	{
		return m_objectsMap;
	}
	
	public String getTextureTilePath()
	{
		return ResTextureTileBaseFolder;
	}
	
	public int getNumberOfTextureTiles()
	{
		return m_textTileMap.size();
	}
}
