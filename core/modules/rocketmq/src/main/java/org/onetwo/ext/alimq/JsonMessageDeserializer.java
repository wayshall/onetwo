package org.onetwo.ext.alimq;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.springframework.util.ClassUtils;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
public class JsonMessageDeserializer implements MessageDeserializer {
	public static final JsonMessageDeserializer INSTANCE = new JsonMessageDeserializer();
	
	private JsonMapper jsonMapper = JsonMapper.defaultMapper();

	@Override
	public Object deserialize(byte[] body, MessageExt message) {
		String typeName = message.getUserProperty(JsonMessageSerializer.PROP_BODY_TYPE);
		//兼容。。。
		if(StringUtils.isBlank(typeName)){
			return MessageDeserializer.DEFAULT.deserialize(body, message);
		}
		try {
			return jsonMapper.fromJson(body, ClassUtils.forName(typeName, null));
		} catch (Exception e) {
			throw new BaseException("deserialize message error!", e);
		}
	}


}
