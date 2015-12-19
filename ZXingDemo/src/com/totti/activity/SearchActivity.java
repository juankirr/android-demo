package com.totti.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.totti.Zxing.Demo.CaptureActivity;
import com.zijunlin.Zxing.Demo.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	private View topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	
	private Button bt_submit;
	private EditText et_number;
	private EditText et_mode;
	private Spinner spn_state;
	private EditText et_managerid;
	private TextView tv_date_from;
	private TextView tv_date_to;
	private DatePickerDialog dialog_date_from;
	private DatePickerDialog dialog_date_to;
	//private DatePicker datepicker_from;
	//private DatePicker datepicker_to;
	private Switch switch_time;
	private Switch switch_number;
	private LinearLayout ll_time;
	private LinearLayout ll_noNumber;
	
	private ArrayAdapter<String> adapter;  
	private List<String> list = new ArrayList<String>();
	
	private String number;
	private String date_from;
	private String date_to;
	private String managerid;
	private String mode;
	private int state=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();
		
	}
	private void initView(){
		topbar = findViewById(R.id.a_search_topbar);
		bt_back = (Button) topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) topbar.findViewById(R.id.topbar_middle);
		tv_middle.setText("物品查询");
		bt_right.setVisibility(View.INVISIBLE);
		
		bt_submit = (Button) findViewById(R.id.a_search_bt_submit);
		switch_time = (Switch) findViewById(R.id.a_search_switch_time);
		switch_number = (Switch) findViewById(R.id.a_search_switch_number);
		ll_time = (LinearLayout) findViewById(R.id.a_search_ll_time);
		ll_noNumber = (LinearLayout) findViewById(R.id.a_search_ll_no_number);
		et_number = (EditText) findViewById(R.id.a_search_et_number);
		et_mode = (EditText) findViewById(R.id.a_search_et_mode);
		spn_state = (Spinner) findViewById(R.id.a_search_spn_state);
		et_managerid = (EditText) findViewById(R.id.a_search_et_manager);
		tv_date_from = (TextView) findViewById(R.id.a_search_tv_from);
		tv_date_to = (TextView) findViewById(R.id.a_search_tv_to);
		//datepicker_from = (DatePicker) findViewById(R.id.a_search_datepicker_from);
		//datepicker_to = (DatePicker) findViewById(R.id.a_search_datepicker_to);
		
		ll_time.setVisibility(View.INVISIBLE);
		et_number.setVisibility(View.GONE);
		
		switch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
				// TODO Auto-generated method stub
				if(ischecked){
					ll_time.setVisibility(View.VISIBLE);
				}else{
					ll_time.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		switch_number.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
				// TODO Auto-generated method stub
				if(ischecked){
					et_number.setVisibility(View.VISIBLE);
					ll_noNumber.setVisibility(View.GONE);
				}else{
					et_number.setVisibility(View.GONE);
					ll_noNumber.setVisibility(View.VISIBLE);
				}
			}
		});
		
		
		bt_back.setOnClickListener(this);
		bt_right.setText("查找");
		bt_submit.setOnClickListener(this);
		tv_date_from.setOnClickListener(this);
		tv_date_to.setOnClickListener(this);
		
		Calendar calendar=Calendar.getInstance();
	    int year=calendar.get(Calendar.YEAR);
	    int month=calendar.get(Calendar.MONTH);
	    int day=calendar.get(Calendar.DAY_OF_MONTH);
	    parseDate(year, month, day);
	    
	    dialog_date_from = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				// TODO Auto-generated method stub
				if(month <= 8){
					if(day <= 9){
						date_from = year + "-"+0+(month+1)+"-"+0+day;
					}else{
						date_from = year + "-"+0+(month+1)+"-"+day;
					}
				}else{
					if(day <= 9){
						date_from = year + "-"+(month+1)+"-"+0+day;
					}else{
						date_from = year + "-"+(month+1)+"-"+day;
					}
				}
				tv_date_from.setText(date_from);
			}
		}, year, month, day);
	    dialog_date_to = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int day) {
				// TODO Auto-generated method stub
				if(month <= 8){
					if(day <= 9){
						date_to = year + "-"+0+(month+1)+"-"+0+day;
					}else{
						date_to = year + "-"+0+(month+1)+"-"+day;
					}
				}else{
					if(day <= 9){
						date_to = year + "-"+(month+1)+"-"+0+day;
					}else{
						date_to = year + "-"+(month+1)+"-"+day;
					}
				}
				tv_date_to.setText(date_to);
			}
		}, year, month, day);
	    
	    
	    list.add("全部");
	    list.add("在库");    
        list.add("已出库");    
        list.add("已借出");    
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        spn_state.setAdapter(adapter);
        spn_state.setOnItemSelectedListener(this);
	}
	
	private void parseDate(int year,int month,int day){
		if(month <= 8){
			if(day <= 9){
				date_from = year + "-"+0+(month+1)+"-"+0+day;
			}else{
				date_from = year + "-"+0+(month+1)+"-"+day;
			}
		}else{
			if(day <= 9){
				date_from = year + "-"+(month+1)+"-"+0+day;
			}else{
				date_from = year + "-"+(month+1)+"-"+day;
			}
		}
		tv_date_from.setText(date_from);
		
		if(month <= 8){
			if(day <= 9){
				date_to = year + "-"+0+(month+1)+"-"+0+day;
			}else{
				date_to = year + "-"+0+(month+1)+"-"+day;
			}
		}else{
			if(day <= 9){
				date_to = year + "-"+(month+1)+"-"+0+day;
			}else{
				date_to = year + "-"+(month+1)+"-"+day;
			}
		}
		tv_date_to.setText(date_to);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.topbar_back://返回
			finish();
			break;
		case R.id.a_search_bt_submit://确认
			number = et_number.getText().toString();
			mode = et_mode.getText().toString();
			managerid = et_managerid.getText().toString();
			
			Intent intent = new Intent(this,ResultActivity.class);
			Bundle bundle = new Bundle();
			if(!switch_number.isChecked()){
				bundle.putBoolean("hasNumber", false);
			}else{
				bundle.putBoolean("hasNumber", true);
				bundle.putString("number", number);
			}
			bundle.putBoolean("hastime", switch_time.isChecked());
			bundle.putInt("from", 0);//0为goods,1为events
			bundle.putInt("state", state);
			bundle.putString("mode", mode);
			bundle.putString("managerid", managerid);
			bundle.putString("date_from", date_from);
			bundle.putString("date_to", date_to);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.a_search_tv_from:
			dialog_date_from.show();
			break;
		case R.id.a_search_tv_to:
			dialog_date_to.show();
			break;
		default:
			break;
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
		case 0:
			state=arg2;//全部
			break;
		case 1:
			state=arg2;//在库
			break;
		case 2:
			state=arg2;//已出库
			break;
		case 3:
			state=arg2;//已借出
			break;
		}
		System.out.println(arg2);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
