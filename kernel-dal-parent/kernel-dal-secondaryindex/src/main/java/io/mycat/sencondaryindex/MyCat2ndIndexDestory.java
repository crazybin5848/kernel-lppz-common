package io.mycat.sencondaryindex;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lppz.dubbo.MicroServiceDestoryInterface;
import com.lppz.util.http.BaseHttpClientsComponent;
import com.lppz.util.http.FutureWrapper;
import com.lppz.util.http.enums.HttpMethodEnum;
import com.lppz.util.kafka.consumer.KafkaBaseRunner;
@Service(protocol="rest",timeout=100000)
public class MyCat2ndIndexDestory extends BaseHttpClientsComponent implements MicroServiceDestoryInterface{
	static Logger logger = LoggerFactory.getLogger(MyCat2ndIndexDestory.class);
	public void closeRest() {
		try {
			HttpResponse hrPost = doHttpStop(true);
			if(hrPost==null)
				return;
			logger.info(new StringBuilder(hrPost.getStatusLine().toString())
					.toString()+" server begin shutdown...");
			while(true){
				try {
					hrPost = doHttpStop(false);
					if(hrPost==null){
						logger.info("server has been shutdown");
						break;
					}
				} catch (Exception e) {
					break;
				}
				logger.info(new StringBuilder(hrPost.getStatusLine().toString())
						.toString()+" server is shutdowning...");
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private HttpResponse doHttpStop(boolean boo) throws InterruptedException,
			ExecutionException {
		HttpRequestBase httpPost = super.createReqBase(LOCALHOST + PORT
				+ SEPARATOR + WEB_CONTEXT_PATH + DESTORY_PATH,
				HttpMethodEnum.POST);
		StringEntity s =new StringEntity(JSON.toJSONString(boo), "UTF-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		((HttpPost)httpPost).setEntity(s);
		FutureWrapper fw;
		try {
			fw = super.doHttpSyncExec(httpPost);
		} catch (IOException e) {
			return null;
		}
		HttpResponse hrPost = fw==null?null:fw.getResp();
		return hrPost;
	}

	public static void main(String[] args) {
		MyCat2ndIndexDestory destory = new MyCat2ndIndexDestory();
		destory.closeRest();
	}

	@Override
	public boolean close(boolean boo) {
		if(!boo)
			return false;
		MyCat2ndIndexStartup.flag=false;
		KafkaBaseRunner.needRun=true;
		return true;
	}
}