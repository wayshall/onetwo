package org.onetwo.ext.alimq;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;

/**
 * @author wayshall
 * <br/>
 */
public class JsonMessageSerializer implements MessageSerializer {

	final public static JsonMapper createJsonMapper(boolean enableTyping) {
		JsonMapper jsonMapper;
		if (enableTyping) {
			jsonMapper = JsonMapper.ignoreNull().enableTyping();
		} else {
			jsonMapper = JsonMapper.ignoreNull();
		}
		return jsonMapper;
	}
	
	public static final JsonMessageSerializer INSTANCE = new JsonMessageSerializer(false);
	public static final JsonMessageSerializer CHECKED_INSTANCE = new JsonMessageSerializer(true);
	public static final JsonMessageSerializer TYPING_INSTANCE = new JsonMessageSerializer(true, true);
	
	public static final String PROP_BODY_TYPE = "PROP_BODY_TYPE";
	private JsonMapper jsonMapper = JsonMapper.defaultMapper();
	private boolean checkMessageBodyInstantiate;
	private Logger logger = ONSUtils.getONSLogger();
	
	public JsonMessageSerializer(){
		//不使用enableDefaultTyping模式，携带了太多类型信息，对反序列化不友好
		/*ObjectMapper mapper = jsonMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);*/
	}
	
	public JsonMessageSerializer(boolean checkMessageBodyInstantiate) {
		this(checkMessageBodyInstantiate, false);
	}
	
	public JsonMessageSerializer(boolean checkMessageBodyInstantiate, boolean enableTyping) {
		super();
		this.checkMessageBodyInstantiate = checkMessageBodyInstantiate;
		this.jsonMapper = createJsonMapper(enableTyping);
	}

	@Override
	public byte[] serialize(Object body, MessageDelegate messageDelegate) {
		if(checkMessageBodyInstantiate){
			//检查消息是否可以用反射实例化，保证反序列化时不会因为没有默认构造函数而出错
			ReflectUtils.newInstance(body.getClass());
		}
		if (messageDelegate!=null && StringUtils.isBlank(messageDelegate.getUserProperties(PROP_BODY_TYPE))) {
			messageDelegate.putUserProperties(PROP_BODY_TYPE, body.getClass().getName());
		}
		if (messageDelegate.isDebug()) {
			if (logger.isInfoEnabled()) {
				String jsonstr = jsonMapper.toJson(body);
				logger.info("ons message body json: {}", jsonstr);
			}
		}
		return jsonMapper.toJsonBytes(body);
	}

	public <T> T deserialize(byte[] body, Class<T> messageType) {
		try {
			return jsonMapper.fromJson(body, messageType);
		} catch (Exception e) {
			throw new BaseException("deserialize message error for class: " + messageType.getName(), e);
		}
	}

	public void setCheckMessageBodyInstantiate(boolean checkMessageBodyInstantiate) {
		this.checkMessageBodyInstantiate = checkMessageBodyInstantiate;
	}

	public JsonMapper getJsonMapper() {
		return jsonMapper;
	}

}
