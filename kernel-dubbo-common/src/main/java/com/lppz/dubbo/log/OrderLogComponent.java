package com.lppz.dubbo.log;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lppz.oms.kafka.dto.InterfaceLogDto;
import com.lppz.oms.kafka.dto.LackOrderLog;
import com.lppz.oms.kafka.dto.OptRecord;
import com.lppz.oms.kafka.dto.OrderLogDto;
import com.lppz.oms.kafka.dto.OrderReturnResult;
import com.lppz.oms.kafka.dto.ProductSynDto;
import com.lppz.oms.kafka.dto.StockSynDto;

@Path("log")
@Consumes({MediaType.APPLICATION_JSON })
@Produces({ ContentType.APPLICATION_JSON_UTF_8})
public interface OrderLogComponent {
	
	/**
	 * 查询订单日志接口
	 * @param dto
	 * @return
	 */
	@POST
	@Path("orderLog")
	public Map<String,Object> getOrderLog(OrderLogDto dto);
	
	/**
	 * 查询接口调用日志
	 */
	@POST
	@Path("interfaceLog")
	public Map<String,Object> getInterfaceLog(InterfaceLogDto dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("interfaceDetailLog")
	public Map<String,Object> getInterfaceDetailLog(InterfaceLogDto dto);
	
	/**
	 * 查询订单报缺日志
	 */
	@POST
	@Path("lackOrderLog")
	public Map<String,Object> getLackOrderLog(LackOrderLog dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("stockSyn")
	public Map<String,Object> getStockSynLog(StockSynDto dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("sendwms")
	public Map<String,Object> getSendWmsLog(OrderReturnResult dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("optrecord")
	public Map<String,Object> getOptRecord(OptRecord dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("productsyn")
	public Map<String,Object> getProductSyn(ProductSynDto dto);
	
}
