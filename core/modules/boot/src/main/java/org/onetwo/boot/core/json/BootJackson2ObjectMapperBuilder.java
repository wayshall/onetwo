package org.onetwo.boot.core.json;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class BootJackson2ObjectMapperBuilder implements Jackson2ObjectMapperBuilderCustomizer {

	@Override
	public void customize(Jackson2ObjectMapperBuilder builder) {
		/*builder.serializationInclusion(Include.NON_NULL)
				.featuresToEnable(Feature.ALLOW_UNQUOTED_FIELD_NAMES,
									Feature.ALLOW_SINGLE_QUOTES)
				.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS,
									DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));*/
	}
	

}
