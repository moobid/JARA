package com.eps_hioa_2013.JointAttentionResearchApp;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final static String EXTRA_SESSION = "com.eps_hioa_2013.JointAttentionResearchApp.EXTRA_SESSION";
	
	private Session mysession;
	private Module modulelist[]; //all Modules are in here
	private Element elementlist[]; //all Elements are in here
	
	Bundle bundle;
	
	//Just activates the first View
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//todo: look for existing Modules (saved in files on the device) and write them into 
		//todo: look for existing Elements (saved in files on the device) and write them into 
	}

	//Dont know yet
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//creates new Intent and saves the Strings were typed in into a new Session-Object called mysession
	//then passes mysession over to ModuleActivity
	public void onclick_login(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, ModuleActivity.class);
    	
    	EditText editText3 = (EditText) findViewById(R.id.password);
    	String string_password = editText3.getText().toString();
    	
    	EditText editText = (EditText) findViewById(R.id.participant);
    	String string_participant = editText.getText().toString();
    	
    	EditText editText2 = (EditText) findViewById(R.id.researcher);
    	String string_researcher = editText2.getText().toString();

    	//this is here as a comment to deactivate the check if the fields are not empty
    	//to not get annoyed everytime you start the programm to test it
    	if(/*((string_password == null) || (string_password.equals("")))
    	|| ((string_participant == null) || (string_participant.equals("")))
    	|| ((string_researcher == null) || (string_researcher.equals("")))*/false)
    	{
    		//shows error if one field is empty
    		Toast.makeText(getApplicationContext(), "One of the fields is empty", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		//mysession gets created, serialized, packed in the bundle and then sent to the next activity
    		mysession = new Session(string_password, string_participant, string_researcher);    		
    		bundle = new Bundle();    		
    		bundle.putSerializable(EXTRA_SESSION, (Serializable) mysession);
        	intent.putExtras(bundle);
        	startActivity(intent);
    	}
    	
    }
	
	

}
