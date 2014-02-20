package fmi.dndtabletop.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Wall implements Serializable{
	
	private ArrayList<Point> m_pointList;
	
	public Wall(Point v1,  Point v2)
	{
		this.m_pointList = new ArrayList<Point>();
		m_pointList.add(v1);
		m_pointList.add(v2);
	}
	
	public Point getV1()
	{
		return m_pointList.get(0);
	}
	
	public Point getV2()
	{
		return m_pointList.get(1);
	}
	
	public String toString()
	{
		StringBuffer xmlFlow = new StringBuffer();
		xmlFlow.append("<Wall>");
		
		for(Point p : m_pointList)
		{
			xmlFlow.append("<Point x=\""+p.x+"\" y=\""+p.y+"\" />");
		}
		
		xmlFlow.append("</Wall>");
		return xmlFlow.toString();
	}
}
