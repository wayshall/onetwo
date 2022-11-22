package org.onetwo.common.jackson.serializer;

import java.io.IOException;

import org.onetwo.common.exception.BaseException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BooleanWithLabelSerializer extends JsonSerializer<Object> {
	
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    	if (value==null) {
			return ;
		}
    	
    	if (!(value instanceof Boolean)) {
    		String actualType = value==null?"null":value.getClass().getSimpleName();
    		throw new BaseException(getClass().getName() + " only use on Boolean Type field, but found: " + actualType);
    	}

    	Boolean boolVal = (Boolean) value;
		String fieldName = jgen.getOutputContext().getCurrentName();
		jgen.writeBoolean((Boolean)value);
    	
		jgen.writeStringField(fieldName + "Label", boolVal?"是":"否");
    }
    
}
