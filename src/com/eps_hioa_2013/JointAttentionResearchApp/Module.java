package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;

import android.app.Activity;
import android.content.SharedPreferences;

public class Module{
	//private File Settingsfile; //should be the txt file, where the settings of the module are saved //not used anymore
	
	private static int number; //number of the module
	private String name;
	private String description;
	private Element elements[];
	
	public Module(int myNumber, String myName, String myDescription)
	{
		number = myNumber;
		name = myName;
		description = myDescription;
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
