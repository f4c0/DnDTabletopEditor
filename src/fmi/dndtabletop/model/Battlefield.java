package fmi.dndtabletop.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Battlefield implements Serializable{
	
	private int m_width;
	private int m_height;
	private Tile m_tiles[][];
	private ArrayList<MovableObject> m_objectsList;
	private ArrayList<Wall> m_walls;
	
	private int m_incrementalRefId;
	
	public Battlefield(int width, int height, int tileId)
	{
		m_width = width;
		m_height = height;
		m_tiles = new Tile[m_width][];
		for(int i = 0; i < m_width; i++)
		{
			m_tiles[i] = new Tile[m_height];
		}
		
		for(int y = 0; y < m_height; y++)
		{
			for(int x = 0; x < m_width; x++)
			{
				m_tiles[x][y] = new Tile(tileId);
			}
		}
		
		m_objectsList = new ArrayList<MovableObject>();
		m_walls = new ArrayList<Wall>();
		
		m_incrementalRefId = 0;
	}

	public int getWidth() {
		return m_width;
	}

	public int getHeight() {
		return m_height;
	}
	
	public Tile getTile(int x, int y) throws ArrayIndexOutOfBoundsException
	{
		if((x >= m_width)||(y >= m_height))
		{
			ArrayIndexOutOfBoundsException e = new ArrayIndexOutOfBoundsException("("+x+","+y+") out of bounds !");
			throw e;
		}
		
		return m_tiles[x][y];
	}
	
	public void addObject(MovableObject object)
	{
		object.setRefId(m_incrementalRefId);
		m_incrementalRefId ++;
		m_objectsList.add(object);		
	}
	
	public ArrayList<MovableObject> getObjectsList()
	{
		return m_objectsList;
	}
	
	public void addWall(Wall wall)
	{
		 m_walls.add(wall);
	}
	
	public ArrayList<Wall> getWallsList()
	{
		return m_walls;
	}
	
	public void rotateSelectedObject(double value)
	{
		for(MovableObject obj : m_objectsList)
		{
			if(obj.isSelected())
			{
				obj.rotate(value);
			}
		}		
	}
	
	public String toString()
	{
		StringBuffer xmlFlow = new StringBuffer();
		
		xmlFlow.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlFlow.append("<Battlefield>");		
		xmlFlow.append("<Tiles width=\""+m_width+"\" height=\""+m_height+"\">");
		
		for(int y = 0; y < m_height; y++)
		{
			for(int x = 0; x < m_width; x++)
			{
				xmlFlow.append(m_tiles[x][y].toString());
			}
		}
		
		xmlFlow.append("</Tiles>");		
		for(MovableObject obj : m_objectsList)
		{
			xmlFlow.append(obj.toString());
		}
		
		for(Wall wall : m_walls)
		{
			xmlFlow.append(wall.toString());
		}
		
		xmlFlow.append("</Battlefield>");
		
		return xmlFlow.toString();
	}
	
}
