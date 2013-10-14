package com.eps_hioa_2013.JointAttentionResearchApp;

import java.util.Date;

public class Timecounter extends Thread {

	private Date deadlineDate;
	private boolean timeover = true;
	
	public Timecounter(Date deadlineDate)
	{

		this.deadlineDate = deadlineDate;
		checkTimeLoop();
	}
	
	
	public void checkTimeLoop()
	{	
		while(true)
		{
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			//todo: check if currentDate bigger as deadlineDate to abort the game
			
		}		 
	}
}
