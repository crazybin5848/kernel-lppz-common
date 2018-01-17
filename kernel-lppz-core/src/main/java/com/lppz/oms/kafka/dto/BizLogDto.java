/*
Copyright (c) 2013 Yi Hao Dian, Inc. as an unpublished work.
Neither this material nor any portion hereof may be copied or distributed without the
express written consent of Home Box Office, Inc.

This material also contains proprietary and confidential information of Home Box Office, Inc.
and its suppliers, and may not be used by or disclosed to any person, in whole or in part,
without the prior written consent of Yi Hao Dian, Inc.
*/
package com.lppz.oms.kafka.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 日志对象DTO
 *
 * @author xuwencai@lppz.com
 * @version 1.0
 */
public class BizLogDto extends MyPageDto implements Serializable {

	private static final long serialVersionUID = 7060258538492398208L;
	private String uuid;//主键
	private String logType;//日志类型
	private int appId; //应用id
	private String appName; //应用名称
	private String className; //类名
	private String methodName; //方法名
	private long costTime; //方法调用耗时，单位ms
	private String remoteIp; //远程ip
	private String localIp; //本地ip
	private int exceptionFlag; //异常标识,0:没有异常;1:有异常
	private String paramInfo;//参数信息,超过4000字符拆成多条记录
	private String refCode; //业务主键,可以是soId,userId
	private String resultInfo; //调用结果,超过4000字符被截断
	private String exceptionInfo; //异常信息,超过4000字符被截断
	private String logHeader; //日志编号.参数信息是很重要的,如果超过4000字符,会分成多条记录,这个字段用来标识这多条记录
	private String createTime; //日志创建时间
	private String consumerTime;//日志保存时间
	
	private String keyword;
	
	private OrderLogDto orderLogDto;
	private InterfaceLogDto interfaceDto;
	private WorkflowDto workFlowDto; //workflow creation info 
	private LackOrderLog lackOrder;//报缺日志
	private OrderReturnResult orderReturnResult;//oms2wms返回结果日志
	private StockSynDto stockSynDto;//库存同步
	private OptRecord optRecord;
	private ProductSynDto pdtSynDto;
	private LppzExBo lppzExBo;
	
	
	public void buildTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(createTime!=null){
				this.createTime=format.format(new Date(Long.parseLong(createTime)));
			}
			if(consumerTime!=null){
				this.consumerTime=format.format(new Date(Long.parseLong(consumerTime)));
			}
		} catch (NumberFormatException e) {
		}
	}
	
	public BizLogDto(){}
	
	public BizLogDto(String logType,String appName,String className,String methodName,String content){
		this.logType=logType;
		this.appName=appName;
		this.className=className;
		this.methodName=methodName;
		this.resultInfo=content;
	}
	
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return Returns the appId.
	 */
	public int getAppId() {
		return appId;
	}
	/**
	 * @param appId The appId to set.
	 */
	public void setAppId(int appId) {
		this.appId = appId;
	}
	/**
	 * @return Returns the appName.
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName The appName to set.
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return Returns the methodName.
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName The methodName to set.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return Returns the costTime.
	 */
	public long getCostTime() {
		return costTime;
	}
	/**
	 * @param costTime The costTime to set.
	 */
	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}
	/**
	 * @return Returns the remoteIp.
	 */
	public String getRemoteIp() {
		return remoteIp;
	}
	/**
	 * @param remoteIp The remoteIp to set.
	 */
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	/**
	 * @return Returns the localIp.
	 */
	public String getLocalIp() {
		return localIp;
	}
	/**
	 * @param localIp The localIp to set.
	 */
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	/**
	 * @return Returns the exceptionFlag.
	 */
	public int getExceptionFlag() {
		return exceptionFlag;
	}
	/**
	 * @param exceptionFlag The exceptionFlag to set.
	 */
	public void setExceptionFlag(int exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}
	/**
	 * @return Returns the paramInfo.
	 */
	public String getParamInfo() {
		return paramInfo;
	}
	/**
	 * @param paramInfo The paramInfo to set.
	 */
	public void setParamInfo(String paramInfo) {
		this.paramInfo = paramInfo;
	}
	/**
	 * @return Returns the refCode.
	 */
	public String getRefCode() {
		return refCode;
	}
	/**
	 * @param refCode The refCode to set.
	 */
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	/**
	 * @return Returns the resultInfo.
	 */
	public String getResultInfo() {
		return resultInfo;
	}
	/**
	 * @param resultInfo The resultInfo to set.
	 */
	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	/**
	 * @return Returns the exceptionInfo.
	 */
	public String getExceptionInfo() {
		return exceptionInfo;
	}
	/**
	 * @param exceptionInfo The exceptionInfo to set.
	 */
	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}
	/**
	 * @return Returns the logHeader.
	 */
	public String getLogHeader() {
		return logHeader;
	}
	/**
	 * @param logHeader The logHeader to set.
	 */
	public void setLogHeader(String logHeader) {
		this.logHeader = logHeader;
	}
	/**
	 * @return Returns the createTime.
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime The createTime to set.
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the consumerTime
	 */
	public String getConsumerTime() {
		return consumerTime;
	}
	/**
	 * @param consumerTime the consumerTime to set
	 */
	public void setConsumerTime(String consumerTime) {
		this.consumerTime = consumerTime;
	}
	/**
	 * @return the logType
	 */
	public String getLogType() {
		return logType;
	}
	/**
	 * @param logType the logType to set
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	/**
	 * @return the orderLogDto
	 */
	public OrderLogDto getOrderLogDto() {
		return orderLogDto;
	}
	/**
	 * @param orderLogDto the orderLogDto to set
	 */
	public void setOrderLogDto(OrderLogDto orderLogDto) {
		this.orderLogDto = orderLogDto;
	}
	/**
	 * @return the interfaceDto
	 */
	public InterfaceLogDto getInterfaceDto() {
		return interfaceDto;
	}
	/**
	 * @param interfaceDto the interfaceDto to set
	 */
	public void setInterfaceDto(InterfaceLogDto interfaceDto) {
		this.interfaceDto = interfaceDto;
	}
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the workFlowDto
	 */
	public WorkflowDto getWorkFlowDto() {
		return workFlowDto;
	}

	/**
	 * @param workFlowDto the workFlowDto to set
	 */
	public void setWorkFlowDto(WorkflowDto workFlowDto) {
		this.workFlowDto = workFlowDto;
	}

	/**
	 * @return the lackOrder
	 */
	public LackOrderLog getLackOrder() {
		return lackOrder;
	}

	/**
	 * @param lackOrder the lackOrder to set
	 */
	public void setLackOrder(LackOrderLog lackOrder) {
		this.lackOrder = lackOrder;
	}

	/**
	 * @return the orderReturnResult
	 */
	public OrderReturnResult getOrderReturnResult() {
		return orderReturnResult;
	}

	/**
	 * @param orderReturnResult the orderReturnResult to set
	 */
	public void setOrderReturnResult(OrderReturnResult orderReturnResult) {
		this.orderReturnResult = orderReturnResult;
	}

	/**
	 * @return the stockSynDto
	 */
	public StockSynDto getStockSynDto() {
		return stockSynDto;
	}

	/**
	 * @param stockSynDto the stockSynDto to set
	 */
	public void setStockSynDto(StockSynDto stockSynDto) {
		this.stockSynDto = stockSynDto;
	}

	/**
	 * @return the optRecord
	 */
	public OptRecord getOptRecord() {
		return optRecord;
	}

	/**
	 * @param optRecord the optRecord to set
	 */
	public void setOptRecord(OptRecord optRecord) {
		this.optRecord = optRecord;
	}

	/**
	 * @return the pdtSynDto
	 */
	public ProductSynDto getPdtSynDto() {
		return pdtSynDto;
	}

	/**
	 * @param pdtSynDto the pdtSynDto to set
	 */
	public void setPdtSynDto(ProductSynDto pdtSynDto) {
		this.pdtSynDto = pdtSynDto;
	}

	public LppzExBo getLppzExBo() {
		return lppzExBo;
	}

	public void setLppzExBo(LppzExBo lppzExBo) {
		this.lppzExBo = lppzExBo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BizLogDto [uuid=");
		builder.append(uuid);
		builder.append(", logType=");
		builder.append(logType);
		builder.append(", appId=");
		builder.append(appId);
		builder.append(", appName=");
		builder.append(appName);
		builder.append(", className=");
		builder.append(className);
		builder.append(", methodName=");
		builder.append(methodName);
		builder.append(", costTime=");
		builder.append(costTime);
		builder.append(", remoteIp=");
		builder.append(remoteIp);
		builder.append(", localIp=");
		builder.append(localIp);
		builder.append(", exceptionFlag=");
		builder.append(exceptionFlag);
		builder.append(", paramInfo=");
		builder.append(paramInfo);
		builder.append(", refCode=");
		builder.append(refCode);
		builder.append(", resultInfo=");
		builder.append(resultInfo);
		builder.append(", exceptionInfo=");
		builder.append(exceptionInfo);
		builder.append(", logHeader=");
		builder.append(logHeader);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", consumerTime=");
		builder.append(consumerTime);
		builder.append(", keyword=");
		builder.append(keyword);
		builder.append(", orderLogDto=");
		builder.append(orderLogDto);
		builder.append(", interfaceDto=");
		builder.append(interfaceDto);
		builder.append(", workFlowDto=");
		builder.append(workFlowDto);
		builder.append(", lackOrder=");
		builder.append(lackOrder);
		builder.append(", orderReturnResult=");
		builder.append(orderReturnResult);
		builder.append(", stockSynDto=");
		builder.append(stockSynDto);
		builder.append(", optRecord=");
		builder.append(optRecord);
		builder.append(", pdtSynDto=");
		builder.append(pdtSynDto);
		builder.append(", lppzExBo=");
		builder.append(lppzExBo);
		builder.append("]");
		return builder.toString();
	}
}
