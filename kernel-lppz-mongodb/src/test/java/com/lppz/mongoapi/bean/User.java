package com.lppz.mongoapi.bean;

public class User {
	private int a;
	private String fuck;
	private String ads;
	private int number;
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public String getFuck() {
		return fuck;
	}
	public void setFuck(String fuck) {
		this.fuck = fuck;
	}
	public String getAds() {
		return ads;
	}
	public void setAds(String ads) {
		this.ads = ads;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [a=");
		builder.append(a);
		builder.append(", fuck=");
		builder.append(fuck);
		builder.append(", ads=");
		builder.append(ads);
		builder.append(", number=");
		builder.append(number);
		builder.append("]");
		return builder.toString();
	}
}
