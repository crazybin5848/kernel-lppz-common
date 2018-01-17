//package com.lppz.dal.validation;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//
//import com.lppz.core.thread.AsyncBaseHandler;
//import com.lppz.dal.validation.Order;
//
//@Component("autoValidationShardingHandler")
//public class AutoValidationShardingHandler extends AsyncBaseHandler<Order,Order> {
//	private final Logger logger = LoggerFactory.getLogger(AutoValidationShardingHandler.class);
//	private List<Order> orderList;
//	
//	private Map<String, String> order2FailState;
//	@Resource
//	private JdbcTemplate jdbcTemplate;
//	@Resource
//	private AutoValidationWorkItemWorker autoValidationWorkItemWorker;
//	
//	
//	@Override
//	protected List<Order> fetchTableId(List<String> pks2) {
//		if (pks2 == null || pks2.size() <= 0) {
//			orderList = null;
//			return null;
//		}
//
//		StringBuffer sql = new StringBuffer("select id,orderId from orders where orderId in (");
//		for (int i = 0; i < pks2.size(); i++) {
//			if (i == pks2.size() - 1)
//				sql.append("?)");
//			else
//				sql.append("?,");
//		}
//		orderList = jdbcTemplate.query(sql.toString(), pks2.toArray(), new RowMapper<Order>() {
//			@Override
//			public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Order o = new Order();
//				o.setId(rs.getString("id"));
//				o.setOrderId(rs.getString("orderId"));
//				return o;
//			}
//
//		});
//
//		return orderList;
//	}
//
//	
//	/**
//	 * 
//	 */
//	@Override
//	protected List<Order> handleBatchOrder(List<Order> list) {
//		final Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("orderData", list);
//		autoValidationWorkItemWorker.executeInternal(parameters);
//		order2FailState = new HashMap<String, String>();
//		List<Order> oc=(List<Order>)parameters.get("orderIdList");
//		
//		Map<String, String> o2f = (Map<String, String>)parameters.get("order2FailState");
//		if(!o2f.isEmpty()){
//			for (Map.Entry<String,String> kv:o2f.entrySet()) {
//				order2FailState.put(kv.getKey(),kv.getValue());
//			}
//			logger.info(Thread.currentThread().getName()+"失败信息:", o2f);
//		}
//		
//		return oc;
//	}
//
//	protected void beforStart() {
//		initAtmCount();
//		setNumPerThread(1);
//	}
//	
//	protected void afterStart(){
//		
//		
//		StringBuffer sqlBuffer = new StringBuffer();
//		sqlBuffer.append(" update orders set state='APPROVED' where id in ( ");
//		if (lidlist != null && lidlist.size() > 0) {
//			List<String> orderIds = new ArrayList<String>();
//			int i=0;
//			for(List<Order> list :lidlist){
//				for (Order tempOrder: list) {
//					if (i != 0)
//						sqlBuffer.append(",");
//					sqlBuffer.append("?");
//					orderIds.add(tempOrder.getId());
//					i++;
//				}
//			}
//			sqlBuffer.append(")");
//			try {
//				jdbcTemplate.update(sqlBuffer.toString(), orderIds.toArray());
//			} catch (Exception e) {
//				logger.error("自动审核写入审核通过状态时发生异常.", e);
//			}
//		}
//		logger.info(Thread.currentThread().getName()+"处理成功结果:{}", lidlist);
//		logger.info(Thread.currentThread().getName()+"处理失败结果:{}", order2FailState);
//	}
//
//}
