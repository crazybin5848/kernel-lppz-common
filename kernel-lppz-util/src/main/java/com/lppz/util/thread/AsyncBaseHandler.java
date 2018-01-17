package com.lppz.util.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lppz.util.CoreUtil;
import com.lppz.util.http.SubCoreRunnable;

public abstract class AsyncBaseHandler<F, T> {
	private final Logger logger = LoggerFactory.getLogger(AsyncBaseHandler.class);

	public void handle(List<String> pks, int numPerThread, final int numAfertThread, Object[] fetchojb, Object[] aobj) {
		beforStart();
		final List<T> list = fetchTableId(pks, fetchojb);
		if (list == null || list.isEmpty()) {
			logger.debug("------ no Objects to create 0");
			return;
		}
		final List<List<F>> lfs = Collections
				.synchronizedList(new ArrayList<List<F>>());
		CoreUtil.multiThreadHandle(list, numPerThread, new SubCoreRunnable<T>() {

			@Override
			protected void doRun(List<T> subList) {
				List<F> lf = handleBatchOrder(subList);
			    if (lf != null)
				   lfs.add(lf);
			}
		}, null);
		afterStart(lfs, aobj);
		logger.info("atmCount:" + "" + "---Objects all finished!");
	}

	protected abstract List<T> fetchTableId(List<String> pks2, Object[] fetchojb);

	protected abstract List<F> handleBatchOrder(List<T> list);

	protected void beforStart() {
	}

	protected void afterStart(List<List<F>> list, Object[] aobj) {
	}
}