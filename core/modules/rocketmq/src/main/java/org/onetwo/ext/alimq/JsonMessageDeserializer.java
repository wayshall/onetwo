package org.onetwo.ext.alimq;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.springframework.util.ClassUtils;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class JsonMessageDeserializer implements MessageDeserializer {
	public static final JsonMessageDeserializer INSTANCE = new JsonMessageDeserializer();
	
	private JsonMapper jsonMapper = JsonMapper.ignoreNull();
	
	private Map<String, String> compatibilityTypeMappings = Maps.newHashMap();

	@Override
	public Object deserialize(byte[] body, MessageExt message) {
		String typeName = message.getUserProperty(JsonMessageSerializer.PROP_BODY_TYPE);
		//兼容。。。
		if(StringUtils.isBlank(typeName)){
			return MessageDeserializer.DEFAULT.deserialize(body, message);
		}
		try {
			if(compatibilityTypeMappings.containsKey(typeName)){
				typeName = compatibilityTypeMappings.get(typeName);
			}
			return jsonMapper.fromJson(body, ClassUtils.forName(typeName, null));
		} catch (Exception e) {
			throw new BaseException("deserialize message error for type: " + typeName, e);
		}
	}

	@Override
	public <T> T deserialize(byte[] body, Class<T> messageType) {
		try {
			return jsonMapper.fromJson(body, messageType);
		} catch (Exception e) {
			throw new BaseException("deserialize message error for class: " + messageType.getName(), e);
		}
	}

	public void setCompatibilityTypeMappings(Map<String, String> compatibilityTypeMappings) {
		this.compatibilityTypeMappings = compatibilityTypeMappings;
	}


}
