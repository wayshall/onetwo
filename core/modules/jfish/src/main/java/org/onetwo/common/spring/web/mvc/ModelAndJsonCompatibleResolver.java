package org.onetwo.common.spring.web.mvc;

import java.util.List;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

public class ModelAndJsonCompatibleResolver extends RequestResponseBodyMethodProcessor {

	private ServletModelAttributeMethodProcessor modelProcessor = new ServletModelAttributeMethodProcessor(true);
	
	public ModelAndJsonCompatibleResolver(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return modelProcessor.supportsParameter(parameter);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		boolean needResolve = webRequest.getParameterMap().isEmpty() && "json".equalsIgnoreCase(JFishWebUtils.requestExtension());
		Object result = null;
		if(needResolve){
			result = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
		}else{
			result = modelProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
		}
		return result;
	}

/*
	public static class JsonStringHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

		public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

		private final List<Charset> availableCharsets;

		private boolean writeAcceptCharset = true;

		//application/json
		public JsonStringHttpMessageConverter() {
			super(new MediaType("application", "json", DEFAULT_CHARSET), MediaType.ALL);
			this.availableCharsets = new ArrayList<Charset>(Charset.availableCharsets().values());
		}

		public void setWriteAcceptCharset(boolean writeAcceptCharset) {
			this.writeAcceptCharset = writeAcceptCharset;
		}

		@Override
		public boolean supports(Class<?> clazz) {
			return JsonMapper.DEFAULT_MAPPER.getObjectMapper().canSerialize(clazz);
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected Object readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException {
			Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
			String content = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
			if(String.class==clazz){
				return content;
			}else{
				Object obj = null;
				obj = JsonMapper.DEFAULT_MAPPER.fromJson(content, clazz);
				return obj;
			}
		}
		
		private String getObjectString(Object s){
			if(String.class.isInstance(s)){
				return (String)s;
			}else{
				String str = JsonMapper.DEFAULT_MAPPER.toJson(s);
				return str;
			}
		}

		@Override
		protected Long getContentLength(Object s, MediaType contentType) {
			Charset charset = getContentTypeCharset(contentType);
			try {
				return (long) getObjectString(s).getBytes(charset.name()).length;
			}
			catch (UnsupportedEncodingException ex) {
				// should not occur
				throw new InternalError(ex.getMessage());
			}
		}

		@Override
		protected void writeInternal(Object s, HttpOutputMessage outputMessage) throws IOException {
			if (writeAcceptCharset) {
				outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
			}
			Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
			FileCopyUtils.copy(getObjectString(s), new OutputStreamWriter(outputMessage.getBody(), charset));
		}

		protected List<Charset> getAcceptedCharsets() {
			return this.availableCharsets;
		}

		private Charset getContentTypeCharset(MediaType contentType) {
			if (contentType != null && contentType.getCharSet() != null) {
				return contentType.getCharSet();
			}
			else {
				return DEFAULT_CHARSET;
			}
		}
	}*/
}
