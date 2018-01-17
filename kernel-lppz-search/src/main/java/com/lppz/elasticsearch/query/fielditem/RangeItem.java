package com.lppz.elasticsearch.query.fielditem;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;


public class RangeItem extends FieldItem{
/**
	 * 
	 */
	private static final long serialVersionUID = 7110980700898830809L;
private String format;
private String from;
private String to;
private String gtStr;
private String geStr;
private String leStr;
private String ltStr;
public String getFormat() {
	return format;
}
public void setFormat(String format) {
	this.format = format;
}
public String getFrom() {
	return from;
}
public void setFrom(String from) {
	this.from = from;
}
public String getTo() {
	return to;
}
public void setTo(String to) {
	this.to = to;
}
public String getGtStr() {
	return gtStr;
}
public void setGtStr(String gtStr) {
	this.gtStr = gtStr;
}
public String getGeStr() {
	return geStr;
}
public void setGeStr(String geStr) {
	this.geStr = geStr;
}
public String getLeStr() {
	return leStr;
}
public void setLeStr(String leStr) {
	this.leStr = leStr;
}
public String getLtStr() {
	return ltStr;
}
public void setLtStr(String ltStr) {
	this.ltStr = ltStr;
}
@Override
public QueryBuilder build() {
	RangeQueryBuilder rqb= QueryBuilders.rangeQuery(termField);
	if(format!=null)
		rqb.format(format);
	if(from!=null)
		rqb.from(from);
	if(to!=null)
		rqb.to(to);
	if(gtStr!=null)
		rqb.gt(gtStr);
	if(ltStr!=null)
		rqb.lt(ltStr);
	if(leStr!=null)
		rqb.lte(leStr);
	if(geStr!=null)
		rqb.gte(geStr);
	return rqb;
}
}
