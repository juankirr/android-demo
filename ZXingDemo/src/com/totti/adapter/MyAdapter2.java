package com.totti.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.totti.activity.DetailListActivity;
import com.totti.activity.GoodsListActivity;
import com.zijunlin.Zxing.Demo.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter2 extends BaseAdapter{

	private ArrayList < HashMap < String , Object > > mAppList;
	private LayoutInflater mInflater;
	private Context mContext; 
    private MyHolder holder;
    private GoodsListActivity mactivity;
	private class MyHolder {
		TextView tv_mode;
		TextView tv_count;
		Button bt_delete;
	}
	 
	public MyAdapter2(GoodsListActivity activity, ArrayList < HashMap < String , Object > > data) {
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
            convertView = mInflater.inflate(R.layout.item_goodlist, parent,false ) ; 
            holder = new MyHolder( ) ; 
            holder.tv_mode = (TextView) convertView.findViewById(R.id.item_goodlist_tv_mode);
            holder.tv_count = (TextView) convertView.findViewById(R.id.item_goodlist_tv_count);
            holder.bt_delete = (Button) convertView.findViewById(R.id.item_goodlist_bt_delete);
            convertView. setTag( holder) ; 
        }
		HashMap < String , Object > appInfo = mAppList. get ( position );
		ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) appInfo.get("list");
		holder.tv_mode.setText((String)appInfo.get("mode"));
		holder.tv_count.setText("count:"+data.size());
		holder.bt_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setTitle("WARNING!");
				builder.setMessage("从表单中删除这一组货物");
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
