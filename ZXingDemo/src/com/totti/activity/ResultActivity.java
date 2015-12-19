package com.totti.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.totti.Bmob.Goods;
import com.totti.Bmob.MyBmobUser;
import com.totti.adapter.MyAdapter3;
import com.zijunlin.Zxing.Demo.R;

import A.in;
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

public class ResultActivity extends Activity implements OnItemClickListener,OnClickListener{
	
	private LinearLayout ll_topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	
	private ListView lv;
	private MyAdapter3 adapter;
	public static ArrayList<HashMap<String, Object>> datalist = new ArrayList<HashMap<String,Object>>();
	
	private Intent intent_got;
	private Bundle bundle_got;
	private int from;
	//==========goods===========
	private String date1_from;
	private String date1_to;
	private String managerid1;
	private String mode1;
	private boolean hasTime;
	private int state1;
	private boolean hasNumber;
	private String number;
	//goods List
	//=============bmob================
	private BmobQuery<Goods> query_goods=new BmobQuery<Goods>();
	private BmobQuery<MyBmobUser> query_user=new BmobQuery<MyBmobUser>();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_result);
		datalist.clear();
		getSearchOptions();
		initView();
		getData();
	}
	private void getSearchOptions(){
		intent_got = getIntent();
		bundle_got = intent_got.getExtras();
		from = bundle_got.getInt("from");
		switch (from) {
		case 0://goods
			date1_from = bundle_got.getString("date_from");
			date1_to = bundle_got.getString("date_to");
			managerid1 = bundle_got.getString("managerid");
			mode1 = bundle_got.getString("mode");
			state1 = bundle_got.getInt("state");
			hasNumber = bundle_got.getBoolean("hasNumber");
			hasTime = bundle_got.getBoolean("hastime");
			number = bundle_got.getString("number");
			break;
		case 1://events
			
			break;
		}
	}
	private void initView(){
		lv = (ListView) findViewById(R.id.a_result_lv);
		ll_topbar = (LinearLayout) findViewById(R.id.a_result_topbar);
		bt_back = (Button) ll_topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) ll_topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) ll_topbar.findViewById(R.id.topbar_middle);
		
		tv_middle.setText("查询结果");
		bt_right.setVisibility(View.INVISIBLE);
		bt_back.setOnClickListener(this);
		
		adapter = new MyAdapter3(this, datalist);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ResultDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", arg2);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	private void getData(){
		switch (from) {
		case 0:// get goods data
			
			if(hasNumber){
				query_goods.addWhereEqualTo("number", number);
				query_goods.findObjects(this, new FindListener<Goods>() {
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(ResultActivity.this, "error:"+arg1, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(List<Goods> arg0) {
						// TODO Auto-generated method stub
						if(arg0.size()==1){
							HashMap<String, Object> data_detail = new HashMap<String, Object>();
							data_detail.put("mode", arg0.get(0).getMode());
							switch (arg0.get(0).getState()) {
							case 1:
								data_detail.put("state", "在库");
								break;
							case 2:
								data_detail.put("state", "已出库");
								break;
							case 3:
								data_detail.put("state", "已借出");
								break;
							default:
								break;
							}
							
							data_detail.put("date", arg0.get(0).getUpdatedAt());
							data_detail.put("number", arg0.get(0).getNumber());
							//================================
							ArrayList<HashMap<String, Object>> detaillist = new ArrayList<HashMap<String,Object>>();
							detaillist.add(data_detail);
							HashMap<String, Object> data = new HashMap<String, Object>();
							data.put("mode", data_detail.get("mode"));
							data.put("detaillist", detaillist);
							datalist.add(data);
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(ResultActivity.this, "error:repeat", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}else{
				/*
				 * 未输入条码号
				 */
				
				if(managerid1.isEmpty()){
					/*
					 * 管理员为空
					 */
					if(!mode1.isEmpty()){
						query_goods.addWhereEqualTo("mode", mode1);
					}
					if(state1 != 0){
						query_goods.addWhereEqualTo("state", state1);
					}
					if(hasTime){
						Date date_from  = null;
						Date date_to = null;
						try {
						    date_from = sdf.parse(date1_from);
						    date_to = sdf.parse(date1_to);
						} catch (ParseException e) {
						}  
						query_goods.addWhereGreaterThanOrEqualTo("updatedAt",new BmobDate(date_from));//包含当天
						query_goods.addWhereLessThan("updatedAt", new BmobDate(date_to));//不包含
					}
					query_goods.findObjects(ResultActivity.this, new FindListener<Goods>() {
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(ResultActivity.this, "Goods查找error:"+arg1, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onSuccess(List<Goods> arg0) {
							// TODO Auto-generated method stub
							Toast.makeText(ResultActivity.this, "有"+arg0.size()+"条数据", Toast.LENGTH_SHORT).show();
							if(arg0.size()==0){
								
							}else{
								for(int i=0;i<arg0.size();i++){
									HashMap<String, Object> data_detail = new HashMap<String, Object>();
									data_detail.put("mode", arg0.get(i).getMode());
									switch (arg0.get(i).getState()) {
									case 1:
										data_detail.put("state", "在库");
										break;
									case 2:
										data_detail.put("state", "已出库");
										break;
									case 3:
										data_detail.put("state", "已借出");
										break;
									default:
										break;
									}
									
									data_detail.put("date", arg0.get(i).getUpdatedAt());
									data_detail.put("number", arg0.get(i).getNumber());
									//========================判断arraylist中是否有该mode============================
									if(datalist.size()==0){
										System.out.println("size==0");
										ArrayList<HashMap<String, Object>> detaillist = new ArrayList<HashMap<String,Object>>();
										detaillist.add(data_detail);
										HashMap<String, Object> data = new HashMap<String, Object>();
										data.put("mode", data_detail.get("mode"));
										data.put("detaillist", detaillist);
										datalist.add(data);
									}else{
										System.out.println("size!=0");
										for(int j=0;j<datalist.size();j++){
											System.out.println("j="+j);
											if(datalist.get(j).get("mode").equals(data_detail.get("mode"))){
												System.out.println("equal");
												ArrayList<HashMap<String, Object>> temp = 
														(ArrayList<HashMap<String, Object>>)datalist.get(j).get("detaillist");
												temp.add(data_detail);
												break;
											}else if(j==datalist.size()-1){
												System.out.println("not equal but scan all");
												ArrayList<HashMap<String, Object>> _detaillist = new ArrayList<HashMap<String,Object>>();
												_detaillist.add(data_detail);
												HashMap<String, Object> data = new HashMap<String, Object>();
												data.put("mode", data_detail.get("mode"));
												data.put("detaillist", _detaillist);
												datalist.add(data);
												break;
											}
										}
									}
								}
								adapter.notifyDataSetChanged();
								
							}
						}
					});
				}else{
					/*
					 * 有管理员号码
					 */
					query_user.addWhereEqualTo("username", managerid1);
					query_user.findObjects(this, new FindListener<MyBmobUser>() {

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							//error
						}

						@Override
						public void onSuccess(List<MyBmobUser> arg0) {
							// TODO Auto-generated method stub
							if(arg0.size()==1){
								
								if(arg0.get(0).getMode() != 1){
									//error
									return;
								}
								query_goods.addWhereEqualTo("manager", arg0.get(0));
								if(!mode1.isEmpty()){
									query_goods.addWhereEqualTo("mode", mode1);
								}
								if(state1 != 0){
									query_goods.addWhereEqualTo("state", state1);
								}
								if(hasTime){
									Date date_from  = null;
									Date date_to = null;
									try {
									    date_from = sdf.parse(date1_from);
									    date_to = sdf.parse(date1_to);
									} catch (ParseException e) {
									}  
									query_goods.addWhereGreaterThanOrEqualTo("updatedAt",new BmobDate(date_from));//包含当天
									query_goods.addWhereLessThan("updatedAt", new BmobDate(date_to));//不包含
								}
								query_goods.findObjects(ResultActivity.this, new FindListener<Goods>() {

									@Override
									public void onError(int arg0, String arg1) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(List<Goods> arg0) {
										// TODO Auto-generated method stub
										Toast.makeText(ResultActivity.this, "有"+arg0.size()+"条数据", Toast.LENGTH_SHORT).show();
										if(arg0.size()==0){
											
										}else{
											for(int i=0;i<arg0.size();i++){
												HashMap<String, Object> data_detail = new HashMap<String, Object>();
												data_detail.put("mode", arg0.get(i).getMode());
												switch (arg0.get(i).getState()) {
												case 1:
													data_detail.put("state", "在库");
													break;
												case 2:
													data_detail.put("state", "已出库");
													break;
												case 3:
													data_detail.put("state", "已借出");
													break;
												default:
													break;
												}
												
												data_detail.put("date", arg0.get(i).getUpdatedAt());
												data_detail.put("number", arg0.get(i).getNumber());
												//========================判断arraylist中是否有该mode============================
												if(datalist.size()==0){
													System.out.println("size==0");
													ArrayList<HashMap<String, Object>> detaillist = new ArrayList<HashMap<String,Object>>();
													detaillist.add(data_detail);
													HashMap<String, Object> data = new HashMap<String, Object>();
													data.put("mode", data_detail.get("mode"));
													data.put("detaillist", detaillist);
													datalist.add(data);
												}else{
													System.out.println("size!=0");
													for(int j=0;j<datalist.size();j++){
														System.out.println("j="+j);
														if(datalist.get(j).get("mode").equals(data_detail.get("mode"))){
															System.out.println("equal");
															ArrayList<HashMap<String, Object>> temp = 
																	(ArrayList<HashMap<String, Object>>)datalist.get(j).get("detaillist");
															temp.add(data_detail);
															break;
														}else if(j==datalist.size()-1){
															System.out.println("not equal but scan all");
															ArrayList<HashMap<String, Object>> _detaillist = new ArrayList<HashMap<String,Object>>();
															_detaillist.add(data_detail);
															HashMap<String, Object> data = new HashMap<String, Object>();
															data.put("mode", data_detail.get("mode"));
															data.put("detaillist", _detaillist);
															datalist.add(data);
															break;
														}
													}
												}
											}
											adapter.notifyDataSetChanged();
											
										}
									}
								});
								
								
							}else{
								//error
								return;
							}
						}
					});
				}
			}
			
			break;
		case 1://get event data
			
			break;
		}
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
