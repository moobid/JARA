package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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

@SuppressLint("ValidFragment")
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
	
	private int stagecounter = 0; //0 = Preaction; 1 = Action/signal; 2 = reward; 
	private int roundcounter = 1;
	private int roundcounterlimit;
	
	private Date DateStartedPlaying = null;
	private int timeToPlayInSeconds;
	//private Timecounter timecounter;
	
	private boolean PreactionPresent = true;
	
	private Module mymodule;
	private String modulenumber;
	private Session mysession;	
	private String[] stages = {"preaction", "signal", "action", "reward"};
	private ArrayList<Integer> validPreactionID;
	private ArrayList<Integer> validActionID;
	private Boolean buttonWorks;
	private StopWatch stopWatch;
	private String endMessage;

	private String timedLocation;
	private ElementPicture timedElement;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		//set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    setContentView(R.layout.activity_game);
	    
	    System.out.println("GameActivity started");
		
	    //All class variables
	    Intent intent = getIntent();
	    validPreactionID = new ArrayList<Integer>();
	    validActionID = new ArrayList<Integer>();
		mysession = (Session) intent.getSerializableExtra(ModuleSettingsActivity.EXTRA_SESSION);		
		modulenumber = (intent.getStringExtra(ModuleSettingsActivity.MODULENUMBER));
		roundcounterlimit = (int) intent.getIntExtra(ModuleSettingsActivity.EXTRA_ROUNDSTOPLAY, 0);
		timeToPlayInSeconds = (int) intent.getIntExtra(ModuleSettingsActivity.EXTRA_TIME, 0); 
		DateStartedPlaying = new Date();
		mymodule = new Module();	
		stopWatch = new StopWatch();
		
		initializeViews(); //initializes the views (Imagebuttons)
		
		mysession.updateStatistics("\n\n" +
			"Started Playing a module\n" +
			"Started playing: " + DateStartedPlaying + "\n" +
			"Modulename: " + getNameOfModule(modulenumber) + "\n" +
			"Preferencename: " + "MODULE" + modulenumber + "\n" +
			"Moduledescription: " + getDescriptionOfModule(modulenumber) + "\n" +
			"Time to play in s: " + timeToPlayInSeconds + "\n" +	
			"Rounds to play: " + roundcounterlimit + "\n"			
				);
		
		loadGameInfo(mysession);
		nextStage();				
		stopWatch.start();	
		if(timeToPlayInSeconds > 0)
			startDurationTimer();
	}

	private void nextStage() {
		
		//check stagecounter number and do appropriate things based on number
		switch(stagecounter)
		{
		case 0:
			//=0, check if preactionElements exist,
			if(!mymodule.getPreactions().isEmpty())
			{
				//yes? -> load stuff needed for preaction.
				LoadPreactionStage();
			}
			else
			{
				//no? increase stagecounter call this method again
				stagecounter++;
				nextStage();
			}
			break;
			
		case 1:
			if(!mymodule.getSignals().isEmpty())
			{
				if(!mymodule.getActions().isEmpty())
				{
					//show actions
					LoadActionStage(false);	//false because button isn't active until signal appears
					LoadSignalStage(true); //True because there is a button, signal will activate button when it appears										
				}
				else
				{
					LoadSignalStage(false); //false because no action. time option is for how long signal appears until reward.
					//no action but signal ? show signal for specified time
				}
			}
			else
			{
				if(!mymodule.getActions().isEmpty())
				{
					//show actions
					LoadActionStage(true); //pressing button will instantly show the reward.
					buttonWorks = true;
				}
				else
				{
					//go straight to reward
					stagecounter++;
					nextStage();
				}
			}
			break;
			
		case 2:
			//Show reward reward will change depending on options.		
			LoadRewardStage();
			stagecounter++;
			nextStage();
			break;
			
		case 3:
			//exit game if number of round are met else go back to stage 0			
			if(roundcounter == roundcounterlimit)
			{
				//exit
				stopGame("The last round has been played");
			}
			else
			{
				stagecounter = 0;
				roundcounter++;
				resetScreen();
				nextStage();
			}
			break;
		}
	}


	//Load elements belonging to this module and put them in the appropriate arrays.
	private void loadGameInfo(Session mysession) {
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

	//Will create a timer that stops the game when the time has passed.
	private void startDurationTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
					stopGame("The set duration has been reached"); 
			  }
			}, timeToPlayInSeconds*1000);
		
	}

	//Check if an action is required and updates the statistics file for press location and time.
	public void onclick_touched(View view)
	{		
		switch(stagecounter)
		{
		case 0: //0 = Preaction;
			if(validPreactionID.contains(view.getId()))
			{
				stagecounter++;
				nextStage();
			}	
				break;
		case 1: //1 = Action
			if(validActionID.contains(view.getId()) && buttonWorks)
			{
				buttonWorks = false;
				stagecounter++;
				nextStage();
			}	
			stagecounter++;
				break;
			default:
				break;
		}
		String currentTime = convertTime(stopWatch.getTime());

		//TODO get correct imageButton name		
		mysession.updateStatistics(currentTime + " " +  getImageButton(view.getId()));
	}
	
	private String convertTime(long millis)
	{
		String time = String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
				TimeUnit.MILLISECONDS.toSeconds(millis) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))); 
		return time;
	}
	
	//Loads the reward(s)
	private void LoadRewardStage() {
		// TODO random element and next element for several rounds		
		Element element = null;		
		element = mymodule.getRewards().get(0);
		loadReward(element);
	}

	//Loads the signal(s) starts timer for signal
	//if boolean true then there is a previous action timer will be used to delay image showing up
	//if boolean false then timer will be used to show signal for that period until reward is shown
	private void LoadSignalStage(boolean actionAvailable) {
		// TODO More options ? several signals
		Element element = mymodule.getSignals().get(0);
		String location = getElementLocation(modulenumber, element.getName() + "Signal"); 
		
		timedElement = (ElementPicture) element;
		timedLocation = location;
		
		if(actionAvailable)
		{
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				  @Override
				  public void run() {					  
					 displayPictureElement(timedElement, getImageButton(timedLocation), 10);
					 buttonWorks = true;
					 this.cancel();
				  }
				}, 5*1000); //TODO use actual time setting, now 5 seconds
		}
		else
		{
			displayPictureElement((ElementPicture) element, getImageButton(location), 10);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				  @Override
				  public void run() {
				    nextStage();
				  }
				}, 5*1000); //TODO use actual time setting, now 5 seconds
		}
	}


	private void LoadActionStage(boolean buttenActive) {
		// TODO more options
		buttonWorks = buttenActive;		
		Element element = mymodule.getActions().get(0);
		String location = getElementLocation(modulenumber, element.getName() + "Action"); 

		// display
		displayPictureElement((ElementPicture) element, getImageButton(location), 10);
		// add valid ID
		validActionID.add(getImageButton(location).getId());		
	}

	private void LoadPreactionStage() {
		// TODO more options
		Element element = mymodule.getPreactions().get(0);
		String location = getElementLocation(modulenumber, element.getName() + "Preaction"); 

		// display
		displayPictureElement((ElementPicture) element, getImageButton(location), 10);
		// add valid ID
		validPreactionID.add(getImageButton(location).getId());	
	}
	
	//stops the game
	private void stopGame(String message) {
		endMessage = message;
		DialogFragment newFragment = new StopModuleDialog();
	    newFragment.show(getFragmentManager(), "endGame");
	    
	}
	
	public class StopModuleDialog extends DialogFragment {		
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(endMessage)
	               .setPositiveButton("ok", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   finish();
	                	   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	//removes everything on screen
	private void resetScreen() {
		// TODO Auto-generated method stub		
	}
	

	
	//Checks which type of element must be loaded
	private void loadReward(Element element) {
		if(element instanceof ElementPicture)
		{
			displayPictureReward((ElementPicture) element);
		}
		else if(element instanceof ElementSound)
		{
			displaySoundReward((ElementSound) element);
		}
		else if(element instanceof ElementVideo)
		{
			displayVideoReward((ElementVideo) element);
		}					
	}
	
	
	public void displayVideoReward(ElementVideo myVideo)
	{
		final VideoView video = ((VideoView)findViewById(R.id.videoViewReward));
		//video.se
		video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer v) {				
				video.setVisibility(View.GONE);
			}
		});
		
		
		video.setVisibility(View.VISIBLE);
		video.setVideoURI(Uri.parse(myVideo.getPath()));
		video.requestFocus();
		video.start();
	}
	

	public void displaySoundReward(ElementSound mySound)
	{
		MediaPlayer mPlayer = new MediaPlayer();
		File file = new File(mySound.getPath());
		
		    if(mPlayer != null) {
		        mPlayer.stop();
		        mPlayer.release();
		    }
		    mPlayer = MediaPlayer.create(this, Uri.parse(mySound.getPath()));
		    mPlayer.start();
	}

	public void displayPictureReward(ElementPicture myPicture)
	{
		ImageView myPhoto = ((ImageView)findViewById(R.id.imageViewReward));
		myPhoto.setImageURI(Uri.parse(myPicture.getPath()));
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
					 ImageView myPhoto = ((ImageView)findViewById(R.id.imageViewReward));
					 myPhoto.setVisibility(View.VISIBLE); 
				}
			}
		});
	}
	public void displayPictureReward(ElementPicture myPicture, final int time)
	{
		ImageView myPhoto = ((ImageView)findViewById(R.id.imageViewReward));
		myPhoto.setImageURI(Uri.parse(myPicture.getPath()));
		myPhoto.setVisibility(View.VISIBLE);
		Chronometer myChrono = ((Chronometer)findViewById(R.id.chronometer1));
		myChrono.setBase(0);
		myChrono.start();
		myChrono.setOnChronometerTickListener(new OnChronometerTickListener(){
			@Override
			public void onChronometerTick(Chronometer c)
			{
				if(c.getBase()>time)
				{
					 c.stop();
					 ImageView myPhoto = ((ImageView)findViewById(R.id.imageViewReward));
					 myPhoto.setVisibility(View.VISIBLE); 
				}
			}
		});
	}
	
	public void displayPictureElement(ElementPicture myPicture, ImageButton myButton, final int time)
	{
		myButton.setImageURI(Uri.parse(myPicture.getPath()));
		Chronometer myChrono = ((Chronometer)findViewById(R.id.chronometer1));
		
		myChrono.setBase(0);
		myChrono.start();
		myChrono.setOnChronometerTickListener(new OnChronometerTickListener(){
			@Override
			public void onChronometerTick(Chronometer c)
			{
				if(c.getBase()>time)
				{
					 c.stop();
					//clearPictureElement 
					 
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
	
	private ImageButton getImageButton(String location)
	{
		ImageButton button = null;
		if(location.equals("topleft"))
			button = topleft;
		else if(location.equals("topmid"))
			button = topmid;
		else if(location.equals("topright"))
			button = topright;
		else if(location.equals("midleft"))
			button = midleft;
		else if(location.equals("midmid"))
			button = midmid;
		else if(location.equals("midright"))
			button = midright;
		else if(location.equals("bottomleft"))
			button = bottomleft;
		else if(location.equals("bottommid"))
			button = bottommid;
		else if(location.equals("bottomright"))
			button = bottomright;
		return button;		
	}

	private String getImageButton(int ID)
	{
		String button = "";
		if(ID == topleft.getId())
			button = "topleft";
		else if(ID == topmid.getId())
			button = "topmid";
		else if (ID == topright.getId())
			button = "topright";
		else if(ID == midleft.getId())
			button = "midleft";
		else if(ID == midmid.getId())
			button = "midmid" ;
		else if(ID == midright.getId())
			button = "midright";
		else if(ID == bottomleft.getId())
			button = "bottomleft";
		else if (ID == bottommid.getId())
			button = "bottommid";
		else if (ID == bottomright.getId())
			button = "bottomright";
		return button;		
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
	
	public String getElementLocation(String i, String elementName)
	{
		String nameOfModulePref = "MODULE" + i;
		SharedPreferences pref_modulesettings = getSharedPreferences(nameOfModulePref, 0);  
		String location = pref_modulesettings.getString(elementName + "location", "Not set");
		return location;
	}
}
