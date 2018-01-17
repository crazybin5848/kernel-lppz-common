package com.lppz.elasticsearch.result;

import java.io.Serializable;
import java.util.List;

public class Pager<T> implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 4510114554016792283L;
private List<T> list;
private long count;
private int pageNum;
private int pageSize;
public List<T> getList() {
	return list;
}
public void setList(List<T> list) {
	this.list = list;
}
public long getCount() {
	return count;
}
public void setCount(long count) {
	this.count = count;
}
public int getPageNum() {
	return pageNum;
}
public void setPageNum(int pageNum) {
	this.pageNum = pageNum;
}
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}
}
