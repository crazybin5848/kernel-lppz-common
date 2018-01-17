package com.lppz.dubbo.micro;


import com.lppz.dubbo.BaseMicroServiceComponent;
import com.lppz.dubbo.BaseMicroStartup;
import com.lppz.dubbo.MicroServiceDestoryInterface;
import com.lppz.util.kafka.consumer.KafkaBaseRunner;
import com.lppz.util.rocketmq.listener.BaseLppzMessageListener;

public class MicroServiceDestory extends BaseMicroServiceComponent implements MicroServiceDestoryInterface {

	public static void main(String[] args) {
		MicroServiceDestory destory = new MicroServiceDestory();
		destory.shutdown();
	}
	
	@Override
	public boolean close(boolean boo) {
		if(!boo)
			return false;
		BaseMicroStartup.flag=false;			
		KafkaBaseRunner.needRun=true;
		BaseLppzMessageListener.continueConsume = false;
		return true;
	}
	
	@Override
	protected String generateCloseRestUrl() {
		StringBuilder sb=new StringBuilder();
		
		sb.append(LOCALHOST).append(PORT).append(SEPARATOR).append(WEB_CONTEXT_PATH).append(DESTORY_PATH);
		
		return sb.toString();
	}
}