package org.onetwo.common.jackson;

import java.io.IOException;
import java.util.Collection;
import java.util.zip.CRC32;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

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
	
	protected String getServerFullPath(String subPath){
		if(isHttpPath(subPath)){
			return subPath;
		}
		return fixPath("", subPath);
	}
	
	protected final boolean isHttpPath(String subPath){
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
