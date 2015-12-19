package com.totti.wifip2pdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.Toast;

public class Wifip2pBroadcastReceiver extends BroadcastReceiver {
	private WifiP2pManager wifip2pManager;
	private Channel channel;
	private Activity activity;
	PeerListListener peerlistLstener;
	public Wifip2pBroadcastReceiver(WifiP2pManager wifiP2pManager,Channel channel,Activity activity,PeerListListener peerlistLstener) {
		// TODO Auto-generated constructor stub
		this.peerlistLstener = peerlistLstener;
		this.activity = activity;
		this.channel = channel;
		this.wifip2pManager = wifiP2pManager;
	}

	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            } else {
            }
        } 
        if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
        	if(wifip2pManager!=null){
        		wifip2pManager.requestPeers(channel, peerlistLstener);
        	}else{
        		Toast.makeText(arg0, "null", Toast.LENGTH_SHORT).show();
        	}
        	
        	
        }
        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
        	Toast.makeText(arg0, "WIFI_P2P_CONNECTION_CHANGED_ACTION", Toast.LENGTH_SHORT).show();
        	if (wifip2pManager == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                // We are connected with the other device, request connection
                // info to find group owner IP
                wifip2pManager.requestConnectionInfo(channel, (ConnectionInfoListener)activity);
            }
        }
        if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
        }
	}

}
