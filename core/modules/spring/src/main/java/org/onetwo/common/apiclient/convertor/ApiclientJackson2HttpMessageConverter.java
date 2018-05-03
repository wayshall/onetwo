package org.onetwo.common.apiclient.convertor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.google.common.collect.Sets;

/**
 * @author wayshall
 * <br/>
 */
public class ApiclientJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	
	private LoadingCache<Type, List<MediaType>> meidaTypeCaches = CacheBuilder.newBuilder()
//																			.weakValues()
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

	private Set<MediaType> supportedMediaTypeSet = Sets.newHashSet();
	
	public ApiclientJackson2HttpMessageConverter() {
		super(JsonMapper.IGNORE_NULL.getObjectMapper());
		/*setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, 
														MediaType.APPLICATION_JSON_UTF8,
														MediaType.TEXT_PLAIN));*/
	}

	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		super.setSupportedMediaTypes(supportedMediaTypes);
		supportedMediaTypeSet = new HashSet<MediaType>(supportedMediaTypes);
	}



	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return this.canRead(clazz, null, mediaType);
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		if (mediaType != null) {
			List<MediaType> mediaTypes = getMediaTypes(type);
			if(LangUtils.isNotEmpty(mediaTypes)){
				return mediaTypes.contains(mediaType);
			}
		}
		return super.canRead(type, contextClass, mediaType);
	}
	
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if(mediaType!=null){
			List<MediaType> mediaTypes = getMediaTypes(clazz);
			if(LangUtils.isNotEmpty(mediaTypes)){
				return mediaTypes.contains(mediaType);
			}
		}
		return super.canWrite(clazz, mediaType);
	}

	
	protected List<MediaType> getMediaTypes(Type clazz){
		try {
			List<MediaType> mediaTypes = meidaTypeCaches.get(clazz);
			addMediaTypeIfNotExists(mediaTypes);
		} catch (ExecutionException e) {
			//ignore
		}
		return Collections.emptyList();
	}
	
	private void addMediaTypeIfNotExists(List<MediaType> mediaTypes){
		if(mediaTypes==null || mediaTypes.isEmpty()){
			return ;
		}
		for(MediaType mediaType : mediaTypes){
			if(!supportedMediaTypeSet.contains(mediaType)){
				supportedMediaTypeSet.add(mediaType);
				super.setSupportedMediaTypes(new ArrayList<MediaType>(supportedMediaTypeSet));
			}
		}
	}
}
