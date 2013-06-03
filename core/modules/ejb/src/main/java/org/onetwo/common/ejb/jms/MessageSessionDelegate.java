package org.onetwo.common.ejb.jms;

import javax.jms.Session;

public class MessageSessionDelegate {
	
	public static MessageSessionDelegate create(Session session){
		return new MessageSessionDelegate(session);
	}
	
	private Session session;
	
	public MessageSessionDelegate(Session session){
		this.session = session;
	}

	public void commit(){
		JMSUtils.commit(session);
	}

	public void rollback(){
		JMSUtils.rollback(session);
	}

	public void close() {
		JMSUtils.close(session);
	}

}
