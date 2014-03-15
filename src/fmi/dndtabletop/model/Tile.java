package fmi.dndtabletop.model;


import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fmi.dndtabletop.resources.ResourceManager;

public class Tile  implements Serializable{
	
	public static final int SIZE = 40;
	
	private JLabel m_view;
	private int m_id;

	public Tile(int id)
	{

		ImageIcon img = ResourceManager.getInstance().getTextureTile(id).getImage();
		
		m_view = new JLabel(img);
		m_view.setMinimumSize(new Dimension(SIZE, SIZE));
		m_view.setMaximumSize(new Dimension(SIZE, SIZE));
		m_view.setPreferredSize(new Dimension(SIZE, SIZE));

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
