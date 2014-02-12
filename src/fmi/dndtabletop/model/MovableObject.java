package fmi.dndtabletop.model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fmi.dndtabletop.resources.ResourceManager;

public class MovableObject implements Serializable{

	private int m_x;
	private int m_y;
	private JLabel m_view;
	private int m_resourceId;
	
	public MovableObject(int x, int y, int resourceId)
	{
		m_x = x;
		m_y = y;
		m_resourceId = resourceId;
		
		ImageIcon img = ResourceManager.getInstance().getObject(m_resourceId).getImage();
		m_view = new JLabel(img);
		m_x -= (img.getIconWidth() / 2);
		m_y -= (img.getIconHeight() / 2);
		m_view.setBounds(m_x, m_y, img.getIconWidth(), img.getIconHeight());
	}
	
	public JLabel getView()
	{
		return m_view;
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public int getY()
	{
		return m_y;
	}
}
