package com.totti.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.totti.activity.GoodsListActivity;
import com.zijunlin.Zxing.Demo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MyAdapter3 extends BaseAdapter {

	private ArrayList < HashMap < String , Object > > mAppList;
	private LayoutInflater mInflater;
	private Context mContext; 
    private MyHolder holder;
    private Activity mactivity;
	private class MyHolder {
		TextView tv_mode;
		TextView tv_count;
	}
	 
	public MyAdapter3(Activity activity, ArrayList < HashMap < String , Object > > data) {
		// TODO Auto-generated constructor stub
		mAppList = data;
		mContext = (Context)activity;
		mInflater = LayoutInflater.from(mContext);
		mactivity = activity;
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
		final int mposition=position;
		if ( convertView != null ) { 
            holder = (MyHolder)convertView.getTag() ; 
        } else { 
            convertView = mInflater.inflate(R.layout.item_result_good_all, parent,false ) ; 
            holder = new MyHolder( ) ; 
            holder.tv_mode = (TextView) convertView.findViewById(R.id.item_good_all_mode);
            holder.tv_count = (TextView) convertView.findViewById(R.id.item_good_all_count);
            convertView. setTag( holder) ; 
        }
		HashMap < String , Object > appInfo = mAppList. get ( position );
		ArrayList<HashMap<String, Object >> temp = (ArrayList<HashMap<String, Object>>) appInfo.get("detaillist");
		holder.tv_mode.setText("ÐÍºÅ:"+appInfo.get("mode"));
		holder.tv_count.setText("ÊýÁ¿:"+ temp.size());
		return convertView;
	}
	

}
