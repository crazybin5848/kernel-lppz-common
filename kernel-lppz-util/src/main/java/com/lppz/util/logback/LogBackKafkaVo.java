package com.lppz.util.logback;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import com.alibaba.fastjson.JSON;
import com.lppz.util.EsJsonSourceModel;

public class LogBackKafkaVo<T> extends EsJsonSourceModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6025313276329498092L;

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public String getCallerDataArray() {
		return callerDataArray;
	}

	public void setCallerDataArray(String callerDataArray) {
		this.callerDataArray = callerDataArray;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getThrowableProxy() {
		return throwableProxy;
	}

	public void setThrowableProxy(String throwableProxy) {
		this.throwableProxy = throwableProxy;
	}

	public String getArgumentArray() {
		return argumentArray;
	}

	public void setArgumentArray(String argumentArray) {
		this.argumentArray = argumentArray;
	}

	public String getFormattedMessage() {
		return formattedMessage;
	}

	public void setFormattedMessage(String formattedMessage) {
		this.formattedMessage = formattedMessage;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLoggerContextVO() {
		return loggerContextVO;
	}

	public void setLoggerContextVO(String loggerContextVO) {
		this.loggerContextVO = loggerContextVO;
	}

	public String getmDCPropertyMap() {
		return mDCPropertyMap;
	}

	public void setmDCPropertyMap(String mDCPropertyMap) {
		this.mDCPropertyMap = mDCPropertyMap;
	}

	private String threadName;
	private String loggerName;
	private String callerDataArray;
	private String timeStamp;
	private String marker;
	private String throwableProxy;
	private String argumentArray;
	private String formattedMessage;
	private String level;
	private String loggerContextVO;
	private String mDCPropertyMap;
	private LogBackBizDto<T> bizDto;

	public String buildBizDtoString() {
		return bizDto == null || bizDto.getT() == null ? null : JSON
				.toJSONString(bizDto.getT());
	}

	@SuppressWarnings("unchecked")
	public LogBackKafkaVo<T> build(ILoggingEvent o) {
		if (o.getArgumentArray() != null) {
			StringBuilder sb = new StringBuilder();
			for (Object oo : o.getArgumentArray()) {
				sb.append(oo.toString()).append("/r/n");
			}
			this.argumentArray = sb.toString();
		}

		if (o.getCallerData() != null) {
			StringBuilder sb = new StringBuilder();
			for (Object oo : o.getCallerData()) {
				sb.append(oo.toString()).append("/r/n");
			}
			this.callerDataArray = sb.toString();
		}

		this.formattedMessage = o.getFormattedMessage();
		try {
			this.bizDto = JSON.parseObject(formattedMessage,
					LogBackBizDto.class);
		} catch (Exception e) {
		}
		if (o.getLevel() != null)
			this.level = o.getLevel().toString();
		if (o.getLoggerContextVO() != null)
			this.loggerContextVO = o.getLoggerContextVO().toString();
		this.loggerName = o.getLoggerName();
		if (o.getMarker() != null)
			this.marker = o.getMarker().toString();
		this.threadName = o.getThreadName();
		if (o.getThrowableProxy() != null) {
			IThrowableProxy it = o.getThrowableProxy();
			StringBuilder sb = new StringBuilder();
			for (StackTraceElementProxy sp : it
					.getStackTraceElementProxyArray()) {
				sb.append(sp.getSTEAsString()).append("/r/n");
			}
			this.throwableProxy = sb.toString();
		}
		this.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o
				.getTimeStamp());
		if (o.getMDCPropertyMap() != null && !o.getMDCPropertyMap().isEmpty())
			this.mDCPropertyMap = JSON.toJSONString(o.getMDCPropertyMap());
		return this;
	}

	public LogBackBizDto<T> getBizDto() {
		return bizDto;
	}

	public void setBizDto(LogBackBizDto<T> bizDto) {
		this.bizDto = bizDto;
	}
}
