package com.lppz.util.http;


public abstract class CallBackRun {
		public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
		private Object[] params;
		public CallBackRun(Object[] params){
			this.params=params;
		}
		public abstract Object doHandleCallBack(Object... objs);
	}