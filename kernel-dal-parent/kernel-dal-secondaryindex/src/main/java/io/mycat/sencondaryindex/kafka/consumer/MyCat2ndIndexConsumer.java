package io.mycat.sencondaryindex.kafka.consumer;

import io.mycat.sencondaryindex.model.Dal2ndIdxKafkaModel;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.lppz.util.kafka.constant.KafkaConstant;
import com.lppz.util.kafka.consumer.BaseKafkaConsumer;
import com.lppz.util.kafka.consumer.listener.BaseByteKafkaConsumerListener;
@Component("myCat2ndIndexConsumer")
public class MyCat2ndIndexConsumer extends BaseKafkaConsumer<Dal2ndIdxKafkaModel> implements InitializingBean
{
	@Resource(name="myCat2ndIndexConsumerListener")
	public void setKafkaListener(
			BaseByteKafkaConsumerListener<Dal2ndIdxKafkaModel> kafkaListener) {
		super.setBytekafkaListener(kafkaListener);
	}

	@Override
	protected void initClazz() {
		super.setClazz(Dal2ndIdxKafkaModel.class);
	}

	@Override
	protected void initTopic() {
		super.setTopic("dalMycatSendaryTopic");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.setGroupId("defaultLppzMyCat2ndIndexGroup");
		super.setConsum_serilize_class(KafkaConstant.BYTEENCODER);
		super.init();
	}   
	
}
