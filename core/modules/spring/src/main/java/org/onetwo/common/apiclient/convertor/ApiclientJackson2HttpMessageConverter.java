package org.onetwo.common.apiclient.convertor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.onetwo.common.apiclient.annotation.SupportedMediaType;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author wayshall
 * <br/>
 */
public class ApiclientJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	
	private LoadingCache<Class<?>, List<MediaType>> meidaTypeCaches = CacheBuilder.newBuilder()
//																			.weakValues()
																			.build(new CacheLoader<Class<?>, List<MediaType>>() {
																				@Override
																				public List<MediaType> load(Class<?> clazz) throws Exception {
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

	
	public ApiclientJackson2HttpMessageConverter() {
		super(JsonMapper.IGNORE_NULL.getObjectMapper());
		setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, 
														MediaType.APPLICATION_JSON_UTF8,
														MediaType.TEXT_PLAIN));
	}



	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		List<MediaType> mediaTypes = getMediaTypes(clazz);
		if(LangUtils.isNotEmpty(mediaTypes)){
			return mediaTypes.contains(mediaType);
		}
		return super.canWrite(clazz, mediaType);
	}


	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		List<MediaType> mediaTypes = getMediaTypes(clazz);
		if(LangUtils.isNotEmpty(mediaTypes)){
			return mediaTypes.contains(mediaType);
		}
		return super.canRead(clazz, mediaType);
	}
	
	protected List<MediaType> getMediaTypes(Class<?> clazz){
		try {
			return meidaTypeCaches.get(clazz);
		} catch (ExecutionException e) {
			//ignore
		}
		return Collections.emptyList();
	}
}
