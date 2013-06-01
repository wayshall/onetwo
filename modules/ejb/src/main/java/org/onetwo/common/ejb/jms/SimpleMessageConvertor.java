package org.onetwo.common.ejb.jms;

import java.io.Serializable;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class SimpleMessageConvertor implements MessageConvertor {

	@SuppressWarnings("rawtypes")
	@Override
	public Message convert(Session session, Object msg) {
		Message message = null;
		try {
			Assert.notNull(msg, "message can not be null!");
			if(msg instanceof Map){
				message = createMapMessage(session, (Map)msg);
			}
			else if(msg instanceof String){
				message = session.createTextMessage(msg.toString());
			}else if(msg instanceof Serializable){
				message = session.createObjectMessage((Serializable)msg);
			}else if(msg instanceof byte[]){
				message = createBytesMessage(session, (byte[])msg);
			}else{
				LangUtils.throwBaseException("can not create message by ["+msg.getClass()+"]");
			}
		} catch (Exception e) {
			LangUtils.throwBaseException("create and conver message error : " + e.getMessage(), e);
		}
		return message;
	}
	
	protected BytesMessage createBytesMessage(Session session, byte[] bytes) throws JMSException{
		BytesMessage bytesMsg = session.createBytesMessage();
		bytesMsg.writeBytes(bytes);
		return bytesMsg;
	}
	
	protected MapMessage createMapMessage(Session session, Map<?, ?> map) throws JMSException{
		MapMessage mapMsg = session.createMapMessage();
		for(Map.Entry<?, ?> entry : map.entrySet()){
			if(!(entry.getKey() instanceof String))
				throw new BaseException("key must be String:"+entry.getKey());
			mapMsg.setObject((String)entry.getKey(), entry.getValue());
		}
		return mapMsg;
	}

}
