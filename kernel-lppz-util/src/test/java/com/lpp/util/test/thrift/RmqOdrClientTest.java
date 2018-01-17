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

public class RmqOdrClientTest {
	public static void main(String[] args) throws TException {
		// 初始化一个连接池（poolsize=15,minsize=1,maxIdleSecond=5,checkInvervalSecond=10）
		final ThriftTransportPool pool = new ThriftTransportPool(1000, 400, 30, 10,
				getServers());
		final AtomicInteger ai=new AtomicInteger(0);
		int size=1;
		for(int i=0;i<size;i++){
			TTransport transport = pool.get();
			TProtocol protocol = new TBinaryProtocol(transport);
			RmqThriftProdService.Client client=new RmqThriftProdService.Client(protocol);
				RmqMessage msg=new RmqMessage();
				msg.setBody(("fuck阿萨德"+ai.addAndGet(1)).getBytes());
				msg.setFlag(0);
				msg.setTopic("mqfuckodr1");
				RmqSendResult rrs=client.sendOrderly(msg, 7777);
				System.out.println(rrs.toString());
		}
		pool.destory();
		System.exit(0);
	}
	
	private static List<ServerInfo> getServers() {
		List<ServerInfo> servers = new ArrayList<ServerInfo>();
		servers.add(new ServerInfo("10.6.30.109", 7912));
		return servers;
	}
}