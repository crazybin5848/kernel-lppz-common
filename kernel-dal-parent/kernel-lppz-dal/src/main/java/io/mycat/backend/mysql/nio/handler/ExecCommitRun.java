package io.mycat.backend.mysql.nio.handler;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.backend.BackendConnection;
import io.mycat.route.RouteResultsetNode;
import io.mycat.server.NonBlockingSession;
import io.mycat.server.sqlcmd.SQLCtrlCommand;

public class ExecCommitRun implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExecCommitRun.class);
	public ExecCommitRun(RouteResultsetNode rrn, SQLCtrlCommand cmdHandler,
			NonBlockingSession session, AtomicInteger started, MultiNodeCoordinator multiNodeCoordinator, AtomicInteger ai) {
		this.rrn=rrn;
		this.cmdHandler=cmdHandler;
		this.session=session;
		this.started=started;
		this.multiNodeCoordinator=multiNodeCoordinator;
		this.ai=ai;
	}

	private RouteResultsetNode rrn;
	private SQLCtrlCommand cmdHandler;
	private NonBlockingSession session;
	private AtomicInteger started;
	private AtomicInteger ai;
	private MultiNodeCoordinator multiNodeCoordinator;
	@Override
	public void run() {
		try {
			final BackendConnection conn = session.getTarget(rrn);
			if (conn != null) {
				conn.setResponseHandler(multiNodeCoordinator);
				cmdHandler.sendCommand(session, conn);
				started.getAndIncrement();
			}
		} finally{
			ai.getAndIncrement();
		}
	}
}