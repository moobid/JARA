package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Environment;

//must implement Serializable to be able to get passed from one activity to another
public class Session implements Serializable {
	
	private static final long serialVersionUID = 4308126669324020168L; //Generated because of Serializable
	private String password;
	private String participant;
	private String researcher;
	
	private Date currentDate;
	private Date deadlineDate = null;
	
	private String filename;
	PrintWriter pw = null;

    private List<Element> elementlist = new ArrayList<Element>();
    
    private List<String> modulenames = new ArrayList<String>();

	//sets Membervaribales (incl. current Date) + calls updateStatistics() the first time
	public Session(String pass, String part, String research) {
		setPassword(pass);
		setParticipant(part);
		setResearcher(research);
		currentDate = new Date();
		setFilename();

		updateStatistics("Filename:    "+this.getFilename()
					  +"\nParticipant: "+this.getParticipant()
					  +"\nResearcher:  "+this.getResearcher()
				      +"\nSessionDate: "+this.getcurrentDate().toString()); //will create a new file at the beginning and later(while playing) update this one
		
	}
	//updates (creates on 1. time) the statisticfile. The String newData gets appended to it
	public void updateStatistics(String newData)
	{
		if(isExternalStorageWritable()) //checks if ExternalStorage is available
		{	
				PrintWriter pw = null;
				String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Jara/Statistics/";
		        try{
		        	pw = new PrintWriter(new FileWriter(dirPath+filename+".txt", true));
	            	pw.write(newData);
	                pw.println();
		        }
		        catch(Exception e) {
		            System.out.println(e);
		        }
		        finally {
		            if (pw != null) {
		                pw.close();
		            }
		        }		
		}
		else
		{
			//todo: show error
		}
	}
	
	//checks if the external storage (sd-card) is available
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public List<String> getModulenames() {
		return modulenames;
	}
	public void setModulenames(List<String> modulenames) {
		this.modulenames = modulenames;
	}
	public List<Element> getElementlist() {
		return elementlist;
	}
	public void setElementlist(List<Element> elementlist) {
		this.elementlist = elementlist;
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
	
	public Date getcurrentDate() {
		return currentDate;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public String getFilename() {
		return filename;
	}
	//creates the filename with the input the user gave in the startscreen
	public void setFilename() {
		this.filename = this.getParticipant()
				+"&&"+this.getResearcher()
				+"&&"+this.getcurrentDate().toString();
	}

}
