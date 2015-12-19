package com.totti.Bmob;

import cn.bmob.v3.BmobObject;

public class Goods extends BmobObject {

	private String mode;//
	private	String number;
	private int state;//1--在库;2--已出库;3--借出
	private MyBmobUser manager;
	public Goods() {
		// TODO Auto-generated constructor stub
	}

	public Goods(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}

	public void setMode(String mode){
		this.mode = mode;
	}
	public void setNumber(String number){
		this.number = number;
	}
	public void setState(int state){
		this.state = state;
	}
	public void setManager(MyBmobUser manager){
		this.manager = manager;
	}
	public String getMode(){
		return mode;
	}
	public String getNumber(){
		return number;
	}
	public int getState(){
		return state;
	}
	public MyBmobUser getManager(){
		return manager;
	}
}
