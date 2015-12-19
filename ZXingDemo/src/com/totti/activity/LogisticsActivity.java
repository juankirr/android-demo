package com.totti.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.totti.Bmob.Events;
import com.zijunlin.Zxing.Demo.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LogisticsActivity extends Activity implements OnClickListener{
	private LinearLayout ll_topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	
	private Intent intent_got;
	private Bundle bundle_got;
	private String number;
	
	private ListView lv;
	private  SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
	//============================
	BmobQuery<Events> query_event;
	
	public LogisticsActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistics);
		receiveData();
		initView();
		postRequest();
	}
	private void receiveData(){
		intent_got = getIntent();
		bundle_got = intent_got.getExtras();
		number = bundle_got.getString("number");
	}
	private void initView(){
		ll_topbar = (LinearLayout) findViewById(R.id.a_logistics_topbar);
		bt_back = (Button) ll_topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) ll_topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) ll_topbar.findViewById(R.id.topbar_middle);
		bt_right.setVisibility(View.INVISIBLE);
		bt_back.setOnClickListener(this);
		tv_middle.setText("操作记录:"+number);
		
		lv = (ListView) findViewById(R.id.a_log_lv);
		adapter = new SimpleAdapter(this, datalist, R.layout.item_result_event,
				new String[]{"jobtype","managerid","time"},
				new int[]{R.id.item_event_type,R.id.item_event_manager,R.id.item_event_time});
		lv.setAdapter(adapter);
	}
	private void postRequest(){
		query_event = new BmobQuery<Events>();
		String[] good_numbers = {number};
		query_event.addWhereContainsAll("goodslist", Arrays.asList(good_numbers));
		query_event.include("manager");
		query_event.findObjects(this, new FindListener<Events>() {
			@Override
			public void onSuccess(List<Events> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(LogisticsActivity.this,"size:"+arg0.size(), Toast.LENGTH_SHORT).show();
				if(arg0.size()==0){
					return;
				}
				for(int i=0;i<arg0.size();i++){
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("managerid", arg0.get(i).getManager().getUsername());
					data.put("time", arg0.get(i).getUpdatedAt());
					switch (arg0.get(i).getJobType()) {
					case 1:
						data.put("jobtype", "入库");
						break;
					case 2:
						data.put("jobtype", "出库");
						break;
					case 3:
						data.put("jobtype", "借出");
						break;
					case 4:
						data.put("jobtype", "归还");
						break;
					}
					datalist.add(data);
				}
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(LogisticsActivity.this, "error:"+arg1, Toast.LENGTH_LONG).show();
			}
		});
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
