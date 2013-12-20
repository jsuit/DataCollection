package Timer;

import com.example.datacollection.GatherData;
import com.example.datacollection.Database.Database;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class Timer implements Runnable {
	Handler timerHandler;
	public static TextView timerValue;
	private Database db;
	private long startTime;
	private static int stopTime;

	/**
	 * Constructor for Timer Class. There is a gotcha: the timerHandler is
	 * passed in because it must from the Activity from which the timerValue is
	 * a member of. By doing this, it makes the handler associated with the UI
	 * thread. We want this because the we want to update the UI.
	 * 
	 * @param timerValue
	 *            : TextView that gets updated
	 * @param cxt
	 *            : Context, it's for the database
	 * @param timerHandler
	 *            : needs to be passed in so it will be associated with UI
	 *            thread
	 * @param stopTime
	 *            : the number of minutes we want to stop at
	 */
	public Timer(TextView timerValue, Context cxt, Handler timerHandler,
			int stopTime) {
		if (timerValue == null) {
			Log.e("Timer class has ", "null timerValue textValue");

		} else {
			Timer.timerValue = timerValue;
			db = new Database(cxt);
			this.timerHandler = timerHandler;
			Timer.stopTime = stopTime;
		}

	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/*
	 * Method that starts the timer
	 */
	public void start(long startTime) {
		setStartTime(startTime);
		timerHandler.postDelayed(this, 0);
	}

	public void reset(Handler handler) {
		// timerHandler.removeCallbacks(this);

		handler.obtainMessage(GatherData.TIMER_STOPPED, 0, 0).sendToTarget();

	}

	/**
	 * Required method for a Runnable object. This does the udpating of UI. It
	 * is called by the handler. It is magic how this happens.
	 */
	@Override
	public void run() {
		long millis = System.currentTimeMillis() - startTime;
		int seconds = (int) (millis / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;
		timerHandler.obtainMessage(GatherData.UPDATE_UI, minutes, seconds)
				.sendToTarget();
		timerHandler.postDelayed(this, 500);
		if (minutes == GatherData.stopTime) {
			timerHandler.sendMessageDelayed(
					timerHandler.obtainMessage(GatherData.TIMER_DONE, 0, 0),
					500);
		}

	}

	public void removeCallbacks() {
		timerHandler.removeCallbacks(this);
	}
}
