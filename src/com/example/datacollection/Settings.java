package com.example.datacollection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {

	private Button logoutBtn;
	private EditText userId;
	private EditText password;
	private String curr_user;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		logoutBtn = (Button) findViewById(R.id.btnLogin);
		userId = (EditText) findViewById(R.id.userId);
		password = (EditText) findViewById(R.id.password);
		curr_user = (String) getIntent().getExtras().get("User");
	}
	
	/**
	 * Button that logs out user. Pops off previous activities and goes back to login
	 * screen
	 * @param v View
	 */
	public void logoutUser(View v){
		Intent intent = new Intent(this, Login.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}
