package com.lppz.mongoapi.client;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LppzMongoClient {
	
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	
	public LppzMongoClient(String database,MongoClient mongoClient){
		this.mongoClient = mongoClient;
		mongoDatabase = mongoClient.getDatabase(database);
	}
	
	public MongoCollection<Document> getCollection(String table){
		return mongoDatabase.getCollection(table);
	}
	
	public void close(){
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

}
