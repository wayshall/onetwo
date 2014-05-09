package org.onetwo.plugins.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.spring.web.mvc.view.JsonView;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.rest.log.RestLogFactory;
import org.onetwo.plugins.rest.log.RestLogInfo;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;

public class RestJsonView extends JsonView {
	
	public static final String REST_RESULT_KEY = "restResult";

	protected final Logger logger = RestLogFactory.getLogger();
	private MessageSource exceptionMessages;
	
	public RestJsonView(){
		super();
	} 

	@Override
	protected void configJson(){
		super.configJson();
		setExtractValueFromSingleKeyModel(true);
	}
	

	protected String getMessage(String code, Object[] args) throws NoSuchMessageException{
//		return this.exceptionMessages.getMessage(code, args, JFishWebUtils.getLocale());
		try {
			return this.exceptionMessages.getMessage(code, args, JFishWebUtils.DEFAULT_LOCAL);
		} catch (Exception e) {
			logger.error("getMessage error :" + e.getMessage());
		}
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	protected Object filterModel(Map<String, Object> model) {
		SingleReturnWrapper singleModelWrapper = null;
		String key = null;
		for(Map.Entry<String, Object> entry : model.entrySet()){
			if(SingleReturnWrapper.class.isInstance(entry.getValue())){
				singleModelWrapper = (SingleReturnWrapper) entry.getValue();
				key = entry.getKey();
				break;
			}else if(RestResult.class.isInstance(entry.getValue())){
				RestResult<?> restResult = (RestResult<?>) entry.getValue();
				if(restResult.isFailed() && StringUtils.isBlank(restResult.getRet_msg())){
					String emsg = this.getMessage(restResult.getError_code(), restResult.getErrorArgs());
					restResult.setRet_msg(emsg);
				}
				singleModelWrapper = SingleReturnWrapper.wrap(restResult);
				break;
			}
		}
		if(singleModelWrapper!=null){
			model.clear();
			model.put(key, singleModelWrapper.getValue());
		}else{
			RestResult<Map<String, Object>> rest = new RestResult<Map<String, Object>>();
			rest.markSucceed();
			Map<String, Object> data = new HashMap<String, Object>();

			for(Map.Entry<String, Object> entry : model.entrySet()){
				if(BindingResult.class.isInstance(entry.getValue())){
					continue;
				}else{
					data.put(entry.getKey(), entry.getValue());
				}
			}
			rest.setData(data);

//			singleModelWrapper = SingleReturnWrapper.wrap(rest);
			
			model.clear();
			model.put(REST_RESULT_KEY, rest);
		}
		Object result = super.delegateSpringFilterModel(model);
		this.doLog(result);
		return result;
	}
	
	protected void doLog(Object result){
		WebHelper helper = JFishWebUtils.webHelper();
		if(helper==null){
			logger.error("no helper");
			return ;
		}
		RestLogInfo info = new RestLogInfo();
		info.setUrl(helper.getRequestURI());
		info.setParams(helper.getRequest().getParameterMap());
		info.setResult(result);
		logger.info(JsonMapper.IGNORE_NULL.toJson(info));
	}

	public MessageSource getExceptionMessages() {
		return exceptionMessages;
	}

	public void setExceptionMessages(MessageSource exceptionMessages) {
		this.exceptionMessages = exceptionMessages;
	}

}
