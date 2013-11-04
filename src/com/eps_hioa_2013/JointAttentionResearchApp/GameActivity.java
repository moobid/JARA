package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

public class GameActivity extends Activity {
	private ImageButton topleft;
	private ImageButton topmid;
	private ImageButton topright;
	private ImageButton midleft;
	private ImageButton midmid;
	private ImageButton midright;
	private ImageButton bottomleft;
	private ImageButton bottommid;
	private ImageButton bottomright;
	
	private int stagecounter = 0; //0 = Preaction; 1 = Action; 2 = Signal; 3 = reward
	private int roundcounter = 0;
	private int roundcounterlimit;
	
	private Date DateStartedPlaying = null;
	private int timeToPlayInSeconds;
	//private Timecounter timecounter;
	
	private boolean PreactionPresent = true;
	
	private Module mymodule;
	private String modulenumber;
	private Session mysession;	
	private String[] stages = {"preaction", "signal", "action", "reward"};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		//set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    setContentView(R.layout.activity_game);
	    
	    System.out.println("GameActivity started");
		
	    Intent intent = getIntent();		
		mysession = (Session) intent.getSerializableExtra(ModuleSettingsActivity.EXTRA_SESSION);		
		modulenumber = (intent.getStringExtra(ModuleSettingsActivity.MODULENUMBER));
		roundcounterlimit = (int) intent.getIntExtra(ModuleSettingsActivity.EXTRA_ROUNDSTOPLAY, 0);		
		timeToPlayInSeconds = (int) intent.getIntExtra(ModuleSettingsActivity.EXTRA_TIME, 0);
		DateStartedPlaying = new Date();
		
		initializeViews(); //initializes the views (Imagebuttons)
		
		//todo: if there's no preaction in module, then the stagecounter has to be 1 in beginning (just in the beginning!)
		
		mysession.updateStatistics("\n\n" +
			"Started Playing a module\n" +
			"Started playing: " + DateStartedPlaying + "\n" +
			"Modulename: " + getNameOfModule(modulenumber) + "\n" +
			"Preferencename: " + "MODULE" + modulenumber + "\n" +
			"Moduledescription: " + getDescriptionOfModule(modulenumber) + "\n" +
			"Time to play in s: " + timeToPlayInSeconds + "\n" +	
			"Rounds to play: " + roundcounterlimit + "\n"			
				);
		
		//starts the time and makes sure to end it, if the time is over; not working; dont know how to do
		//timecounter = new Timecounter(mysession.getDeadlineDate());
		mymodule = new Module();
		loadGameInfo(mysession, modulenumber);
		nextStage();
	}

	private void nextStage() {
		// TODO Auto-generated method stub
		
	}

	//Load elements belonging to this module and put them in the appropriate arrays.
	private void loadGameInfo(Session mysession, String modulenumber) {
		int size = mysession.getElementlist().size();
		String currentStage = "";
		for(int o = 0; o < 4; o++)
		{
			currentStage = stages[o];
			for(int i = 0; i < size; i++)
			{
				Element gameElement = mysession.getElementlist().get(i);
				if(getBooleanOfModule(modulenumber, gameElement.getName() + currentStage))
				{
					mymodule.addElement(o, gameElement);					
				}
			}
		}
		
	}


	public void onclick_touched(View view)
	{		
		switch(stagecounter)
		{
		case 0: //0 = Preaction;
			//todo: if(view.getId() == id of indeed a Preactionimage)
			{
				//todo: showAllActions()
				//.....
			}
			
			stagecounter++;
				break;
		case 1: //1 = Action
		
			//after certain amount of time:
			//todo: showAllSignals()
			stagecounter++;
				break;
		case 2: //2 = Signal
			//todo: if(view.getId() == id of indeed a Actionimage)
			{
				//show reward:
				Element myReward = mymodule.getRandomRewardElement();
				if (myReward instanceof ElementVideo)
				{
					playMyVideo((ElementVideo)myReward);
				}
				if (myReward instanceof ElementSound)
				{
					playMySound((ElementSound)myReward);
				}
				if (myReward instanceof ElementPicture)
				{
					//todo: show picture over whole screen
				}
				
				if(/*todo: if no preaction*/false) stagecounter = 1; //if no Preaction selected
				else stagecounter = 0;
				
				roundcounter++;
				stagecounter++;
			}
				break;
		}
		
		if(roundcounter >= roundcounterlimit) //check if limit of rounds reached
		{			
			//end game
		}
		
	}
	
	public void playMyVideo(ElementVideo myVideo)
	{
		final VideoView video = ((VideoView)findViewById(R.id.videoView1));
		//video.se
		video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer v) {
				// TODO Auto-generated method stub
				video.setVisibility(View.GONE);
			}
		});
		
		
		video.setVisibility(View.VISIBLE);
		video.setVideoURI(Uri.parse(myVideo.getPath()));
		video.requestFocus();
		video.start();
	}
	

	public void playMySound(ElementSound mySound)
	{
		MediaPlayer mPlayer = new MediaPlayer();
		File file = new File(mySound.getPath());
		
		    if(mPlayer != null) {
		        mPlayer.stop();
		        mPlayer.release();
		    }
		    mPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+mySound.getPath()));
		    mPlayer.start();
	}
	public void playMyPicture(ElementPicture myPicture)
	{
		ImageView myPhoto = ((ImageView)findViewById(R.id.imageView1));
		myPhoto.setImageURI(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+myPicture.getPath()));
		myPhoto.setVisibility(View.VISIBLE);
		Chronometer myChrono = ((Chronometer)findViewById(R.id.chronometer1));
		myChrono.setBase(0);
		myChrono.start();
		myChrono.setOnChronometerTickListener(new OnChronometerTickListener(){
			@Override
			public void onChronometerTick(Chronometer c)
			{
				if(c.getBase()>5000)
				{
					 c.stop();
					 ImageView myPhoto = ((ImageView)findViewById(R.id.imageView1));
					 myPhoto.setVisibility(View.VISIBLE); 
				}
			}
		});
	}

	private void initializeViews() {
		topleft = (ImageButton) findViewById(R.id.topleft);
		topmid = (ImageButton) findViewById(R.id.topmid);
		topright = (ImageButton) findViewById(R.id.topright);
		midleft = (ImageButton) findViewById(R.id.midleft);
		midmid = (ImageButton) findViewById(R.id.midmid);
		midright = (ImageButton) findViewById(R.id.midright);
		bottomleft = (ImageButton) findViewById(R.id.bottomleft);
		bottommid = (ImageButton) findViewById(R.id.bottommid);
		bottomright = (ImageButton) findViewById(R.id.bottomright);		
	}

	public String getNameOfLastEditedModule()
	{	
        int modulecounter = getModulecounterOutOfPreferences();
        
		String nameOfModulePref = "MODULE" + modulecounter;
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameOfModulePref, 0);  
        String lastEditedModule = pref_modulesettings.getString("module_name", ACCESSIBILITY_SERVICE);
		return lastEditedModule;				
	}
	
	//gets module_name out of the module i
	public String getNameOfModule(String i)
	{	
		String nameOfModulePref = "MODULE" + i;
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameOfModulePref, 0);  
        String nameOfModule = pref_modulesettings.getString("module_name", ACCESSIBILITY_SERVICE);
		return nameOfModule;				
	}
	
	public String getDescriptionOfModule(String i)
	{	
		String nameOfModulePref = "MODULE" + i;
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameOfModulePref, 0);  
        String nameOfDescrition = pref_modulesettings.getString("descrition", ACCESSIBILITY_SERVICE);
		return nameOfDescrition;		
	}
	
	//Does the module contain this element
	public Boolean getBooleanOfModule(String i, String elementName)
	{	
		String nameOfModulePref = "MODULE" + i;
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameOfModulePref, 0);  
        Boolean nameOfDescription = pref_modulesettings.getBoolean(elementName, false);        
		return nameOfDescription;
	}
	
	//this Method is also present in ModuleSettingsActivity.java; This should be solved in a better way
	public int getModulecounterOutOfPreferences() {
		SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0); 
        int modulecounter = pref_modulecounter.getInt("modulecounter", 0);
		return modulecounter;
	}
}
