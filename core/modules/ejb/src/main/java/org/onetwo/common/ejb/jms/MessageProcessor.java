package org.onetwo.common.ejb.jms;


public interface MessageProcessor {
	public MessageConf getConf();
	public MessageSessionDelegate send(Object msg);
//	public void receive(MessageHandler handler);
	public void onMessageHandler(boolean block, MessageHandler handler);
	public void destroy();
}