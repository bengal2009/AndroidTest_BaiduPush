package cn.bvin.app.test.push;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class MessageReceiver extends FrontiaPushMessageReceiver{

	Gson gson;
	
	/**
	 * 调用PushManager.startWork后，sdk将对pushserver发起绑定请求，
	 * 这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，
	 * 需要把这里获取的channelid和user id上传到应用server中，
	 * 再调用server接口用channel id和user id给单个手机或者用户推送。
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
		 String responseString = "onBind errorCode=" + errorCode + " appid="
	                + appid + " userId=" + userId + " channelId=" + channelId
	                + " requestId=" + requestId;
		PushApplication app =  PushApplication.getInstance();
		gson = app.getGson();
		app.setUserId(userId);
		app.setChannelId(channelId);
		Log.e("MessageReceiver#onBind", responseString);
		sendData(context, "onBind","用户id："+ userId+"；频道Id:"+channelId);
	}

	@Override
	public void onMessage(Context arg0, String message, String customContentString) {
		String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
		Log.e("MessageReceiver#onMessage", message);
		Gson mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();;
		Message msg = mGson.fromJson(message, Message.class);
		sendData(arg0, "onMessage","收到消息："+msg.getMessage()+"\n");
	}

	private void sendData(Context context,String key,String value) {
		Intent intent = new Intent(MainActivity.ACTION_COMMUNICATION);
		intent.putExtra(key, value);
		context.getApplicationContext().sendBroadcast(intent);
	}
	
	
	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2, List<String> arg3, String arg4) {
		
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2, String arg3) {
		
	}

	@Override
	public void onNotificationClicked(Context arg0, String arg1, String arg2, String arg3) {
		
	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2, List<String> arg3, String arg4) {
		StringBuilder sb = new StringBuilder();
		if (arg1==0) {
			sb.append("设置成功的tag:");
			for (String string : arg2) {
				sb.append(string).append(";");
			}
		} else {
			sb.append("设置失败的tag:");
			for (String string : arg3) {
				sb.append(string).append(";");
			}
		}
		sendData(arg0, "onSetTags",sb.toString());
	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		
	}

}
