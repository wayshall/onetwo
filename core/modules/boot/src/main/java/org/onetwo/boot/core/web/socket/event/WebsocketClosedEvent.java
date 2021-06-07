package org.onetwo.boot.core.web.socket.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@SuppressWarnings("serial")
public class WebsocketClosedEvent extends ApplicationEvent {

	final private WebSocketSession session;
	final private CloseStatus closeStatus;
	
	public WebsocketClosedEvent(Object source, WebSocketSession session, CloseStatus closeStatus) {
		super(source);
		this.session = session;
		this.closeStatus = closeStatus;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public CloseStatus getCloseStatus() {
		return closeStatus;
	}

}
