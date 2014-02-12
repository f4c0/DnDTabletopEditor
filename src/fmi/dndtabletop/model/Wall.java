package fmi.dndtabletop.model;

import java.awt.Point;
import java.io.Serializable;

public class Wall implements Serializable{
	
	private Point v1;
	private Point v2;
	
	public Wall(Point v1,  Point v2)
	{
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Point getV1()
	{
		return v1;
	}
	
	public Point getV2()
	{
		return v2;
	}
	
	public String toString()
	{
		return "["+v1+";"+v2+"]";
	}
}
