package org.onetwo.boot.module.oauth2.result;

import java.io.IOException;
import java.util.Map.Entry;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.OAuth2ExceptionJackson2Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class OAuth2ExceptionDataResultJsonSerializer extends OAuth2ExceptionJackson2Serializer {
	

	@Override
	public void serialize(OAuth2Exception value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
        jgen.writeStartObject();
		jgen.writeStringField("code", value.getOAuth2ErrorCode());
		jgen.writeStringField("message", value.getMessage());
		if (value.getAdditionalInformation()!=null) {
			for (Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
				String key = entry.getKey();
				String add = entry.getValue();
				jgen.writeStringField(key, add);				
			}
		}
        jgen.writeEndObject();
	}
	
	@JsonSerialize(using=OAuth2ExceptionDataResultJsonSerializer.class)
	public static interface OAuth2ExceptionMixin {
		
	}

}
