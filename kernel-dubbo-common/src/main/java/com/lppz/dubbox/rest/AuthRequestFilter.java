package com.lppz.dubbox.rest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;

import com.lppz.dubbox.annotation.RestAuth;
import com.lppz.util.DubboPropertiesUtils;
import com.lppz.util.ReFelctionUtil;
import com.lppz.util.jedis.cluster.concurrent.OmsJedisCluster;
  
@Provider  
@Priority(Priorities.AUTHENTICATION)
public class AuthRequestFilter extends BaseRestFilter implements ContainerRequestFilter { 
	public static final String AUTHRESTIPS="authrestips";
	private static Map<String,String> ipConfigMaps=new HashMap<String,String>();
	private static OmsJedisCluster jedisCluster;
	static{
//		jedisCluster=JedisSequenceUtil.getInstance().getJedisCluster();
		if(jedisCluster==null){
			String s=DubboPropertiesUtils.getKey(AUTHRESTIPS);
			if(s==null){
				ipConfigMaps=null;
			}
			else{
				String[] ss=s.split(",");
				for(String x:ss){
					ipConfigMaps.put(x, x);
				}
			}
		}
	}
	@Override  
    public void filter(ContainerRequestContext context) throws IOException {  
		Method m=((PostMatchContainerRequestContext)context).getResourceMethod().getMethod();
		if(!checkNeedAuth(m))
			return;
		HttpServletRequest req = getHttpReq(context);
		String clientIp=req.getRemoteAddr();
		if(!doAuth(clientIp))
			throw new IllegalStateException(clientIp+" has No Access");
    }  
    
	private boolean doAuth(String clientIp) {
		if(jedisCluster!=null){
			return jedisCluster.hget(AUTHRESTIPS, clientIp)!=null;
		}
		if(ipConfigMaps==null){
			throw new IllegalStateException("No auth config,pls config jedisCluster and use "+AUTHRESTIPS+" as key to store all passed ip list or config authrestips=xx.xx.xx,yy.yy.yy in /META-INF/dubbo.properties in ur web container!");
		}
		return ipConfigMaps.containsKey(clientIp);
	}

	private boolean checkNeedAuth(Method m) {
		if(cacheAuth.get(m)!=null){
			return cacheAuth.get(m);
		}
		RestAuth rh=m.getAnnotation(RestAuth.class);
    	if(rh!=null){
    		cacheAuth.put(m,rh.value());
    		return rh.value();
		}
    	Class<?> clazz=ReFelctionUtil.getDynamicObj(m.getClass(), "clazz", m);
    	rh=clazz.getAnnotation(RestAuth.class);
    	if(rh==null){
    		cacheAuth.put(m,false);
    		return false;
    	}
    	cacheAuth.put(m,rh.value());
    	return rh.value();
	}
}  