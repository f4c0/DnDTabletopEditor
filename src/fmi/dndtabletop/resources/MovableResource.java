package fmi.dndtabletop.resources;

import java.io.FileNotFoundException;

public class MovableResource extends Resource {

	public MovableResource(int id, String name, String path, String fileName) throws FileNotFoundException
	{
		super(id, name,path, fileName);
	}
}
