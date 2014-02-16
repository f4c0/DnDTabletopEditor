package fmi.dndtabletop.ihm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.LayerUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import fmi.dndtabletop.network.MessageHandler;
import fmi.dndtabletop.resources.DrawnResource;
import fmi.dndtabletop.resources.MovableResource;
import fmi.dndtabletop.resources.ResourceManager;
import fmi.dndtabletop.resources.TileResource;

public class MainWindow extends JFrame{

	private BattleView m_battleView;
	private JScrollPane m_centerSc;
	private MetricRule m_ColRule;
	private MetricRule m_RowRule;
	private JTree m_tree;
	
	public static final String TREE_NODE_NAME_WALL = "Murs";
	public static final String TREE_NODE_NAME_GND = "Sols";
	public static final String TREE_NODE_NAME_OBJ = "Objets";			

	private static final String APP_NAME = "DnD Tabletop Editor";
	private MessageHandler m_network;

	private static final String EXT_FILE = "btf";
	private JLayer<BattleView> m_jlayer;

	public MainWindow()
	{
		super(APP_NAME);		
		m_network = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPanel = (JPanel)this.getContentPane();

		createMenu();		

		contentPanel.setLayout(new BorderLayout());

		//Center pane
		JComponent cpanel = new JPanel();		
		cpanel.setLayout(new GridLayout(1,1));	
		//m_battleView = new BattleView(1,1,0);
		
		
		LayerUI<BattleView> layerUI = new BattlefieldLayerUI();
		m_jlayer = new JLayer<BattleView>(m_battleView, layerUI);
		
		m_centerSc = new JScrollPane(m_jlayer);
		m_ColRule = new MetricRule(MetricRule.HORIZONTAL, false, null);
		m_RowRule = new MetricRule(MetricRule.VERTICAL, false, null);
		//m_ColRule.resize(m_battleView);
		//m_RowRule.resize(m_battleView);
		m_centerSc.setColumnHeaderView(m_ColRule);
		m_centerSc.setRowHeaderView(m_RowRule);

		cpanel.setOpaque(true);
		cpanel.add(m_centerSc);

		m_centerSc.setVisible(false);

		m_tree = createTree();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_tree, cpanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);


		contentPanel.add(splitPane, BorderLayout.CENTER);
	}

	private void createMenu(){
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFichier = new JMenu("Fichier");
		JMenuItem menuItemNouveau = new JMenuItem("Nouveau");
		menuItemNouveau.addActionListener(new FileMenuListener());
		JMenuItem menuItemOuvrir = new JMenuItem("Ouvrir");
		menuItemOuvrir.addActionListener(new FileMenuListener());
		JMenuItem menuItemEnregistre = new JMenuItem("Enregistrer sous...");
		menuItemEnregistre.addActionListener(new FileMenuListener());		
		JMenuItem menuItemExportMesh = new JMenuItem("Exporter mesh");
		menuItemExportMesh.addActionListener(new FileMenuListener());		
		JMenuItem menuItemQuitter = new JMenuItem("Quitter");
		menuItemQuitter.addActionListener(new FileMenuListener());
		menuFichier.add(menuItemNouveau);
		menuFichier.add(menuItemOuvrir);
		menuFichier.add(menuItemEnregistre);
		menuFichier.addSeparator();
		menuFichier.add(menuItemExportMesh);
		menuFichier.addSeparator();
		menuFichier.add(menuItemQuitter);

		JMenu menuNetwork = new JMenu("R\u00E9seau");
		JMenuItem menuItemConnection = new JMenuItem("Connexion au renderer distant");
		menuItemConnection.addActionListener(new NetworkMenuListener());
		JMenuItem menuItemPush = new JMenuItem("Push");
		menuItemPush.addActionListener(new NetworkMenuListener());
		JMenuItem menuItemSendRawMessage = new JMenuItem("Send raw datagram");
		menuItemSendRawMessage.addActionListener(new NetworkMenuListener());
		menuNetwork.add(menuItemConnection);
		menuNetwork.add(menuItemPush);
		menuNetwork.add(menuItemSendRawMessage);

		JMenu menuInfo = new JMenu("?");
		JMenuItem menuItemApropos = new JMenuItem("A propos de "+APP_NAME);
		menuInfo.add(menuItemApropos);

		menuBar.add(menuFichier);
		menuBar.add(menuNetwork);
		menuBar.add(menuInfo);

		this.setJMenuBar(menuBar);
	}

	private JTree createTree()
	{
		JTree tree;
		DefaultMutableTreeNode top 		=  new DefaultMutableTreeNode("Palette");
		DefaultMutableTreeNode grounds 	=  new DefaultMutableTreeNode(TREE_NODE_NAME_GND);
		DefaultMutableTreeNode walls 	=  new DefaultMutableTreeNode(TREE_NODE_NAME_WALL);
		DefaultMutableTreeNode objects 	=  new DefaultMutableTreeNode(TREE_NODE_NAME_OBJ);

		top.add(grounds);
		top.add(walls);
		top.add(objects);

		ResourceManager resMan = ResourceManager.getInstance();

		for (Map.Entry<Long, TileResource> entry : resMan.getTextureTileMap().entrySet())
		{
			DefaultMutableTreeNode textNode =  new DefaultMutableTreeNode(entry.getValue());
			grounds.add(textNode);
		}
		
		//tests !
		DrawnResource resWall;
		try{
		resWall = new DrawnResource("Mur simple", "/fmi/dndtabletop/resources/objects/", "free-stone-wall_mini.jpg");
		walls.add(new DefaultMutableTreeNode(resWall));
		}catch(IOException e)
		{
			m_battleView.setDrawingMode(true);
		}
		
		
		for (Map.Entry<Long, MovableResource> entry : resMan.getObjectMap().entrySet())
		{
			DefaultMutableTreeNode textNode =  new DefaultMutableTreeNode(entry.getValue());
			objects.add(textNode);
		}

		tree = new JTree(top);
		tree.setDragEnabled(true);
		tree.setTransferHandler(new TransferHandler()
		{
			protected Transferable createTransferable(JComponent c)
			{
				JTree tree = (JTree)c;
				String  path = "";
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						tree.getLastSelectedPathComponent();			

				if (node != null)
				{//Node selected
					if(node.isLeaf())
					{
						if(node.getUserObject() instanceof MovableResource)
						{
							MovableResource obj = (MovableResource)node.getUserObject();
							path = ""+obj.getId();
						}
					}

				}	
				return new StringSelection(path);
			}
			public int getSourceActions(JComponent c)
			{
				return TransferHandler.COPY;
			}
		});
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(new PaletteTreeCellRenderer());

		return tree;
	}
	
	public JTree getPaletteTree()
	{
		return m_tree;
	}
	
	public JLayer<BattleView> getBattleViewUI()
	{
		return m_jlayer;
	}

	public class FileMenuListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {

			String sourceText = ((JMenuItem)arg0.getSource()).getText();
			if(sourceText.equals("Nouveau"))
			{
				BattleView bvTemp = new CreateBattlefieldDialog(null, "Nouveau champ de bataille", true, MainWindow.this).showDialog();
				if(bvTemp != null)
				{
					m_battleView = bvTemp;
					m_jlayer.setView(m_battleView);
					m_centerSc.setViewportView(m_jlayer);
					
					m_ColRule.resize(m_battleView);
					m_RowRule.resize(m_battleView);

					m_centerSc.setColumnHeaderView(m_ColRule);
					m_centerSc.setRowHeaderView(m_RowRule);
					m_centerSc.setVisible(true);
					m_centerSc.repaint();
				}

			}else if(sourceText.equals("Ouvrir"))
			{
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Battlefield (*."+EXT_FILE+")", EXT_FILE);
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(MainWindow.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					
					String absFileName = chooser.getSelectedFile().getAbsolutePath();
					if(!(absFileName.contains(EXT_FILE)))
					{
						absFileName = absFileName + "." + EXT_FILE;
					}
					
					System.out.println("You chose to open this file: " +
							absFileName);

					try {

						FileInputStream fichier = new FileInputStream(absFileName);
						ObjectInputStream ois = new ObjectInputStream(fichier);
						m_battleView = (BattleView) ois.readObject();
						m_jlayer.setView(m_battleView);
						m_centerSc.setViewportView(m_jlayer);
						
						m_ColRule.resize(m_battleView);
						m_RowRule.resize(m_battleView);

						m_centerSc.setColumnHeaderView(m_ColRule);
						m_centerSc.setRowHeaderView(m_RowRule);
						m_centerSc.setVisible(true);
						m_centerSc.repaint();
						ois.close();
					}

					catch (java.io.IOException e) {

						e.printStackTrace();

					}

					catch (ClassNotFoundException e) {

						e.printStackTrace();

					}
				}
			}else if(sourceText.equals("Enregistrer sous..."))
			{
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Battlefield (*."+EXT_FILE+")", EXT_FILE);
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(MainWindow.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {

					String absFileName = chooser.getSelectedFile().getAbsolutePath();
					if(!(absFileName.contains(EXT_FILE)))
					{
						absFileName = absFileName + "." + EXT_FILE;
					}

					System.out.println("You chose to save this file: " +
							absFileName);

					try {
						FileOutputStream fichier = new FileOutputStream(absFileName);
						ObjectOutputStream oos = new ObjectOutputStream(fichier);
						oos.writeObject(m_battleView);
						oos.flush();
						oos.close();
					}

					catch (java.io.IOException e) {

						e.printStackTrace();

					}
				}
			}			
			else if(sourceText.equals("Quitter"))
			{
				if(m_network != null)
				{
					m_network.close();
				}
				System.exit(0);
			}
		}    
	}

	public class NetworkMenuListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {     

			String sourceText = ((JMenuItem)arg0.getSource()).getText();
			if(sourceText.equals("Connexion au renderer distant"))
			{
				String serveur = JOptionPane.showInputDialog(null, "Entrez l'adresse du serveur IP:PORT (localhost:33333)", "Connexion au renderer distant", JOptionPane.QUESTION_MESSAGE);
				if(serveur != null)
				{
					StringTokenizer st = new StringTokenizer(serveur, ":");
					if(st.countTokens() == 2)
					{
						try
						{
							String host = st.nextToken();
							int port = Integer.parseInt(st.nextToken());
							if(m_network == null)
							{
								m_network = new MessageHandler(host, port);
							}else
							{
								m_network.reconfigure(host, port);
							}
						}catch(UnknownHostException e)
						{
							JOptionPane.showMessageDialog(null, "Host inconnu!\n"+e.toString(), "UnknownHostException", JOptionPane.ERROR_MESSAGE);
						}catch(SocketException e)
						{
							JOptionPane.showMessageDialog(null, "Impossible de cr\u00E9er le socket!\n"+e.toString(), "SocketException", JOptionPane.ERROR_MESSAGE);
						}
						catch(Exception e)
						{
							JOptionPane.showMessageDialog(null, "Erreur inconnue!\n"+e.toString(), "Exception", JOptionPane.ERROR_MESSAGE);
						}
					}else
					{
						JOptionPane.showMessageDialog(null, "Format invalide!", "Exception", JOptionPane.ERROR_MESSAGE);
					}

				}
			}else if(sourceText.equals("Push"))
			{

			}else if(sourceText.equals("Send raw datagram"))
			{
				if(m_network != null)
				{
					String message = JOptionPane.showInputDialog(null, "Message", "Send raw datagram", JOptionPane.QUESTION_MESSAGE);
					if(message != null)
					{
						try{
							m_network.sendRawMessage(message);
						}catch (IOException e)
						{
							JOptionPane.showMessageDialog(null, "Erreur d'E/S!\n"+e.toString(), "IOException", JOptionPane.ERROR_MESSAGE);
						}	    				
					}

				}else
				{
					JOptionPane.showMessageDialog(null, "Connecte toi d'abord !", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
				}
			}

		}    
	}

	public class InfoMenuListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {      
			//Idem
		}    
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}

		MainWindow mainWin = new MainWindow();
		mainWin.pack();
		mainWin.setSize(800, 600);
		mainWin.setLocationRelativeTo(null);
		mainWin.setVisible(true);
	}

	public static void main(String[] args) { 

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});



	}

}

