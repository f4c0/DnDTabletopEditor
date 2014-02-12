package fmi.dndtabletop.ihm;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import fmi.dndtabletop.resources.Resource;

public class PaletteTreeCellRenderer extends DefaultTreeCellRenderer{

	public PaletteTreeCellRenderer() {
		super();
	}

	@Override
	public Component getTreeCellRendererComponent(	JTree tree, Object value, 
			boolean selected, boolean expanded, 
			boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		if(leaf)
		{
			Object o = ((DefaultMutableTreeNode) value).getUserObject();
			if (o instanceof Resource)
			{
				Resource textTile = (Resource) o;			
				this.setIcon(trunc(textTile.getImage().getImage(), 20, 20));
				this.setText(textTile.getName());
			}else
			{
				this.setIcon(null);
				this.setText("" + value);
			}
		}else
		{
			String folderIconPath = "/fmi/dndtabletop/resources/icon_spellbook.gif";
			URL imgURL = getClass().getResource(folderIconPath);
			if(imgURL != null)
			{
				Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
				this.setIcon(new ImageIcon(img));
			}else
			{
				System.err.println(getClass().toString()+"getTreeCellRendererComponent Failed !!");
			}
			
		}


		return this;
	}
	
	 private ImageIcon scale(Image src, double scale) {
	        int w = (int)(scale*src.getWidth(this));
	        int h = (int)(scale*src.getHeight(this));
	        int type = BufferedImage.TYPE_INT_RGB;
	        BufferedImage dst = new BufferedImage(w, h, type);
	        Graphics2D g2 = dst.createGraphics();
	        g2.drawImage(src, 0, 0, w, h, this);
	        g2.dispose();
	        return new ImageIcon(dst);
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
