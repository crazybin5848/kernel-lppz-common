package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.alibaba.tamper.BeanMap;
//import com.alibaba.tamper.BeanMapping;
//import com.alibaba.tamper.core.builder.BeanMappingBuilder;
import com.lppz.mongoapi.bean.Order;
import com.lppz.mongoapi.client.LppzMongoClient;
import com.lppz.mongoapi.dao.MongoDao;
import com.mongodb.client.FindIterable;

public class MappingTest extends SpringBaseTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MappingTest.class);
	
	@Resource
	private MongoDao mongoDao;
	
	@Resource(name="lppzMongoClient")
	private LppzMongoClient mongoClient;
	
	String table = "orders";
//	public BeanMap beanMap = BeanMap.create(Order.class);
//	BeanMappingBuilder builder = new BeanMappingBuilder() {
//
//        protected void configure() {
//            mapping(LinkedHashMap.class, Order.class).batch(false).reversable(true).keys("src", "target");
//            fields(srcField("order_id"), targetField("order_id")).convertor("convertor");
//            fields(srcField("toUid").clazz(ArrayList.class), targetField("toUid")).convertor("convertor");
////            fields(srcField("three").clazz(ArrayList.class), targetField("threeOther").clazz(HashSet.class)).recursiveMapping(true);
//        }
//
//    };
    
    @Test
    public void findClass(){
    	Document query = new Document();
		query.append("userid", "1twtcd7725g1o");
		 FindIterable<Order> orders = mongoClient.getCollection(table).find(query, Order.class);
		 System.out.println(orders.first());
    }
    
    @Test
	public void find(){
		Document query = new Document();
		query.append("userid", "1twtcd7725g1o");
		List<String> fieldNames = new ArrayList<>();
//		fieldNames.add("order_id");
//		fieldNames.add("toUid.a");
		List<String> excludefieldNames = new ArrayList<>();
		excludefieldNames.add("order_id");
		excludefieldNames.add("toUid");
		excludefieldNames.add("_id");
		List<Document> result = mongoDao.findMongo(table, query, fieldNames, null, 0 ,2);
//		Map<String,Object> tmpmap = null;
		for (Document document : result) {
			Order order = mapping(document);
			System.out.println(order);
		}
//		logger.info("result:{}",result);
//		logger.info("result size :{}",result.size());
	}

	private Order mapping(Map<String,Object> map) {
		Order order = new Order();
//		BeanMapping mapping = new BeanMapping(builder);
////		mapping.mapping(map, order);//使用
//		beanMap.populate(order, map);
		return order;
	}

}
