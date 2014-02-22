package fmi.dndtabletop.model;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fmi.dndtabletop.resources.ResourceManager;

public class Tile  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_WIDTH = 40;
	public static final int DEFAULT_HEIGHT = 40;
	
	private JLabel m_view;
	private int m_id;

	public Tile(int id)
	{

		ImageIcon img = ResourceManager.getInstance().getTextureTile(id).getImage();

		
		m_view = new JLabel(img);
		m_view.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		m_view.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		m_view.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		m_id = id;
	}
	
	public JLabel getView()
	{
		return m_view;
	}
	
	
	public void setTextureTile(int id)
	{
		ImageIcon img = ResourceManager.getInstance().getTextureTile(id).getImage();
		m_view.setIcon(img);
		m_id = id;
	}
	
	public String toString()
	{
		return "<Tile id=\""+m_id+"\" />";
	}


}
