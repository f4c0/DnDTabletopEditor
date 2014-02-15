package fmi.dndtabletop.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import fmi.dndtabletop.model.Battlefield;
import fmi.dndtabletop.model.MovableObject;
import fmi.dndtabletop.model.Tile;
import fmi.dndtabletop.model.Wall;
import fmi.dndtabletop.resources.ResourceManager;
import fmi.dndtabletop.resources.TileResource;

public class BattleView extends JPanel implements MouseListener{

	private Battlefield m_bf;
	private boolean m_drawingModeActivated;
	
	private Point m_point;

	public BattleView(int width, int height, long tileId)
	{
		super();
		this.m_bf = new Battlefield(width, height, tileId);
		this.m_drawingModeActivated = false;
		this.setLayout(new GridLayout(this.m_bf.getHeight(), this.m_bf.getWidth()));
		m_point = null;

		for(int y = 0; y < this.m_bf.getHeight(); y++)
		{
			for(int x = 0; x < this.m_bf.getWidth(); x++)
			{				
				this.add(this.m_bf.getTile(x, y).getView(), new Integer(0));
			}
		}

		this.setDropTarget(new DropTarget()
		{
			public void drop(DropTargetDropEvent dtde) 
			{
				try{
					String img = (String)dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);

					if(!img.equals(""))
					{
						long tempId = Long.valueOf(img);
						if(ResourceManager.getInstance().getObjectMap().containsKey(tempId))
						{
							MovableObject obj = new MovableObject(dtde.getLocation().x, dtde.getLocation().y, Math.toRadians(45), tempId);
							m_bf.addObject(obj);
							repaint();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		this.addMouseListener(this);

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		JTree palette = ((MainWindow)SwingUtilities.getRoot(this)).getPaletteTree();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)palette.getLastSelectedPathComponent();			

		if (node != null)
		{
			if(node.isLeaf())
			{
				if(node.getParent().toString().equals("Sols"))
				{
					m_point = null;
					
					int cellX = arg0.getX() / this.getComponent(0).getWidth();
					int cellY = arg0.getY() / this.getComponent(0).getHeight();
					Tile t = this.m_bf.getTile(cellX, cellY);
					TileResource obj = (TileResource)node.getUserObject();
					t.setTextureTile(obj.getId());
				}else if(node.getParent().toString().equals("Murs"))
				{
					if(m_point == null)
					{
						m_point = new Point(arg0.getX(), arg0.getY());
					}else
					{
						this.m_bf.addWall(new Wall(m_point, new Point(arg0.getX(), arg0.getY())));
						m_point = null;
						repaint();
					}
				}else
				{
					m_point = null;
				}
			}

		}	

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub


	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public ArrayList<MovableObject> getObjectsList()
	{
		return this.m_bf.getObjectsList();
	}
	
	public ArrayList<Wall> getWallsList()
	{
		return this.m_bf.getWallsList();
	}

	public int getBattlefieldWidth()
	{
		return this.m_bf.getWidth();
	}

	public int getBattlefieldHeight()
	{
		return this.m_bf.getHeight();
	}

	public int getTileWidth()
	{
		return (int)this.m_bf.getTile(0, 0).getView().getPreferredSize().getWidth();
	}

	public int getTileHeight()
	{
		return (int)this.m_bf.getTile(0, 0).getView().getPreferredSize().getHeight();
	}	

	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}
	
	public void setDrawingMode(boolean drawingModeActivated)
	{
		m_drawingModeActivated = drawingModeActivated;
	}
	
	public boolean getDrawingMode()
	{
		return m_drawingModeActivated;
	}
}
