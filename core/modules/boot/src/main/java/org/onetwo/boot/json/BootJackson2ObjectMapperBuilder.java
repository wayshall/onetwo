package org.onetwo.boot.json;

import java.text.SimpleDateFormat;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

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
