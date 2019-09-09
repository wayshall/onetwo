package org.onetwo.common.apiclient.convertor;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.onetwo.common.apiclient.RequestContextData;
import org.onetwo.common.apiclient.annotation.SupportedMediaType;
import org.onetwo.common.spring.rest.RestExecuteThreadLocal;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author weishao zeng
 * <br/>
 */
public class SupportedMediaTypeExtractor {
	private LoadingCache<Type, List<MediaType>> mediaTypeCaches = CacheBuilder.newBuilder()
//			.weakValues()
			.build(new CacheLoader<Type, List<MediaType>>() {
				@Override
				public List<MediaType> load(Type type) throws Exception {
					if(!Class.class.isInstance(type)){
						return Collections.emptyList();
					}
					Class<?> clazz = (Class<?>) type;
					List<MediaType> mediaTypes = Collections.emptyList();
					SupportedMediaType mediaTypeAnnotation = AnnotatedElementUtils.getMergedAnnotation(clazz, SupportedMediaType.class);
					if(mediaTypeAnnotation!=null){
						String[] mediaTypeStrs = mediaTypeAnnotation.value();
						mediaTypeStrs = mediaTypeStrs==null?LangUtils.EMPTY_STRING_ARRAY:mediaTypeStrs;
						mediaTypes = MediaType.parseMediaTypes(Arrays.asList(mediaTypeStrs));
					}
					return mediaTypes;
				}
			});
	
	public List<MediaType> getProduceMediaTypes() {
		RequestContextData ctx = RestExecuteThreadLocal.get();
		if (ctx==null) {
			return Collections.emptyList();
		}
		return ctx.getInvokeMethod().getProduceMediaTypes();
	}
	
	public List<MediaType> getConsumeMediaTypes() {
		RequestContextData ctx = RestExecuteThreadLocal.get();
		if (ctx==null) {
			return Collections.emptyList();
		}
		return ctx.getInvokeMethod().getConsumeMediaTypes();
	}
	
	
	public List<MediaType> getMediaTypes(Type clazz) {
		try {
			return mediaTypeCaches.get(clazz);
		} catch (ExecutionException e) {
			// ignore
			return Collections.emptyList();
		}
	}
}

