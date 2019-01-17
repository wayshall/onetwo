package org.onetwo.common.jackson;

import java.io.IOException;
import java.util.Collection;
import java.util.zip.CRC32;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class UrlJsonSerializer extends JsonSerializer<Object> {
	
	@SuppressWarnings("unchecked")
	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		if(value==null)
			return ;
		if(value.getClass().isArray()){
			gen.writeStartArray();
			for(String s: (String[])value){
				gen.writeString(getServerFullPath(s));
			}
			gen.writeEndArray();
		}else if(value instanceof Collection){
			gen.writeStartArray();
			for(String s: (Collection<String>)value){
				gen.writeString(getServerFullPath(s));
			}
			gen.writeEndArray();
		}else{
			String str = value.toString();
			str = getServerFullPath(str);
			gen.writeString(str);
		}
	}
	
	
	/****
	 * 序列化需要类型形式时，即objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY)，必须实现此方法
	 */
	@Override
	public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
		typeSer.writeTypePrefixForScalar(value, gen);
		serialize(value, gen, serializers);
		typeSer.writeTypePrefixForScalar(value, gen);
	}



	protected String getServerFullPath(String subPath){
		if(isHttpPath(subPath)){
			return subPath;
		}
		return fixPath("", subPath);
	}
	
	protected final boolean isHttpPath(String subPath){
		if(subPath==null) {
			return false;
		}
		subPath = subPath.toLowerCase();
		return subPath.startsWith("http://") || subPath.startsWith("https://");
	}
	protected String fixPath(String basePath, String subPath){
		if(!basePath.endsWith("/") && !subPath.startsWith("/")){
			basePath += "/";
		}
		return basePath + subPath;
	}
	
	protected long getServerIndex(String subPath, int serverCount){
		CRC32 crc = new CRC32();
		crc.update(subPath.getBytes());
		long index = crc.getValue()%serverCount;
		return index;
	}

}
