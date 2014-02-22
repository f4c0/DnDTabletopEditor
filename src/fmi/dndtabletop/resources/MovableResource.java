package fmi.dndtabletop.resources;

import java.io.FileNotFoundException;

public class MovableResource extends Resource {

	public MovableResource(String name, int id, String path, String fileName) throws FileNotFoundException
	{
		super(name, id, path, fileName);
	}
}
