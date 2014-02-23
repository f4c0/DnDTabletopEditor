package fmi.dndtabletop.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;

import fmi.dndtabletop.ihm.BattleView.DrawingMode;
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

		//Draw objects
		ArrayList<MovableObject> list = bfv.getObjectsList();
		for(MovableObject obj : list)
		{
			// Push matrix
			AffineTransform saveXform = g2d.getTransform();			
			ImageIcon icon = (ImageIcon)obj.getView().getIcon();
			
			int xCenter = icon.getIconWidth()/2;
			int yCenter = icon.getIconHeight()/2;
			g2d.translate(obj.getX()+xCenter, obj.getY()+yCenter);
			g2d.rotate(obj.getRotation());
			g2d.drawImage(icon.getImage(), -xCenter, -yCenter, c);			
			
			if(obj.isSelected())
			{
				g2d.setColor(Color.red);
				g2d.drawRect(-xCenter, -yCenter, icon.getIconWidth(), icon.getIconHeight());
			}else
			{
				g2d.setColor(Color.black);
			}
			
			// Pop matrix
			g2d.setTransform(saveXform);
		}

		g2d.setColor(Color.black);
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

			g2d.setStroke(new BasicStroke(8.0F));
			for(int i = 0; i < walls.size(); i++)				
			{
				Path2D.Float wallShape = new Path2D.Float();
				ArrayList<Point> points = walls.get(i).getPoints();
				for(int j = 0; j < points.size(); j++)
				{
					if(j == 0)
					{
						wallShape.moveTo(points.get(j).x, points.get(j).y);
					}else
					{
						wallShape.lineTo(points.get(j).x, points.get(j).y);
					}
				}
				
				g2d.draw(wallShape);
			}		
		}
		
		if(bfv.getDrawingMode() == DrawingMode.LINE)
		{
			g2d.setStroke(new BasicStroke(3.0F));
			g2d.setColor(Color.blue);
			g2d.drawLine(bfv.getP1().x, bfv.getP1().y, 
					bfv.getP2().x, bfv.getP2().y );
		}else if(bfv.getDrawingMode() == DrawingMode.RECT)
		{
			g2d.setStroke(new BasicStroke(3.0F));
			g2d.setColor(Color.blue);
			g2d.drawRect(Math.min(bfv.getP1().x, bfv.getP2().x), Math.min(bfv.getP1().y, bfv.getP2().y), 
					Math.abs(bfv.getP2().x - bfv.getP1().x), Math.abs(bfv.getP2().y - bfv.getP1().y));
		}



	}
}