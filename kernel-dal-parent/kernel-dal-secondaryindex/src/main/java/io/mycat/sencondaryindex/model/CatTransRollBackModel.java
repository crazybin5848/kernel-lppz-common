package io.mycat.sencondaryindex.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CatTransRollBackModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5766194039591048443L;
	private List<CatTransDetailModel> detailModelList=new ArrayList<CatTransDetailModel>();
	private Set<String> pkSet=new HashSet<String>();
	private Set<String> shardingValueSet=new HashSet<String>();
	public List<CatTransDetailModel> getDetailModelList() {
		return detailModelList;
	}
	public void setDetailModelList(List<CatTransDetailModel> detailModelList) {
		this.detailModelList = detailModelList;
	}
	public Set<String> getPkSet() {
		return pkSet;
	}
	public void setPkSet(Set<String> pkSet) {
		this.pkSet = pkSet;
	}
	public Set<String> getShardingValueSet() {
		return shardingValueSet;
	}
	public void setShardingValueSet(Set<String> shardingValueSet) {
		this.shardingValueSet = shardingValueSet;
	}
}
