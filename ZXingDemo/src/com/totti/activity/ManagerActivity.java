package com.totti.activity;


import com.totti.Zxing.Demo.CaptureActivity;
import com.zijunlin.Zxing.Demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManagerActivity extends Activity implements OnClickListener{
	private Button bt_in;
	private Button bt_out;
	private Button bt_borrow;
	private Button bt_return;
	private Button bt_search;
	private Button bt_event;
	private LinearLayout ll_topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		
		initView();
	}
	private void initView(){
		bt_in = (Button) findViewById(R.id.a_manager_bt_intput);
		bt_out = (Button) findViewById(R.id.a_maneger_bt_output);
		bt_borrow = (Button) findViewById(R.id.a_manager_bt_borrow);
		bt_return = (Button) findViewById(R.id.a_manager_bt_return);
		bt_search = (Button) findViewById(R.id.a_manager_bt_search);
		bt_event = (Button) findViewById(R.id.a_manager_bt_event);
		ll_topbar = (LinearLayout) findViewById(R.id.a_manager_topbar);
		bt_back = (Button) ll_topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) ll_topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) ll_topbar.findViewById(R.id.topbar_middle);
		tv_middle.setText("业务管理");
		bt_back.setVisibility(View.INVISIBLE);
		bt_right.setVisibility(View.INVISIBLE);
		
		
		bt_in.setOnClickListener(this);
		bt_out.setOnClickListener(this);
		bt_borrow.setOnClickListener(this);
		bt_return.setOnClickListener(this);
		bt_search.setOnClickListener(this);
		bt_event.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.a_manager_bt_intput://input
			Intent intent1 = new Intent(this,GoodsListActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("jobtype", 1);//1入，2出
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		case R.id.a_maneger_bt_output://out
			Intent intent2 = new Intent(this,GoodsListActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putInt("jobtype", 2);//1入，2出
			intent2.putExtras(bundle2);
			startActivity(intent2);
			break;
		case R.id.a_manager_bt_borrow://3借
			Intent intent3 = new Intent(this,GoodsListActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putInt("jobtype", 3);//
			intent3.putExtras(bundle3);
			startActivity(intent3);
			break;
		case R.id.a_manager_bt_return://4还
			Intent intent4 = new Intent(this,GoodsListActivity.class);
			Bundle bundle4 = new Bundle();
			bundle4.putInt("jobtype", 4);//
			intent4.putExtras(bundle4);
			startActivity(intent4);
			break;
		case R.id.a_manager_bt_search:
			Intent intent5 = new Intent(this,SearchActivity.class);
			startActivity(intent5);
			break;
		case R.id.a_manager_bt_event:
			Intent intent6 = new Intent(this,EventSearchActivity.class);
			startActivity(intent6);
			break;
		}
	}

}
