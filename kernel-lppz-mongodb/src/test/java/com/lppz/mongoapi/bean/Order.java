package com.lppz.mongoapi.bean;

import java.util.Date;
import java.util.List;

public class Order {
	private String userid;
	private String order_id;
	private int company_id;
	private int user_id;
	private int fetcher_id;
	private Date fetch_schedule_begin;
	private Date fetch_schedule_end;
	private int sender_id;
	private String mail_no;
	private String mail_type;
	private String order_code;
	private int order_status;
	private int prev_order_id;
	private int trade_id;
	private String goods_remark;
	private String receiver_name;
	private String receiver_wangwang_id;
	private String receiver_mobile_phone;
	private String receiver_zip_code;
	private String receiver_telephone;
	private int receiver_county_id;
	private String receiver_address;
	private Date gmt_create;
	private Date gmt_modified;
	private String status_reason;
	private int logis_type;
	private String seller_wangwang_id;
	private int seller_send_confirm;
	private int shipping;
	private String company_code;
	private String taobao_trade_id;
	private int options;
	private int shipping2;
	private int order_source;
	private Date status_date;
	private int timeout_status;
	private String feature;
	private int service_fee;
	private String seller_store_id;
	private int items_value;
	private int pre_status;
	private String ticket_id;
	private String tfs_url;
//	private List<User> toUser;
//	private List<User> formUser;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getFetcher_id() {
		return fetcher_id;
	}
	public void setFetcher_id(int fetcher_id) {
		this.fetcher_id = fetcher_id;
	}
	public Date getFetch_schedule_begin() {
		return fetch_schedule_begin;
	}
	public void setFetch_schedule_begin(Date fetch_schedule_begin) {
		this.fetch_schedule_begin = fetch_schedule_begin;
	}
	public Date getFetch_schedule_end() {
		return fetch_schedule_end;
	}
	public void setFetch_schedule_end(Date fetch_schedule_end) {
		this.fetch_schedule_end = fetch_schedule_end;
	}
	public int getSender_id() {
		return sender_id;
	}
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	public String getMail_no() {
		return mail_no;
	}
	public void setMail_no(String mail_no) {
		this.mail_no = mail_no;
	}
	public String getMail_type() {
		return mail_type;
	}
	public void setMail_type(String mail_type) {
		this.mail_type = mail_type;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public int getPrev_order_id() {
		return prev_order_id;
	}
	public void setPrev_order_id(int prev_order_id) {
		this.prev_order_id = prev_order_id;
	}
	public int getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(int trade_id) {
		this.trade_id = trade_id;
	}
	public String getGoods_remark() {
		return goods_remark;
	}
	public void setGoods_remark(String goods_remark) {
		this.goods_remark = goods_remark;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getReceiver_wangwang_id() {
		return receiver_wangwang_id;
	}
	public void setReceiver_wangwang_id(String receiver_wangwang_id) {
		this.receiver_wangwang_id = receiver_wangwang_id;
	}
	public String getReceiver_mobile_phone() {
		return receiver_mobile_phone;
	}
	public void setReceiver_mobile_phone(String receiver_mobile_phone) {
		this.receiver_mobile_phone = receiver_mobile_phone;
	}
	public String getReceiver_zip_code() {
		return receiver_zip_code;
	}
	public void setReceiver_zip_code(String receiver_zip_code) {
		this.receiver_zip_code = receiver_zip_code;
	}
	public String getReceiver_telephone() {
		return receiver_telephone;
	}
	public void setReceiver_telephone(String receiver_telephone) {
		this.receiver_telephone = receiver_telephone;
	}
	public int getReceiver_county_id() {
		return receiver_county_id;
	}
	public void setReceiver_county_id(int receiver_county_id) {
		this.receiver_county_id = receiver_county_id;
	}
	public String getReceiver_address() {
		return receiver_address;
	}
	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}
	public Date getGmt_create() {
		return gmt_create;
	}
	public void setGmt_create(Date gmt_create) {
		this.gmt_create = gmt_create;
	}
	public Date getGmt_modified() {
		return gmt_modified;
	}
	public void setGmt_modified(Date gmt_modified) {
		this.gmt_modified = gmt_modified;
	}
	public String getStatus_reason() {
		return status_reason;
	}
	public void setStatus_reason(String status_reason) {
		this.status_reason = status_reason;
	}
	public int getLogis_type() {
		return logis_type;
	}
	public void setLogis_type(int logis_type) {
		this.logis_type = logis_type;
	}
	public String getSeller_wangwang_id() {
		return seller_wangwang_id;
	}
	public void setSeller_wangwang_id(String seller_wangwang_id) {
		this.seller_wangwang_id = seller_wangwang_id;
	}
	public int getSeller_send_confirm() {
		return seller_send_confirm;
	}
	public void setSeller_send_confirm(int seller_send_confirm) {
		this.seller_send_confirm = seller_send_confirm;
	}
	public int getShipping() {
		return shipping;
	}
	public void setShipping(int shipping) {
		this.shipping = shipping;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getTaobao_trade_id() {
		return taobao_trade_id;
	}
	public void setTaobao_trade_id(String taobao_trade_id) {
		this.taobao_trade_id = taobao_trade_id;
	}
	public int getOptions() {
		return options;
	}
	public void setOptions(int options) {
		this.options = options;
	}
	public int getShipping2() {
		return shipping2;
	}
	public void setShipping2(int shipping2) {
		this.shipping2 = shipping2;
	}
	public int getOrder_source() {
		return order_source;
	}
	public void setOrder_source(int order_source) {
		this.order_source = order_source;
	}
	public Date getStatus_date() {
		return status_date;
	}
	public void setStatus_date(Date status_date) {
		this.status_date = status_date;
	}
	public int getTimeout_status() {
		return timeout_status;
	}
	public void setTimeout_status(int timeout_status) {
		this.timeout_status = timeout_status;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public int getService_fee() {
		return service_fee;
	}
	public void setService_fee(int service_fee) {
		this.service_fee = service_fee;
	}
	public String getSeller_store_id() {
		return seller_store_id;
	}
	public void setSeller_store_id(String seller_store_id) {
		this.seller_store_id = seller_store_id;
	}
	public int getItems_value() {
		return items_value;
	}
	public void setItems_value(int items_value) {
		this.items_value = items_value;
	}
	public int getPre_status() {
		return pre_status;
	}
	public void setPre_status(int pre_status) {
		this.pre_status = pre_status;
	}
	public String getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
	}
	public String getTfs_url() {
		return tfs_url;
	}
	public void setTfs_url(String tfs_url) {
		this.tfs_url = tfs_url;
	}
//	public List<User> getToUser() {
//		return toUser;
//	}
//	public void setToUser(List<User> toUser) {
//		this.toUser = toUser;
//	}
//	public List<User> getFormUser() {
//		return formUser;
//	}
//	public void setFormUser(List<User> formUser) {
//		this.formUser = formUser;
//	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Order [userid=");
		builder.append(userid);
		builder.append(", order_id=");
		builder.append(order_id);
		builder.append(", company_id=");
		builder.append(company_id);
		builder.append(", user_id=");
		builder.append(user_id);
		builder.append(", fetcher_id=");
		builder.append(fetcher_id);
		builder.append(", fetch_schedule_begin=");
		builder.append(fetch_schedule_begin);
		builder.append(", fetch_schedule_end=");
		builder.append(fetch_schedule_end);
		builder.append(", sender_id=");
		builder.append(sender_id);
		builder.append(", mail_no=");
		builder.append(mail_no);
		builder.append(", mail_type=");
		builder.append(mail_type);
		builder.append(", order_code=");
		builder.append(order_code);
		builder.append(", order_status=");
		builder.append(order_status);
		builder.append(", prev_order_id=");
		builder.append(prev_order_id);
		builder.append(", trade_id=");
		builder.append(trade_id);
		builder.append(", goods_remark=");
		builder.append(goods_remark);
		builder.append(", receiver_name=");
		builder.append(receiver_name);
		builder.append(", receiver_wangwang_id=");
		builder.append(receiver_wangwang_id);
		builder.append(", receiver_mobile_phone=");
		builder.append(receiver_mobile_phone);
		builder.append(", receiver_zip_code=");
		builder.append(receiver_zip_code);
		builder.append(", receiver_telephone=");
		builder.append(receiver_telephone);
		builder.append(", receiver_county_id=");
		builder.append(receiver_county_id);
		builder.append(", receiver_address=");
		builder.append(receiver_address);
		builder.append(", gmt_create=");
		builder.append(gmt_create);
		builder.append(", gmt_modified=");
		builder.append(gmt_modified);
		builder.append(", status_reason=");
		builder.append(status_reason);
		builder.append(", logis_type=");
		builder.append(logis_type);
		builder.append(", seller_wangwang_id=");
		builder.append(seller_wangwang_id);
		builder.append(", seller_send_confirm=");
		builder.append(seller_send_confirm);
		builder.append(", shipping=");
		builder.append(shipping);
		builder.append(", company_code=");
		builder.append(company_code);
		builder.append(", taobao_trade_id=");
		builder.append(taobao_trade_id);
		builder.append(", options=");
		builder.append(options);
		builder.append(", shipping2=");
		builder.append(shipping2);
		builder.append(", order_source=");
		builder.append(order_source);
		builder.append(", status_date=");
		builder.append(status_date);
		builder.append(", timeout_status=");
		builder.append(timeout_status);
		builder.append(", feature=");
		builder.append(feature);
		builder.append(", service_fee=");
		builder.append(service_fee);
		builder.append(", seller_store_id=");
		builder.append(seller_store_id);
		builder.append(", items_value=");
		builder.append(items_value);
		builder.append(", pre_status=");
		builder.append(pre_status);
		builder.append(", ticket_id=");
		builder.append(ticket_id);
		builder.append(", tfs_url=");
		builder.append(tfs_url);
//		builder.append(", toUser=");
//		builder.append(toUser);
//		builder.append(", formUser=");
//		builder.append(formUser);
		builder.append("]");
		return builder.toString();
	}
}
