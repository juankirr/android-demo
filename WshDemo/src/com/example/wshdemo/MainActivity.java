package com.example.wshdemo;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.PushListener;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener{
	Notification notification = null;
	NotificationManager notificationManager = null;
	
	EditText edit1 = null;
	EditText edit2 = null;
	Button bt1 = null;
	Button bt2 = null;
	
	BmobPushManager pushManager = null;
	BmobQuery<BmobInstallation> query = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        
        initView();
        
        Bmob.initialize(this, "a2513893767e8140fd2f0fca007f9db2");
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this, "a2513893767e8140fd2f0fca007f9db2");
        pushManager = new BmobPushManager(this);
        query = BmobInstallation.getQuery();
        
        initNotification();
    }
    private void initView(){
    	edit1 = (EditText) findViewById(R.id.editttext1);
    	edit2 = (EditText) findViewById(R.id.edittext2);
    	bt1 = (Button) findViewById(R.id.button1);
    	bt2 = (Button) findViewById(R.id.button2);
    	
    	bt1.setOnClickListener(this);
    	bt2.setOnClickListener(this);
    }
    @Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.button1:
			pushManager.pushMessageAll(edit1.getText().toString());
			break;
		case R.id.button2:
			query.addWhereEqualTo("installationId", edit2.getText().toString());
			pushManager.setQuery(query);
			pushManager.pushMessage(edit1.getText().toString());
			break;
		}
	}
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	clearNotification();
    	super.onStart();
    }
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	
    }
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub5
    	super.onStop();
    	showNotification();
    	
    }
    private void initNotification(){
    	notification = new Notification(R.drawable.ic_launcher, "", System.currentTimeMillis());
    	notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	notification.flags = Notification.FLAG_ONGOING_EVENT;
    	Intent intent = new Intent(this, MainActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0 );
    	notification.setLatestEventInfo(this, "PushService", "PushService is running in background.", pendingIntent);
    	
    }
    private void showNotification(){
    	notificationManager.notify(0, notification);
    }
    private void clearNotification(){
    	notificationManager.cancel(0);
    }
}
