package fmi.dndtabletop.model;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Battlefield implements Serializable{
	
	private int m_width;
	private int m_height;
	private Tile m_tiles[][];
	private ArrayList<MovableObject> m_objectsList;
	private ArrayList<Wall> m_walls;
	
	private static final int DEFAULT_TEXTURE_ID = 0;

	private static final long serialVersionUID = 1L;
	
	public Battlefield(int width, int height, long tileId)
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
	
}
