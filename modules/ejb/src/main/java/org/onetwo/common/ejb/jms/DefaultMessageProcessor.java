package org.onetwo.common.ejb.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class DefaultMessageProcessor implements MessageProcessor {
	
	private Context context;
	private ConnectionFactory connectionFactory;
	private MessageConvertor convertor;
	private Connection connection;
//	private Session session;
//	private T destination;
	
	private MessageConf conf;
	private boolean initialized;
	
	public DefaultMessageProcessor(MessageConvertor convertor, MessageConf conf){
		this.convertor = convertor;
		this.conf = conf;
		if(!conf.isLazyInit())
			this.init(true);
	}
	
	public MessageConf getConf() {
		return conf;
	}

	protected void init(boolean startConnect){
		try {
			this.context = new InitialContext(conf.getContextProps());
			
			connectionFactory = (ConnectionFactory)context.lookup(conf.getConnectionFactoryName());
			Assert.notNull(connectionFactory, "can not lookup the connectionFacotry by jndi : " + conf.getConnectionFactoryName());
			LangUtils.println("connectionFactory : ${0}", connectionFactory);
			
			/*this.destination = (T) context.lookup(conf.getDestination());
			Assert.notNull(connectionFactory, "can not lookup the destination by name : " + conf.getDestination());
			
			
			this.session = this.connection.createSession(conf.isTransacted(), conf.getSessionMode());*/
			connection = createConnection(connectionFactory);
//			LangUtils.println("connection : ${0}", connection);
			if(startConnect)
				connection.start();
			
			this.initialized = true;
//			LangUtils.println("initialized : ${0}", initialized);
		} catch (Exception e) {
			LangUtils.throwBaseException("init context error : " + e.getMessage(), e);
		}
	}
	
	protected Connection createConnection(ConnectionFactory cf) throws JMSException{
		Connection connection = cf.createConnection();
		return connection;
	}
	
	protected Session createSession(Connection con, boolean transacted, int sessionMode) throws JMSException{
		Session session = con.createSession(transacted, sessionMode);
		return session;
	}
	
	protected Destination lookupDestination(Context context, String destinationName) throws NamingException{
		Assert.hasText(destinationName, "destinantion must has text!");
		Destination dest = (Destination)context.lookup(destinationName);
		return dest;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.ejb.jms.MessageSender#send(java.lang.Object)
	 */
	@Override
	public MessageSessionDelegate send(Object msg){
//		Connection con = null;
		Session session = null;
		try {
			if(!this.initialized)
				this.init(true);
			
			session = createSession(connection, conf.isTransacted(), conf.getSessionMode());
			
			Destination dest = lookupDestination(context, conf.getDestination());
			
			MessageProducer producer = session.createProducer(dest);
			Message message = this.convertor.convert(session, msg);
			producer.send(message);
			
			if(conf.isTransacted())
				JMSUtils.commit(session);
			
		} catch (Exception e) {
			LangUtils.throwBaseException("send message error : " + e.getMessage(), e);
			if(conf.isTransacted())
				JMSUtils.rollback(session);
		}finally{
			JMSUtils.close(session);
//			JMSUtils.close(con);
		}
		
		return MessageSessionDelegate.create(session);
	}
	
	@Override
	public void onMessageHandler(boolean block, final MessageHandler handler) {

		try {
			if(!this.initialized)
				this.init(block);
			
			Session session = createSession(connection, conf.isTransacted(), conf.getSessionMode());
			final MessageSessionDelegate delegateSession = MessageSessionDelegate.create(session);;
			
			Destination dest = lookupDestination(context, conf.getDestination());
			MessageConsumer consumer = session.createConsumer(dest);
			
			Message message = null;
			if(block){
				message = consumer.receive();
				handler.doMessage(this, delegateSession, message);
				
				if(conf.isTransacted())
					delegateSession.commit();
			}
			else{
				consumer.setMessageListener(new MessageListener() {
					@Override
					public void onMessage(Message message) {
						try {
							handler.doMessage(DefaultMessageProcessor.this, delegateSession, message);

							/*if(conf.isTransacted())
								delegateSession.commit();*/
						} catch (JMSException e) {
							LangUtils.throwBaseException("onMessage error : " + e.getMessage(), e);
						}
					}
				});
				
				this.connection.start();
			}
			
		}catch (Exception e) {
			LangUtils.throwBaseException("send message error : " + e.getMessage(), e);
		}finally{
//			JMSUtils.close(session);
		}
		
	}
/*
	public void onMessageHandler(final MessageHandler handler){
		try {
			if(!this.initialized)
				this.init(false);
			
			Session session = createSession(connection, conf.isTransacted(), conf.getSessionMode());
			final MessageSessionDelegate finalDelegate = MessageSessionDelegate.create(session);
			
			Destination dest = lookupDestination(context, conf.getDestination());
			
			MessageConsumer consumer = session.createConsumer(dest);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						handler.doMessage(DefaultMessageProcessor.this, finalDelegate, message);
					} catch (JMSException e) {
						LangUtils.throwBaseException("onMessage error : " + e.getMessage(), e);
					}
				}
			});
			
			this.connection.start();
		}catch (Exception e) {
			LangUtils.throwBaseException("send message error : " + e.getMessage(), e);
		}finally{
//			JMSUtils.close(session);
		}
		
	}
	
	public void receive(MessageHandler handler){
		Session session = null;
		Message message = null;
		try {
			if(!this.initialized)
				this.init(true);
			session = createSession(connection, conf.isTransacted(), conf.getSessionMode());
			MessageSessionDelegate delegateSession = MessageSessionDelegate.create(session);
			
			Destination dest = lookupDestination(context, conf.getDestination());
			
			MessageConsumer consumer = session.createConsumer(dest);
			message = consumer.receive();
			handler.doMessage(this, delegateSession, message);
			
			if(conf.isTransacted())
				delegateSession.commit();
		}catch (Exception e) {
			LangUtils.throwBaseException("send message error : " + e.getMessage(), e);
		}finally{
			JMSUtils.close(session);
		}
	}
	
	public Message receive2(){
		Session session = null;
		Message message = null;
		try {
			if(!this.initialized)
				this.init(true);
			session = createSession(connection, conf.isTransacted(), conf.getSessionMode());
			
			Destination dest = lookupDestination(context, conf.getDestination());
			
			MessageConsumer consumer = session.createConsumer(dest);
			message = consumer.receive();
		}catch (Exception e) {
			LangUtils.throwBaseException("send message error : " + e.getMessage(), e);
		}finally{
			JMSUtils.close(session);
		}
		return message;
	}*/
	
	public void destroy(){
		JMSUtils.close(connection);
		this.initialized = false;
		if(this.connectionFactory!=null)
			this.connectionFactory = null;
	}
	
	
}
