package pl.froger.servicebinding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClientActivity extends Activity {
	private Button btnStartService;
	private Button btnStopService;
	private Button btnSendCustomMessage;
	private Button btnShowServiceOperationCounter;
	private EditText etCustomMessage;
	
	private MyService myService;
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
			myService = null;
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			MyService.MyBinder binder = (MyService.MyBinder) service;
			myService = binder.getMyService();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnStartService = (Button) findViewById(R.id.btnStartService);
		btnStopService = (Button) findViewById(R.id.btnStopService);
		btnSendCustomMessage = (Button) findViewById(R.id.btnSendCustomMessage);
		btnShowServiceOperationCounter = (Button) findViewById(R.id.btnShowServiceOperationCounter);
		etCustomMessage = (EditText) findViewById(R.id.etCustomMessage);
		initButtonsOnClick();
	}

	private void initButtonsOnClick() {
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnStartService:
					startMyService();
					break;
				case R.id.btnStopService:
					stopMyService();
					break;
				case R.id.btnSendCustomMessage:
					sendCustomMessage();
					break;
				case R.id.btnShowServiceOperationCounter:
					showServiceOperationCounter();
					break;
				default:
					break;
				}
			}
		};
		btnStartService.setOnClickListener(listener);
		btnStopService.setOnClickListener(listener);
		btnSendCustomMessage.setOnClickListener(listener);
		btnShowServiceOperationCounter.setOnClickListener(listener);
	}
	
	private void startMyService() {
		Intent serviceIntent = new Intent(this, MyService.class);
		startService(serviceIntent);
		bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void stopMyService() {
		Intent serviceIntent = new Intent(this, MyService.class);
		unbindService(serviceConnection);
		stopService(serviceIntent);
	}
	
	private void sendCustomMessage() {
		if(myService != null) {
			String msg = etCustomMessage.getText().toString();
			myService.setCustomMessage(msg);
		}
	}

	private void showServiceOperationCounter() {
		if(myService != null) {
			myService.showOperationCounter();
		}
	}
}