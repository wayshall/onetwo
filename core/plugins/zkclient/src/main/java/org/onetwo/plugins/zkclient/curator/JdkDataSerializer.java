package org.onetwo.plugins.zkclient.curator;

import org.apache.commons.lang3.SerializationException;
import org.onetwo.plugins.zkclient.utils.ZkUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

public class JdkDataSerializer implements DataSerializer {

	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();
	
	@Override
	public byte[] serialize(Object object) {
		if (object == null) {
			return ZkUtils.EMPTY_ARRAY;
		}
		try {
			return serializer.convert(object);
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
			T rs = objectClass.cast(deserializer.convert(bytes));
//			Assert.isInstanceOf(objectClass, rs);
			return rs;
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}
	
	

}
