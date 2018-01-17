package com.lppz.dal;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class TestBean {
	
	private String name;
	
	private int age;
	private String address;
	private String gate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public String getGate() {
		return gate;
	}
	public void setGate(String gate) {
		this.gate = gate;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestBean [name=");
		builder.append(name);
		builder.append(", age=");
		builder.append(age);
		builder.append(", address=");
		builder.append(address);
		builder.append(", gate=");
		builder.append(gate);
		builder.append("]");
		return builder.toString();
	}
	public static void main(String[] args) {
		TestBean bean1 = new TestBean();
		bean1.setAddress("xxx");
		bean1.setAge(1);
		bean1.setName("name");
		Map<String,TestBean> map = new HashMap<String, TestBean>();
		map.put("test", bean1);
		System.out.println(new Yaml().dump(map));
	}
}
