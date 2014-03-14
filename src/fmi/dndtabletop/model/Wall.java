package fmi.dndtabletop.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;

import fmi.dndtabletop.ihm.BattlefieldLayerUI;

public class Wall implements Serializable{
	
	
	private static final Color BASIC_COLOR = Color.black;
	private static final Color SELECTED_COLOR = Color.green;

	private ArrayList<Point> m_pointList;
	private boolean m_isSelected;

	public Wall()
	{
		this.m_pointList = new ArrayList<Point>();
		m_isSelected = false;
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

	public void draw(Graphics2D g2d)
	{
		if(m_isSelected)
		{
			g2d.setColor(SELECTED_COLOR);			
		}else
		{
			g2d.setColor(BASIC_COLOR);
		}
		
		g2d.setStroke(new BasicStroke(BattlefieldLayerUI.WALL_STROKE));
		Path2D.Float wallShape = new Path2D.Float();
		
		for(int j = 0; j < m_pointList.size(); j++)
		{
			if(j == 0)
			{
				wallShape.moveTo(m_pointList.get(j).x, m_pointList.get(j).y);
			}else
			{
				wallShape.lineTo(m_pointList.get(j).x, m_pointList.get(j).y);
			}
		}

		g2d.draw(wallShape);
	}
}
