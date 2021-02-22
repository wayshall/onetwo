package org.onetwo.boot.core.web.socket;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class StompMessageSender {
	@Autowired
	private WebSocketProperties properties;
	
	@Autowired
	private SimpMessageSendingOperations messageSendingOperations;
	
	private String defaultUserDestination  = "/inbox";

	/***
	 * 默认把第一个前缀(/broadcast)作为广播地址
	 * @param payload
	 */
	public void sendToBroadcast(Object payload) {
		String destination = LangUtils.getFirst(properties.getBroker().getSimplePrefixes());
		sendTo(destination, payload);
	}
	
	/***
	 * 发送到 /broadcast/{subpath}
	 * 默认把第一个前缀作为广播地址
	 * @param subpath
	 * @param payload
	 */
	public void sendToBroadcast(String subpath, Object payload) {
		String destination = LangUtils.getFirst(properties.getBroker().getSimplePrefixes());
		destination = destination + StringUtils.appendStartWithSlash(subpath);
		sendTo(destination, payload);
	}
	
	public void sendTo(String destination, Object payload) {
		messageSendingOperations.convertAndSend(destination, payload);
	}

	/***
	 * 发送到某个用户的邮箱，即点对点消息
	 * 用户邮箱默认的destination为：inbox
	 * @param user
	 * @param payload
	 */
	public void sendToUserInbox(String user, Object payload) {
		this.sendToUser(user, defaultUserDestination, payload);
	}
	
	public void sendToUser(String user, String destination, Object payload) {
		messageSendingOperations.convertAndSendToUser(user, destination, payload);
	}

	public SimpMessageSendingOperations getMessageSendingOperations() {
		return messageSendingOperations;
	}

	public void setMessageSendingOperations(SimpMessageSendingOperations messageSendingOperations) {
		this.messageSendingOperations = messageSendingOperations;
	}
	
}
