package org.onetwo.boot.module.swagger.json;

import java.io.IOException;

import springfox.documentation.service.Documentation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author wayshall <br/>
 */
public class DocumentationDesializer extends StdDeserializer<Documentation> {

	protected DocumentationDesializer() {
		super(Documentation.class);
	}

	@Override
	public Documentation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().
		return null;
	}

}
