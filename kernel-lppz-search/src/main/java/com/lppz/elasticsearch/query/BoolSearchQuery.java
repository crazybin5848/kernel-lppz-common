package com.lppz.elasticsearch.query;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

import com.lppz.elasticsearch.query.fielditem.FieldItem;

public class BoolSearchQuery extends SearchQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5533642270862320210L;
	private List<FieldItem> kvList=new ArrayList<FieldItem>();
	private Operator op = Operator.OR;

	public BoolSearchQuery(Operator op) {
		this.op = op;
	}
	public void addFileItem(FieldItem fi){
		kvList.add(fi);
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

	public QueryBuilder build() {
		if (op==null||kvList.isEmpty())
			return null;
		op.setKvList(kvList);
		return op.build();
	}
}
