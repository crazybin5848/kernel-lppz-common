package com.lppz.dubbo.micro;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.dubbo.rpc.support.DubboRpcLog;

@Path("/redis")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ ContentType.APPLICATION_JSON_UTF_8})
//@DubboRpcLog
public interface RedisMicroService {
	
	/**
	 * 获取key的自增序列
	 * @param key
	 * @return
	 */
	@GET
	@Path("/getIncKey/{key}")
	public Map<String, Object> getIncKey(@PathParam("key") String key);
	
	/**
	 * 根据key获取范围内的自增序列，如果redis中的值已经是最大值，则会返回-1
	 * @param key
	 * @param minValue 返回最小值
	 * @param maxValue 返回最大值
	 * @return 结果集合 value:返回的序列值
	 *         msg: 错误信息，正常情况为null
	 */
	@GET
	@Path("/getIncKeyWithRange/{key}/{minValue}/{maxValue}")
	public Map<String, Object> getIncKeyWithRange(@PathParam("key") final String key
			, @PathParam("minValue") final long minValue, @PathParam("maxValue") final long maxValue);
	
	/**
	 * 获取大于等于最小值的自增序列
	 * @param key 自定义键
	 * @param minValue 序列最小值
	 * @return 自增值，异常返回null
	 */
	@GET
	@Path("/getIncKeyGreaterThenMinValue/{key}/{minValue}")
	public Long getIncKeyGreaterThenMinValue(@PathParam("key") final String key
			, @PathParam("minValue") final long minValue);
	
	/**
	 * 获取部分表自增序列，值从100000开始
	 * @param key 自定义键
	 * @return 自增值，异常返回null
	 */
	@GET
	@Path("/getTableSequenceNo/{key}")
	public Long getTableSequenceNo(@PathParam("key") final String key);
	
	
	/**
	 * 获取订单号自增序列
	 * @return
	 */
	@GET
	@Path("/generateSequenceNo")
	public Long generateSequenceNo();
	
	/**
	 * 全局增加订单锁
	 * @param orderid
	 * @param value
	 * @return
	 */
	@GET
	@Path("/orderLockOff/{orderid}/{value}")
	public Boolean orderLockOff(@PathParam("orderid") final String orderid, @PathParam("value") final String value);
	
	/**
	 * 解锁全局订单锁
	 * @param orderid
	 * @return
	 */
	@GET
	@Path("/orderUnlock/{orderid}")
	public Boolean orderUnlock(@PathParam("orderid") final String orderid);
	
	/**
	 * 根据传入key设置锁，如果key已存在，返回失败，否则返回成功并设置默认值
	 * @param key
	 * @return
	 */
	@GET
	@Path("/lockOff/{key}")
	public Boolean lockOff(@PathParam("key") final String key);
	
	/**
	 * 删除key,解锁
	 * @param key
	 * @return
	 */
	@GET
	@Path("/unlock/{key}")
	public Boolean unlock(@PathParam("key") final String key);
	
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	@GET
	@Path("/existKey/{key}")
	public Boolean existKey(@PathParam("key") final String key);
	
	@GET
	@Path("/hmget/{key}/{hkeys}")
	public List<String> hmget(@PathParam("key") final String key, @PathParam("hkeys") final String... hkeys);
	
	@GET
	@Path("/hgetAll/{key}")
	public Map<String, String> hgetAll(@PathParam("key") final String key);
	
	@GET
	@Path("/hincrBy/{key}/{field}/{value}")
	public Long hincrBy(@PathParam("key") final String key, @PathParam("field") final String field, @PathParam("value") final Long value);
	
	@GET
	@Path("/hset/{key}/{field}/{value}")
	public Long hset(@PathParam("key") final String key, @PathParam("field") final String field, @PathParam("value") final String value);
	
	@GET
	@Path("/set/{key}/{value}")
	public String set(@PathParam("key") final String key, @PathParam("value") String value);
	
	@GET
	@Path("/get/{key}")
	public String get(@PathParam("key") final String key);
	
	@GET
	@Path("/orderLockOffExprie/{orderid}/{value}/{exprieSecond}")
	public Boolean orderLockOffExpire(@PathParam("orderid") final String orderid, @PathParam("value") final String value, @PathParam("exprieSecond") final String exprieSecond);
	
}
