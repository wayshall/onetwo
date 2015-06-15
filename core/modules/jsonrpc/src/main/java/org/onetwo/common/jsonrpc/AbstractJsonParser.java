package org.onetwo.common.jsonrpc;

import java.lang.reflect.Type;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.convert.Types;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractJsonParser {

	protected JsonMapper mapper;
	
	public AbstractJsonParser(JsonMapper mapper) {
		super();
		this.mapper = mapper;
	}

	protected Object parseNode(JsonNode node, Type valueClass) throws Exception{
		Assert.notNull(node);
		Assert.notNull(valueClass);
		Object value = null;
//		Class<?> valueClass = (Class<?>) valueType;
		if(node.isArray()){
//			List<Object> list = LangUtils.newArrayList();
			/*if(valueClass.isArray()){
//				value = (Object[])mapper.fromJsonAsArray(node.toString(), valueType);
				for (int i = 0; i < node.size(); i++) {
					Object val = parseNode(node, valueClass.getComponentType());
					list.add(val);
				}
				value = list.toArray();
//				value = mapper.fromJsonAsElementArray(node.toString(), valueClass.getComponentType());
				
			}else{
				valueClass = ReflectUtils.getGenricType(valueClass, 0);
				for (int i = 0; i < node.size(); i++) {
					Object val = parseNode(node, valueClass.getComponentType());
					list.add(val);
				}
				value = list;
				Class<?> elementClass = ReflectUtils.getGenricType(parameter.getGenericParameterType(), 0);
				value = mapper.fromJsonAsList(node.toString(), elementClass);
			}*/
			value = mapper.fromJson(node.toString(), valueClass);
		}else if(node.isObject()){
			value = mapper.fromJson(node.toString(), valueClass);
		}else if(node.isBinary()){
			value = node.binaryValue();
		}else if(node.isNull()){
			value = null;
		}else{
			value = Types.convertValue(node.asText(), (Class<?>)valueClass);
		}
		return value;
	}
}
