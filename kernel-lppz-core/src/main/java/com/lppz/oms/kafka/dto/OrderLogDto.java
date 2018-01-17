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
public class OrderLogDto extends MyPageDto implements Serializable {

	private static final long serialVersionUID = 7060258538492398208L;
	private Integer pkid;
	public Integer getPkid() {
		return pkid;
	}

	public void setPkid(Integer pkid) {
		this.pkid = pkid;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public void buildTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(operateTime!=null){
				this.operateTime=format.format(new Date(Long.parseLong(operateTime)));
			}
			if(endTime!=null){
				this.endTime=format.format(new Date(Long.parseLong(endTime)));
			}
		} catch (NumberFormatException e) {
		}
	}

	private String uuid; //主键
	private String appName; //应用名称
	private String className; //类名
	private String methodName; //方法名
	private String productId;//产品id
	private String productName;//产品名称
	private String changeMoney;//
	private String operate;//操作
	private String remark;//描述
	private String operateUser;//操作人
	private String operateTime;//操作时间
	
	private String status;//状态
	private String orderId;//订单号
	private String porderId;//父订单号
	private String outOrderId;//外部订单号
	private String systemSourcing;//是否系统寻源
	private String sourcingLocation;//寻源仓库
	private String sourcingResult;//寻源结果
	private String endTime;//结束时间
	
	private String returnCode;
	private String result;
	private String userId;
	public OrderLogDto(){}

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
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}


	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}


	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}


	/**
	 * @return the changeMoney
	 */
	public String getChangeMoney() {
		return changeMoney;
	}

	/**
	 * @param changeMoney the changeMoney to set
	 */
	public void setChangeMoney(String changeMoney) {
		this.changeMoney = changeMoney;
	}


	/**
	 * @return the operate
	 */
	public String getOperate() {
		return operate;
	}

	/**
	 * @param operate the operate to set
	 */
	public void setOperate(String operate) {
		this.operate = operate;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}


	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}


	/**
	 * @return the operateUser
	 */
	public String getOperateUser() {
		return operateUser;
	}


	/**
	 * @param operateUser the operateUser to set
	 */
	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}


	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	/**
	 * @return the outOrderId
	 */
	public String getOutOrderId() {
		return outOrderId;
	}


	/**
	 * @param outOrderId the outOrderId to set
	 */
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}


	/**
	 * @return the systemSourcing
	 */
	public String getSystemSourcing() {
		return systemSourcing;
	}


	/**
	 * @param systemSourcing the systemSourcing to set
	 */
	public void setSystemSourcing(String systemSourcing) {
		this.systemSourcing = systemSourcing;
	}

	/**
	 * @return the sourcingLocation
	 */
	public String getSourcingLocation() {
		return sourcingLocation;
	}

	/**
	 * @param sourcingLocation the sourcingLocation to set
	 */
	public void setSourcingLocation(String sourcingLocation) {
		this.sourcingLocation = sourcingLocation;
	}

	/**
	 * @return the sourcingResult
	 */
	public String getSourcingResult() {
		return sourcingResult;
	}

	/**
	 * @param sourcingResult the sourcingResult to set
	 */
	public void setSourcingResult(String sourcingResult) {
		this.sourcingResult = sourcingResult;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getPorderId() {
		return porderId;
	}


	public void setPorderId(String porderId) {
		this.porderId = porderId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderLogDto [pkid=");
		builder.append(pkid);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", appName=");
		builder.append(appName);
		builder.append(", className=");
		builder.append(className);
		builder.append(", methodName=");
		builder.append(methodName);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", changeMoney=");
		builder.append(changeMoney);
		builder.append(", operate=");
		builder.append(operate);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", operateUser=");
		builder.append(operateUser);
		builder.append(", operateTime=");
		builder.append(operateTime);
		builder.append(", status=");
		builder.append(status);
		builder.append(", orderId=");
		builder.append(orderId);
		builder.append(", porderId=");
		builder.append(porderId);
		builder.append(", outOrderId=");
		builder.append(outOrderId);
		builder.append(", systemSourcing=");
		builder.append(systemSourcing);
		builder.append(", sourcingLocation=");
		builder.append(sourcingLocation);
		builder.append(", sourcingResult=");
		builder.append(sourcingResult);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", returnCode=");
		builder.append(returnCode);
		builder.append(", result=");
		builder.append(result);
		builder.append(", userId=");
		builder.append(userId);
		builder.append("]");
		return builder.toString();
	}
}
