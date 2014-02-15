package fmi.dndtabletop.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;

import fmi.dndtabletop.model.MovableObject;
import fmi.dndtabletop.model.Wall;

public class BattlefieldLayerUI  extends LayerUI<BattleView>{

	@Override
	public void paint (Graphics g, JComponent c) {
		super.paint(g, c);
		Graphics2D g2d = (Graphics2D)g;
		Rectangle drawHere = g2d.getClipBounds();
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(2));
		BattleView bfv = ((JLayer<BattleView>)c).getView();

		//Draw grid
		for(int i = 0; i < bfv.getBattlefieldWidth(); i++)
		{
			g2d.drawLine(i * bfv.getTileWidth(), drawHere.y, i * bfv.getTileWidth(), drawHere.y + drawHere.height);
		}
		for(int i = 0; i < bfv.getBattlefieldHeight(); i++)
		{
			g2d.drawLine(drawHere.x, i * bfv.getTileHeight(), drawHere.x+ drawHere.width, i * bfv.getTileHeight() );
		}

		//draw objects
		ArrayList<MovableObject> list = bfv.getObjectsList();
		for(int i = 0; i < list.size(); i++)
		{
			MovableObject tempObj = list.get(i);
			ImageIcon icon = (ImageIcon)tempObj.getView().getIcon();
			g2d.drawImage(icon.getImage(), tempObj.getX(), tempObj.getY(), c);
		}

		ArrayList<Wall> walls = bfv.getWallsList();
		if(walls.size() > 0)
		{
			//draw wall painter
			URL imgURL = getClass().getResource("/fmi/dndtabletop/resources/objects/free-stone-wall_mini.jpg");

			Image img = Toolkit.getDefaultToolkit().getImage(imgURL);

			// Create a buffered image with transparency
			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			// Draw the image on to the buffered image
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();

			TexturePaint paint = new TexturePaint(bimage, new Rectangle(10, 40, 20, 44));
			g2d.setPaint(paint);
			//g2d.setColor(Color.gray);
			g2d.setStroke(new BasicStroke(8.0F));
			for(int i = 0; i < walls.size(); i++)				
			{
				System.out.println(walls.get(i));
				g2d.drawLine(walls.get(i).getV1().x, walls.get(i).getV1().y, 
						walls.get(i).getV2().x,walls.get(i).getV2().y );
			}		
			
		}



	}
}