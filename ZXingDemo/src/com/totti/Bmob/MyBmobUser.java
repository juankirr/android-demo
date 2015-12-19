package com.totti.Bmob;

import cn.bmob.v3.BmobUser;

public class MyBmobUser extends BmobUser {
	private int mode;//0:³öÈë¿â;1:½è
	
	public MyBmobUser() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	public int getMode(){
		return mode;
	}
}
