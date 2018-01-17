package com.lppz.elasticsearch;

import java.io.Serializable;
import java.util.List;

import com.lppz.elasticsearch.result.SearchResult;

public abstract class PrepareBulk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5907187629051758698L;

	public abstract void bulk(final List<SearchResult> listRes);
}
