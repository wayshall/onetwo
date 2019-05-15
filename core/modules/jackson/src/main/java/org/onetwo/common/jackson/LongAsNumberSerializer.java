package org.onetwo.common.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * 有些时候项目统一把long序列化为string，单前端的确需要number类型
 */
public class LongAsNumberSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long number, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(number);
    }

    @Override
    public void serializeWithType(Long value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        gen.writeNumber(value);
    }
}
