package com.totti.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.totti.Bmob.Events;
import com.totti.Bmob.Goods;
import com.totti.Bmob.MyBmobUser;
import com.totti.adapter.MyAdapter2;
import com.zijunlin.Zxing.Demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsListActivity extends Activity implements OnClickListener,OnItemClickListener{
	private View topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	
	private EditText et_info;
	private ListView lv_goodslist;
	private Button bt_submit;
	//==============intent/bundle================
	private Intent intent_got;
	private Bundle bundle_got;
	int jobtype;
	//==============listview====================
	private MyAdapter2 adapter;
	public static ArrayList<HashMap<String, Object>> datalist_all = new ArrayList<HashMap<String,Object>>();
	//==============bmob=======================
	private MyBmobUser currentManager ;
	private BmobQuery<Goods> query_good;
	private List<BmobObject> goods_update = new ArrayList<BmobObject>();
	private List<BmobObject> goods_insert = new ArrayList<BmobObject>();
	private List<BmobObject> goods_delete = new ArrayList<BmobObject>();
	private List<BmobObject> goods_event = new ArrayList<BmobObject>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_goods_list);
		
		currentManager = BmobUser.getCurrentUser(this, MyBmobUser.class);
		datalist_all.clear();
		goods_delete.clear();
		goods_insert.clear();
		goods_update.clear();
		getIntentAndBundle();
		initView();
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		reflash();
	}
	@Override
	protected void onResume() {
		super.onResume();
	};
	public void remove(int index){
		datalist_all.remove(index);
	}
	public void reflash(){
		adapter.notifyDataSetChanged();
	}
	private void getIntentAndBundle(){
		intent_got = getIntent();
		bundle_got = intent_got.getExtras();
		jobtype = bundle_got.getInt("jobtype");
	}
	private void initView(){
		topbar = findViewById(R.id.a_goodslist_topbar);
		bt_back = (Button) topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) topbar.findViewById(R.id.topbar_middle);
		et_info = (EditText) findViewById(R.id.a_goodslist_info);
		lv_goodslist = (ListView) findViewById(R.id.a_goodslist_lv);
		bt_submit = (Button) findViewById(R.id.a_goodslist_new);
		
		switch (jobtype) {
		case 1:
			bt_submit.setText("入库");
			tv_middle.setText("入库表单");
			break;
		case 2:
			bt_submit.setText("出库");
			tv_middle.setText("出库表单");
			break;
		case 3:
			bt_submit.setText("借出");
			tv_middle.setText("借出表单");
			break;
		case 4:
			bt_submit.setText("归还");
			tv_middle.setText("归还表单");
			break;
		}
		
		bt_back.setOnClickListener(this);
		bt_right.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		
		adapter = new MyAdapter2(this, datalist_all);
		lv_goodslist.setAdapter(adapter);
		lv_goodslist.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,DetailListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", arg2);
		bundle.putInt("jobtype", jobtype);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.a_goodslist_new://submit
			switch (jobtype) {
			case 1://input
				if(!isRepeat(datalist_all)){
					input();
				}else{
					System.out.println("repeat");
				}
				break;
			case 2://output
				if(!isRepeat(datalist_all)){
					output();
				}else{
					System.out.println("repeat");
				}
				break;
			case 3://borrow
				if(!isRepeat(datalist_all)){
					borrow();
				}else{
					System.out.println("repeat");
				}
				break;
			case 4://return
				if(!isRepeat(datalist_all)){
					returnGoods();
				}else{
					System.out.println("repeat");
				}
				break;
			default:
				break;
			}
			break;
		case R.id.topbar_back:
			finish();
			break;
		case R.id.topbar_right://new
			//=================

			Intent intent1 = new Intent(this,DetailListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("position", -1);
			bundle.putInt("jobtype", jobtype);
			intent1.putExtras(bundle);
			startActivity(intent1);
			//================
			break;
		default:
			break;
		}
	}
	private boolean isRepeat(ArrayList<HashMap<String, Object>> list_unchecked){//list_unchecked
		if(list_unchecked.size() == 1){
			return false;
		}else if(list_unchecked.size() > 1){
			ArrayList<String> list_checked = new ArrayList<String>();
			list_checked.addAll((ArrayList<String>)list_unchecked.get(0).get("list"));
			for(int i=1;i<list_unchecked.size();i++){
				ArrayList<String> list = (ArrayList<String>) list_unchecked.get(i).get("list");
				for(int j=0;j<list.size();j++){
					if(list_checked.contains(list.get(j))){
						/*
						 * 提示重复
						 */
						Toast.makeText(this, "重复:"+list_unchecked.get(i).get("型-:")+list.get(j), Toast.LENGTH_LONG).show();
						return true;
					}else if(j==list.size()-1){
						list_checked.addAll(list);
					}
				}
			}
			return false;
		}else{
			Toast.makeText(this, "不能提交空的表单", Toast.LENGTH_LONG).show();
			return true;
		}
		
	}
	private void input(){
		goods_update.clear();
		goods_insert.clear();
		goods_event.clear();
		query_good = new BmobQuery<Goods>();
		query_good.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				
					for(int i=0;i<GoodsListActivity.datalist_all.size();i++){
						HashMap<String, Object> hashmap_list = GoodsListActivity.datalist_all.get(i);
						ArrayList<String> datalist = (ArrayList<String>) hashmap_list.get("list");
						for(int j=0;j<datalist.size();j++){
							if(arg0.size()==0){
								Goods good = new Goods();
								good.setNumber((String) datalist.get(j));
								good.setMode((String) datalist_all.get(i).get("mode"));
								good.setState(1);
								good.setManager(currentManager);
								goods_insert.add(good);
							}else{
								for(int k=0;k<arg0.size();k++){
									if(arg0.get(k).getNumber().equals(datalist.get(j))){//存在记录--修改
										//================
										if(arg0.get(k).getState() != 1){
											arg0.get(k).setState(1);
											arg0.get(k).setManager(currentManager);
											goods_update.add(arg0.get(k));
											
										}else{
											Toast.makeText(GoodsListActivity.this, ""+arg0.get(k).getMode()+"-"+arg0.get(k).getNumber()+" 已经在库", Toast.LENGTH_SHORT).show();
											return;
										}
										break;
									}else if(k==arg0.size()-1){//没有记录
										Goods good = new Goods();
										good.setNumber((String) datalist.get(j));
										good.setMode((String) datalist_all.get(i).get("mode"));
										good.setState(1);
										good.setManager(currentManager);
										goods_insert.add(good);
									}
								}	
							}
							//=================
						}
					}
				
				//================分配完成,开始批量入库====================
				goods_event.addAll(goods_insert);
				goods_event.addAll(goods_update);
				addEvent(1, currentManager, goods_event);
				new BmobObject().insertBatch(GoodsListActivity.this,goods_insert, new SaveListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "insert success", Toast.LENGTH_SHORT).show();
						goods_insert.clear();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "insert error:"+arg1, Toast.LENGTH_SHORT).show();
						goods_insert.clear();
					}
				});
				new BmobObject().updateBatch(GoodsListActivity.this, goods_update, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update success", Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update error:"+arg1, Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
				});
				GoodsListActivity.this.finish();
				
			}
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListActivity.this, "find error:"+arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void output(){
		goods_update.clear();
		goods_event.clear();
		query_good = new BmobQuery<Goods>();
		query_good.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListActivity.this, "find error:"+arg1, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<GoodsListActivity.datalist_all.size();i++){
					HashMap<String, Object> hashmap_list = GoodsListActivity.datalist_all.get(i);
					ArrayList<String> datalist = (ArrayList<String>) hashmap_list.get("list");
					for(int j=0;j<datalist.size();j++){
						for(int k=0;k<arg0.size();k++){
							if(arg0.get(k).getNumber().equals(datalist.get(j))){//存在记录--修改
								//================
								if(arg0.get(k).getState() == 1){
									arg0.get(k).setState(2);
									arg0.get(k).setManager(currentManager);
									goods_update.add(arg0.get(k));
									
								}else{
									Toast.makeText(GoodsListActivity.this, ""+arg0.get(k).getNumber()+" 不在库", Toast.LENGTH_SHORT).show();
									return;
								}
								break;
							}else if(k==arg0.size()-1){//没有记录
								Toast.makeText(GoodsListActivity.this, ""+datalist.get(j)+" 无记录，无法出库", Toast.LENGTH_SHORT).show();
								return;
							}
						}
						//=================
					}
				}
				//====
				goods_event.addAll(goods_update);
				addEvent(2, currentManager, goods_event);
				new BmobObject().updateBatch(GoodsListActivity.this, goods_update, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update success", Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update error:"+arg1, Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
				});
				GoodsListActivity.this.finish();
			}
		});
	}
	private void borrow(){
		goods_update.clear();
		goods_event.clear();
		query_good = new BmobQuery<Goods>();
		query_good.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListActivity.this, "find error:"+arg1, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<GoodsListActivity.datalist_all.size();i++){
					HashMap<String, Object> hashmap_list = GoodsListActivity.datalist_all.get(i);
					ArrayList<String> datalist = (ArrayList<String>) hashmap_list.get("list");
					for(int j=0;j<datalist.size();j++){
						for(int k=0;k<arg0.size();k++){
							if(arg0.get(k).getNumber().equals(datalist.get(j))){//存在记录--修改
								//================
								if(arg0.get(k).getState() == 1){
									arg0.get(k).setState(3);
									arg0.get(k).setManager(currentManager);
									goods_update.add(arg0.get(k));
									
								}else{
									Toast.makeText(GoodsListActivity.this, ""+arg0.get(k).getNumber()+" 不在库", Toast.LENGTH_SHORT).show();
									return;
								}
								break;
							}else if(k==arg0.size()-1){//没有记录
								Toast.makeText(GoodsListActivity.this, ""+datalist.get(j)+" 无记录，无法借出", Toast.LENGTH_SHORT).show();
								return;
							}
						}
						//=================
					}
				}
				//====
				goods_event.addAll(goods_update);
				addEvent(3, currentManager, goods_event);
				new BmobObject().updateBatch(GoodsListActivity.this, goods_update, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update success", Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update error:"+arg1, Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
				});
				GoodsListActivity.this.finish();
			}
		});
	}
	private void returnGoods(){
		goods_update.clear();
		goods_event.clear();
		query_good = new BmobQuery<Goods>();
		query_good.findObjects(this, new FindListener<Goods>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListActivity.this, "find error:"+arg1, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<GoodsListActivity.datalist_all.size();i++){
					HashMap<String, Object> hashmap_list = GoodsListActivity.datalist_all.get(i);
					ArrayList<String> datalist = (ArrayList<String>) hashmap_list.get("list");
					for(int j=0;j<datalist.size();j++){
						for(int k=0;k<arg0.size();k++){
							if(arg0.get(k).getNumber().equals(datalist.get(j))){//存在记录--修改
								//================
								if(arg0.get(k).getState() == 3){
									arg0.get(k).setState(1);
									arg0.get(k).setManager(currentManager);
									goods_update.add(arg0.get(k));
									
								}else{
									Toast.makeText(GoodsListActivity.this, ""+arg0.get(k).getNumber()+" 未借出", Toast.LENGTH_SHORT).show();
									return;
								}
								break;
							}else if(k==arg0.size()-1){//没有记录
								Toast.makeText(GoodsListActivity.this, ""+datalist.get(j)+" 无记录，无法归还", Toast.LENGTH_SHORT).show();
								return;
							}
						}
						//=================
					}
				}
				//====
				goods_event.addAll(goods_update);
				addEvent(4, currentManager, goods_event);
				new BmobObject().updateBatch(GoodsListActivity.this, goods_update, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update success", Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(GoodsListActivity.this, "update error:"+arg1, Toast.LENGTH_SHORT).show();
						goods_update.clear();
					}
				});
				GoodsListActivity.this.finish();
			}
		});
	}
	private void addEvent(int jobtype,MyBmobUser manager,List<BmobObject> list){
		Events event = new Events();
		event.setJobType(jobtype);
		event.setManager(manager);
		event.setInfo(et_info.getText().toString());
		ArrayList<String> number_list= new ArrayList<String>();
		Goods good_temp;
		for (int i = 0; i < list.size(); i++) {
			good_temp = (Goods) list.get(i);
			number_list.add(good_temp.getNumber());
		}
		event.setGoodsList(number_list);
		Toast.makeText(GoodsListActivity.this, "number_list size:"+number_list.size(), Toast.LENGTH_SHORT).show();
		
		event.save(GoodsListActivity.this, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListActivity.this, "event save success", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListActivity.this, "event save error:"+arg1, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
}
