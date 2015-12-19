package com.totti.adapter;

import java.util.ArrayList;
import java.util.HashMap;








import com.totti.activity.DetailListActivity;
import com.zijunlin.Zxing.Demo.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyAdapter1 extends BaseAdapter{

	private ArrayList <String> mAppList;
	private ArrayList<String> mAppList_mode;
	private LayoutInflater mInflater;
	private Context mContext; 
    private MyHolder holder;
    private DetailListActivity mactivity;
	private class MyHolder {
		TextView tv_number;
		TextView tv_mode;
		Button bt_delete;
	}
	 
	public MyAdapter1(Context c, ArrayList <String > data,DetailListActivity activity,ArrayList<String> data_mode) {
		// TODO Auto-generated constructor stub
		mAppList = data;
		mAppList_mode = data_mode;
		mContext = c;
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
            convertView = mInflater.inflate(R.layout.item_good, parent,false ) ; 
            holder = new MyHolder( ) ; 
            holder.tv_number = (TextView) convertView.findViewById(R.id.item_good_number);
            holder.tv_mode = (TextView) convertView.findViewById(R.id.item_good_mode);
            holder.bt_delete = (Button) convertView.findViewById(R.id.item_good_delete);
            convertView. setTag( holder) ; 
        }
		holder.tv_mode.setText(mAppList_mode.get(position));
		holder.tv_number.setText(mAppList.get(position));
		holder.bt_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setTitle("WARNING!");
				builder.setMessage("确认删除这此货物?");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
						mactivity.remove(mposition);
						mactivity.reflash();
					}

				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				});
				builder.create().show();
			}
		});
		
		return convertView;
	}
	
}
