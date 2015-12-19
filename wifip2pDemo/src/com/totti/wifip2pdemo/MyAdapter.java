package com.totti.wifip2pdemo;

import java.util.ArrayList;
import java.util.HashMap;




import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private ArrayList <WifiP2pDevice> mAppList;
	private LayoutInflater mInflater;
	private Context mContext; 
    private ViewHolder holder;
    
	public MyAdapter(Context c, ArrayList <WifiP2pDevice> data) {
		// TODO Auto-generated constructor stub
		mContext = c;
		mAppList = data;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	}
	
	
	private class ViewHolder {
		TextView name;
		TextView type;
		TextView port;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mAppList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position , View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if ( convertView != null ) { 
            holder = (ViewHolder) convertView. getTag ( ) ; 
        } else { 
            convertView = mInflater.inflate(R.layout.item_nsd, parent,false ) ; 
            holder = new ViewHolder( ) ; 
            holder.name = (TextView)convertView.findViewById(R.id.item_service_name); 
            holder.type = (TextView)convertView.findViewById(R.id.item_service_type); 
            holder.port = (TextView)convertView.findViewById(R.id.item_service_port);
            convertView. setTag( holder) ; 
        }
		WifiP2pDevice appInfo = mAppList. get ( position );
        
        holder.name.setText(appInfo.deviceName);
        
        return convertView;
	}

}
