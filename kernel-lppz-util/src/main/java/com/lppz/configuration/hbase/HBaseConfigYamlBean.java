package com.lppz.configuration.hbase;

import com.lppz.configuration.es.EsBaseYamlBean;

public class HBaseConfigYamlBean {
	private String zkClientPort = "2181";
	private String zkQuorum;
	private Long hbaseRpcTimeOut = 100000000l;
	private Integer hbaseScanTimeoutPeriod = 100000000;
	private Long hbaseClientScannerCaching = 20l;
	private EsBaseYamlBean esBean;

	public String getZkClientPort() {
		return zkClientPort;
	}

	public void setZkClientPort(String zkClientPort) {
		this.zkClientPort = zkClientPort;
	}

	public String getZkQuorum() {
		return zkQuorum;
	}

	public void setZkQuorum(String zkQuorum) {
		this.zkQuorum = zkQuorum;
	}

	public Long getHbaseRpcTimeOut() {
		return hbaseRpcTimeOut;
	}

	public void setHbaseRpcTimeOut(Long hbaseRpcTimeOut) {
		this.hbaseRpcTimeOut = hbaseRpcTimeOut;
	}

	public Long getHbaseClientScannerCaching() {
		return hbaseClientScannerCaching;
	}

	public void setHbaseClientScannerCaching(Long hbaseClientScannerCaching) {
		this.hbaseClientScannerCaching = hbaseClientScannerCaching;
	}

	public EsBaseYamlBean getEsBean() {
		return esBean;
	}

	public void setEsBean(EsBaseYamlBean esBean) {
		this.esBean = esBean;
	}

	public Integer getHbaseScanTimeoutPeriod() {
		return hbaseScanTimeoutPeriod;
	}

	public void setHbaseScanTimeoutPeriod(Integer hbaseScanTimeoutPeriod) {
		this.hbaseScanTimeoutPeriod = hbaseScanTimeoutPeriod;
	}
}