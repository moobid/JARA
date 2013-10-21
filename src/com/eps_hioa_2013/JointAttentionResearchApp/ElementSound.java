package com.eps_hioa_2013.JointAttentionResearchApp;

public class ElementSound extends Element {

public String srcPath;
public String name;
	
public ElementSound(String mySoundPath, String myName)
{
	name = myName; 
	srcPath = mySoundPath;
}

public String getSrcPath()
{
	return srcPath;
}

}
