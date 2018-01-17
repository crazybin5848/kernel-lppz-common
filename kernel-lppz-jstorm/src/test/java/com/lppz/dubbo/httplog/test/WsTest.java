//package com.lppz.dubbo.httplog.test;
//
//
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//
///**
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:META-INF/kernel-dubbo-httplog-test.xml"})
//public class WsTest {
//	
////	@Reference(protocol="webservice")
////	private TestHttpInterface ti;
//	@Test
//	public void test() throws InterruptedException{
//		TestHttpInterface ti=(TestHttpInterface) BaseDubboWsUtil.getInstance().get(TestHttpInterface.class);
//		doBatchGet(ti,"123123");
//		while(true){
//			Thread.sleep(1000);
//		}
//	}
//	
//	private String doBatchGet(final TestHttpInterface ti,String id) {
//		new Thread(new Runnable(){
//			int size=10;
//			@Override
//			public void run() {
//				for(int j=0;j<10;j++){
//					final AtomicInteger ai=new AtomicInteger(0);
//				for(int i=0;i<size;i++){
//					new Thread(new Runnable(){
//						@Override
//						public void run() {
//							try{ti.doGet(UUID.randomUUID().toString());}
//							finally{
//								ai.getAndAdd(1);
//							}
//						}
//					}).start();
//				}
//				while(true){
//					if(ai.get()==size){
//						break;
//					}
//				}
//				}
//			}
//		}).run();
//		return "succ";
//	}
//}
