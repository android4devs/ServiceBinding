package pl.froger.servicebinding;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	private int operationCounter;
	private String customMessage;
	
	private Toast toast;
	private Timer timer;
	private TimerTask timerTask;
	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			if(customMessage == null) {
				showToast("Your service is still working");
			} else {
				showToast("Your custom message: " + customMessage);
			}
			operationCounter++;
		}
	}
	
	private final IBinder binder = new MyBinder();
	public class MyBinder extends Binder {
		MyService getMyService() {
			return MyService.this;
		}
	}
	
	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}
	
	public void showOperationCounter() {
		showToast("Operation counter: " + operationCounter);
	}
	
	private void showToast(String text) {
		toast.setText(text);
		toast.show();
	}
	
	private void writeToLogs(String message) {
		Log.d("HelloServices", message);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		writeToLogs("Called onCreate() method.");
		timer = new Timer();
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		operationCounter = 1;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		writeToLogs("Called onStartCommand() methond");
		clearTimerSchedule();
		initTask();
		timer.scheduleAtFixedRate(timerTask, 4 * 1000, 4 * 1000);
		showToast("Your service has been started");
		return super.onStartCommand(intent, flags, startId);
	}

	private void clearTimerSchedule() {
		if(timerTask != null) {
			timerTask.cancel();
			timer.purge();
		}
	}
	
	private void initTask() {
		timerTask = new MyTimerTask();
	}

	@Override
	public void onDestroy() {
		writeToLogs("Called onDestroy() method");
		clearTimerSchedule();
		showToast("Your service has been stopped");
		super.onDestroy();
	}

	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
}