package com.eps_hioa_2013.JointAttentionResearchApp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"; //participant will put in here
	public final static String EXTRA_MESSAGE2 = "com.example.myfirstapp.MESSAGE2"; //researcher will put in here
	public final static String EXTRA_MESSAGE3 = "com.example.myfirstapp.MESSAGE3"; //subject will put in here

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onclick_login(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, ModuleActivity.class);
    	
    	EditText editText3 = (EditText) findViewById(R.id.subject);
    	String string_password = editText3.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, string_password);
    	
    	EditText editText = (EditText) findViewById(R.id.participant);
    	String string_participant = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE2, string_participant);
    	
    	EditText editText2 = (EditText) findViewById(R.id.researcher);
    	String string_researcher = editText2.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE3, string_researcher);
    	
    	Session session = new Session(string_password, string_participant, string_researcher);
    	

    	
    	
    	startActivity(intent);
    }
	
	

}
