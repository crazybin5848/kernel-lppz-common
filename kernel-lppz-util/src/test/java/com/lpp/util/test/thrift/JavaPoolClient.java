package com.lpp.util.test.thrift;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import com.lppz.util.thrift.ServerInfo;
import com.lppz.util.thrift.ThriftTransportPool;

public class JavaPoolClient {
	public static void main(String[] args) {
		// 初始化一个连接池（poolsize=15,minsize=1,maxIdleSecond=5,checkInvervalSecond=10）
		final ThriftTransportPool pool = new ThriftTransportPool(800, 500, 30, 10,
				getServers());
		final AtomicInteger ai=new AtomicInteger(0);
		int size=1000000;
		ExecutorService threadPool = Executors.newFixedThreadPool(800);
		long start=System.currentTimeMillis();
		for(int i=0;i<size;i++){
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					TTransport transport = pool.get();
					TProtocol protocol = new TBinaryProtocol(transport);
					RedisMicroService.Client client=new RedisMicroService.Client(protocol);
//					UserService.Client client = new UserService.Client(protocol);
					try {
						long num=client.getTableSequenceNo("nodefuck");
						System.out.println(num);
					} catch (TException e) {
						e.printStackTrace();
					}finally{
						pool.release(transport);
						ai.addAndGet(1);
					}
				}
			});
		}
		while(true){
			if(ai.get()==size){
				System.out.println(System.currentTimeMillis()-start);
				pool.destory();
				System.exit(0);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	private static List<ServerInfo> getServers() {
		List<ServerInfo> servers = new ArrayList<ServerInfo>();
		servers.add(new ServerInfo("10.6.30.58", 9090));
		return servers;
	}
}