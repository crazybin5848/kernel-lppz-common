package com.lppz.oms.persist;

import java.util.List;
import java.util.Map;

import javax.ws.rs.BeanParam;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public interface EmployeeMapper {
	List<Map> findEmployees(Map params);
	//one more PageBounds ,and share the same sql with findEmployees(Map params)/
	List<Map> findEmployees(Map params,PageBounds page);
	
	Map loadEmployee(Map employee);
	
	void updateStatus(Map employee);
	
	void addEmployee(Map employee);
	
	void removeEmployee(String userid);
	
    void changePwd(Map employee);
}
