package fmi.dndtabletop.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class MetricRule extends JComponent {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private static final int SIZE = 25;

	private static final char LETTER[]=
		{
		'A','B','C','D','E','F','G','H',
		'I','J','K','L','M','N','O','P',
		'Q','R','S','T','U','V','W','X',
		'Y','Z'
		};



	private int m_orientation;
	private BattleView m_bv;

	public MetricRule(int orientation, boolean isInMeter, BattleView battleView)
	{
		//super();
		m_orientation = orientation;
		m_bv = battleView;
	}

	public void resize(BattleView battleView)
	{
		m_bv = battleView;
		if(m_orientation == HORIZONTAL)
		{
			setPreferredSize(new Dimension(m_bv.getBattlefieldWidth() * m_bv.getTileWidth(), SIZE));
		}else
		{
			setPreferredSize(new Dimension(SIZE, m_bv.getBattlefieldHeight() * m_bv.getTileHeight()));
		}
	}

	private String generateRowName(int value)
	{
		String s;
		if(value < 10)
		{
			s = "0"+value;
		}else
		{
			s = new Integer(value).toString();
		}

		return s;
	}

	private String generateColumnName(int value)
	{
		String s;
		if(value < 26)
		{
			s = " "+LETTER[value];
		}else
		{
			int msv = value / 26 - 1;
			s = "" + LETTER[msv] + LETTER[value%26];
		}

		return s;
	}

	protected void paintComponent(Graphics g) {

		super.paintComponents(g);

		Rectangle drawHere = g.getClipBounds();		 

		//Fill clipping area with dirty brown/orange.
		g.setColor(new Color(130, 30, 30));
		g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

		// Do the ruler labels in a small font that's black.
		g.setFont(new Font("SansSerif", Font.BOLD, 12));

		if(m_orientation == HORIZONTAL)
		{

			for(int i = 0; i < m_bv.getBattlefieldWidth(); i++)
			{
				g.setColor(Color.white);
				g.drawString(generateColumnName(i).toString(), (m_bv.getTileWidth() / 2) + i * m_bv.getTileWidth() -4, SIZE/2);
				g.setColor(Color.black);
				g.drawLine(i * m_bv.getTileWidth(), SIZE/2, i * m_bv.getTileWidth(), SIZE);
			}

		}else
		{
			for(int i = 0; i < m_bv.getBattlefieldHeight(); i++)
			{
				g.setColor(Color.white);
				g.drawString(generateRowName(i), SIZE/2-4, (m_bv.getTileHeight() / 2) + i * m_bv.getTileHeight()+4);
				g.setColor(Color.black);
				g.drawLine(SIZE/2, i * m_bv.getTileHeight(), SIZE, i * m_bv.getTileHeight());
			}
		}
	}
}
