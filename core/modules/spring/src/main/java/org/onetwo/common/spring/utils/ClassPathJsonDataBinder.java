package org.onetwo.common.spring.utils;

import java.io.IOException;

import org.onetwo.common.jackson.JsonDataBinder;
import org.springframework.core.io.ClassPathResource;

/**
 * @author wayshall
 * <br/>
 */
public class ClassPathJsonDataBinder<T> extends JsonDataBinder<T> {
	
	public static <E> E from(Class<E> dataType, String dataFilePath){
		ClassPathJsonDataBinder<E> binder = new ClassPathJsonDataBinder<>(dataType, dataFilePath);
		return binder.buildData();
	}
	
	public ClassPathJsonDataBinder(Class<T> dataType, String dataFilePath) {
		super(dataType, dataFilePath);
	}

	public T buildData(){
		ClassPathResource res = new ClassPathResource(dataFilePath);
		T data;
		try {
			data = jsonMapper.fromJson(res.getInputStream(), dataType);
		} catch (IOException e) {
			throw new RuntimeException("build data error:"+e.getMessage(), e);
		}
		return data;
	}
}
