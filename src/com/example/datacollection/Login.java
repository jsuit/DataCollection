package com.example.datacollection;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
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
		Intent i = new Intent(getApplicationContext(), homePage.class);
		i.putExtra("User", "Jonathan".toLowerCase());
		startActivity(i);
	}

	/**
	 * A new user presses this.
	 */
	public void registerUser(View v) {

	}

}
