package org.onetwo.boot.core.web.view;

import java.io.IOException;
import java.lang.reflect.Type;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.data.DataResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	protected BootJFishConfig bootJfishConfig;
	
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (!bootJfishConfig.getMvc().getJson().isAutoWrapResult() || object instanceof MappingJacksonValue) {
			super.writeInternal(object, type, outputMessage);
			return ;
		}
		Object data = dataResultWrapper.wrapResult(object);
		super.writeInternal(data, type, outputMessage);
	}

}
