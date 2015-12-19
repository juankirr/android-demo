package com.totti.Bmob;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Events extends BmobObject {
	private int jobtype;
	private MyBmobUser manager;
	private String info;
	private ArrayList<String> goodslist;
	public Events() {
		// TODO Auto-generated constructor stub
	}

	public Events(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	public int getJobType(){
		return jobtype;
	}
	public MyBmobUser getManager(){
		return manager;
	}
	public String getInfo(){
		return info;
	}
	public ArrayList<String> getGoodsList(){
		return goodslist;
	}
	public void setJobType(int jobtype){
		this.jobtype = jobtype;
	}
	public void setManager(MyBmobUser manager){
		this.manager = manager;
	}
	public void setInfo(String info){
		this.info = info;
	}
	public void setGoodsList(ArrayList<String> goodslist){
		this.goodslist = goodslist;
	}
}
