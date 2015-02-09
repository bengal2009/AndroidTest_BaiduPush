package cn.bvin.app.test.push;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;

public class MainActivity extends Activity implements SendMsgAsyncTask.OnSendScuessListener{

	public static final String APP_KEY = "6VcQVy58uM1v2GQA0YBsupl7";
	public static final String SECRIT_KEY = "luFEGyoNPPWAfK5BRlM2MNsU5z0aT5Ib";
	PushApplication app;
	Gson mGson;
	
	public static final String ACTION_COMMUNICATION = "ACTION_COMMUNICATION";
	
	BroadcastReceiver commReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("onBind")) {
				String info = intent.getStringExtra("onBind");
				Log.e("onReceive", info);
				((TextView)findViewById(R.id.textView1)).append(info);
			}else if (intent.hasExtra("onMessage")) {
				String info = intent.getStringExtra("onMessage");
				Log.e("onReceive", info);
				((TextView)findViewById(R.id.textView2)).append(info);
			}else if (intent.hasExtra("onSetTags")) {
				String info = intent.getStringExtra("onSetTags");
				Log.e("onReceive", info);
				((TextView)findViewById(R.id.textView1)).append(info);
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_COMMUNICATION);
		registerReceiver(commReceiver, intentFilter);
		PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, APP_KEY);
		Log.e("PushManager", "startWork");
		setContentView(R.layout.activity_main);
		app = PushApplication.getInstance();
		mGson = app.getGson();
	}
	
	public void send(View v) {
		String userId = app.getUserId();
		String channelId = app.getChannelId();
		String msgString = ((EditText)findViewById(R.id.etMsg)).getText().toString();
		Message message = new Message(userId, channelId, System.currentTimeMillis(), msgString, "");
		SendMsgAsyncTask task = new SendMsgAsyncTask(mGson.toJson(message), userId);
		task.setOnSendScuessListener(this);
		task.send();
	}
	
	public void setTag(View v) {
		String userId = app.getUserId();
		SetTagTask task = new SetTagTask("TAG_GROUP", userId);
		task.setTags();
	}

	@Override
	public void sendScuess() {
		Log.e("sendScuess", "发送成功");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(commReceiver);
	}
	
	
}
