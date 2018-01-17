package com.lppz.dubbox.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;

import com.alibaba.dubbo.rpc.protocol.rest.DubboHttpServer;
import com.alibaba.dubbo.rpc.support.DubboInvokeDetail;
import com.alibaba.dubbo.rpc.support.KafkaDubboUtil;
import com.lppz.util.ReFelctionUtil;

/**
 *
 */
@Provider
@Priority(Integer.MIN_VALUE)
public class LppzLoggingFilter extends BaseRestFilter implements
		ContainerRequestFilter, ClientRequestFilter, ContainerResponseFilter,
		ClientResponseFilter, WriterInterceptor, ReaderInterceptor {

	public void filter(ClientRequestContext context) throws IOException {
		// logHttpHeaders(context.getStringHeaders());
	}

	public void filter(ClientRequestContext requestContext,
			ClientResponseContext responseContext) throws IOException {
		// logHttpHeaders(responseContext.getHeaders());
	}

	public void filter(ContainerRequestContext context) throws IOException {
		Method m = ((PostMatchContainerRequestContext) context)
				.getResourceMethod().getMethod();
		if (!KafkaDubboUtil.checkNeedHttpLogging(m))
			return;
		// String header=logHttpHeaders(context.getHeaders());
		HttpServletRequest req = getHttpReq(context);
		checkkafkaProducer(context, req);
		DubboHttpServer.getTh().set(new DubboInvokeDetail());
		DubboInvokeDetail lppzRestHttpLogDto = DubboHttpServer.getTh().get();
		lppzRestHttpLogDto.setProtocol("rest");
		lppzRestHttpLogDto.setMethod(m.getName());
		Class<?> clazz=ReFelctionUtil.getDynamicObj(m.getClass(), "clazz", m);
		lppzRestHttpLogDto.setService(clazz.getName());
		lppzRestHttpLogDto.setProvider(req.getLocalAddr());
		lppzRestHttpLogDto.setType("provider");
		lppzRestHttpLogDto.setConsumer(req.getRemoteAddr() + ":"
				+ req.getRemotePort());
		lppzRestHttpLogDto.setHostUri("http://"+req.getLocalAddr() + ":"
				+ req.getLocalPort() + req.getRequestURI());
		// lppzRestHttpLogDto.setRequesthttpHeader(header);
		lppzRestHttpLogDto
				.setHttpMethodType(m.isAnnotationPresent(GET.class) ? "GET"
						: "POST");
	}

	private void checkkafkaProducer(ContainerRequestContext context,
			HttpServletRequest req) {
		if (KafkaDubboUtil.getLogSender() == null) {
			String uri = req.getLocalAddr() + ":" + req.getLocalPort()
					+ req.getRequestURI();
			throw new IllegalStateException(
					"since "
							+ uri
							+ " need wslog, so pls config your kafka producer:auto scan com.lppz.configuration.dubbo.log.DubboKafkaProducerConfiguration in ur container!");
		}
	}

	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		// LppzRestHttpLogDto lppzRestHttpLogDto=th.get();
		// if(lppzRestHttpLogDto!=null)
		// lppzRestHttpLogDto.setResponsehttpHeader(logHttpHeaders(responseContext.getStringHeaders()));
	}

	public Object aroundReadFrom(ReaderInterceptorContext context)
			throws IOException, WebApplicationException {
		byte[] buffer = IOUtils.toByteArray(context.getInputStream());
		context.setInputStream(new ByteArrayInputStream(buffer));
		DubboInvokeDetail lppzRestHttpLogDto = DubboHttpServer.getTh().get();
		if (lppzRestHttpLogDto != null)
			lppzRestHttpLogDto.setRequestBody(new String(buffer, "UTF-8"));
		return context.proceed();
	}

	public void aroundWriteTo(WriterInterceptorContext context)
			throws IOException, WebApplicationException {
		DubboInvokeDetail lppzRestHttpLogDto = DubboHttpServer.getTh().get();
		OutputStreamWrapper wrapper = new OutputStreamWrapper(
				context.getOutputStream());
		context.setOutputStream(wrapper);
		context.proceed();
		if (lppzRestHttpLogDto != null) {
			lppzRestHttpLogDto.setResponseBody(new String(wrapper.getBytes(),
					"UTF-8"));
		}
	}

	protected String logHttpHeaders(MultivaluedMap<String, String> headers) {
		StringBuilder msg = new StringBuilder("The HTTP headers are: \n");
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			msg.append(entry.getKey()).append(": ");
			for (int i = 0; i < entry.getValue().size(); i++) {
				msg.append(entry.getValue().get(i));
				if (i < entry.getValue().size() - 1) {
					msg.append(", ");
				}
			}
			msg.append("\n");
		}
		return msg.toString();
	}

	protected static class OutputStreamWrapper extends OutputStream {

		private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		private final OutputStream output;

		private OutputStreamWrapper(OutputStream output) {
			this.output = output;
		}

		@Override
		public void write(int i) throws IOException {
			buffer.write(i);
			output.write(i);
		}

		@Override
		public void write(byte[] b) throws IOException {
			buffer.write(b);
			output.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			buffer.write(b, off, len);
			output.write(b, off, len);
		}

		@Override
		public void flush() throws IOException {
			output.flush();
		}

		@Override
		public void close() throws IOException {
			output.close();
		}

		public byte[] getBytes() {
			return buffer.toByteArray();
		}
	}
}
