package org.onetwo.common.ejb.jms;

import javax.jms.Message;
import javax.jms.Session;

public interface MessageConvertor {

	public Message convert(Session session, Object msg);
}
