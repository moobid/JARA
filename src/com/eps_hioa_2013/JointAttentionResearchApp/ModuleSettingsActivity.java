package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	//Example to explain the oncoming 4 lines: When you have an donald.jpg in the Elementsfolder on your tablet:
	//	There will be an Element on position 0 with the name donald in preactions
	//	The appropriate Button is in buttonPreactions on position 0
	//	The appropriate Checkbox is in checkboxPreactions also on position 0
	//In that way, the Element, Button and the CheckBox can be connected to each other.
	//The donald Element will of course also be in Signals and Actions because its a picture
	private List<CheckBox> checkboxPreactions = new ArrayList<CheckBox>();
	private List<Button> buttonPreactions = new ArrayList<Button>();
	private List<Element> preactions = new ArrayList<Element>();
	private int preactionsCounter = 0;
	
	private List<CheckBox> checkboxSignals = new ArrayList<CheckBox>();
	private List<Button> buttonSignals = new ArrayList<Button>();
	private List<Element> signals = new ArrayList<Element>();
	private int signalsCounter = 0;
	
	private List<CheckBox> checkboxActions = new ArrayList<CheckBox>();
	private List<Button> buttonActions = new ArrayList<Button>();
	private List<Element> actions = new ArrayList<Element>();
	private int actionsCounter = 0;
	
	private List<CheckBox> checkboxRewards = new ArrayList<CheckBox>();
	private List<Button> buttonRewards = new ArrayList<Button>();
	private List<Element> rewards = new ArrayList<Element>();
	private int rewardsCounter = 0;
	
	private int currentModuleNumber = -1;
	
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("ModuleSettingsActivity started");
		super.onCreate(savedInstanceState);
		//addPreferencesFromResource(R.xml.settings); deactivated for testing
		setContentView(R.layout.activity_module_settings);
		setupActionBar();
		
//		addElementToList("asdfasdf", "Signals");
		Intent intent = getIntent();
		String mode = (intent.getStringExtra(ModuleActivity.MODE));
		if(mode.equals("0"))
		{
			//makes Start module button disapear
			Button start_module = (Button) findViewById(R.id.start_module);
			start_module.setVisibility(View.GONE);
		}
		mysession = (Session) intent.getSerializableExtra(ModuleActivity.EXTRA_SESSION);
		String modulenumber = (intent.getStringExtra(ModuleActivity.MODULENUMBER));
		this.currentModuleNumber = Integer.parseInt(modulenumber);
		
		 //shows the Elements at the right place and saves them in the Membervariables
		setupDynamicElementList(mysession.getElementlist());
		
		configureNumberPickers(); //just some settings for the NumberPickers; nothing special
		
		loadModuleSettings(modulenumber); //loads and shows all the saved Settings of a module
	}
	

	public void loadModuleSettings(String modulenumber) {
		
		if(Integer.parseInt(modulenumber) != -1) 
		{
			EditText editText2 = (EditText) findViewById(R.id.editText2); //name gets loaded
			editText2.setText(getNameOfModule(modulenumber));
	    	
	    	EditText editText1 = (EditText) findViewById(R.id.editText1); //descrition gets loaded
	    	editText1.setText(getDescriptionOfModule(modulenumber));
	    	Toast.makeText(getApplicationContext(), "MODULE" + this.currentModuleNumber + " loaded", Toast.LENGTH_SHORT).show();
	    	
	    	for(int i = 0; i <= preactionsCounter; i++) //the states of all checkboxes get loaded
	    	{	    		
	    		Boolean b = getBooleanOfModule(modulenumber, preactions.get(i).getName());
	    		checkboxPreactions.get(i).setChecked(b);
	    	}
	    	
	    	for(int i = 0; i <= signalsCounter; i++) //the states of all checkboxes get loaded
	    	{	    		
	    		Boolean b = getBooleanOfModule(modulenumber, signals.get(i).getName());
	    		checkboxSignals.get(i).setChecked(b);
	    	}
	    	
	    	for(int i = 0; i <= actionsCounter; i++) //the states of all checkboxes get loaded
	    	{	    		
	    		Boolean b = getBooleanOfModule(modulenumber, actions.get(i).getName());
	    		checkboxActions.get(i).setChecked(b);
	    	}
	    	
	    	for(int i = 0; i <= rewardsCounter; i++) //the states of all checkboxes get loaded
	    	{	    		
	    		Boolean b = getBooleanOfModule(modulenumber, rewards.get(i).getName());
	    		checkboxRewards.get(i).setChecked(b);
	    	}
	    	
	    	
		}
		else {/*nothing to do here*/}
	}
	
	public void onclick_start_game(View view)
	{	
		//todo: check if roundcounterlimit and timetoplay != 0
		//todo: save made changes by the user before starting the game!
		mysession.updateStatistics("Clicked on Start Module");		
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(MODULENUMBER, Integer.toString(currentModuleNumber));
		intent.putExtra(EXTRA_ROUNDSTOPLAY, npRoundsToPlay.getValue()); //Rounds to Play (doesnt get saved in module)
		intent.putExtra(EXTRA_TIME, calculateTimeToPlayInSeconds()); //Time to Play (doesnt get saved in module)
		bundle = new Bundle();
		bundle.putSerializable(EXTRA_SESSION, (Serializable) mysession);
    	intent.putExtras(bundle);
		startActivity(intent);
	}

	public void onclick_save(View view)
	{	
		EditText editText2 = (EditText) findViewById(R.id.editText2);
    	String module_name = editText2.getText().toString();

       	EditText editText1 = (EditText) findViewById(R.id.editText1);
    	String module_description = editText1.getText().toString();
    	
    	//if the modulecounter is smaller than -1 it gets set to -1
    	if(getModulecounterOutOfPreferences() < -1) resetModulecounterInPreferences();
    	
    	
    	String nameForModule = null;
    	if(this.currentModuleNumber == -1) //jumps in here if new modules gets added
    	{
    		//modulecounter gets iterated
    		incrementModulecounterInPreferences();     	
	    	//the Name for the new Module gets created. It is like MODULE0, MODULE1, MODULE2, ...
	    	nameForModule = "MODULE"+getModulecounterOutOfPreferences();
	    	this.currentModuleNumber = getModulecounterOutOfPreferences();
    	}
    	else //jumps in here if existing module gets saved
    	{
    		nameForModule = "MODULE"+this.currentModuleNumber;
    	}
    	
    	
    	//saves the variables into the ShardPreferences for the current Module START START START START
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameForModule, 0);
        SharedPreferences.Editor editor = pref_modulesettings.edit();       
        editor.putString("module_name", module_name);
        editor.putString("module_description", module_description);
        
    	//ask all Preaction TextBoxes if checked or unchecked
    	for(int i = 0; i <= preactionsCounter; i++)
    	{
	    	Boolean checked = false;
	    	if(checkboxPreactions.get(i).isChecked())
	    	{
	    		checked = true;
	    		editor.putBoolean(preactions.get(i).getName(), checked);
	    		//saves true (checked) or false (unchecked) as a Boolean named after the Element in the Shared Pref
	    	}
    	}
    	//ask all Signals TextBoxes if checked or unchecked
    	for(int i = 0; i <= signalsCounter; i++)
    	{
	    	Boolean checked = false;
	    	if(checkboxSignals.get(i).isChecked())
	    	{
	    		checked = true;
	    		editor.putBoolean(signals.get(i).getName(), checked);
	    		//saves true (checked) or false (unchecked) as a Boolean named after the Element in the Shared Pref
	    	}
    	}
    	//ask all Action TextBoxes if checked or unchecked
    	for(int i = 0; i <= actionsCounter; i++)
    	{
	    	Boolean checked = false;
	    	if(checkboxActions.get(i).isChecked())
	    	{
	    		checked = true;
	    		editor.putBoolean(actions.get(i).getName(), checked);
	    		//saves true (checked) or false (unchecked) as a Boolean named after the Element in the Shared Pref
	    	}
    	}
    	//ask all Rewards TextBoxes if checked or unchecked
    	for(int i = 0; i <= preactionsCounter; i++)
    	{
	    	Boolean checked = false;
	    	if(checkboxRewards.get(i).isChecked())
	    	{
	    		checked = true;
	    		editor.putBoolean(rewards.get(i).getName(), checked);
	    		//saves true (checked) or false (unchecked) as a Boolean named after the Element in the Shared Pref
	    	}
    	}
        
        
    	if((module_name == null) || (module_name.equals("")))
    	{
    		Toast.makeText(getApplicationContext(), "Modulename empty", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
            editor.commit();        
            Toast.makeText(getApplicationContext(), "Saved as MODULE" + this.currentModuleNumber, Toast.LENGTH_SHORT).show();
            super.onBackPressed(); //goes back to last Activity (ModuleActivity)
    	}

	}
	
	private void setupDynamicElementList(List<Element> elements)
	{
		for(int i = 0; i <= elements.size(); i++)
		{
			if(elements.get(i) instanceof ElementPicture)
			{
				//creates CheckBox and Button and shows it in the right place and adds both in the Membervariables
				addElementToList(elements.get(i).getName(), "Preactions");
				preactions.add(preactionsCounter, elements.get(i)); //adds the Element in the Membervariables
				preactionsCounter++; //counter how many elements are in this array
				addElementToList(elements.get(i).getName(), "Actions");
				actions.add(actionsCounter, elements.get(i));
				actionsCounter++;
				addElementToList(elements.get(i).getName(), "Signals");
				signals.add(signalsCounter, elements.get(i));
				signalsCounter++;
				addElementToList(elements.get(i).getName(), "Rewards");
				rewards.add(rewardsCounter, elements.get(i));
				rewardsCounter++;
			}
			if((elements.get(i) instanceof ElementVideo) || (elements.get(i) instanceof ElementSound))
			{
				addElementToList(elements.get(i).getName(), "Rewards");
				rewards.add(rewardsCounter, elements.get(i));
				rewardsCounter++;
			}
		}
	}
	
	//adds a Checkbox for an Element to the Settingsscreen
	//WARNING: elementType must be either "Preactions", "Signals", "Actions" or "Rewards"
	public void addElementToList(String elementName, String elementType)
	{		
		TableLayout table = null;
		
		if(elementType == "Preactions"){
			table = (TableLayout) findViewById(R.id.Preactions);
			//creates new Tablerow and sets it into the new Tablelayout
			TableRow newTablerow = new TableRow(this);
			table.addView(newTablerow);
			
			//creates new Checkbox with the name of the element and sets it into the Tablerow
			checkboxPreactions.add(preactionsCounter, new CheckBox(this));
			checkboxPreactions.get(preactionsCounter).setText(elementName);
		//	checkboxPreactions.add(preactionsCounter, (checkboxPreactions.get(preactionsCounter)));
			
			newTablerow.addView(checkboxPreactions.get(preactionsCounter));
			
			//creates new Button and sets it also into the Tablerow
			buttonPreactions.add(preactionsCounter, new Button(this));
			buttonPreactions.get(preactionsCounter).setText("E");
			buttonPreactions.get(preactionsCounter).setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	showElementSettingsDiaglog();
	            }
	        });			
			newTablerow.addView(buttonPreactions.get(preactionsCounter)); 
		}
		
		if(elementType == "Signals"){
			table = (TableLayout) findViewById(R.id.Signals);
			//creates new Tablerow and sets it into the new Tablelayout
			TableRow newTablerow = new TableRow(this);
			table.addView(newTablerow);
			
			//creates new Checkbox with the name of the element and sets it into the Tablerow
			checkboxSignals.add(signalsCounter, new CheckBox(this));
			checkboxSignals.get(signalsCounter).setText(elementName);
		//	checkboxPreactions.add(preactionsCounter, (checkboxPreactions.get(preactionsCounter)));
			
			newTablerow.addView(checkboxSignals.get(signalsCounter));
			
			//creates new Button and sets it also into the Tablerow
			buttonSignals.add(signalsCounter, new Button(this));
			buttonSignals.get(signalsCounter).setText("E");
			buttonSignals.get(signalsCounter).setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	showElementSettingsDiaglog();
	            }
	        });			
			newTablerow.addView(buttonSignals.get(signalsCounter)); 		
		}
		
		if(elementType == "Actions"){
			table = (TableLayout) findViewById(R.id.Actions);
			//creates new Tablerow and sets it into the new Tablelayout
			TableRow newTablerow = new TableRow(this);
			table.addView(newTablerow);
			
			//creates new Checkbox with the name of the element and sets it into the Tablerow
			checkboxActions.add(actionsCounter, new CheckBox(this));
			checkboxActions.get(actionsCounter).setText(elementName);
		//	checkboxPreactions.add(preactionsCounter, (checkboxPreactions.get(preactionsCounter)));
			
			newTablerow.addView(checkboxActions.get(actionsCounter));
			
			//creates new Button and sets it also into the Tablerow
			buttonActions.add(actionsCounter, new Button(this));
			buttonActions.get(actionsCounter).setText("E");
			buttonActions.get(actionsCounter).setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	showElementSettingsDiaglog();
	            }
	        });			
			newTablerow.addView(buttonActions.get(actionsCounter)); 		
		}
		
		if(elementType == "Rewards"){
			table = (TableLayout) findViewById(R.id.Rewards);
			//creates new Tablerow and sets it into the new Tablelayout
			TableRow newTablerow = new TableRow(this);
			table.addView(newTablerow);
			
			//creates new Checkbox with the name of the element and sets it into the Tablerow
			checkboxRewards.add(rewardsCounter, new CheckBox(this));
			checkboxRewards.get(rewardsCounter).setText(elementName);
		//	checkboxPreactions.add(preactionsCounter, (checkboxPreactions.get(preactionsCounter)));
			
			newTablerow.addView(checkboxRewards.get(rewardsCounter));
			
			//creates new Button and sets it also into the Tablerow
			buttonRewards.add(rewardsCounter, new Button(this));
			buttonRewards.get(rewardsCounter).setText("E");
			buttonRewards.get(rewardsCounter).setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	showElementSettingsDiaglog();
	            }
	        });			
			newTablerow.addView(buttonRewards.get(rewardsCounter)); 			
		}
		
		if(table == null){ //error
			Toast.makeText(getApplicationContext(),		
				"Error in: public void addElementToList(String elementName, String elementType); WRONG elementType",
				Toast.LENGTH_LONG).show();
		}       
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
	
	private int calculateTimeToPlayInSeconds() {
		int minutes = npMinutes.getValue();
		int seconds = npSeconds.getValue();		
		return ((minutes*60)+seconds);
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
	
	public Boolean getBooleanOfModule(String i, String elementName)
	{	
		String nameOfModulePref = "MODULE" + i;
    	SharedPreferences pref_modulesettings = getSharedPreferences(nameOfModulePref, 0);  
        Boolean nameOfDescrition = pref_modulesettings.getBoolean(elementName, false);
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
