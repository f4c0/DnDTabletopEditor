package fmi.dndtabletop.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import fmi.dndtabletop.model.Battlefield;
import fmi.dndtabletop.model.MovableObject;
import fmi.dndtabletop.model.Tile;
import fmi.dndtabletop.model.Wall;
import fmi.dndtabletop.network.MessageHandler;
import fmi.dndtabletop.network.MessageHandler.Direction;
import fmi.dndtabletop.resources.ResourceManager;
import fmi.dndtabletop.resources.TileResource;

public class BattleView extends JPanel implements MouseListener, MouseMotionListener, KeyListener{

	private Battlefield m_bf;
	private DrawingMode m_drawingModeActivated;

	private Point m_point1;
	private Point m_point2;

	private JLayer<BattleView> m_layerUI;

	private boolean m_inGameMode;

	enum DrawingMode
	{
		NONE,
		LINE,
		RECT
	};

	public BattleView(int width, int height, int tileId, JLayer<BattleView> layer, boolean inGameMode)
	{
		super();
		this.m_layerUI = layer;
		this.m_bf = new Battlefield(width, height, tileId);		
		this.m_drawingModeActivated = DrawingMode.NONE;
		this.setLayout(new GridLayout(this.m_bf.getHeight(), this.m_bf.getWidth()));
		m_point1 = null;
		m_point2 = new Point();
		m_inGameMode = inGameMode;

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
						int tempId = Integer.valueOf(img);
						if(ResourceManager.getInstance().getObjectMap().containsKey(tempId))
						{
							MovableObject obj = new MovableObject(dtde.getLocation().x, dtde.getLocation().y, 0, tempId);
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
		this.addMouseMotionListener(this);
		this.addKeyListener(this);		
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvt) {

		requestFocusInWindow();

		JTree palette = ((MainWindow)SwingUtilities.getRoot(this)).getPaletteTree();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)palette.getLastSelectedPathComponent();			

		if (node != null)
		{
			if(node.isLeaf())
			{

				if(node.getParent().toString().equals(MainWindow.TREE_NODE_NAME_GND))
				{
					m_point1 = null;

					int cellX = mouseEvt.getX() / this.getComponent(0).getWidth();
					int cellY = mouseEvt.getY() / this.getComponent(0).getHeight();
					Tile t = this.m_bf.getTile(cellX, cellY);
					TileResource obj = (TileResource)node.getUserObject();
					t.setTextureTile(obj.getId());
				}else if(node.getParent().toString().equals(MainWindow.TREE_NODE_NAME_WALL))
				{
					if(m_point1 == null)
					{
						m_point1 = new Point(mouseEvt.getX(), mouseEvt.getY());
						m_point2.setLocation(m_point1);
						m_drawingModeActivated = DrawingMode.LINE;
					}else
					{
						Wall w = new Wall();
						w.addPoint(m_point1);
						w.addPoint(new Point(mouseEvt.getX(), mouseEvt.getY()));
						this.m_bf.addWall(w);
						m_point1 = null;
						m_drawingModeActivated = DrawingMode.NONE;
					}
				}else if(node.getParent().toString().equals(MainWindow.TREE_NODE_NAME_ROOM))
				{
					if(m_point1 == null)
					{
						m_point1 = new Point(mouseEvt.getX(), mouseEvt.getY());
						m_point2.setLocation(m_point1);
						m_drawingModeActivated = DrawingMode.RECT;
					}else
					{
						Wall w = new Wall();
						m_point2.setLocation(mouseEvt.getX(), mouseEvt.getY());
						w.addPoint(new Point(m_point1));
						w.addPoint(new Point(m_point2.x, m_point1.y));
						w.addPoint(new Point(m_point2.x, m_point2.y));
						w.addPoint(new Point(m_point1.x, m_point2.y));
						w.addPoint(new Point(m_point1));
						//System.out.println(w);
						this.m_bf.addWall(w);
						m_point1 = null;
						m_drawingModeActivated = DrawingMode.NONE;
					}
				}else if(node.getParent().toString().equals(MainWindow.TREE_NODE_NAME_OBJ))
				{
					ArrayList<MovableObject> objList = m_bf.getObjectsList();
					for(MovableObject obj : objList)
					{
						if(obj.IsCursorInBounds(mouseEvt))
						{							
							obj.switchSelection();
							break;
						}
					}
					m_point1 = null;
				}else
				{
					m_point1 = null;
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
	public void mousePressed(MouseEvent mouseEvt) {


	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		m_layerUI.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvt) {
		JTree palette = ((MainWindow)SwingUtilities.getRoot(this)).getPaletteTree();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)palette.getLastSelectedPathComponent();			

		if (node != null)
		{
			if(node.isLeaf())
			{				
				if(node.getParent().toString().equals(MainWindow.TREE_NODE_NAME_OBJ))
				{

					ArrayList<MovableObject> objList = m_bf.getObjectsList();
					for(MovableObject obj : objList)
					{
						if(obj.IsCursorInBounds(mouseEvt))
						{							
							obj.move(mouseEvt);
							break;
						}
					}
				}else
				{
					m_point1 = null;
				}
			}

		}	

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(m_drawingModeActivated != DrawingMode.NONE)
		{
			m_point2.setLocation(arg0.getX(), arg0.getY());
			m_layerUI.repaint();
		}
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

	public DrawingMode getDrawingMode()
	{
		return m_drawingModeActivated;
	}

	public Point getP1()
	{
		return m_point1;
	}

	public Point getP2()
	{
		return m_point2;
	}

	public void rotateSelectedObject(double value)
	{
		m_bf.rotateSelectedObject(value);
	}

	public Battlefield getBattlefieldModel()
	{
		return m_bf;
	}

	@Override
	public void keyPressed(KeyEvent kEvent) {
		try{
			switch(kEvent.getKeyCode())
			{
			case KeyEvent.VK_ESCAPE:
				m_drawingModeActivated = DrawingMode.NONE;
				m_point1 = null;
				m_layerUI.repaint();
				break;
			case KeyEvent.VK_Z:
				MessageHandler.getInstance().sendCmd_moveCam(Direction.UP, 3);
				break;
			case KeyEvent.VK_S:
				MessageHandler.getInstance().sendCmd_moveCam(Direction.DOWN, 3);
				break;
			case KeyEvent.VK_Q:
				MessageHandler.getInstance().sendCmd_moveCam(Direction.LEFT, 3);
				break;
			case KeyEvent.VK_D:
				MessageHandler.getInstance().sendCmd_moveCam(Direction.RIGHT, 3);
				break;
			default:
				break;
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


}
