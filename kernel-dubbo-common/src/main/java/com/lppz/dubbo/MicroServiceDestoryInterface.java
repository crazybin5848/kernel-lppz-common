package com.lppz.dubbo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lppz.util.LppzPropertiesUtils;

@Path("/microservice")
@Produces({ContentType.APPLICATION_JSON_UTF_8})
@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
public interface MicroServiceDestoryInterface {
	public static final String LOCALHOST = "http://127.0.0.1";

	public static final String WEB_CONTEXT_PATH = "services";

	public static final String SEPARATOR = "/";

	public static final String PORT = ":"
			+ LppzPropertiesUtils.getKey("dubbo.rest.port");

	public static final String DESTORY_PATH = "/microservice/close";
	@Path("/close")
	@POST
	public boolean close(boolean boo);
}
