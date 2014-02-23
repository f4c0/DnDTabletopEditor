package fmi.dndtabletop.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Wall implements Serializable{
	
	private ArrayList<Point> m_pointList;
	
	public Wall()
	{
		this.m_pointList = new ArrayList<Point>();
	}
	
	public void addPoint(Point p)
	{
		m_pointList.add(p);
	}
	
	public ArrayList<Point> getPoints()
	{
		return m_pointList;
	}
	
	public String toString()
	{
		StringBuffer xmlFlow = new StringBuffer();
		xmlFlow.append("<Wall>");
		
		for(Point p : m_pointList)
		{
			xmlFlow.append("<Point x=\""+(p.x*1.5/Tile.DEFAULT_WIDTH)+"\" y=\""+(p.y*1.5/Tile.DEFAULT_HEIGHT)+"\" />");
		}
		
		xmlFlow.append("</Wall>");
		return xmlFlow.toString();
	}
}
