package fmi.dndtabletop.resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.swing.ImageIcon;

public class Resource {

	private int m_id;
	private String m_name;
	private String m_fileName;
	private ImageIcon m_Img;

	public Resource(String name, int id, String path, String fileName) throws FileNotFoundException
	{
		m_name = name;
		m_id = id;
		m_fileName = fileName;

		URL imgURL = getClass().getResource(path + fileName);
		if(imgURL == null)
		{
			throw new FileNotFoundException("Le fichier \""+ path + fileName +"\" n'existe pas !");
		}
		Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
		m_Img = new ImageIcon(img);
	}

	public int getId() {
		return m_id;
	}

	public String getName() {
		return m_name;
	}

	public String getM_fileName() {
		return m_fileName;
	}

	public ImageIcon getImage()
	{
		return m_Img;
	}
}
