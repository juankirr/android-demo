package com.totti.activity;

import java.util.ArrayList;
import java.util.HashMap;

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

public class ResultDetailActivity extends Activity implements OnItemClickListener,OnClickListener{
	private LinearLayout ll_topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	
	private ListView lv;
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
	
	private Intent intent_got;
	private Bundle bundle_got;
	private int position = -1;
	
	public ResultDetailActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_detail);
		getIntentAndBundle();
		initView();
	}
	private void getIntentAndBundle(){
		intent_got = getIntent();
		bundle_got = intent_got.getExtras();
		position = bundle_got.getInt("position");
	}
	private void initView(){
		ll_topbar = (LinearLayout) findViewById(R.id.a_result_detail_topbar);
		bt_back = (Button) ll_topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) ll_topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) ll_topbar.findViewById(R.id.topbar_middle);
		bt_right.setVisibility(View.INVISIBLE);
		bt_back.setOnClickListener(this);
		
		lv = (ListView) findViewById(R.id.a_r_d_lv);
		ArrayList<HashMap<String, Object>> temp = (ArrayList<HashMap<String, Object>>) ResultActivity.datalist.get(position).get("detaillist");
		String mode = (String) ResultActivity.datalist.get(position).get("mode");
		tv_middle.setText(mode);
		datalist.addAll(temp);
		adapter = new SimpleAdapter(this, datalist, R.layout.item_result_good,
				new String[]{"mode","state","date","number"}, new int[]{R.id.item_good_mode,R.id.item_good_state,R.id.item_good_time,
				R.id.item_good_number});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,LogisticsActivity.class);
		Bundle bundle  = new Bundle();
		bundle.putString("number", (String) datalist.get(arg2).get("number"));
		intent.putExtras(bundle);
		startActivity(intent);
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
