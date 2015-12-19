package com.totti.activity;



import com.totti.Bmob.MyBmobUser;
import com.zijunlin.Zxing.Demo.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText username;
	private EditText password;
	private Button login;
	private Button signup;
	
	private String str_username;
	private String str_password;
	
	private MyBmobUser user;
	private MyBmobUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        
        Bmob.initialize(this, "775ca9e12e9081f9440c00278afc5fb2");
        
        
    }
    private void initView(){
    	username = (EditText) findViewById(R.id.a_login_username);
    	password = (EditText) findViewById(R.id.a_login_password);
    	login = (Button) findViewById(R.id.a_login_login);
    	signup = (Button) findViewById(R.id.a_login_signup);
    	
    	login.setOnClickListener(this);
    	signup.setOnClickListener(this);
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.a_login_login:
			System.out.println("点击login");
			str_username = username.getText().toString();
			str_password = password.getText().toString();
			if(str_username == null || str_password == null){
				Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
			}else{
				user = new MyBmobUser();
				user.setUsername(str_username);
				user.setPassword(str_password);
				
				
				user.login(this, new SaveListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						currentuser = BmobUser.getCurrentUser(LoginActivity.this,MyBmobUser.class);
						int mode = -1;
						boolean emailVerified = false;
						mode = currentuser.getMode();
						emailVerified = currentuser.getEmailVerified();
						/*
						 * 是否登陆及跳转
						 */
						if(emailVerified){
							/*
							 * 根据状态登陆
							 */
							switch (mode) {
							case 0://普通用户
								Intent intent0 = new Intent(LoginActivity.this,SearchActivity.class);
								startActivity(intent0);
								LoginActivity.this.finish();
								break;
							case 1://管理员
								Intent intent1 = new Intent(LoginActivity.this,ManagerActivity.class);
								startActivity(intent1);
								LoginActivity.this.finish();
								break;
							}
						}else{
							Toast.makeText(LoginActivity.this, "请先验证邮箱", Toast.LENGTH_SHORT).show();
							currentuser.logOut(LoginActivity.this);
						}
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(LoginActivity.this, "登陆失败"+arg1, Toast.LENGTH_SHORT).show();
					}
				});
				
			}
			
			break;
		case R.id.a_login_signup:
			Intent intentToSignUp = new Intent(this,SignUpActivity.class);
			startActivity(intentToSignUp);
			System.out.println("点击signup");
			break;
		default:
			break;
		}
	}
}
