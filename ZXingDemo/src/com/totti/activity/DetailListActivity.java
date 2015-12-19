package com.totti.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.totti.Bmob.Goods;
import com.totti.Zxing.Demo.CaptureActivity;
import com.totti.adapter.MyAdapter1;
import com.zijunlin.Zxing.Demo.R;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailListActivity extends Activity implements OnClickListener{
	private View topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	private EditText et_mode;
	private ListView lv_detaillist;
	private Button bt_start;
	private Button bt_add;
	
	private MyAdapter1 adapter;
	public static ArrayList<String> datalist=new ArrayList<String>();
	public static ArrayList<String> datalist_mode = new ArrayList<String>();
	
	private Intent intent_got;
	private Bundle bundle_got;
	private int position = -1;
	private int jobtype = -1;
	//=================================================
	private BmobQuery<Goods> query_goods = new BmobQuery<Goods>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detail_list);
		datalist.clear();
		initData();
		initView();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		reflash();
		if(datalist.size()!=0){
			query_goods.findObjects(this, new FindListener<Goods>() {
				@Override
				public void onSuccess(List<Goods> arg0) {
					// TODO Auto-generated method stub
					if(arg0.size()!=0){
						for(int i=0;i<datalist.size();i++){
							for(int j=0;j<arg0.size();j++){
								if(datalist.get(i).equals(arg0.get(j).getNumber())){
									datalist_mode.set(i, arg0.get(j).getMode());
									adapter.notifyDataSetChanged();
								}
							}
						}
					}else{
						
					}
				}
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(DetailListActivity.this, "restart find error:"+arg1, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//===================================================
	}
	private void initData(){
		intent_got = getIntent();
		bundle_got = intent_got.getExtras();
		position = bundle_got.getInt("position");
		jobtype = bundle_got.getInt("jobtype");
		/*
		 * 1--入库
		 * 2--出库
		 * 3--借出
		 * 4--归还
		 */
	}
	private void initView(){
		topbar = findViewById(R.id.a_detaillist_topbar);
		bt_back = (Button) topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) topbar.findViewById(R.id.topbar_middle);
		lv_detaillist = (ListView) findViewById(R.id.a_detaillist_lv);
		bt_start = (Button) findViewById(R.id.a_detaillist_start);
		et_mode = (EditText) findViewById(R.id.a_detaillist_et_mode);
		bt_add = (Button) findViewById(R.id.a_detaillist_add);
		bt_right.setVisibility(View.INVISIBLE);
		
		if(jobtype != 1){
			et_mode.setVisibility(View.GONE);
		}
		tv_middle.setText("详情列表");
		
		bt_right.setText("添加");
		bt_back.setOnClickListener(this);
		bt_start.setOnClickListener(this);
		bt_add.setOnClickListener(this);
		
		adapter = new MyAdapter1(this, datalist, this,datalist_mode);
		lv_detaillist.setAdapter(adapter);
		
		if(position >= 0){
			et_mode.setText((String)GoodsListActivity.datalist_all.get(position).get("mode"));
			datalist.addAll((ArrayList<String>)GoodsListActivity.datalist_all.get(position).get("list"));
			reflash();
			System.out.println("position:"+position);
		}
	}
	public void add(String data){
		datalist.add(data);
	}
	public void remove(int index){
		datalist.remove(index);
		datalist_mode.remove(index);
	}
	public void reflash(){
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.topbar_back:
			finish();
			break;
		case R.id.a_detaillist_add:
			String str_mode = et_mode.getText().toString();
			if(jobtype==1 && str_mode.isEmpty()){
				Toast.makeText(this, "型号不能为空", Toast.LENGTH_SHORT).show();
			}else if(datalist.size() == 0){
				Toast.makeText(this, "货品表单不能为空", Toast.LENGTH_SHORT).show();
			}else{
				if(position >= 0){
					GoodsListActivity.datalist_all.remove(position);
				}
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("mode", str_mode);
				data.put("list", new ArrayList<String>(datalist));
				GoodsListActivity.datalist_all.add(data);
				
				finish();
				Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
			}
			//==================
			
			break;
		case R.id.a_detaillist_start:
			Intent intent = new Intent(this, CaptureActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("jobtype", jobtype);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
