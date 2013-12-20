package com.example.datacollection;

import Timer.Timer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class GatherData extends Activity {

	private static boolean visible = true;
	public final static int TIMER_DONE = 0;
	public final static int TIMER_STOPPED = 1;
	public final static int UPDATE_UI = 2;
	public static int windowLength = 5;
	public static int stopTime = 1;
	private TextView timer;
	private Spinner chooseActivitySpinner;
	private CheckBox phoneInclude;
	private String curr_user;
	private ArrayAdapter<CharSequence> spinner_adapter;
	private String cur_activity;
	private boolean phone;
	private Timer mTimer;
	private Handler timerHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gatherdata);
		curr_user = (String) getIntent().getExtras().get("User");
		timer = (TextView) findViewById(R.id.timerValue);
		chooseActivitySpinner = (Spinner) findViewById(R.id.Activity);
		phoneInclude = (CheckBox) findViewById(R.id.PhoneSensor);
		phoneInclude.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				buttonView.setChecked(isChecked);
			}
		});
		spinner_adapter = ArrayAdapter
				.createFromResource(this, R.array.Activity,
						android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		chooseActivitySpinner.setAdapter(spinner_adapter);
		chooseActivitySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						cur_activity = (String) arg0.getItemAtPosition(arg2);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		timerHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case TIMER_DONE:
					Toast.makeText(getApplicationContext(), "Done",
							Toast.LENGTH_SHORT).show();
					Timer.timerValue.setText(String.format("%02d:%02d", msg.arg1, msg.arg2));
					timerHandler.removeMessages(GatherData.UPDATE_UI);
					timerHandler.removeMessages(GatherData.TIMER_DONE);
					ChangeUIElementsUnclickable();
					Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
					v.vibrate(500);
					break;
				case TIMER_STOPPED:
					
					Timer.timerValue.setText(String.format("%02d:%02d", msg.arg1, msg.arg2));
					timerHandler.removeMessages(GatherData.UPDATE_UI);
					timerHandler.removeMessages(GatherData.TIMER_DONE);
					timerHandler.removeMessages(GatherData.TIMER_STOPPED);
					ChangeUIElementsUnclickable();
					break;
				case UPDATE_UI:
					Timer.timerValue.setText(String.format("%02d:%02d", msg.arg1, msg.arg2));
					break;
				}
			}
		};
		mTimer = new Timer(timer, getApplicationContext(), timerHandler,
				stopTime);
	}

	/**
	 * This method is fired when the button to stop or start the timer is
	 * pressed.
	 * 
	 * @param v
	 */
	public void stopStartBtn(View v) {
		// if it says stop, then set it to start
		if (((Button) v).getText().toString().compareTo("STOP") == 0) {
			//((Button) v).setText("Start");
			mTimer.reset(timerHandler);

		} else {
			// it says start, so make it say stop.
			// if it says start, then it must be stopped. so start the timer
			//((Button) v).setText("Stop");
			ChangeUIElementsUnclickable();
			mTimer.start(System.currentTimeMillis());
		}

	}

	private void ChangeUIElementsUnclickable() {
		if (visible) {
			chooseActivitySpinner.setVisibility(View.GONE);
			phoneInclude.setVisibility(View.GONE);
			findViewById(R.id.ActivityTextView).setVisibility(View.GONE);
			((Button) findViewById(R.id.stopbtn)).setText("STOP");
		} else {
			chooseActivitySpinner.setVisibility(View.VISIBLE);
			phoneInclude.setVisibility(View.VISIBLE);
			findViewById(R.id.ActivityTextView).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.stopbtn)).setText("START");
		}
		visible ^= true;
	}

	@Override
	public void onPause() {
		super.onPause();
		mTimer.removeCallbacks();
		Button b = (Button) findViewById(R.id.stopbtn);
		b.setText("start");
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	;
}
