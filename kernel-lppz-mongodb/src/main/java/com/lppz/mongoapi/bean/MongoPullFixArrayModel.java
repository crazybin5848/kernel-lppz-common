package com.lppz.mongoapi.bean;

import java.util.List;

import org.bson.Document;

public class MongoPullFixArrayModel {
	private int kegNo;
	private List<Document> arrays;
	
	public MongoPullFixArrayModel(){
		
	}
	public MongoPullFixArrayModel(int kegNo, List<Document> arrays){
		this.kegNo = kegNo;
		this.arrays = arrays;
	}

	public int getKegNo() {
		return kegNo;
	}

	public void setKegNo(int kegNo) {
		this.kegNo = kegNo;
	}

	public List<Document> getArrays() {
		return arrays;
	}

	public void setArrays(List<Document> arrays) {
		this.arrays = arrays;
	}

	@Override
	public String toString() {
		return "MongoPullFixArrayModel [kegNo=" + kegNo + ", arrays=" + arrays
				+ "]";
	}
}
