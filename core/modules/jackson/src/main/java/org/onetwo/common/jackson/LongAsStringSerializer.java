package org.onetwo.common.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * 防止前端long类型超出精度，序列化为字符串
 */
public class LongAsStringSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long number, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(number.toString());
    }

    @Override
    public void serializeWithType(Long value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeString(value.toString());
    }
}
