/**
 * 
 */
package com.lppz.dubbo.log;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.dubbo.rpc.support.DubboInvokeDetail;
import com.lppz.elasticsearch.result.Pager;
import com.lppz.util.logback.LogBackKafkaVo;


/**
 * @author Bin
 *
 */
@Path("/dubbolog")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public interface DubboLogInterface {
	

	@Path("/list")
	@POST
	public Pager<DubboInvokeDetail> getRestHttpLogList(DubboInvokeDetail dto,Integer page,Integer rows);
	
	
	@Path("/logBackList")
	@POST
	public Pager<LogBackKafkaVo> getLogBackList(LogBackKafkaVo dto,Integer page,Integer rows);
	
	@Path("/close")
	@POST
	public void close();
}
