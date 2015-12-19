package com.totti.activity;

import com.totti.Bmob.MyBmobUser;
import com.zijunlin.Zxing.Demo.R;

import cn.bmob.v3.listener.SaveListener;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpActivity extends Activity implements OnClickListener{
	
	private EditText username;
	private EditText password;
	private EditText password_again;
	private EditText email;
	private Button signUp;
	
	private String str_username;
	private String str_password;
	private String str_password_again;
	private String str_email;
	
	private MyBmobUser user;
	
	public SignUpActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		initView();
		
	}
	private void initView(){
		username = (EditText) findViewById(R.id.a_signup_username);
		password = (EditText) findViewById(R.id.a_signup_password);
		email = (EditText) findViewById(R.id.a_signup_email);
		signUp = (Button) findViewById(R.id.a_signup_signup);
		password_again = (EditText) findViewById(R.id.a_signup_passwordagain);
		signUp.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.a_signup_signup:
			str_username = username.getText().toString();
			str_password = password.getText().toString();
			str_password_again = password_again.getText().toString();
			str_email = email.getText().toString();
			System.out.println(str_username);
			System.out.println(str_password);
			System.out.println(str_password_again);
			System.out.println(str_email);
			System.out.println("true or false:"+str_password.equals(str_password_again));
			if(str_username != null && str_password != null && str_password_again != null && str_email != null && str_password.equals(str_password_again)){
				
				user = new MyBmobUser();
				user.setMode(0);//mode:0为客户，1为管理员
				user.setUsername(str_username);
				user.setPassword(str_password);
				user.setEmail(str_email);

				user.signUp(this, new SaveListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(SignUpActivity.this, "注册成功,请到邮箱验证", Toast.LENGTH_SHORT).show();
						SignUpActivity.this.finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(SignUpActivity.this, "注册失败"+arg1, Toast.LENGTH_SHORT).show();
					}
				});

			}else{
				Toast.makeText(SignUpActivity.this, "请完善信息并确认2次输入的密码相同", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
}
