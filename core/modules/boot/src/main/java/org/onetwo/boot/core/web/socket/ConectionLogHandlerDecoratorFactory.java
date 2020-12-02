package org.onetwo.boot.core.web.socket;

import org.onetwo.boot.core.web.socket.event.WebsocketClosedEvent;
import org.onetwo.boot.core.web.socket.event.WebsocketConnectedEvent;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

public class ConectionLogHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public WebSocketHandler decorate(WebSocketHandler handler) {
		return new LogWebSocketHandlerDecorator(applicationContext, handler);
	}
	
	public static class LogWebSocketHandlerDecorator extends WebSocketHandlerDecorator {
		
		private final Logger logger = JFishLoggerFactory.getLogger(getClass());
		private ApplicationContext applicationContext;

		public LogWebSocketHandlerDecorator(ApplicationContext applicationContext, WebSocketHandler delegate) {
			super(delegate);
			this.applicationContext = applicationContext;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			if (logger.isInfoEnabled()) {
				logger.info("websocket连接已监听, 用户：{}, sid: {}", session.getPrincipal()==null?"null":session.getPrincipal().getName(), session.getId());
			}
			applicationContext.publishEvent(new WebsocketConnectedEvent(this, session));
			super.afterConnectionEstablished(session);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			if (logger.isInfoEnabled()) {
				logger.info("websocket连接已监听, 用户：{}, sid: {}", session.getPrincipal()==null?"null":session.getPrincipal().getName(), session.getId());
			}
			applicationContext.publishEvent(new WebsocketClosedEvent(this, session, closeStatus));
			super.afterConnectionClosed(session, closeStatus);
		}
	}
	
}
