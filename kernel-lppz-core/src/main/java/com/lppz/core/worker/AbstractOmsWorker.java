//package com.lppz.cache.worker;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import com.lppz.cache.worker.entity.Order;
//
//
//public abstract class AbstractOmsWorker implements OmsWorkerInteface{
//	protected List<OmsWorkerInteface> nextWorkerList;
//	ExecutorService pool = Executors.newCachedThreadPool();
//	protected boolean isBatch = true;
//
//	public void execute(Order... orders) {
//		final Map<OmsWorkerInteface, List<Order>> map = handle(orders);
//		exec(map);
//	}
//
//	private void exec(final Map<OmsWorkerInteface, List<Order>> map) {
//		if (map == null)
//			return;
//		for (final OmsWorkerInteface worker : map.keySet()) {
//			if (map.size() > 1) {
//				pool.execute(new IntenalRunnable(worker, map));
//			} else {
//				new IntenalRunnable(worker, map).run();
//			}
//		}
//	}
//
//	private class IntenalRunnable implements Runnable {
//		public IntenalRunnable(OmsWorkerInteface worker,
//				Map<OmsWorkerInteface, List<Order>> map) {
//			this.worker = worker;
//			this.map = map;
//		}
//
//		Map<OmsWorkerInteface, List<Order>> map;
//		OmsWorkerInteface worker;
//
//		@Override
//		public void run() {
//			Order[] orders = (Order[]) map.get(worker).toArray();
//			if (((AbstractOmsWorker)worker).isBatch) {
//				worker.execute(orders);
//			} else {
//				((AbstractOmsWorker)worker).executeSingle(orders);
//			}
//		}
//	}
//
//	private void executeSingle(Order... orders) {
//		Map<OmsWorkerInteface, List<Order>> tmmap = new HashMap<OmsWorkerInteface, List<Order>>();
//		for (Order o : orders) {
//			final Map<OmsWorkerInteface, List<Order>> map = handle(o);
//			for (OmsWorkerInteface abs : map.keySet()) {
//				List<Order> l = tmmap.get(abs);
//				if (l == null) {
//					l = new ArrayList<Order>();
//				}
//				l.addAll(map.get(abs));
//				tmmap.put(abs, l);
//			}
//		}
//		exec(tmmap);
//	}
//
//	protected abstract Map<OmsWorkerInteface, List<Order>> handle(Order... orders);
//}
