package fmi.dndtabletop.ihm;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import fmi.dndtabletop.resources.Resource;

public class ListIconRenderer extends DefaultListCellRenderer{
	
	public ListIconRenderer() 
	{
		super();
	}

	@Override

	public Component getListCellRendererComponent(JList list, Object value, int index,
										boolean isSelected, boolean cellHasFocus) {

		// Get the renderer component from parent class
		super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
		
		if (value instanceof Resource)
	    {
			Resource textTile = (Resource) value;
			this.setIcon(trunc(textTile.getImage().getImage(), 20, 20));
			this.setText(textTile.getName());
	    }
	    else
	    {
	    	this.setText("???");
	    }
		return this;
	}
	
	 private ImageIcon trunc(Image src, int w, int h) {
	        //int w = (int)(scale*src.getWidth(this));
	        //int h = (int)(scale*src.getHeight(this));
	        int type = BufferedImage.TYPE_INT_RGB;
	        BufferedImage dst = new BufferedImage(w, h, type);
	        Graphics2D g2 = dst.createGraphics();
	        g2.drawImage(src, 0, 0, w, h, this);
	        g2.dispose();
	        return new ImageIcon(dst);
	    }
}
