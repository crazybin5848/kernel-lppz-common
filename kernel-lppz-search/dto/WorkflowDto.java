package com.lppz.oms.kafka.dto;

import java.io.Serializable;
import java.util.Map;

public class WorkflowDto {
    private String processDefId;
    private String businessKey;
    private Map<String,Serializable> parameters;
	/**
	 * @return the processDefId
	 */
	public String getProcessDefId() {
		return processDefId;
	}
	/**
	 * @param processDefId the processDefId to set
	 */
	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}
	/**
	 * @return the businessKey
	 */
	public String getBusinessKey() {
		return businessKey;
	}
	/**
	 * @param businessKey the businessKey to set
	 */
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	/**
	 * @return the parameters
	 */
	public Map<String, Serializable> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Serializable> parameters) {
		this.parameters = parameters;
	}
    
    
}
