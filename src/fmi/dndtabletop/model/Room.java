package fmi.dndtabletop.model;

import java.util.ArrayList;

public class Room {
	
	private ArrayList<Wall> m_walls;
	private ArrayList<Tile> m_tiles;
	private boolean m_isSelected;
	
	public Room()
	{
		m_walls = new ArrayList<Wall>();
		m_tiles = new ArrayList<Tile>();
		m_isSelected = false;
	}
	
	public void addWall(Wall w)
	{
		m_walls.add(w);
	}
	
	public void addTile(Tile t)
	{
		m_tiles.add(t);
	}
	
	
}
