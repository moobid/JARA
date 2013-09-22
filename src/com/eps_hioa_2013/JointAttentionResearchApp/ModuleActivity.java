package com.eps_hioa_2013.JointAttentionResearchApp;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ModuleActivity extends Activity {
	private Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_module);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();

		session = (Session) intent.getSerializableExtra(MainActivity.EXTRA_SESSION);

		// Create the text view
		TextView textView = (TextView) findViewById(R.id.password_textview);
		textView.setText(session.getPassword());
		
		TextView textView2 = (TextView) findViewById(R.id.participant_textview);
		textView2.setText(session.getParticipant());

		TextView textView3 = (TextView) findViewById(R.id.researcher_textview);
		textView3.setText(session.getResearcher());
		
		TextView textView4 = (TextView) findViewById(R.id.date_textview);
		textView4.setText(session.getcurrentDate().toString());
	}
	
	public void onclick_add_module()
	{
		
		Intent intent = new Intent(this, ModuleSettingsActivity.class);
		//todo: create new module (start modulesettings with a new module
		startActivity(intent);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.module, menu);
		return true;
	}

	@Override
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
