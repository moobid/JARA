package com.eps_hioa_2013.JointAttentionResearchApp;

public class Session {
	String password;
	String participant;
	String researcher;
	
	String date;
	String time;
	


	public Session(String pass, String part, String research) {
		setPassword(pass);
		setParticipant(part);
		setResearcher(research);
		
		
		//get date from system and save it
		//get time from system and save it
	}
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
