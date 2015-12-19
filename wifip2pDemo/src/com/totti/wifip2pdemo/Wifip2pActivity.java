package com.totti.wifip2pdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Wifip2pActivity extends Activity implements 
					OnClickListener,OnItemClickListener,ActionListener,PeerListListener,ConnectionInfoListener{
	private Button bt_discover,bt_send;
	private ListView lv_peers;
	private MyAdapter adapter;
	private ArrayList<WifiP2pDevice> datalist = new ArrayList<WifiP2pDevice>();
	//==============================
	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private WifiP2pManager wifiManager;
	private Wifip2pBroadcastReceiver receiver;
	private WifiP2pInfo info;
	//-------------handler---------------
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				Log.d("handler", "notify adapter");
				break;
			case 2:
				Toast.makeText(Wifip2pActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Wifip2pActivity.this, "connected!!!", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(Wifip2pActivity.this, "本机地址:"+(String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(Wifip2pActivity.this, "new client socket", Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(Wifip2pActivity.this, "bind", Toast.LENGTH_SHORT).show();
				break;
			case 7:
				Toast.makeText(Wifip2pActivity.this, "exception:"+(String)msg.obj, 3000).show();
				break;
			default:
				break;
			}
		};
	};
	public Wifip2pActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifip2p);
		initView();
		initWifiComponents();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		receiver = new Wifip2pBroadcastReceiver(wifiManager, channel, this,this);
		registerReceiver(receiver, intentFilter);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wifiManager.stopPeerDiscovery(channel, this);
		unregisterReceiver(receiver);
	}
	
	private void initView(){
		bt_discover = (Button) findViewById(R.id.a_w_bt_discover);
		bt_send=(Button) findViewById(R.id.a_w_bt_send);
		lv_peers = (ListView) findViewById(R.id.a_w_lv);
		
		adapter = new MyAdapter(this, datalist);
		lv_peers.setAdapter(adapter);
		lv_peers.setOnItemClickListener(this);
		bt_discover.setOnClickListener(this);
		
	}
	
	private void initWifiComponents(){
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	    
	    wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    channel = wifiManager.initialize(this, getMainLooper(), null);
	}
	//========================onItemClickListener==========================
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		WifiP2pDevice device = datalist.get(arg2);
		
		WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        
        wifiManager.connect(channel, config, this);
	}
	//=========================onClickListener==============================
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.a_w_bt_discover:
			System.out.println("onclick");
			Toast.makeText(this, "onclick", 1000).show();
			wifiManager.discoverPeers(channel, this);
			break;

		default:
			break;
		}
	}
	//==================actionListener========================
	@Override
	public void onFailure(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("fail:"+arg0);
	}
	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "action success", Toast.LENGTH_SHORT).show();
	}
	//==================PeerListListener========================
	@Override
	public void onPeersAvailable(WifiP2pDeviceList arg0) {
		// TODO Auto-generated method stub
		datalist.clear();
        datalist.addAll(arg0.getDeviceList());
        Message msg = new Message();
        msg.what = 1;
        mhandler.sendMessage(msg);
 
	}
	//===================ConnectionInfoListener===========================
	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo arg0) {
		// TODO Auto-generated method stub
		// InetAddress from WifiP2pInfo struct.
		Toast.makeText(this, "onConnectionInfoAvailable:", Toast.LENGTH_SHORT).show();
		info = arg0;
        // After the group negotiation, we can determine the group owner.
        if (arg0.groupFormed && arg0.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
        	Toast.makeText(this, "this is client", Toast.LENGTH_SHORT).show();
        	Toast.makeText(this, "主机地址:"+info.groupOwnerAddress.getHostAddress(), Toast.LENGTH_SHORT).show();
        	//===================================================
        	new Thread(){
    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				super.run();
    				try {
						ServerSocket server = new ServerSocket(8993);
						Message msg_ip = new Message();
						msg_ip.what = 4;
						msg_ip.obj = server.getLocalSocketAddress().toString();
						mhandler.sendMessage(msg_ip);
						
						Socket client = server.accept();
						Message msg_con = new Message();
						msg_con.what = 3;
						mhandler.sendMessage(msg_con);
						
						BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String str = br.readLine();
						Message msg = new Message();
						msg.obj = str;
						msg.what = 2;
						mhandler.sendMessage(msg);
						
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    					bw.write("hellow--from client");
    					bw.newLine();
    					bw.flush();
    					
						client.close();
						server.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
    			}
    		}.start();
        } else if (arg0.groupFormed) {
            // The other device acts as the client. In this case,
            // you'll want to create a client thread that connects to the group
            // owner.
        	Toast.makeText(this, "this is Server", Toast.LENGTH_SHORT).show();
        	bt_send.setText("send(clickable)");
        	bt_send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new Thread(){
						
		    			@Override
		    			public void run() {
		    				// TODO Auto-generated method stub
		    				super.run();
		    				try {
		    					Socket socket = new Socket();
		    					Message msg_new = new Message();
								msg_new.what = 5;
								mhandler.sendMessage(msg_new);
		    					socket.bind(null);
		    					Message msg_bin = new Message();
								msg_bin.what = 6;
								mhandler.sendMessage(msg_bin);
		    					socket.connect(new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), 8993));
		    					Message msg_con = new Message();
								msg_con.what = 3;
								mhandler.sendMessage(msg_con);
								
		    					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		    					bw.write("hellow--from client");
		    					bw.newLine();
		    					bw.flush();
		    					
		    					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								String str = br.readLine();
								
								Message msg = new Message();
								msg.obj = str;
								msg.what = 2;
								mhandler.sendMessage(msg);
								
		    					socket.close();
		    				} catch (IOException e) {
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    					Message msg_exc = new Message();
								msg_exc.obj = e.toString();
								msg_exc.what = 7;
								mhandler.sendMessage(msg_exc);
		    				}
		    			}
		    		}.start();
				}
			});
        }
	}
}
