package org.onetwo.boot.core.web.view;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.onetwo.boot.core.config.BootJFishConfig.MvcConfig.AutoWrapResultConfig;
import org.onetwo.common.data.DataResultWrapper;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

/**
 * @author wayshall
 * <br/>
 */
public class AutoWrapJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	private DataResultWrapper dataResultWrapper = BootJsonView.DATA_RESULT_WRAPPER;

	final private AutoWrapResultConfig config;

	public AutoWrapJackson2HttpMessageConverter(AutoWrapResultConfig config) {
		super(JsonMapper.ignoreNull().getObjectMapper());
		this.config = config;
	}
	
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (!config.isEnable() || object instanceof MappingJacksonValue) {
			super.writeInternal(object, type, outputMessage);
			return ;
		}
		List<String> includePackages = config.getPackages();
		if(LangUtils.isEmpty(includePackages)){
			super.writeInternal(object, type, outputMessage);
			return ;
		}
		String elementPackage = LangUtils.getFirstOptional(object).map(o->o.getClass().getPackage().getName()).orElse("").toString();
		boolean matchPackage = includePackages.stream().anyMatch(p->elementPackage.startsWith(p));
		if(!matchPackage){
			super.writeInternal(object, type, outputMessage);
			return ;
		}
		Object data = dataResultWrapper.wrapResult(object);
		super.writeInternal(data, type, outputMessage);
	}

}
