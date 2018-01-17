//package com.lppz.dal.validation;
//
///*
// * [y] hybris Platform
// * 自动审核 worker
// * add by zhaozhen
// * 2015-04-15 15:20
// */
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import com.lppz.oms.kafka.constant.LogKeyword;
//import com.lppz.oms.kafka.dto.BizLogDto;
//import com.lppz.oms.kafka.dto.OrderLogDto;
//import com.lppz.oms.kafka.producer.BizLogProducer;
//
//
///**
// * 自动审核 service 服务类.
// */
//@Service
//public class AutoValidationWorkItemWorker 
//{
//	private static final Logger LOG = LoggerFactory.getLogger(AutoValidationWorkItemWorker.class);
//
//	@Resource
//	private JdbcTemplate jdbcTemplate;
//
//
//	@Transactional
//	public void executeInternal(final Map<String, Object> parameters)
//	{
//		LOG.info("AutoValidationWorkItemWorker ");
//		
//		boolean success = true;
//		@SuppressWarnings("unchecked")
//		List<Order> orderList = ((List<Order>) parameters.get("orderData"));
//		if(orderList==null || orderList.size()<=0){
//			return ;
//		}
////		StringBuffer sqlBuffer = new StringBuffer();
////		sqlBuffer.append(" update orders set state='APPROVED' where orderid in ( ");
//		
//		List<Order> orderIdList = new ArrayList<Order>();
//		Map<String, String> order2FaileState = new HashMap<String, String>();
//		for (Order order : orderList) {
//			this.autoValidation( success,order,orderIdList,order2FaileState);
//		}
//		
//		parameters.put("orderIdList",orderIdList);
//		parameters.put("order2FailState", order2FaileState);
//	}
//	
//	/**
//	 * @param context
//	 * @param success
//	 */
//	private void autoValidation( boolean success,Order order,List<Order> orderIdList,Map<String, String> order2FailState) {
//		final String orderId = order.getOrderId();
//		// 存放审核结果标志 使用set
//		final Set<String> setState = new LinkedHashSet<String>();
//		final StringBuffer stateBuffere = new StringBuffer();
//		final StringBuffer sb = new StringBuffer();
//		LOG.debug("自动审核开始 ", orderId);
//		try
//		{
//			/**
//			 * 自动审核 结果 1. 黑名单 2. 逾金额 3. 有备注
//			 */
//			if (order != null)
//			{
//			}
//
//		}
//		catch (final Exception  e)
//		{
//			LOG.warn("自动审核异常.", e);
//			success = false;
//		}
//		finally
//		{
//			if (success)
//			{
//				orderIdList.add(order);
//			}
//			else
//			{
//				for (final String string : setState)
//				{
//					stateBuffere.append(string).append(",");
//				}
//				order2FailState.put(order.getOrderId(), stateBuffere.toString());
//			}
////			insertBizLog(order.getOrderId(), sb.toString());
//			LOG.debug(Thread.currentThread().getName()+"Done autoValidation work item on order id: {}. Outcome: {}", order.getOrderId(), Boolean.toString(success));
//		}
//	}
//	// 日志记录
//	protected void insertBizLog(final String orderId, final String createdState)
//	{
//		final BizLogDto dto = new BizLogDto();
//		dto.setKeyword(LogKeyword.ORDERLOG);
//		final OrderLogDto orderDto = new OrderLogDto();
//		orderDto.setOrderId(orderId);
//		orderDto.setOperateTime(new Date());
//		orderDto.setOperateUser("system");
//		orderDto.setStatus("订单审核结束");
//		if (StringUtils.isEmpty(createdState))
//		{
//			orderDto.setRemark("系统审核结果: 成功");
//		}
//		else
//		{
//			orderDto.setRemark("系统审核结果: " + createdState);
//		}
//		dto.setOrderLogDto(orderDto);
////		BizLogProducer.sendMsg(dto);
//	}
//
//}
