package com.lppz.oms.kafka.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志类型 enum
 * @author Administrator
 *
 */
public enum LogType {
	
	LOG_SYSTEM(1,"log-system","系统日志"),
	LOG_BIZ(2,"log-biz","业务日志"),
	LOG_INTERFACE(3,"log-interface","接口日志");
	
	private static Map<Integer, LogType> map = new HashMap<Integer, LogType>();
	static{
		for(LogType log : LogType.values()){
			map.put(log.getId(), log);
		}
	}
	
	/**
	 * 根据id获取app
	 */	
	public static LogType getById(Integer id){
		return map.get(id);
	}
	
	public static List<LogType> getAll(){
		List<LogType> result = new ArrayList<LogType>();
		for(LogType log : LogType.values()){
			result.add(log);
		}
		return result;
	}
	
	private Integer id;
	private String name;
	private String desc;
	
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}

	private LogType(Integer id, String name, String desc){
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
}
