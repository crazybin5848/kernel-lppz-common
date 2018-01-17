/**
 * 
 */
package com.lppz.dubbo.log;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lppz.oms.kafka.dto.BizLogDto;
import com.lppz.oms.kafka.dto.ExceptionLogDto;


/**
 * @author lei
 *
 */
@Path("/log")
@Produces({ContentType.APPLICATION_JSON_UTF_8})
@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
public interface RestLogInterface {

	@Path("/list")
	@POST
	public List<BizLogDto> getAll(HttpServletRequest request,Integer page,Integer rows);
	
	@Path("/listPage")
	@POST
	public Map<String,Object> getPage(@FormParam(value="page") Integer page
			,@FormParam(value="rows") Integer rows
			,@FormParam(value="className" )String className
			,@FormParam(value="methodName") String methodName);
	
	@Path("/listExceptionLog")
	@POST
	public Map<String,Object> getExceptionLogPByage(@FormParam(value="page") Integer pageNumber,
			@FormParam(value="rows") Integer pageSize,
			@FormParam(value="name") String name,
			@FormParam(value="sendFlag") String sendFlag,
			@FormParam(value="ip") String ip,
			@FormParam(value="startCreatetime") String startCreatetime,
			@FormParam(value="endCreatetime") String endCreatetime);
	
	@Path("/getExceptionLogDetail")
	@POST
	public ExceptionLogDto getExceptionLogPByage(@FormParam(value="id") Integer id);
	
	@Path("/orderLog")
	@POST
	public Map<String,Object> getOrderLog(@FormParam(value="orderId")String orderId,
			@FormParam(value="appName")String appName,
			@FormParam(value="outOrderId")String outOrderId,
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
	
	@Path("/orderLogList")
	@POST
	public Map<String,Object> getOrderLogList(@FormParam(value="orderId")String orderId,
			@FormParam(value="appName")String appName,
			@FormParam(value="outOrderId")String outOrderId,
			@FormParam(value="returnCode")String returnCode,
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
	
	@Path("/interfaceLog")
	@POST
	public Map<String,Object> getInterfaceLog(@FormParam(value="orderId")String in_messageId,
			@FormParam(value="messageId")String messageId,
			@FormParam(value="interfaceId")String interfaceId,
			@FormParam(value="piFlag")String piFlag,
			@FormParam(value="startDate")String startDate,
			@FormParam(value="endDate")String endDate,
			@FormParam(value="sourceType")String sourceType,
			@FormParam(value="targetType")String targetType,
			@FormParam(value="resultFlag")String resultFlag,
			@FormParam(value="interfaceName")String interfaceName,
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
	
	@Path("/interfaceDetailLog")
	@POST
	public Map<String,Object> getInterfaceDetailLog(@FormParam(value="interfaceLogId")String interfaceLogId);
	
	@Path("/lackOrderLog")
	@POST
	public Map<String,Object> getLackOrderLog(@FormParam(value="mergerId")String mergerId,
			@FormParam(value="orderId")String orderId,
			@FormParam(value="productId")String productId,
			@FormParam(value="whouse")String whouse,
			@FormParam(value="productname")String productname,
			@FormParam(value="starttime")String starttime,
			@FormParam(value="endtime")String endtime,
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page,
			@FormParam(value="issuccess")String issuccess);
	
	@Path("/stockSyn")
	@POST
	public Map<String,Object> getLackOrderLog(@FormParam(value="productId")String productId, 
			@FormParam(value="whouse")String whouse,
			@FormParam(value="stockType")String stockType, 
			@FormParam(value="inOutFlag")String inOutFlag, 
			@FormParam(value="synWay")String synWay,
			@FormParam(value="incrementSynWay")String incrementSynWay,
			@FormParam(value="synQuantity")String synQuantity, 
			@FormParam(value="starttime")String starttime,
			@FormParam(value="endtime")String endtime, 
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
	
	@Path("/deliveryOrder")
	@POST
	public Map<String,Object> getLackOrderLog(@FormParam(value="send")String send, 
			@FormParam(value="receive")String receive,
			@FormParam(value="orderId")String orderId, 
			@FormParam(value="outSys")String outSys, 
			@FormParam(value="starttime")String starttime,
			@FormParam(value="endtime")String endtime, 
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
	
	@Path("/optrecord")
	@POST
	public Map<String,Object> getOptRecord(@FormParam(value="funcName")String funcName, 
			@FormParam(value="optType")String optType,
			@FormParam(value="userId")String userId,
			@FormParam(value="userName")String userName, 
			@FormParam(value="starttime")String starttime,
			@FormParam(value="endtime")String endtime, 
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
	
	@Path("/productsyn")
	@POST
	public Map<String,Object> getProductSyn(@FormParam(value="productId")String productId, 
			@FormParam(value="productName")String productName,
			@FormParam(value="productCode")String productCode,
			@FormParam(value="starttime")String starttime,
			@FormParam(value="endtime")String endtime, 
			@FormParam(value="rows")Integer rows,
			@FormParam(value="page")Integer page);
}
