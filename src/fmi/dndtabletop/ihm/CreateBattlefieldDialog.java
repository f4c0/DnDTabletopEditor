package fmi.dndtabletop.ihm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import fmi.dndtabletop.resources.Resource;
import fmi.dndtabletop.resources.ResourceManager;
import fmi.dndtabletop.resources.TileResource;

public class CreateBattlefieldDialog extends JDialog {

	private BattleView m_bf;
	private JFormattedTextField txtWidth;
	private JFormattedTextField txtHeight;
	private JList tileList;
	private MainWindow m_mainWin;

	public CreateBattlefieldDialog(JFrame parent, String title, boolean modal, MainWindow mainWin){
		super(parent, title, modal);
		this.m_mainWin = mainWin;
		this.setSize(320, 210);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initComponent();
	}

	public BattleView showDialog(){
		this.setVisible(true);
		
		return this.m_bf;      
	}

	private void initComponent(){
		
		this.setLayout(new BorderLayout());
		JPanel panData = new JPanel();
		panData.setLayout(new GridLayout(2, 2));
		
		JLabel labWidth = new JLabel("Largeur (en cases): ");
		JLabel labHeight = new JLabel("Hauteur (en cases): ");
		txtWidth = new JFormattedTextField();
		txtWidth.setValue(new Integer(1));
		txtWidth.setColumns(3);
		txtHeight = new JFormattedTextField();
		txtHeight.setValue(new Integer(1));
		txtHeight.setColumns(3);
		panData.add(labWidth);
		panData.add(txtWidth);
		panData.add(labHeight);
		panData.add(txtHeight);
		panData.setBorder(new TitledBorder("Dimensions"));
		
		JPanel panTile = new JPanel();
		panTile.setLayout(new GridLayout(1, 1));
		ResourceManager resMan = ResourceManager.getInstance();
		Vector<Resource> vResources = new Vector<Resource>();
		for (Map.Entry<Integer, TileResource> entry : resMan.getTextureTileMap().entrySet())
		{
			vResources.add(entry.getValue());
		}
		tileList = new JList(vResources);
		
		tileList.setCellRenderer(new ListIconRenderer());	
		//panTile.add(new JLabel("Texture de base: "));
		panTile.add(new JScrollPane(tileList));
		panTile.setBorder(new TitledBorder("Texture de base"));
		
		JPanel panButton = new JPanel();
		panButton.setLayout(new GridLayout(1, 4));
		
		JButton bOk = new JButton("OK");
		JButton bCancel = new JButton("Annuler");		
		
		panButton.add(bOk);
		panButton.add(bCancel);		
		
		this.add(panData, BorderLayout.NORTH);
		this.add(panTile, BorderLayout.CENTER);
		this.add(panButton, BorderLayout.SOUTH);
		
		bOk.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {				
		
				m_bf = new BattleView(Integer.valueOf(txtWidth.getText()), Integer.valueOf(txtHeight.getText()), ((Resource)tileList.getSelectedValue()).getId(), m_mainWin.getBattleViewUI(), m_mainWin.getInGameMode());				
				setVisible(false);
			}
		});
		
		bCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				m_bf = null;
				setVisible(false);
				
			}
		});
	}  
}
