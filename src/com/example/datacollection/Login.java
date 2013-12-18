package com.example.datacollection;

import com.example.datacollection.Database.Database;
import com.example.datacollection.Database.DatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private static Database db;
	private String userId;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		if (db == null) {
			db = new Database(getApplicationContext());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Fired when user presses Login button. If no user found puts an error
	 * message to screen. Else goes to next screen. Name of this function should
	 * match the one in the login.xml file for the login button
	 */
	public void loginUser(View v) {
		userId = ((EditText) findViewById(R.id.userIDEditText)).getText()
				.toString();
		password = ((EditText) findViewById(R.id.password)).getText()
				.toString();
		if (db.ableToLogin(userId, password)) {
			Intent i = new Intent(getApplicationContext(), homePage.class);
			i.putExtra("User", userId.toLowerCase());
			startActivity(i);
		} else {
			Toast.makeText(this, "Invalid userId and/or Password",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * A new user presses this.
	 */
	public void registerUser(View v) {
		Intent i = new Intent(getApplicationContext(), Settings.class);
		startActivity(i);
	}
}
