package fmi.dndtabletop.resources;

import java.io.FileNotFoundException;

public class MovableResource extends Resource {

	public MovableResource(String name, String path, String fileName) throws FileNotFoundException
	{
		super(name,path, fileName);
	}
}
