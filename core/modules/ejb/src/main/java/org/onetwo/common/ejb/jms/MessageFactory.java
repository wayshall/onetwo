package org.onetwo.common.ejb.jms;

import java.util.Properties;

import javax.jms.Session;
import javax.naming.Context;

import org.onetwo.common.utils.propconf.PropConfig;

public abstract class MessageFactory {

	public static final String JBOSS_JNP = "org.jnp.interfaces.NamingContextFactory";

	public static final String CONNECTION_FACTORY_NAME = "connection.factory";
	public static final String DESTINATION_NAME = "destination";
	public static final String SESSION_MODEL = "session.mode";
	public static final String LAZY_INIT = "lazy.init";
	public static final String SESSION_TRANSACTED = "session.transacted";
	
	public static MessageProcessor createSender(PropConfig config){
		MessageConf conf = new MessageConf();
		
		Properties prop = new Properties();
		prop.setProperty(Context.INITIAL_CONTEXT_FACTORY, config.getProperty(Context.INITIAL_CONTEXT_FACTORY, JBOSS_JNP));
		prop.setProperty(Context.PROVIDER_URL, config.getAndThrowIfEmpty(Context.PROVIDER_URL));//must
		conf.setContextProps(prop);

		conf.setConnectionFactoryName(config.getAndThrowIfEmpty(CONNECTION_FACTORY_NAME));//must
		conf.setDestination(config.getAndThrowIfEmpty(DESTINATION_NAME));//must
		conf.setSessionMode(config.getInt(SESSION_MODEL, Session.AUTO_ACKNOWLEDGE));
		conf.setLazyInit(config.getBoolean(LAZY_INIT, true));
		conf.setTransacted(config.getBoolean(SESSION_TRANSACTED, false));

		return createSender(createSimpleMessageConvertor(), conf);
	}
	
	protected static MessageConvertor createSimpleMessageConvertor(){
		return new SimpleMessageConvertor();
	}

	public static MessageProcessor createSender(MessageConf conf){
		return createSender(createSimpleMessageConvertor(), conf);
	}
	
	public static MessageProcessor createSender(MessageConvertor convertor, MessageConf conf){
		DefaultMessageProcessor sender = new DefaultMessageProcessor(convertor, conf);
		return sender;
	}

}
