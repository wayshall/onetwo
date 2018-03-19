package org.onetwo.boot.core.json;

import java.util.List;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wayshall
 * <br/>
 */
public interface ObjectMapperProvider {
	
	ObjectMapper createObjectMapper();
	
	public interface ObjectMapperCustomizer {
		void afterCreate(ObjectMapper objectMapper);
	}
	
	ObjectMapperProvider DEFAULT = new DefaultObjectMapperProvider();
	
	public class DefaultObjectMapperProvider implements ObjectMapperProvider {
//		@Autowired(required=false)
		private ObjectMapper objectMapper;
		@Autowired(required=false)
		private List<ObjectMapperCustomizer> customizers;
		@Autowired(required=false)
		private Jackson2ObjectMapperBuilder builder;

		@Override
		public ObjectMapper createObjectMapper() {
			ObjectMapper objectMapper = this.objectMapper;
			if(objectMapper==null){
				if(builder!=null){
					objectMapper = builder.createXmlMapper(false).build();
				}else{
					objectMapper = JsonMapper.ignoreNull().getObjectMapper();
				}
				for(ObjectMapperCustomizer c : customizers){
					c.afterCreate(objectMapper);
				}
				this.objectMapper = objectMapper;
			}
			return objectMapper;
		}
		
	}

}
