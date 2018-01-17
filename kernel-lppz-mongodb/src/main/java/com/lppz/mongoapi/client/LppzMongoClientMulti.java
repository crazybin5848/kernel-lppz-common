package com.lppz.mongoapi.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class LppzMongoClientMulti {
	
	private Map<String,  LppzMongoClient> map;
	private Map<String, String> preMap = new HashMap<>();
	
	public LppzMongoClientMulti(Map<String,  LppzMongoClient> map){
		this.map = map;
	}
	
	public MongoCollection<Document> getCollection(String table){
		return getClient(table).getCollection(table);
	}
	
	public void close(){
		for (Entry<String, LppzMongoClient> entry : map.entrySet()) {
			if (entry.getValue() != null) {
				entry.getValue().close();
			}
		}
	}
	
	private LppzMongoClient getClient(String table){
		LppzMongoClient client = null;
		String prefix = preMap.get(table);
		if (prefix == null) {
			String lowerCaseTableName = table.toLowerCase();
			for (Entry<String, LppzMongoClient> entry : map.entrySet()) {
				if(lowerCaseTableName.startsWith(entry.getKey())||("sns".equals(entry.getKey())&&"AppDict".equals(table))){
					preMap.put(table, entry.getKey());
					client = entry.getValue();
					break;
				}
			}
		}else{
			client = map.get(prefix);
		}
		
		return client;
	}

}
