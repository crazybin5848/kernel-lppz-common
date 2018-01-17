package com.lppz.canal.service;

import java.util.List;

import com.lppz.canal.bean.ChangeDataBean;
import com.lppz.canal.enums.EtlEnums;

public interface QueueSenderService {
	
	void sendNext(EtlEnums etlEnums, List<ChangeDataBean> changeDatas);

}
