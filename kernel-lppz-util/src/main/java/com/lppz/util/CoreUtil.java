package com.lppz.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.http.CallBackRun;
import com.lppz.util.http.CoreRunnable;
import com.lppz.util.http.SubCoreRunnable;

public class CoreUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(CoreUtil.class);

	public static int calcThreadNum(int tempOrderSize, int pageSize) {
		return (int) Math.ceil(Float.parseFloat(String.valueOf(tempOrderSize))
				/ pageSize);
	}

	@SuppressWarnings("unchecked")
	public static <T> Object multiThreadHandle(List<T> list,
			CoreRunnable<T> cr, int step, CallBackRun cb) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		ExecutorService httpExecutor = Executors.newCachedThreadPool();
		AtomicInteger atmCount = new AtomicInteger(0);
		for (T t : list) {
			// Constructor<CoreRunnable<T>> c1=null;
			// CoreRunnable<T> r=null;
			// try {
			// c1 = cr.getClass().getDeclaredConstructor(new
			// Class[]{t.getClass(),AtomicInteger.class,int.class});
			// c1.setAccessible(true);
			// r=c1.newInstance(new Object[]{t,atmCount,step});
			// } catch (NoSuchMethodException | SecurityException |
			// InstantiationException | IllegalAccessException |
			// IllegalArgumentException | InvocationTargetException e) {
			// e.printStackTrace();
			// }
			CoreRunnable<T> r = null;
			try {
				r = ((CoreRunnable<T>) cr.clone());
				r.setAtmCount(atmCount);
				r.setStep(step);
				r.setT(t);
			} catch (CloneNotSupportedException e) {
				logger.error(e.getMessage(), e);
			}
			httpExecutor.execute(r);
		}
		Object o = handleAfter(list, cb, atmCount);
		httpExecutor.shutdown();
		return o;
	}

	private static <T> Object handleAfter(List<T> list, CallBackRun cb,
			AtomicInteger atmCount) {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				logger.error(e1.getMessage(), e1);
			}
			if (atmCount.get() == list.size()) {
				if (cb != null) {
					return cb.doHandleCallBack(cb.getParams());
				}
				break;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> Object multiThreadHandle(List<T> list, int pageSize,
			SubCoreRunnable<T> cr, CallBackRun cb) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		int threadNum0 = calcThreadNum(list.size(), pageSize);
		ExecutorService httpExecutor = Executors.newCachedThreadPool();
		final AtomicInteger atmCount = new AtomicInteger(0);
		for (int i = 0; i < threadNum0;) {
			final List<T> subList = list.subList((i++) * pageSize,
					i * pageSize > list.size() ? list.size() : i * pageSize);
			SubCoreRunnable<T> r = null;
			try {
				r = ((SubCoreRunnable<T>) cr.clone());
				r.setAtmCount(atmCount);
				r.setSubList(subList);
			} catch (CloneNotSupportedException e) {
				logger.error(e.getMessage(), e);
			}
			httpExecutor.execute(r);
		}
		Object o = handleAfter(list, cb, atmCount);
		httpExecutor.shutdown();
		return o;
	}

	public static String byteBufferToString(ByteBuffer buffer) {
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}