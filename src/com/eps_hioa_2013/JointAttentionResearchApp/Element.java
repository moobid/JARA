package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;
import java.io.Serializable;

public class Element implements Serializable {
	private String name;
	
	private String path;
	private File file;
	
	private int type;
	
	public Element(String myPath, int myType)
	{
		setType(myType);
		setPath(myPath);
	}
	
	public void showElement()
	{
		//todo
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		String name = path.substring(path.lastIndexOf("/")+1);
		setName(name.substring(0, name.lastIndexOf(".")));
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

}
