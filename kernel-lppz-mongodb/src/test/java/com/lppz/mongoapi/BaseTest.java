package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class BaseTest {
	private static int maxWaitTime = 300000;
	private static int socketTimeout = 200000;
	private static int maxConnectionLifeTime = 300000;
	private static int connectTimeout = 60000;
	private final static int POOLSIZE = 500;
	protected static MongoDatabase database;
	protected static DB db;
	static String dbn = null;
	
	
	@BeforeClass
	public static void init(){
		dbn = "lppzappsns";
//		dbn = "test";
		MongoClientOptions options = MongoClientOptions.builder()
				.connectionsPerHost(POOLSIZE).maxWaitTime(maxWaitTime)
				.socketTimeout(socketTimeout)
				.maxConnectionLifeTime(maxConnectionLifeTime)
				.connectTimeout(connectTimeout).build();
		final List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		// seeds.add(new ServerAddress("192.168.37.230:50000"));
//		seeds.add(new ServerAddress("192.168.37.242:50000"));
		seeds.add(new ServerAddress("10.8.102.168:50000"));
		// seeds.add(new ServerAddress("192.168.37.245:50000"));
		// seeds.add(new ServerAddress("192.168.37.246:50000"));
		// seeds.add(new ServerAddress("192.168.37.247:50000"));
		final MongoClient client = new MongoClient(seeds, options);
		database = client.getDatabase(dbn);
		db = client.getDB(dbn);
	}

}
