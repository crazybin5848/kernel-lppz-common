package com.lppz.util.kafka.producer.async;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.KafkaPropertiesUtils;
import com.lppz.util.kafka.constant.KafkaConstant;

public class BaseMsgAsyncKafkaHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(BaseMsgAsyncKafkaHandler.class);
	public static Long queue_buffering_max_ms = 1000l;
	public static int batch_num_messages = 5000;
	public static volatile boolean isInit = false;

	public void init() {
		try {
			if (!isInit) {
				String maxMs = KafkaPropertiesUtils
						.getKey(KafkaConstant.Producer.queue_buffering_max_ms);
				queue_buffering_max_ms = maxMs == null ? queue_buffering_max_ms
						: Long.parseLong(maxMs);
				String numMsg = KafkaPropertiesUtils
						.getKey(KafkaConstant.Producer.batch_num_messages);
				batch_num_messages = numMsg == null ? batch_num_messages
						: Integer.parseInt(numMsg);
				isInit = true;
			}
		} catch (Exception e) {
			if(e instanceof FileNotFoundException){
				isInit = true;
				return;
			}
			logger.error("init kafka async error:" + e.getMessage(), e);
		}
	}
	}