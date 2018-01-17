package com.lppz.dubbo.oms.init;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

@Path("/cache")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ ContentType.APPLICATION_JSON_UTF_8})
public interface InitComponentInternet {
	public static final String PREFIX="init";
	public static final String SUFFIX="FromDB";
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	@Path("/initBaseStore")
	@POST
	public boolean initBasestoreFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	@Path("/initLogistic")
	@POST
	public boolean initLogisticFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	@Path("/initArea")
	@POST
	public boolean initAreaFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	@Path("/initProduct")
	@POST
	public boolean initProductFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	@Path("/initStockRoomLocation")
	@POST
	public boolean initStockRoomLocationFromDB(boolean ... isKafka);
	
	/**
	 * 获取店铺信息
	 * @param cp
	 * key  查询字段值
	 * type  BaseStoreEnum，key的类型
	 * @return
	 */
	@Path("/getBaseStoreJson")
	@POST
	public String getBaseStoreJson(CacheParam cp);
	
	/**
	 * 获取物流商信息
	 * @param cp
	 * key  查询字段值
	 * type  LogisticEnum，key的类型
	 * @return
	 */
	@Path("/getLogisticJson")
	@POST
	public String getLogisticJson(CacheParam cp);
	
	/**
	 * 获取地址信息
	 * @param cp
	 * key  查询字段值
	 * type  AreaEnum，key的类型
	 * @return
	 */
	@Path("/getAreaJson")
	@POST
	public String getAreaJson(CacheParam cp);
	
	/**
	 * 获取商品信息
	 * @param cp
	 * key  查询字段值
	 * type  ProductEnum，key的类型
	 * @return
	 */
	@Path("/getProductJson")
	@POST
	public String getProductJson(CacheParam cp);
	
	/**
	 * 获取仓库信息
	 * @param cp
	 * key  查询字段值
	 * type  StockRoomLocationEnum，key的类型
	 * @return
	 */
	@Path("/getStockRoomLocationJson")
	@POST
	public String getStockRoomLocationJson(CacheParam cp);
}