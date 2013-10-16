package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.Serializable;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

//ModuleSettingsActivity gets called, when you create a new Module or like to edit an existing one
public class ModuleSettingsActivity extends Activity {
	public final static String MODULENUMBER = "com.eps_hioa_2013.JointAttentionResearchApp.MODULENUMBER";	
	public final static String EXTRA_SESSION = "com.eps_hioa_2013.JointAttentionResearchApp.EXTRA_SESSION";
	public final static String EXTRA_ROUNDSTOPLAY = "com.eps_hioa_2013.JointAttentionResearchApp.EXTRA_ROUNDSTOPLAY";
	public final static String EXTRA_TIME = "com.eps_hioa_2013.JointAttentionResearchApp.EXTRA_TIME";
	
	Bundle bundle;
	Session mysession;
	
	NumberPicker npMinutes;
	NumberPicker npSeconds;
	NumberPicker npRoundsToPlay;
	private int counterPreactions = 0;
	private int counterSignals = 0;
	private int counterActions = 0;
	private int counterRewards = 0;
	
	private int currentModuleNumber = -1;
	
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("ModuleSettingsActivity started");
		super.onCreate(savedInstanceState);
		//addPreferencesFromResource(R.xml.settings); deactivated for testing
		setContentView(R.layout.activity_module_settings);
		
		// Show the Up button in the action bar.
		setupActionBar();
		addElementToList("asdf", "Preactions");
		addElementToList("asdf", "Actions");
		addElementToList("asdf", "Signals");
		addElementToList("asdfasdf", "Signals");
		Intent intent = getIntent();
		String mode = (intent.getStringExtra(ModuleActivity.MODE));
		if(mode.equals("0"))
		{
			Button start_module = (Button) findViewById(R.id.start_module);
			start_module.setVisibility(View.GONE);
		}
		mysession = (Session) intent.getSerializableExtra(ModuleActivity.EXTRA_SESSION);
		
		
		
		String modulenumber = (intent.getStringExtra(ModuleActivity.MODULENUMBER));
		this.currentModuleNumber = Integer.parseInt(modulenumber);
		configureNumberPickers();
		System.out.println("ModuleSettingsActivity started3");
		loadModuleSettings(modulenumber);
		

	}
	private void configureNumberPickers() {
		npMinutes = (NumberPicker) findViewById(R.id.npMinutes);
		npMinutes.setMaxValue(999);
		npMinutes.setMinValue(0);
		
		npSeconds = (NumberPicker) findViewById(R.id.npSeconds);
		npSeconds.setMaxValue(59);
		npSeconds.setMinValue(0);
				
		npRoundsToPlay = (NumberPicker) findViewById(R.id.npRoundsToPlay);
		npRoundsToPlay.setMaxValue(999);
		npRoundsToPlay.setMinValue(0);		
	}
	public void loadModuleSettings(String modulenumber) {
		
		if(Integer.parseInt(modulenumber) != -1) 
		{
			EditText editText2 = (EditText) findViewById(R.id.editText2); //name gets loaded
			editText2.setText(getNameOfModule(modulenumber));
	    	
	    	EditText editText1 = (EditText) findViewById(R.id.editText1); //descrition gets loaded
	    	editText1.setText(getDescriptionOfModule(modulenumber));
	    	Toast.makeText(getApplicationContext(), "MODULE" + this.currentModuleNumber + " loaded", Toast.LENGTH_SHORT).show();
		}
		else {/*nothing to do here*/}
	}
	
	public void onclick_start_game(View view)
	{	
		mysession.updateStatistics("onclic_start 1");
		
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(MODULENUMBER, Integer.toString(currentModuleNumber));
		intent.putExtra(EXTRA_ROUNDSTOPLAY, npRoundsToPlay.getValue());		
		intent.putExtra(EXTRA_TIME, calculateTimeToPlayInSeconds());
		bundle = new Bundle();
		bundle.putSerializable(EXTRA_SESSION, (Serializable) mysession);
    	intent.putExtras(bundle);
    	mysession.updateStatistics("onclic_start 2");
		startActivity(intent);
	}

	private int calculateTimeToPlayInSeconds() {
		int minutes = npMinutes.getValue();
		int seconds = npSeconds.getValue();		
		return ((minutes*60)+seconds);
	}
	public void onclick_save(View view)
	{	
		//gets the infos out of the Layout in Variables. START
		EditText editText2 = (EditText) findViewById(R.id.editText2);
    	String module_name = editText2.getText().toString();
    	
    	EditText editText1 = (EditText) findViewById(R.id.editText1);
    	String module_description = editText1.getText().toString();
    	
//    	CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkBox1);
//    	Boolean blue_circle = false;
//    	if(checkbox1.isChecked())
//    		{
//    			blue_circle = true;
//    		}
    	   	
    	//gets the infos out of the Layout in Variables. END
    	
    	//if the modulecounter is smaller than -1 it gets set to -1
    	if(getModulecounterOutOfPreferences() < -1) resetModulecounterInPreferences();
    	
    	
    	String nameForNewModule = null;
    	if(this.currentModuleNumber == -1) //jumps in here if new modules gets added
    	{
    		//modulecounter gets iterated
    		incrementModulecounterInPreferences();     	
	    	//the Name for the new Module gets created. It is like MODULE0, MODULE1, MODULE2, ...
	    	nameForNewModule = "MODULE"+getModulecounterOutOfPreferences();
	    	this.currentModuleNumber = getModulecounterOutOfPreferences();
    	}
    	else //jumps in here if existing module gets saved
    	{
    		nameForNewModule = "MODULE"+this.currentModuleNumber;
    	}
    	
    	
    	//puts the variables into the ShardPreferences for the current Module
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameForNewModule, 0);
        SharedPreferences.Editor editor = pref_modulesettings.edit();       
        editor.putString("module_name", module_name);
        editor.putString("module_description", module_description);
//        editor.putBoolean("blue_circle", blue_circle);
//        editor.putBoolean("yellow_square", yellow_square);
        editor.commit();
       
        
        Toast.makeText(getApplicationContext(), "Saved as MODULE" + this.currentModuleNumber, Toast.LENGTH_SHORT).show();
        super.onBackPressed(); //goes back to last Activity (ModuleActivity)
	}
	
	//adds a Checkbox for an Element to the Settingsscreen
	//WARNING: elementType must be either "Preactions", "Signals", "Actions" or "Rewards"
	public void addElementToList(String elementName, String elementType)
	{
		
		TableLayout table = null;
		if(elementType == "Preactions"){
			table = (TableLayout) findViewById(R.id.Preactions);
			counterPreactions++;
		}
		if(elementType == "Signals"){
			table = (TableLayout) findViewById(R.id.Signals);
			counterSignals++;
		}
		if(elementType == "Actions"){
			table = (TableLayout) findViewById(R.id.Actions);
			counterActions++;
		}
		if(elementType == "Rewards"){
			table = (TableLayout) findViewById(R.id.Rewards);
			counterRewards++;
		}
		if(table == null){ //error
			Toast.makeText(getApplicationContext(),		
				"Error in: public void addElementToList(String elementName, String elementType); WRONG elementType",
				Toast.LENGTH_LONG).show();
		}
		//creates new Tablerow and sets it into the new Tablelayout
		TableRow newTablerow = new TableRow(this);
		table.addView(newTablerow);
		
		//creates new Checkbox with the name of the element and sets it into the Tablerow
		CheckBox newCheckbox = new CheckBox(this);
		newCheckbox.setText(elementName);
		newTablerow.addView(newCheckbox);
		
		//creates new Button and sets it also into the Tablerow
		Button newButton = new Button(this);
		newButton.setText("E");

		newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showElementSettingsDiaglog();
            }
        });
		
		newTablerow.addView(newButton);        
	}
	
	//shows the popup when you click on the Edit-button
	public void showElementSettingsDiaglog() {
	    DialogFragment newFragment = new ElementSettingsDialog();
	    newFragment.show(getFragmentManager(), "diaglogsettings");
	}
	
	//returns the counter for the modules
	//this Method is also present in ModuleActivity.java; This should be solved in a better way
	public int getModulecounterOutOfPreferences() {
		SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0); 
        int modulecounter = pref_modulecounter.getInt("modulecounter", 0);
		return modulecounter;
	}
	
	//increments the modulecounter
	public void incrementModulecounterInPreferences() {		
    	SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0);
        SharedPreferences.Editor editor = pref_modulecounter.edit();
        int count = getModulecounterOutOfPreferences() + 1;
        editor.putInt("modulecounter", count);
        editor.commit();
	}
	
	//decrements the modulecounter
	public void decrementModulecounterInPreferences() {		
    	SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0);
        SharedPreferences.Editor editor = pref_modulecounter.edit();
        int count = getModulecounterOutOfPreferences() - 1;
        editor.putInt("modulecounter", count);
        editor.commit();
	}
	
	//sets the modulecounter to -1. When a new module gets added, it has the number 0
	//this Method is also present in ModuleActivity.java; This should be solved in a better way
	public void resetModulecounterInPreferences() {
		SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0);
        SharedPreferences.Editor editor = pref_modulecounter.edit();
        int count = -1;
        editor.putInt("modulecounter", count);
        editor.commit();
	}
	
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
        String nameOfDescrition = pref_modulesettings.getString("module_description", ACCESSIBILITY_SERVICE);
		return nameOfDescrition;		
	}	
	

	
	

	

	
	



	
/////////////////////////////////NOT IMPORTANT FOR NOW//////////////////////////////////
	//Autogenerated, dont know
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	//Autogenerated, dont know
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.module_settings, menu);
		return true;
	}

	//Autogenerated, dont know
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
