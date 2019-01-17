package org.onetwo.common.apiclient;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.function.BiFunction;

import org.onetwo.common.apiclient.resouce.FileNameByteArrayResource;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.converter.ValueEnum;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wayshall
 * <br/>
 */
public class ValueConvertor implements BiFunction<PropertyDescriptor, Object, Object> {
	@Override
	public Object apply(PropertyDescriptor prop, Object v) {
		if(v instanceof Enum){
			Enum<?> e = (Enum<?>)v;
			if(e instanceof ValueEnum){
				v = ((ValueEnum<?>)e).getValue();
			}else{//默认使用name
				v = e.name();
			}
		}else if(v instanceof ClassPathResource){
			ClassPathResource res = (ClassPathResource)v;
	        try {
	        	final String fileName = res.getFilename();
				ByteArrayResource byteArrayResource = new FileNameByteArrayResource(fileName, FileUtils.toByteArray(res.getInputStream()));
				v = byteArrayResource;
			} catch (IOException e) {
				throw new BaseException("convert file error: " + e.getMessage(), e);
			}
		}else if(v instanceof Resource || 
				v instanceof byte[] ||
				v instanceof ClassLoader){
			//ignore，忽略，不转为string
		}else if(v instanceof MultipartFile){
			MultipartFile mf = (MultipartFile)v;
			try{
				FileNameByteArrayResource res = new FileNameByteArrayResource(mf.getOriginalFilename(), FileUtils.toByteArray(mf.getInputStream()));
				v = res;
			}catch(Exception e){
				throw new BaseException("convert file error: " + e.getMessage(), e);
			}
		}else if(v instanceof ApiArgumentTransformer){
			v = ((ApiArgumentTransformer)v).asApiValue();
		}/*else if(v instanceof Date){
			Date d = (Date) v;
			prop
			v = d.getTime();
		}*/else{
			v = v==null?v:v.toString();
		}
		return v;
	}
	
}
