package org.onetwo.common.apiclient.convertor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.google.common.collect.Sets;

/**
 * @author wayshall
 * <br/>
 */
public class ApiclientJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	private SupportedMediaTypeExtractor supportedMediaTypes = new SupportedMediaTypeExtractor();

	private Set<MediaType> supportedMediaTypeSet = Sets.newHashSet();
	
	public ApiclientJackson2HttpMessageConverter() {
		super(JsonMapper.ignoreNull().getObjectMapper());
		setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, 
														MediaType.APPLICATION_JSON_UTF8,
														MediaType.TEXT_PLAIN,//for wechat api
														MediaType.TEXT_HTML));
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
		List<MediaType> produces = this.supportedMediaTypes.getProduceMediaTypes();
		if (!produces.isEmpty()) {
			return CollectionUtils.containsAny(produces, super.getSupportedMediaTypes());
		}
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
		List<MediaType> consumes = this.supportedMediaTypes.getConsumeMediaTypes();
		if (!consumes.isEmpty()) {
			return CollectionUtils.containsAny(consumes, super.getSupportedMediaTypes());
		}
		
		if(mediaType!=null){
			List<MediaType> mediaTypes = getMediaTypes(clazz);
			if(LangUtils.isNotEmpty(mediaTypes)){
				return mediaTypes.contains(mediaType);
			}
		}
		return super.canWrite(clazz, mediaType);
	}

	protected List<MediaType> getMediaTypes(Type clazz){
		List<MediaType> mediaTypes = supportedMediaTypes.getMediaTypes(clazz);
		addMediaTypeIfNotExists(mediaTypes);
		return mediaTypes;
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
