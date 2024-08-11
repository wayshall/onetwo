package org.onetwo.ext.alimq;

import java.util.Map;

import org.apache.rocketmq.common.message.MessageExt;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jackson.exception.JsonException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class JsonMessageDeserializer implements MessageDeserializer {
	public static final JsonMessageDeserializer INSTANCE = new JsonMessageDeserializer();
	public static final JsonMessageDeserializer TYPING_INSTANCE = new JsonMessageDeserializer(true);
	
	private JsonMapper jsonMapper;
	
	private Map<String, String> compatibilityTypeMappings = Maps.newHashMap();
	private boolean enableTyping;
	private Logger logger = ONSUtils.getONSLogger();
	
	public JsonMessageDeserializer() {
		this.jsonMapper = JsonMessageSerializer.createJsonMapper(false);
	}
	public JsonMessageDeserializer(boolean enableTyping) {
		this.jsonMapper = JsonMessageSerializer.createJsonMapper(enableTyping);
		this.enableTyping = enableTyping;
	}

	@Override
	public Object deserialize(byte[] body, MessageExt message) {
		String typeName = message.getUserProperty(JsonMessageSerializer.PROP_BODY_TYPE);
		boolean debug = ONSUtils.isDebugMessage(message);
		//兼容：如果没有类名，则直接使用jdk的反序列化。。。
		/*if(StringUtils.isBlank(typeName)){
			return MessageDeserializer.DEFAULT.deserialize(body, message);
		}*/
		try {
			if(compatibilityTypeMappings.containsKey(typeName)){
				typeName = compatibilityTypeMappings.get(typeName);
			}
			return jsonMapper.fromJson(body, ClassUtils.forName(typeName, ClassUtils.getDefaultClassLoader()), debug);
		} catch (Exception e) {
			if (debug) {
				logger.error("deserialize message body : {}", LangUtils.newString(body));
				throw new JsonException("deserialize message error for type: " + typeName, e);
			} else {
				throw new JsonException("deserialize message error for type: " + typeName, e);
			}
		}
	}

	@Override
	public <T> T deserialize(byte[] body, Class<T> messageType) {
		try {
			return jsonMapper.fromJson(body, messageType);
		} catch (Exception e) {
			throw new JsonException("deserialize message error for class: " + messageType.getName(), e);
		}
	}

	public void setCompatibilityTypeMappings(Map<String, String> compatibilityTypeMappings) {
		this.compatibilityTypeMappings = compatibilityTypeMappings;
	}
	
	public boolean isEnableTyping() {
		return enableTyping;
	}
	public void setEnableTyping(boolean enableTyping) {
		this.enableTyping = enableTyping;
	}

}
