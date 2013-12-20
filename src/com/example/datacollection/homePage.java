/**
 * 
 */
package com.example.datacollection;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author jsuit
 * 
 */
public class homePage extends Activity{

	private ArrayAdapter<String> settingsAdapter;
	private ListView listOfOptions;
	private String[] arrayOfOptions;
	private String curr_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		curr_user = (String) i.getExtras().get("User");
		setContentView(R.layout.homepage);
		listOfOptions = (ListView) findViewById(R.id.homePageListView);
		settingsAdapter = new ArrayAdapter<String>(this,
				R.layout.homepagerow);
		
		arrayOfOptions = getResources().getStringArray(R.array.homePageOptions);
		for (String option : arrayOfOptions) {
			settingsAdapter.add(option);
		}
		listOfOptions.setAdapter(settingsAdapter);
		listOfOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("inside ", "on itemclick");
				// TODO Auto-generated method stub
				Intent i;
				switch (arg2) {
				case 0:
					//Gather Training Data
					 i = new Intent(getApplicationContext(), GatherData.class);
					 i.putExtra("User", curr_user);
					 startActivity(i);
					break;
				case 1:
					//processTrainingData
					// i = new Intent(getApplicationContext(), ProcessData.class);
					// i.putExtra("User", curr_user);
					break;
				case 2:
					//Test
					break;
				case 3:
					i = new Intent(getApplicationContext(), Bluetooth.class);
					i.putExtra("User", curr_user);
					startActivity(i);
					break;
				case 4:
					 i = new Intent(getApplicationContext(), Settings.class);
					 i.putExtra("User", curr_user);
					 startActivity(i);
					break;
				default:
					Toast.makeText(getApplicationContext(), "Click something",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}

		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;

	}

}
