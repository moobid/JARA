package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

//must implement Serializable to be able to get passed from one activity to another
public class Session implements Serializable {
	
	private String password;
	private String participant;
	private String researcher;
	
	private Date currentDate;
	
	private String sessionInfo; //Simon: not sure what this is for:(
	
	private String filename;
	private File statisticsPath;
	private File statisticsFile;
	


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
	
	public void updateStatistics(String newData)
	{
		if(isExternalStorageWritable()) //checks if ExternalStorage is available
		{	
			if(/*file exists*/true) /*file exists*/
			{
				try
				{
					FileOutputStream fOut = new FileOutputStream(statisticsFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					
					myOutWriter.append(newData); //new data gets appended
					
					myOutWriter.close();
					fOut.close();
				} catch (Exception e) {
					//todo: error
				}
			}
			if(/*file does not exist*/true) /*file does not exist*/
			{		
				try {
					statisticsFile = new File("/sdcard/"+filename+".txt");
					statisticsFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(statisticsFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append(newData); //the header of the file gets created
					myOutWriter.close();
					fOut.close();
					
				} catch (Exception e) {
					//todo: error
				}
			}
		}
		else
		{
			//todo: show error
		}
	}
	
	public void saveSessionInfo()
	{
		//Simon: not sure what this is doing:(
	}
	
	//checks if the external storage (sd-card) is available
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public File getAlbumStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory. 
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_DOWNLOADS), albumName);
	    if (!file.mkdirs()) {
	        Log.e("1", "Directory not created");
	    }
	    return file;
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

	public String getFilename() {
		return filename;
	}

	public void setFilename() {
		this.filename = "P="+this.getParticipant()
				+"&R="+this.getResearcher()
				+"&D="+this.getcurrentDate().toString();
	}
}
