package io.mycat.backend.mysql.nio.handler;

import io.mycat.backend.BackendConnection;
import io.mycat.route.RouteResultsetNode;
import io.mycat.server.NonBlockingSession;
import io.mycat.server.sqlcmd.SQLCtrlCommand;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiNodeCoordinator implements ResponseHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MultiNodeCoordinator.class);
	private final AtomicInteger runningCount = new AtomicInteger(0);
	private final AtomicInteger faileCount = new AtomicInteger(0);
	private volatile int nodeCount;
	private final NonBlockingSession session;
	private SQLCtrlCommand cmdHandler;
	private final AtomicBoolean failed = new AtomicBoolean(false);

	public MultiNodeCoordinator(NonBlockingSession session) {
		this.session = session;
	}

	static ExecutorService commitPool = Executors.newCachedThreadPool();
	public void executeBatchNodeCmd(SQLCtrlCommand cmdHandler) {
		this.cmdHandler = cmdHandler;
		final int initCount = session.getTargetCount();
		runningCount.set(initCount);
		nodeCount = initCount;
		failed.set(false);
		faileCount.set(0);
		// 执行
		AtomicInteger started = new AtomicInteger(0);
		AtomicInteger ai = new AtomicInteger(0);
		int size=session.getTargetKeys().size();
		for (RouteResultsetNode rrn : session.getTargetKeys()) {
			if (rrn == null) {
				LOGGER.error("null is contained in RoutResultsetNodes, source = "
						+ session.getSource());
				ai.getAndIncrement();
				continue;
			}
			commitPool.execute(new ExecCommitRun(rrn,cmdHandler,session,started,this,ai));
		}
		while(true){
			try {
				Thread.sleep(7);
			} catch (InterruptedException e) {
			}
			if(ai.get()==size)
				break;
		}
		if (started.get() < nodeCount) {
			runningCount.set(started.get());
			LOGGER.warn("some connection failed to execut "
					+ (nodeCount - started.get()));
			/**
			 * assumption: only caused by front-end connection close. <br/>
			 * Otherwise, packet must be returned to front-end
			 */
			failed.set(true);
		}
	}

	private boolean finished() {
		int val = runningCount.decrementAndGet();
		return (val == 0);
	}

	@Override
	public void connectionError(Throwable e, BackendConnection conn) {
	}

	@Override
	public void connectionAcquired(BackendConnection conn) {

	}

	@Override
	public void errorResponse(byte[] err, BackendConnection conn) {
		faileCount.incrementAndGet();

		if (this.cmdHandler.releaseConOnErr()) {
			session.releaseConnection(conn);
		} else {
			session.releaseConnectionIfSafe(conn, LOGGER.isDebugEnabled(),
					false);
		}
		if (this.finished()) {
			cmdHandler.errorResponse(session, err, this.nodeCount,
					this.faileCount.get());
			if (cmdHandler.isAutoClearSessionCons()) {
				session.clearResources(session.getSource().isTxInterrupted());
			}
		}

	}

	@Override
	public void okResponse(byte[] ok, BackendConnection conn) {
		if (this.cmdHandler.relaseConOnOK()) {
			session.releaseConnection(conn);
		} else {
			session.releaseConnectionIfSafe(conn, LOGGER.isDebugEnabled(),
					false);
		}
		if (this.finished()) {
			cmdHandler.okResponse(session, ok);
			if (cmdHandler.isAutoClearSessionCons()) {
				session.clearResources(false);
			}
		}
	}

	@Override
	public void fieldEofResponse(byte[] header, List<byte[]> fields,
			byte[] eof, BackendConnection conn) {

	}

	@Override
	public void rowResponse(byte[] row, BackendConnection conn) {

	}

	@Override
	public void rowEofResponse(byte[] eof, BackendConnection conn) {
	}

	@Override
	public void writeQueueAvailable() {

	}

	@Override
	public void connectionClose(BackendConnection conn, String reason) {

	}

}
