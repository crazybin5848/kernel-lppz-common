package io.mycat.sencondaryindex.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CatTransModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8766194039591048443L;
	private String idxCurrsuffix;
	private String type;
	private List<CatTransDetailModel> detailModelList=new ArrayList<CatTransDetailModel>();
	public String getIdxCurrsuffix() {
		return idxCurrsuffix;
	}
	public void setIdxCurrsuffix(String idxCurrsuffix) {
		this.idxCurrsuffix = idxCurrsuffix;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<CatTransDetailModel> getDetailModelList() {
		return detailModelList;
	}
	public void setDetailModelList(List<CatTransDetailModel> detailModelList) {
		this.detailModelList = detailModelList;
	}
}