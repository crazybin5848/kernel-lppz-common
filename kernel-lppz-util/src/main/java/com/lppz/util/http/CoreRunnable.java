package com.lppz.util.http;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class CoreRunnable<T> implements Runnable,Cloneable{
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
		private T t;
		private AtomicInteger atmCount;
		private int step;
		public CoreRunnable(T t,AtomicInteger atmCount,int step){
			this.t=t;
			this.atmCount=atmCount;
			this.step=step;
		}
		public CoreRunnable(){
		}
		@Override
		public void run() {
		try {
			doRun(t);
		} finally {
			atmCount.getAndAdd(step);
		}
		}
		public AtomicInteger getAtmCount() {
			return atmCount;
		}
		public void setAtmCount(AtomicInteger atmCount) {
			this.atmCount = atmCount;
		}
		public int getStep() {
			return step;
		}
		public void setStep(int step) {
			this.step = step;
		}
		protected abstract void doRun(T t);
	}