package com.lppz.util.http;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SubCoreRunnable<T> implements Runnable,Cloneable{
	public List<T> getSubList() {
		return subList;
	}
	public void setSubList(List<T> subList) {
		this.subList = subList;
	}
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
		private List<T> subList;
		private AtomicInteger atmCount;
		public SubCoreRunnable(){
		}
		@Override
		public void run() {
		try {
			doRun(subList);
		} finally {
			atmCount.getAndAdd(subList.size());
		}
		}
		public AtomicInteger getAtmCount() {
			return atmCount;
		}
		public void setAtmCount(AtomicInteger atmCount) {
			this.atmCount = atmCount;
		}
		protected abstract void doRun(List<T> subList);
	}