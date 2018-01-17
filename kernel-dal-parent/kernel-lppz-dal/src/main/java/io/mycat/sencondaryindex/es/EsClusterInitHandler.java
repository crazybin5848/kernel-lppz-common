package io.mycat.sencondaryindex.es;

import java.io.File;
import java.io.FileInputStream;

import org.elasticsearch.client.support.AbstractClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.elasticsearch.EsClientUtil;
import com.lppz.elasticsearch.LppzEsComponent;

public class EsClusterInitHandler {
	private static final Logger LOG = LoggerFactory.getLogger(EsClusterInitHandler.class);
	private static EsClusterInitHandler instance=new EsClusterInitHandler();
	private EsClusterInitHandler(){}
	public static EsClusterInitHandler getInstance(){
		return instance;
	}
	public void initEs(String home) {
		AbstractClient client=null;
		try {
			FileInputStream stream=new FileInputStream(new File(home+"/conf/es-cluster.yaml"));
			client = EsClientUtil.buildPoolClientProxy(stream);
			LppzEsComponent.getInstance().setClient(client);
			LOG.info("es 4 2nd idx has been created!");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
	}
}