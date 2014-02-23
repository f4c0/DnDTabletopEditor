package fmi.dndtabletop.model;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayer;

import fmi.dndtabletop.ihm.BattleView;
import fmi.dndtabletop.resources.ResourceManager;

public class MovableObject implements Serializable{

	private int m_x;
	private int m_y;
	private JLabel m_view;
	private int m_resourceId;
	private int m_refId;
	private float m_rotate;
	private boolean m_selected;
	
	public MovableObject(int x, int y, float rotate, int resourceId)
	{
		m_x = x;
		m_y = y;
		m_rotate = rotate;
		m_resourceId = resourceId;
		m_refId = 0;
		
		ImageIcon img = ResourceManager.getInstance().getObject(m_resourceId).getImage();
		m_view = new JLabel(img);
		m_x -= (img.getIconWidth() / 2);
		m_y -= (img.getIconHeight() / 2);
		m_view.setBounds(m_x, m_y, img.getIconWidth(), img.getIconHeight());
		m_selected = false;
	}
	
	public JLabel getView()
	{
		return m_view;
	}
	
	public int getWidth()
	{
		return m_view.getIcon().getIconWidth();
	}
	
	public int getHeight()
	{
		return m_view.getIcon().getIconHeight();
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public int getY()
	{
		return m_y;
	}
	
	public double getRotation()
	{
		return Math.toRadians(m_rotate);
	}
	
	public boolean IsCursorInBounds(MouseEvent mouseEvt)
	{
		Rectangle bounds = m_view.getBounds();		
		
		return  ((mouseEvt.getX() >= m_x) &&
				(mouseEvt.getX() <= (m_x + bounds.width)) &&
				(mouseEvt.getY() >= m_y) &&
				(mouseEvt.getY() <= (m_y + bounds.height)));
	}
	
	public void move(MouseEvent mouseEvt)
	{
		m_x = mouseEvt.getX() - m_view.getWidth()/2;
		m_y = mouseEvt.getY() - m_view.getHeight()/2;
	}
	
	public void select()
	{
		m_selected = true;
	}
	
	public void unselect()
	{
		m_selected = false;
	}
	
	public void switchSelection()
	{
		m_selected = !m_selected;
	}
	
	public boolean isSelected()
	{
		return m_selected;
	}
	
	public void rotate(double value)
	{
		m_rotate += value;
	}
	
	public void setRefId(int value)
	{
		m_refId = value;
	}
	
	public String toString()
	{
		StringBuffer xmlFlow = new StringBuffer();
		Rectangle bounds = m_view.getBounds();
		
		float x = (m_x * 1.0f + bounds.width / 2.0f) * 1.5f / Tile.DEFAULT_WIDTH;
		float y = (m_y * 1.0f + bounds.height / 2.0f) * 1.5f / Tile.DEFAULT_HEIGHT;		
		
		xmlFlow.append("<MovableObject ");
		xmlFlow.append("x=\""+x+"\" ");
		xmlFlow.append("y=\""+y+"\" ");
		xmlFlow.append("rotationInDeg=\""+m_rotate+"\" ");
		xmlFlow.append("resourceId=\""+m_resourceId+"\" ");
		xmlFlow.append("refId=\""+m_refId+"\" ");
		xmlFlow.append("/>");		
		
		return xmlFlow.toString();
	}
}
