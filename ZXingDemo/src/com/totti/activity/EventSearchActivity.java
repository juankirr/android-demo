package com.totti.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.impl.cookie.DateParseException;

import com.zijunlin.Zxing.Demo.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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

public class EventSearchActivity extends Activity implements OnItemSelectedListener,OnClickListener{
	
	private Spinner spn_jobtype;
	private EditText et_managerid;
	private TextView tv_date_from;
	private TextView tv_date_to;
	private DatePickerDialog dialog_from;
	private DatePickerDialog dialog_to;
	private Button bt_submit;
//	private DatePicker dp_from;
//	private DatePicker dp_to;
	private View topbar;
	private Button bt_back;
	private Button bt_right;
	private TextView tv_middle;
	private Switch switch_time;
	private LinearLayout ll_time;
	
	private ArrayAdapter<String> adapter;  
	private List<String> list = new ArrayList<String>();
	private int jobtype = 0;
	private String managerid;
	private String date_from;
	private String date_to;
	
	//=================================
	public EventSearchActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_search);
		initVeiw();
	}
	private void initVeiw(){
		spn_jobtype = (Spinner) findViewById(R.id.a_event_spn_type);
		et_managerid = (EditText) findViewById(R.id.a_event_et_manager);
		bt_submit = (Button) findViewById(R.id.a_eventsearch_submit);
//		dp_from = (DatePicker) findViewById(R.id.a_event_datepicker_from);
//		dp_to = (DatePicker) findViewById(R.id.a_event_datepicker_to);
		tv_date_from = (TextView) findViewById(R.id.a_eventsearch_tv_from);
		tv_date_to = (TextView) findViewById(R.id.a_eventsearch_tv_to);
		switch_time = (Switch) findViewById(R.id.a_event_switch_time);
		ll_time = (LinearLayout) findViewById(R.id.a_event_ll_time);
		ll_time.setVisibility(View.INVISIBLE);
		
		switch_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					ll_time.setVisibility(View.VISIBLE);
				}else{
					ll_time.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		topbar = findViewById(R.id.a_event_topbar);
		bt_back = (Button) topbar.findViewById(R.id.topbar_back);
		bt_right = (Button) topbar.findViewById(R.id.topbar_right);
		tv_middle = (TextView) topbar.findViewById(R.id.topbar_middle);
		tv_middle.setText("操作记录");
		bt_right.setVisibility(View.INVISIBLE);
		
		bt_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		tv_date_from.setOnClickListener(this);
		tv_date_to.setOnClickListener(this);
		
		Calendar calendar=Calendar.getInstance();
	    int year=calendar.get(Calendar.YEAR);
	    int month=calendar.get(Calendar.MONTH);
	    int day=calendar.get(Calendar.DAY_OF_MONTH);
	    parseDate(year, month, day);
	    
	    dialog_from = new DatePickerDialog(this, new OnDateSetListener() {
			
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
	    dialog_to = new DatePickerDialog(this, new OnDateSetListener() {
			
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
//	    dp_from.init(year, month, day-1, null);
//	    dp_to.init(year, month, day, null);
	    
	    list.add("全部");
	    list.add("入库");
	    list.add("出库");
	    list.add("借出");
	    list.add("归还");
	    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_jobtype.setAdapter(adapter);
        spn_jobtype.setOnItemSelectedListener(this);
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		jobtype = arg2;
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.topbar_back:
			finish();
			break;
		case R.id.a_eventsearch_submit:
			managerid = et_managerid.getText().toString();
			Intent intent = new Intent(this,EventResultActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("hasTime", switch_time.isChecked());
			bundle.putInt("jobtype", jobtype);
			bundle.putString("managerid", managerid);
			bundle.putString("date_from", date_from);
			bundle.putString("date_to", date_to);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.a_eventsearch_tv_from:
			dialog_from.show();
			break;
		case R.id.a_eventsearch_tv_to:
			dialog_to.show();
			break;
		}
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
}
