package com.lpp.util.test.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.lppz.util.http.BaseHttpClientsComponent;
import com.lppz.util.http.FutureWrapper;
import com.lppz.util.http.enums.HttpMethodEnum;

/**
 */
public class RestClient extends BaseHttpClientsComponent{
//	static Client client = ClientBuilder.newClient();
	@Test
    public void restTest() throws Exception {
//        final String port = "8808";
//        super.init();
//        ExecutorService httpExecutor = Executors.newCachedThreadPool();
//        	long tStart=System.currentTimeMillis();
//        final int size=1;
//        for(int k=0;k<1;k++){
//         final AtomicInteger atmCount=new AtomicInteger(0);
//        	for(int j=0;j<size;){
//        		j++;
//        		httpExecutor.execute(new Runnable(){
//					@Override
//					public void run() {
//						try {
//							RestClient.super.initHttpClient();
//							for(int i=0;i<1;i++){
//								 RestClient.super.initHttpClient();
//							        HttpRequestBase httpPost=RestClient.super.createReqBase("http://10.6.30.133:" + port + "/services/test/doPost", HttpMethodEnum.POST);
//							    	Requestrr req = new Requestrr(Long.toString(UUID.randomUUID().getLeastSignificantBits(),36), Long.toString(UUID.randomUUID().getMostSignificantBits(),36));
//							    	StringEntity s =new StringEntity(JSON.toJSONString(req), "UTF-8");
//									s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//									((HttpPost)httpPost).setEntity(s);
//									FutureWrapper fwPost=RestClient.super.doHttpExec(httpPost, null, 0);
//									HttpRequestBase httpGet=RestClient.super.createReqBase("http://10.6.30.133:" + port + "/services/test/doGet/"+UUID.randomUUID().getMostSignificantBits(),  HttpMethodEnum.GET);
//									FutureWrapper fwGet=RestClient.super.doHttpExec(httpGet, null, 0);
//									HttpResponse hrPost=fwPost.getFh().get();
//									HttpResponse hrGet=fwGet.getFh().get();
//									System.out.println(new StringBuilder(hrPost.getStatusLine().toString()).append(":=>").append(hrPost.getEntity()).toString());
//									System.out.println(new StringBuilder(hrGet.getStatusLine().toString()).append(":=>").append(hrPost.getEntity()).toString());
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//							System.exit(-1);
//						}
//						finally{
//						    atmCount.getAndAdd(1);
//						    RestClient.super.closeHttpClient();
//					    }
//					}
//        		});
//        	}
//        	while(true){
//    			if(atmCount.get()==size){
//    				long tEnd = System.currentTimeMillis();
//    				System.out.println(atmCount.get());
//    				System.out.println(tEnd - tStart + "millions");
//    				break;
//    			}
//    			Thread.sleep(100);
//    		}
//        	}
    }

	@Test
	public void testSimpleRestPost(){
		super.initHttpClient();
      HttpRequestBase httpPost=RestClient.super.createReqBase("http://10.6.30.97:8808/fuck", HttpMethodEnum.POST);
      
      StringEntity s =new StringEntity(JSON.toJSONString(new User("1","fuck")), "UTF-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		((HttpPost)httpPost).setEntity(s);
		FutureWrapper fwPost=RestClient.super.doHttpExec(httpPost, null, 0);
//		HttpRequestBase httpGet=RestClient.super.createReqBase("http://10.6.30.133:" + port + "/services/test/doGet/"+UUID.randomUUID().getMostSignificantBits(),  HttpMethodEnum.GET);
//		FutureWrapper fwGet=RestClient.super.doHttpExec(httpGet, null, 0);
		try {
			HttpResponse hrPost=fwPost.getFh().get();
			System.out.println(hrPost.getStatusLine());
			User user=JSON.parseObject(convertStreamToString(hrPost.getEntity().getContent()), User.class);
			System.out.println(user.getId()+":"+user.getName());
		} catch (InterruptedException | ExecutionException | IllegalStateException | IOException e) {
			e.printStackTrace();
		}finally{
			super.closeHttpClient();
		}
//		HttpResponse hrGet=fwGet.getFh().get();
		
	}
	
//    private void post(String url, MediaType mediaType) {
//    	Requestrr user = new Requestrr(Long.toString(UUID.randomUUID().getLeastSignificantBits(),36), Long.toString(UUID.randomUUID().getMostSignificantBits(),36));
//        WebTarget target = client.target(url);
//        Response response = target.request().post(Entity.entity(user, mediaType));
//        try {
//            if (response.getStatus() != 200) {
//                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
//            }
//            System.out.println("Successfully got result: " + response.readEntity(String.class));
//        } finally {
//            response.close();
////            client.close();
//        }
//    }
//
//    private void get(String url) {
//        WebTarget target = client.target(url);
//        Response response = target.request().get();
//        try {
//            if (response.getStatus() != 200) {
//                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
//            }
//            System.out.println("Successfully got result: " + response.readEntity(String.class));
//        } finally {
//            response.close();
////            client.close();
//        }
//    }
	
	 public String convertStreamToString(InputStream is) {   
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
		     StringBuilder sb = new StringBuilder();   
		        String line = null;   
		        try {   
		            while ((line = reader.readLine()) != null) {   
		                sb.append(line + "\n");   
		            }   
		        } catch (IOException e) {   
		            e.printStackTrace();   
		        } finally {   
		            try {   
		                is.close();   
		            } catch (IOException e) {   
		                e.printStackTrace();   
		            }   
		        }   
		        return sb.toString();   
		    }   
}
