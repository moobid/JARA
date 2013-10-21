package com.eps_hioa_2013.JointAttentionResearchApp;

public class ElementVideo extends Element {

public String srcPath;
public String name;
	
public ElementVideo(String myVideoPath, String myName)
{
	name = myName; 
	srcPath = myVideoPath;
	//Theophile : temporary lignes
	srcPath = "/mnt/sdcard/Download/youShallNotPass.wmv";
}

public String getSrcPath()
{
	return srcPath;
}
}
