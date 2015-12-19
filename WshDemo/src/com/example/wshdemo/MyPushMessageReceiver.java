package com.example.wshdemo;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyPushMessageReceiver extends BroadcastReceiver {
	
	public MyPushMessageReceiver() {
		// TODO Auto-generated constructor stub
		System.out.println("MyPushMessageReceiver的构造方法");
	}

	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String s = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            JSONObject jo = null;
            
            showNotification(arg0, s);
        }
	}
	private JSONObject parseMessage(String str) throws JSONException{
		JSONTokener jsonTonkener = new JSONTokener(str);
		JSONObject jsonObject = (JSONObject) jsonTonkener.nextValue();
		return jsonObject;
	}
	private void showNotification(Context context,String str){
		Notification notification = new Notification(R.drawable.ic_launcher, str, System.currentTimeMillis());
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.ledARGB = 0xff00ff00;
		notification.ledOffMS = 1000;
		notification.ledOnMS = 300;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    	notification.flags |= Notification.FLAG_ONGOING_EVENT;
    	Intent intent = new Intent(context, MainActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0 );
    	notification.setLatestEventInfo(context, "这是来自王思鸿的测试通知", str, pendingIntent);
    	
    	notificationManager.notify(0, notification);
	}

}
