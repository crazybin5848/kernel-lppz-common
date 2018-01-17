package com.lppz.canal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lppz.canal.bean.ChangeDataBean;
import com.lppz.canal.enums.EtlEnums;
import com.lppz.canal.service.QueueSenderService;
import com.lppz.canal.service.SenderFactory;

@Service
public class QueueSenderServiceImpl implements QueueSenderService{
	
	@Value("${rocketmq.nameserver}")
	private String nameSrv;
	
	@Override
	public void sendNext(EtlEnums etlEnums, List<ChangeDataBean> changeDatas) {
		SenderFactory.nameSrv = nameSrv;
		for (ChangeDataBean data : changeDatas) {
			String key = data.getKey();
			int mainId = buildMainId(key);
			SenderFactory.sendMsg(etlEnums, data, key, mainId);
		}
	}
	
	private int buildMainId(String key){
		return Math.abs(key.hashCode());
	}

}
