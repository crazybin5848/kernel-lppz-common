package com.lppz.webauth.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OmsSessionListener implements SessionListener
{

	private static final Logger logger = LoggerFactory.getLogger(OmsSessionListener.class);

	public void onStart(Session session)
	{
		logger.info("session created, session id = " + session.getId());
	}

	public void onExpiration(Session session)
	{
		logger.info("session timeout, remove user info. session id = " + session.getId());
	}

	public void onStop(Session session)
	{
		logger.info("session stopped, session id = " + session.getId());
	}

}
