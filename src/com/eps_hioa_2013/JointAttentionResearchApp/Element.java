package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;
import java.io.Serializable;

/*
 * Represents an Element and contains its properties
 *
 * 
 * @author Leon van Tuijl, Simon Irsch
 */

public class Element implements Serializable {

	private static final long serialVersionUID = 2111016546249060952L;//Generated because of Serializable

	private String name;
	
	private String path;
	private File file;
	
	private int moduleNumber;
	private int type;
	private int imageButtonID;
	
	public Element(String myPath, int myType)
	{
		setType(myType);
		setPath(myPath);
		moduleNumber = -1;
	}
	
	public int getModuleNumber()
	{
		return moduleNumber;
	}
	public int getImageButtonID()
	{
		return imageButtonID;
	}
	
	public void setImageButtonID(int id)
	{
		imageButtonID = id;
	}
	

	public void setModuleNumber(int number)
	{
		moduleNumber = number;
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
		setName(name);
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
