package com.lppz.dubbo.oms.init;

import java.util.Map;

import com.lppz.dubbo.oms.pojo.Area;
import com.lppz.dubbo.oms.pojo.BaseStore;
import com.lppz.dubbo.oms.pojo.Logistic;
import com.lppz.dubbo.oms.pojo.Product;
import com.lppz.dubbo.oms.pojo.StockRoomLocation;

public interface InitComponent {
	public static final String PREFIX="init";
	public static final String SUFFIX="FromDB";
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	public boolean initBasestoreFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	public boolean initLogisticFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	public boolean initAreaFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	public boolean initProductFromDB(boolean ... isKafka);
	
	/**
	 * 重读数据库，更新缓存数据
	 * @param isKafka 缓存集群是否同步更新，不传默认不同步
	 * @return
	 */
	public boolean initStockRoomLocationFromDB(boolean ... isKafka);
	
	/**
	 * 获取店铺信息
	 * @param cp
	 * key  查询字段值
	 * type  BaseStoreEnum，key的类型
	 * @return
	 */
	public BaseStore getBaseStore(CacheParam cp);
	
	/**
	 * 获取物流商信息
	 * @param cp
	 * key  查询字段值
	 * type  LogisticEnum，key的类型
	 * @return
	 */
	public Logistic getLogistic(CacheParam cp);
	
	/**
	 * 获取地址信息
	 * @param cp
	 * key  查询字段值
	 * type  AreaEnum，key的类型
	 * @return
	 */
	public Map<String,Area> getArea(CacheParam cp);
	
	/**
	 * 获取商品信息
	 * @param cp
	 * key  查询字段值
	 * type  ProductEnum，key的类型
	 * @return
	 */
	public Product getProduct(CacheParam cp);
	
	/**
	 * 获取仓库信息
	 * @param cp
	 * key  查询字段值
	 * type  StockRoomLocationEnum，key的类型
	 * @return
	 */
	public StockRoomLocation getStockRoomLocation(CacheParam cp);
}