package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;
import java.io.Serializable;

import android.util.Log;

//must implement Serializable to be able to get passed from one activity to another
public class Session implements Serializable {
	private String password;
	private String participant;
	private String researcher;
	
	private String date;
	private String time;
	
	private String sessionInfo; //Simon: not sure what this is for:(
	
	private File statisticsPath;
	private File statisticsFile; 
	


	public Session(String pass, String part, String research) {
		setPassword(pass);
		setParticipant(part);
		setResearcher(research);
		
		
		
		//get date from system and save it
		//get time from system and save it
	}
	
	public void updateStatistics(File statisticFile)
	{
		//todo: append new statistic data to the statisticFile while the participant is playing
	}
	
	public void saveSessionInfo()
	{
		//Simon: not sure what this is doing:(
	}
	
	///////////GET AND SET HERE:
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getResearcher() {
		return researcher;
	}

	public void setResearcher(String researcher) {
		this.researcher = researcher;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}	
}
