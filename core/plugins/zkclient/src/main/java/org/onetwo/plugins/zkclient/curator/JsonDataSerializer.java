package org.onetwo.plugins.zkclient.curator;

import org.apache.commons.lang3.SerializationException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.Assert;
import org.onetwo.plugins.zkclient.utils.ZkUtils;

public class JsonDataSerializer implements DataSerializer {

	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	@Override
	public byte[] serialize(Object object) {
		if (object == null) {
			return ZkUtils.EMPTY_ARRAY;
		}
		try {
			return mapper.toJson(object).getBytes();
		} catch (Exception ex) {
			throw new SerializationException("Cannot serialize", ex);
		}
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> objectClass) {
		if (ZkUtils.isEmpty(bytes)) {
			return null;
		}

		try {
			Assert.notNull(objectClass);
			String json = new String(bytes);
			return mapper.fromJson(json, objectClass);
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}
	
	

}
