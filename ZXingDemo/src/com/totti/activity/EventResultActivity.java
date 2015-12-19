package com.totti.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.totti.Bmob.Events;
import com.totti.Bmob.MyBmobUser;
import com.zijunlin.Zxing.Demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EventResultActivity extends Activity implements OnItemClickListener,OnClickListener{
	private LinearLayout ll_topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	
	private ListView listview;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
	
	private Intent intent_got;
	private Bundle bundle_got;
	private int jobtype;
	private String managerid;
	private String date_from;
	private String date_to;
	private boolean hastime;
	
	private BmobQuery<Events> query_event;	
	private BmobQuery<MyBmobUser> query_user;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	public EventResultActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_result);
		initView();
		getData();
	}
	private void initView(){
		ll_topbar = (LinearLayout) findViewById(R.id.a_result_topbar);
		bt_back = (Button) ll_topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) ll_topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) ll_topbar.findViewById(R.id.topbar_middle);
		bt_right.setVisibility(View.INVISIBLE);
		tv_middle.setText("操作日志查询");
		bt_back.setOnClickListener(this);
		
		listview = (ListView) findViewById(R.id.a_result_lv);
		adapter = new SimpleAdapter(this, datalist, R.layout.item_result_event,
				new String[]{"jobtype","managerid","count","time"},
				new int[]{R.id.item_event_type,R.id.item_event_manager,R.id.item_event_count,R.id.item_event_time});
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}
	private void getData(){
		intent_got = getIntent();
		bundle_got = intent_got.getExtras();
		jobtype = bundle_got.getInt("jobtype");
		managerid = bundle_got.getString("managerid");
		date_from = bundle_got.getString("date_from");
		date_to = bundle_got.getString("date_to");
		hastime = bundle_got.getBoolean("hasTime");
		
		query_event = new BmobQuery<Events>();
		
		if(jobtype != 0){
			query_event.addWhereEqualTo("jobtype", jobtype);
		}
		if(hastime){
			Date from  = null;
			Date to = null;
			try {
			    from = sdf.parse(date_from);
			    to = sdf.parse(date_to);
			} catch (ParseException e) {
			}  
			query_event.addWhereGreaterThanOrEqualTo("updatedAt",new BmobDate(from));//包含当天
			query_event.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(to));//不包含
		}
		query_event.include("manager");
		//====================managerid isEmpty?=========================================
		if(managerid.isEmpty()){
			query_event.findObjects(this, new FindListener<Events>() {
				@Override
				public void onSuccess(List<Events> arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(EventResultActivity.this, "共找到"+arg0.size()+"条记录", Toast.LENGTH_SHORT).show();
					if(arg0.size() > 0){
						for(int i=0;i<arg0.size();i++){
							HashMap<String , Object> data = new HashMap<String, Object>();
							if(arg0.get(i).getJobType() == 1){
								data.put("jobtype", "入库");
							}else if(arg0.get(i).getJobType() == 2){
								data.put("jobtype", "出库");
							}else if(arg0.get(i).getJobType() == 3){
								data.put("jobtype", "借出");
							}else if(arg0.get(i).getJobType() == 4){
								data.put("jobtype", "归还");
							}
							data.put("count", "数量:"+arg0.get(i).getGoodsList().size());
							data.put("managerid", arg0.get(i).getManager().getUsername());
							data.put("time", arg0.get(i).getUpdatedAt());
							datalist.add(data);
						}
						adapter.notifyDataSetChanged();
					}else{
					}
				}
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(EventResultActivity.this, "find error:"+arg1, Toast.LENGTH_SHORT).show();
				}
			});
		}else{//===================is not Empty==============================
			query_user = new BmobQuery<MyBmobUser>();
			query_user.addWhereEqualTo("username", managerid);
			query_user.findObjects(this, new FindListener<MyBmobUser>() {
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(EventResultActivity.this, "find manager error:"+arg1, Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onSuccess(List<MyBmobUser> arg0) {
					// TODO Auto-generated method stub
					if(arg0.size()==1){
						if(arg0.get(0).getMode() == 1){
							query_event.addWhereEqualTo("manager", arg0.get(0));
							query_event.findObjects(EventResultActivity.this, new FindListener<Events>() {
								@Override
								public void onError(int arg0, String arg1) {
									// TODO Auto-generated method stub
									Toast.makeText(EventResultActivity.this, "find error:"+arg1, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onSuccess(List<Events> arg0) {
									// TODO Auto-generated method stub
									Toast.makeText(EventResultActivity.this, "共找到"+arg0.size()+"条记录", Toast.LENGTH_SHORT).show();
									if(arg0.size() > 0){
										for(int i=0;i<arg0.size();i++){
											HashMap<String , Object> data = new HashMap<String, Object>();
											if(arg0.get(i).getJobType() == 1){
												data.put("jobtype", "入库");
											}else if(arg0.get(i).getJobType() == 2){
												data.put("jobtype", "出库");
											}else if(arg0.get(i).getJobType() == 3){
												data.put("jobtype", "借出");
											}else if(arg0.get(i).getJobType() == 4){
												data.put("jobtype", "归还");
											}
											data.put("managerid", arg0.get(i).getManager().getUsername());
											data.put("count", "数量:"+arg0.get(i).getGoodsList().size());
											data.put("time", arg0.get(i).getUpdatedAt());
											datalist.add(data);
											System.out.println(arg0.get(i).getManager().getUsername());
										}
										adapter.notifyDataSetChanged();
									}else{
									}
								}
							});
						}else{
							Toast.makeText(EventResultActivity.this, "不是管理员", Toast.LENGTH_SHORT).show();
						}
					}else if(arg0.size()==0){
						Toast.makeText(EventResultActivity.this, "不存在该用户:", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(EventResultActivity.this, "user repeat", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.topbar_back:
			finish();
			break;

		default:
			break;
		}
	}
}
