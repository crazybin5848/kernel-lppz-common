package com.lppz.configuration.biz.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lppz.oms.exception.BaseException;
import com.lppz.oms.kafka.annotation.LppzExceptionParam;
import com.lppz.oms.kafka.constant.LogKeyword;
import com.lppz.oms.kafka.dto.BizLogDto;
import com.lppz.oms.kafka.dto.LppzExBo;
import com.lppz.util.JmxGetIPPort;
import com.lppz.util.kafka.producer.KafkaProducer;

@Aspect
@Component
public class ExceptionInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

	@Resource(name="lppzExProducer")
	private KafkaProducer<BizLogDto> lppzExProducer;
	
	@Around(value = "@annotation(com.lppz.oms.kafka.annotation.LppzException)")
	public Object around(final ProceedingJoinPoint pjp) {
   
		BaseException baseException = valitionException(pjp, null);
		Object obj = null;
		if (baseException == null)
			return obj;
		try {

			obj = pjp.proceed();

		} catch (Throwable e) {
			sendException(e, baseException);
			return obj;
		}
		if (!baseException.getIsThrow())
			sendException(baseException, baseException);
		return obj;
	}

	protected BaseException valitionException(ProceedingJoinPoint pjp, Throwable e) {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		if (method.getDeclaringClass().isInterface()) {
			try {
				method = pjp.getTarget().getClass().getDeclaredMethod(pjp.getSignature().getName(), method.getParameterTypes());
			} catch (final SecurityException se) {
				logger.error("获取实例方法失败", se);
			} catch (final NoSuchMethodException nme) {
				logger.error("获取实例方法失败", nme);
			}
		}

		Annotation[][] ans = method.getParameterAnnotations();
		Object[] args = pjp.getArgs();
		Map<Object, LppzExceptionParam> eps = new HashMap<Object, LppzExceptionParam>();

		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				if (ans[i][j] instanceof LppzExceptionParam) {
					eps.put(args[i], (LppzExceptionParam) ans[i][j]);
				}
			}
		}

		BaseException baseException = null;

		if (eps.size() == 0) {
			if (e != null)
				logger.error("参数错误:LppzExceptionParam注解必须添加!", e);
			if (e == null)
				logger.error("参数错误:LppzExceptionParam注解必须添加!");
			return baseException;
		}
		if (eps.size() > 1) {
			if (e != null)
				logger.error("参数错误:LppzExceptionParam只支持唯一参数!", e);
			if (e == null)
				logger.error("参数错误:LppzExceptionParam只支持唯一参数!");
			return baseException;
		}

		for (Object obj : eps.keySet()) {
			if (obj instanceof BaseException) {
				baseException = (BaseException) obj;
			}
		}
		if (baseException == null) {
			if (e != null)
				logger.error("参数错误:LppzExceptionParam只支持BaseException!", e);
			if (e == null)
				logger.error("参数错误:LppzExceptionParam只支持BaseException!");
			return baseException;
		}
		String name = baseException.getClass().getSimpleName();
		if (name.equals(BaseException.class.getSimpleName())) {
			logger.error("异常参数不支持" + pjp.getSourceLocation().getWithinType() + "." + ((MethodSignature) pjp.getSignature()).getName() + "() ", e);
			baseException = null;
			return baseException;
		}
		if (baseException.getIsMain()) {
			List<BaseException> baseExceptions = baseException.getBaseExceptions();
			if (baseExceptions == null || baseExceptions.size() == 0) {
				return baseException;
			} else {
				for (BaseException tempBase : baseExceptions) {
					if (tempBase.getClass().getSimpleName().equals(BaseException.class.getSimpleName())) {
						logger.error("异常参数不支持" + pjp.getSourceLocation().getWithinType() + "." + ((MethodSignature) pjp.getSignature()).getName() + "() ", e);
						baseException = null;
						return baseException;
					}
				}
			}
		}

		return baseException;
	}

	protected void sendException(Throwable e, BaseException baseException) {

		if (baseException.getIsMain()) {
			List<BaseException> baseExceptions = baseException.getBaseExceptions();
			for (BaseException tempBase : baseExceptions) {
				sendException(tempBase, tempBase);
			}
			return;
		}

		String ip = JmxGetIPPort.getIPPort();
		ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
		e.printStackTrace(new PrintWriter(buf, true));
		String stackTrace = buf.toString();
		try {
			buf.close();
		} catch (IOException e1) {
			logger.error("写堆栈信息关闭输出流失败", e1);
		}
		sendMsg(baseException, ip, stackTrace);
	    //logger.debug(JSON.toJSONString(lppzExBo));
	}

	private LppzExBo sendMsg(BaseException baseException, String ip,
			String stackTrace) {
		BizLogDto dto = new BizLogDto();
		LppzExBo lppzExBo = new LppzExBo();
		lppzExBo.setName(baseException.getClass().getSimpleName());
		lppzExBo.setDesc(baseException.getDesc());
		lppzExBo.setIp(ip);
		lppzExBo.setStackTrace(stackTrace);
		lppzExBo.setParams(baseException.getParams());
		lppzExBo.setCreatetime(String.valueOf(new Date().getTime()));
		dto.setLppzExBo(lppzExBo);
		dto.setKeyword(LogKeyword.LPPZEXCEPTION);
		try {
			lppzExProducer.sendMsg(dto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return lppzExBo;
	}
}
