package com.lppz.mongodb;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7561020752328655868L;

	private int userId;
	private String userName;
	private int age;
	private String address;
	
	public User(){}
	
	public User(int userId,String name,int age,String address){
		this.userId=userId;
		this.userName=name;
		this.age=age;
		this.address=address;
	}
	
	public  int getUserId() {
		return userId;
	}
	public void setUserId( int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
