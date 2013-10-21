package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;

import android.app.Activity;
import android.content.SharedPreferences;

public class Module{
	//private File Settingsfile; //should be the txt file, where the settings of the module are saved //not used anymore
	
	private static int number; //number of the module
	private String name;
	private String description;
	private Element preactionelements[]; //Pictures in it
	private Element signalelements[]; //Pictures in it
	private Element actionelements[]; //Pictures in it
	private Element rewardelements[];
	
	
	
	public Module(int myNumber, String myName, String myDescription)
	{
		number = myNumber;
		name = myName;
		description = myDescription;
		//Edition Theophile
		rewardelements = new Element[1];
		rewardelements[0]= new ElementVideo("/mnt/sdcard/Download/youShallNotPass.wmv", "youShallNotPass");
		//Edition Theophile END
	}

	public Element getRandomRewardElement()
	{
		return rewardelements[(int)(Math.random() * (rewardelements.length-1))];
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}

	public static int getNumber() {
		return number;
	}

	public static void setNumber(int number) {
		Module.number = number;
	}
}
