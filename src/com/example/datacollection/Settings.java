package com.example.datacollection;

import com.example.datacollection.Database.Database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

	private Button logoutBtn;
	private EditText userId;
	private EditText password;
	private String curr_user;
	private Button saveBtn;
	private Database db;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		logoutBtn = (Button) findViewById(R.id.btnLogin);
		userId = (EditText) findViewById(R.id.userId);
		password = (EditText) findViewById(R.id.password);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			curr_user = (String) extras.get("User");
		}
		saveBtn = (Button) findViewById(R.id.saveBtn);
		db = new Database(getApplicationContext());
	}

	/**
	 * Button that logs out user. Pops off previous activities and goes back to
	 * login screen
	 * 
	 * @param v
	 *            View
	 */
	public void logoutUser(View v) {
		Intent intent = new Intent(this, Login.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	/**
	 * Button that either registers a new user or updates current info. Assumes
	 * that we already have name of current user if updating.
	 * 
	 * @param v
	 */
	public void saveUserInfo(View v) {

		String id = userId.getText().toString().toLowerCase();
		String pwrd = password.getText().toString();
		// see if we are registering or updating user
		if (curr_user != null) {
			// we are updating user
			// didn't enter anything
			if ("".compareTo(id) == 0 && "".compareTo(pwrd) == 0) {
				Toast.makeText(this,
						"Cannot Change User Name and/or Password. ",
						Toast.LENGTH_SHORT).show();
			} else {
				// user id left blank; update password
				if ("".compareTo(id) == 0 && "".compareTo(pwrd) != 0) {
					db.updatePwd(pwrd, curr_user, db.getPassword(curr_user));
					Toast.makeText(this, "Updated Password for " + curr_user,
							Toast.LENGTH_SHORT).show();
					// trying to update userId, and not password
				} else if ("".compareTo(id) != 0 && "".compareTo(pwrd) == 0) {
					if (!db.seeIfUserExists(id)) {
						db.updateId(id, curr_user);
						Toast.makeText(this, "Updated User Name ",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "UserName already taken ",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					// enter into this block iff both are trying to be changed
					if (db.seeIfUserExists(id)) {
						//can't change to already existing username
						Toast.makeText(this, "UserName already taken ",
								Toast.LENGTH_SHORT).show();
					} else {
						
						db.changeUserNameAndPwd(id, pwrd, curr_user);
						Toast.makeText(this, "Change user id and password ",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else {
			// register new user
			if (!("".compareTo(pwrd) == 0 && "".compareTo(id) == 0)) {
				// valid password and username
				db.saveUser(id, pwrd);
				curr_user = id;
				Toast.makeText(this, "You are now registered ",
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), homePage.class);
				i.putExtra("User", curr_user);
				startActivity(i);
			} else {
				Toast.makeText(this, "Invalid Password and/or User Name",
						Toast.LENGTH_SHORT);
			}

		}

	}
}
