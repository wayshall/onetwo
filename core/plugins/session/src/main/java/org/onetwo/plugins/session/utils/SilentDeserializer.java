package org.onetwo.plugins.session.utils;

import java.io.IOException;
import java.io.InputStream;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.NestedIOException;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.Deserializer;

public class SilentDeserializer implements Deserializer<Object> {
	private final Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	private Deserializer<Object> delegetedDeser = new DefaultDeserializer();

	@Override
	public Object deserialize(InputStream inputStream) throws IOException {
		try {
			return delegetedDeser.deserialize(inputStream);
		} catch (NestedIOException e) {
			logger.error("SilentDeserializer error: {}", e.getMessage());
		}
		return null;
	}

}
