package com.eps_hioa_2013.JointAttentionResearchApp;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This gets the stored Modules and displays them in a list
 * It offers the user to create a new Module or delete all Modules
 * 
 * @author Simon Irsch, Leon van Tuijl
 */

public class ModuleActivity extends ListActivity {
	private Session mysession;
	private List<Module> myModules;
	
	public final static String MODULENUMBER = "com.eps_hioa_2013.JointAttentionResearchApp.MODULENUMBER";
	Bundle bundle;
	public final static String EXTRA_SESSION = "com.eps_hioa_2013.JointAttentionResearchApp.EXTRA_SESSION";

	//receives the sessionobject and saves it into mysession
	//+ sets the input in the overview of the top of the screen
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("ModuleActivity started");
		super.onCreate(savedInstanceState);
		
		//set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_module);
		
		Intent intent = getIntent();
		mysession = this.fillSession(intent);		
		myModules = createModules();
		
		//saves the names of the modules in an array for use in the settingsscreen
		List<String> modulenames = new ArrayList<String>();
		for(int i = 0; i < myModules.size(); i++)
		{
			modulenames.add(i, myModules.get(i).getName());
		}
		mysession.setModulenames(modulenames);
				
		setupModuleList(); //for the list 
		setupOnModuleClick();
	}

	protected void onResume() {		
		myModules = createModules();
		setupModuleList();
		super.onResume();
	}
	
	//shows the Modulesettings-screen for setting up a new module
	public void onclick_add_module(View view)
	{				
		Intent intent = new Intent(this, ModuleSettingsActivity.class);
		intent.putExtra(MODULENUMBER, Integer.toString(-1)); //-1 for new module
		bundle = new Bundle();    		
		bundle.putSerializable(EXTRA_SESSION, (Serializable) mysession);
    	intent.putExtras(bundle);
		startActivity(intent);
	}
	

	
	//makes the items in the list clickable and sends the user to the settings of each module
	private void setupOnModuleClick() {
		ListView listView1 = (ListView) findViewById(android.R.id.list);
		listView1.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position,
	                long id) {
	            
	            String modulename = ((TextView)view).getText().toString();
	            	            
	            for(int i = 0; i <= getModulecounterOutOfPreferences(); i++)
	    		{	            	
	            	Module currentmodule = myModules.get(i);
		            String name = currentmodule.getName();
		            
		            if(name.equals(modulename))
	            	{
	            		loadSettingsOfModule(i);
	            		i = getModulecounterOutOfPreferences();
	            	}
	    		}
	        }
	    });
	}
	
	//sends the user to the settings of each module (to ModuleSettingsActivity)
	public void loadSettingsOfModule(int moduleNumber)
	{
		Intent intent = new Intent(this, ModuleSettingsActivity.class);
		intent.putExtra(MODULENUMBER, Integer.toString(moduleNumber));
		bundle = new Bundle();    		
		bundle.putSerializable(EXTRA_SESSION, (Serializable) mysession);
    	intent.putExtras(bundle);
		startActivity(intent);
	}


	
	//fills a Session-Object with the details delivered by the user in Activty_module screen
	public Session fillSession(Intent intent)
	{
		mysession = (Session) intent.getSerializableExtra(MainActivity.EXTRA_SESSION);
		
		// Create the text view		
		TextView textView2 = (TextView) findViewById(R.id.participant_textview);
		textView2.setText(mysession.getParticipant());

		TextView textView3 = (TextView) findViewById(R.id.researcher_textview);
		textView3.setText(mysession.getResearcher());
		
		TextView textView4 = (TextView) findViewById(R.id.date_textview);
		textView4.setText(mysession.getcurrentDate().toString());
		
		return mysession;
	}
	
	//returns an Array with the name of all modules in it
	public String[] getAllModulenames()
	{

		String[] modulelist = null;
		List<String> names = new ArrayList<String>();

		for(int i = 0; i <= getModulecounterOutOfPreferences(); i++)
		{
			if(!myModules.get(i).getName().equals("accessibility"))
			names.add(myModules.get(i).getName()) ;
		}
		modulelist = names.toArray(new String[names.size()]);
		return modulelist;
	}
	
	private List<Module> createModules() {
		//create container for new modules
		List<Module> moduleContainer = new ArrayList<Module>();
		
		//loop through preference file for all module names
		for(int i = 0; i <= getModulecounterOutOfPreferences(); i++)
		{
			//start values for error checking
			Module currentModule = null;
			String name = "";
			String description = "";
			int number = -1;
			
			//get the preference with currentModule information
			String nameOfModulePref = "MODULE" + i;
	    	SharedPreferences pref_currentModule = getSharedPreferences(nameOfModulePref, 0);  
	        
			// for each name found create new Module object with preference name and description name and number
			name = pref_currentModule.getString("module_name", ACCESSIBILITY_SERVICE);
			description = pref_currentModule.getString("module_description", ACCESSIBILITY_SERVICE);
			number = i;
			currentModule = new Module(number, name, description);
			
			// add new Module object to container
			moduleContainer.add(currentModule);
		}

		// Return Container
		return moduleContainer;		
	}
	

	
	public void setupModuleList()
	{
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				getAllModulenames()
				);		
		setListAdapter(adapter);
	}
	
	//gets module_name out of the last edited module
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
	
	public int getModulecounterOutOfPreferences() {
		SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0); 
        int modulecounter = pref_modulecounter.getInt("modulecounter", 0);
		return modulecounter;
	}
	
	//sets the modulecounter to -1. When a new module gets added, it has the number 0
	public void onclick_reset_modulecounter(View view)
	{
		DialogFragment newFragment = new ElementYouSureDialog();
		newFragment.show(getFragmentManager(), "dialogsettings");
	}
	
	@SuppressLint("ValidFragment")
	public class ElementYouSureDialog extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("Are you sure? Can not be undone!")
	               .setPositiveButton("Delete all Modules", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                   	SharedPreferences pref_modulecounter = getSharedPreferences("counter", 0);
	                    SharedPreferences.Editor editor = pref_modulecounter.edit();
	                    int count = -1;
	                    editor.putInt("modulecounter", count);
	                    editor.commit();
	                    
	                    myModules = createModules();
	            		setupModuleList();
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}

}
