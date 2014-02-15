package fmi.dndtabletop.resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.MessageDigest;

import javax.swing.ImageIcon;

public class Resource {

	private long m_id;
	private String m_name;
	private String m_fileName;
	private ImageIcon m_Img;

	public Resource(String name, String path, String fileName) throws FileNotFoundException
	{
		m_name = name;
		m_fileName = fileName;

		String fullpath = path + fileName;

		URL imgURL = getClass().getResource(fullpath);
		if(imgURL == null)
		{
			throw new FileNotFoundException("Le fichier \""+ path + fileName +"\" n'existe pas !");
		}
		Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
		m_Img = new ImageIcon(img);

		m_id = generateId(name+"/"+fullpath);
	}

	public long getId() {
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

	private long generateId(String s)
	{
		int intLength = s.length() / 4;
		long sum = 0;
		for (int j = 0; j < intLength; j++) {
			char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
			long mult = 1;
			for (int k = 0; k < c.length; k++) {
				sum += c[k] * mult;
				mult *= 256;
			}
		}

		char c[] = s.substring(intLength * 4).toCharArray();
		long mult = 1;
		for (int k = 0; k < c.length; k++) {
			sum += c[k] * mult;
			mult *= 256;
		}

		return Math.abs(sum);

	}

}
