package com.lppz.dubbo.micro;

import com.lppz.dubbo.log.pojo.InterfaceLog;
import com.lppz.dubbo.log.pojo.LackOrder;
import com.lppz.dubbo.log.pojo.LppzExLog;
import com.lppz.dubbo.log.pojo.OptRecord;
import com.lppz.dubbo.log.pojo.OrderLog;
import com.lppz.dubbo.log.pojo.OrderReturnResultLog;
import com.lppz.dubbo.log.pojo.ProductSyn;
import com.lppz.dubbo.log.pojo.StockSyn;
import com.lppz.oms.kafka.dto.WorkflowDto;
import com.lppz.oms.kafka.enums.LogKeywordEnum;


/**
 * omslog微服务接口
 * @author licheng
 *
 */
public interface OmsLogBizService {
	
	/**
	 * oms操作日志发送方法
	 * @param orderId oms订单号
	 * @param key 日志类型 com.lppz.oms.kafka.enums.LogKeywordEnum中的常量
	 * @param msg 操作信息
	 * @param user 操作人
	 * @param status 操作订单状态
	 * @return 发送是否成功
	 */
	public boolean sendMsg(String orderId, LogKeywordEnum key, String msg,String user, String status);
	
	/**
	 * oms操作日志发送方法
	 * @param orderLog 业务日志对象
	 * @param key es关键字
	 * @return 是否发送成功
	 */
	public boolean sendMsg(OrderLog orderLog, LogKeywordEnum key);
	
	/**
	 * 接口日志发送方法
	 * @param interface
	 * @return
	 */
	public boolean sendMsg(InterfaceLog interfaceLog);
	
	/**
	 * 工作流日志发送方法
	 * @param workFlowDto
	 * @return
	 */
	public boolean sendMsg(WorkflowDto workFlowDto);
	
	/**
	 * 报缺日志发送方法
	 * @param lackOrder
	 * @return
	 */
	public boolean sendMsg(LackOrder lackOrder);
	
	/**
	 * oms2wms返回结果日志发送
	 * @param orderReturnResult
	 * @return
	 */
	public boolean sendMsg(OrderReturnResultLog orderReturnResult);
	
	/**
	 * 库存同步日志发送
	 * @param stockSyn
	 * @return
	 */
	public boolean sendMsg(StockSyn stockSyn);
	
	/**
	 * 操作记录日志发送
	 * @param optRecord
	 * @return
	 */
	public boolean sendMsg(OptRecord optRecord);
	
	/**
	 * 商品同步日志发送
	 * @param pdtSyn
	 * @return
	 */
	public boolean sendMsg(ProductSyn pdtSyn);
	
	/**
	 * 异常日志发送
	 * @param lppzExLog
	 * @return
	 */
	public boolean sendMsg(LppzExLog lppzExLog);
}
