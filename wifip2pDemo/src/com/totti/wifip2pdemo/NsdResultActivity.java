package com.totti.wifip2pdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class NsdResultActivity extends Activity {
	private ListView lv;
	
	public NsdResultActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_nsd);
		
	}
	private void initView(){
		lv = (ListView) findViewById(R.id.a_r_nsd_lv);
	}
}
