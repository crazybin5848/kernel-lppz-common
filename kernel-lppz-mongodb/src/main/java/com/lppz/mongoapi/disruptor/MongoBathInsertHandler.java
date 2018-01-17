package com.lppz.mongoapi.disruptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.lppz.mongoapi.client.LppzMongoClientMulti;
import com.lppz.util.disruptor.BaseHandler;

public class MongoBathInsertHandler implements BaseHandler<Map<String, Document>> {
	private static final Logger logger = LoggerFactory.getLogger(MongoBathInsertHandler.class);
	
	private LppzMongoClientMulti lppzMongoClient;
	
	public MongoBathInsertHandler(LppzMongoClientMulti lppzMongoClient) {
		Assert.notNull(lppzMongoClient);
		this.lppzMongoClient = lppzMongoClient;
	}

	@Override
	public void handle(List<Map<String, Document>> list) {
		Map<String, List<Document>> documnts = new HashMap<>();
		Set<Entry<String, Document>> set = null;
		List<Document> tmpList = null;
		try {
			for (Map<String, Document> documnt : list) {
				set = documnt.entrySet();
				for (Entry<String, Document> entry : set) {
					tmpList = documnts.get(entry.getKey());
					if (tmpList == null) {
						tmpList = new ArrayList<>();
						documnts.put(entry.getKey(), tmpList);
					}
					tmpList.add(entry.getValue());
				}
			}
			
			Set<Entry<String,List<Document>>> opSet = documnts.entrySet();
			for (Entry<String,List<Document>> entry : opSet) {
				lppzMongoClient.getCollection(entry.getKey()).insertMany(entry.getValue());
				if(logger.isDebugEnabled()){					
					logger.debug("disruptor batch insert table={},size={}",entry.getKey(),entry.getValue().size());
				}
			}
		} catch (Exception e) {
			logger.error("批量插入异常",e);
		}finally{
			list.clear();
		}
	}

}
