package org.onetwo.boot.module.swagger.json;

import java.io.IOException;

import springfox.documentation.service.Documentation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author wayshall <br/>
 */
public class DocumentationDesializer extends StdDeserializer<Documentation> {

	protected DocumentationDesializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Documentation deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return null;
	}

}
