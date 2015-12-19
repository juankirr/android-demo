package com.totti.wifip2pdemo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.os.Build;

public class NsdActivity extends Activity implements OnClickListener,RegistrationListener,DiscoveryListener{
	
	private Button bt_discovery;
	private Button bt_register;
	private Button bt_unregister;
	private Button bt_stopDiscovery;
	private Button bt_resolve;
	private EditText edit1;
	private ListView lv;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
	
	private String expected_servicename;
	
	private NsdServiceInfo nsdInfo = null;
	private NsdManager nsdManager = null;
	private int port = 0;

	private WifiP2pManager wifip2pManager;
	private Channel channel;
	private Wifip2pBroadcastReceiver receiver;
	//private final IntentFilter intentFilter = new IntentFilter();
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}
	
	//===================
	private MyHandler handler;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        
        handler = new MyHandler();
        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        
    }        
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	//registerReceiver(receiver, intentFilter);
    	super.onResume();
    }
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	//unregisterReceiver(receiver);
    	super.onPause();
    }
    private void initView(){
    	bt_discovery = (Button) findViewById(R.id.button1);
    	bt_register = (Button) findViewById(R.id.button2);
    	bt_unregister = (Button) findViewById(R.id.button3);
    	bt_stopDiscovery = (Button) findViewById(R.id.button4);
    	bt_resolve = (Button) findViewById(R.id.button5);
    	edit1 = (EditText) findViewById(R.id.edit1);

    	lv = (ListView) findViewById(R.id.a_m_lv);
    	adapter = new SimpleAdapter(this, datalist, R.layout.item_nsd,
    			new String[]{"name","type","port"}, new int[]{R.id.item_service_name,R.id.item_service_type,R.id.item_service_port});
    	lv.setAdapter(adapter);
    	
    	bt_discovery.setOnClickListener(this);
    	bt_register.setOnClickListener(this);
    	bt_unregister.setOnClickListener(this);
    	bt_stopDiscovery.setOnClickListener(this);
    	bt_resolve.setOnClickListener(this);
    }
    private void initReceiver(){
    }
    private void initIntentFilter(){
    	//intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        //intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        //intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        //intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
    private void registerService(){
    	try {
			ServerSocket socket = new ServerSocket(0);
			port = socket.getLocalPort();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	nsdInfo = new NsdServiceInfo();
    	nsdInfo.setServiceName(edit1.getText().toString());
    	nsdInfo.setServiceType("_http._tcp.");
    	nsdInfo.setPort(port);
    	
    	nsdManager.registerService(nsdInfo, NsdManager.PROTOCOL_DNS_SD, this);
    }
    private void unregisterService(){
    	nsdManager.unregisterService(this);
    }
    private void discoverService(){
    	nsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, this);
    }
    private void stopDiscovery(){
    	nsdManager.stopServiceDiscovery(this);
    }
    private void resolve(){
    	expected_servicename = edit1.getText().toString();
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.button1:
			discoverService();
			break;
		case R.id.button2:
			registerService();
			break;
		case R.id.button3:
			unregisterService();
			break;
		case R.id.button4:
			stopDiscovery();
			 break;
		case R.id.button5:
			resolve();
			break;
		default:
			break;
		}
	}
	@Override
	public void onDiscoveryStarted(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("onDiscoveryStarted");
	}
	@Override
	public void onDiscoveryStopped(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("onDiscoverySropped");
	}
	@Override
	public void onServiceFound(NsdServiceInfo arg0) {
		// TODO Auto-generated method stub
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("name", arg0.getServiceName());
		data.put("type", arg0.getServiceType());
		data.put("port", arg0.getPort());
		data.put("info", arg0);
		datalist.add(data);
		
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
	}
	@Override
	public void onServiceLost(NsdServiceInfo arg0) {
		// TODO Auto-generated method stub
		System.out.println("onServiceLost");
	}
	@Override
	public void onStartDiscoveryFailed(String arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("onStartDiscoveryFailed,errorcode:"+arg1);
	}
	@Override
	public void onStopDiscoveryFailed(String arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("onStopDiscoveryFailed,errorcode:"+arg1);
	}
	@Override
	public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("service registered failed");
	}
	@Override
	public void onServiceRegistered(NsdServiceInfo arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this,"service registered succeed,service name is:"+arg0.getServiceName()+";The port is "+port+";arg0.getPort:"+arg0.getPort() , Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onServiceUnregistered(NsdServiceInfo arg0) {
		// TODO Auto-generated method stub
		System.out.println("service unregistered succeed");
	}
	@Override
	public void onUnregistrationFailed(NsdServiceInfo arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("service unregistered failed");
	}
}
