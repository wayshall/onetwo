package org.onetwo.boot.core.web.socket.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

@SuppressWarnings("serial")
public class WebsocketConnectedEvent extends ApplicationEvent {

	final private WebSocketSession session;
	
	public WebsocketConnectedEvent(Object source, WebSocketSession session) {
		super(source);
		this.session = session;
	}

	public WebSocketSession getSession() {
		return session;
	}

}
