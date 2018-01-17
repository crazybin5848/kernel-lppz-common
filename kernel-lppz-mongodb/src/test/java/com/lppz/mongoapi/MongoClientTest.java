package com.lppz.mongoapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Test;

import com.lppz.mongoapi.client.LppzMongoClientMulti;
import com.lppz.mongoapi.enums.MongoUpdate;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.operation.UpdateOperation;

public class MongoClientTest extends SpringBaseTest {
	
	@Resource(name="lppzMongoClientMulti")
	private LppzMongoClientMulti mongoClient;
	
	@Test
	public void createIndexs(){
		String table = "SnsUserFollowed";
		String column = "userId";
		String column2 = "kegNo";
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);
		mongoClient.getCollection(table).createIndex(new Document(column, 1).append(column2, 1), indexOptions);
	}
	
	@Test
	public void pushSimpleArray(){
		String table = "SnsUserFollowed";
		String arrayField = "simpleArray";
		List<String> array = Arrays.asList("xxxx","yyyy","fffff","ddddd");
		UpdateOptions op = new UpdateOptions();
		op.upsert(true);
		Document query = new Document().append("userId", "1117801409");
//		Document detail = new Document(arrayField, array);
		List<Document> detail = buildDetails(array,arrayField);
		Document update = new Document(MongoUpdate.pushAll.getOp(),detail);
		
		mongoClient.getCollection(table).updateOne(query, update,op);
	}

	private List<Document> buildDetails(List<String> array,String field) {
		List<Document> list = new ArrayList<Document>();
		for (String s : array) {
			list.add(new Document(field, s));
		}
		// TODO Auto-generated method stub
		return list;
	}

}
