package org.onetwo.boot.core.web.view;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.utils.ModelAttr;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.MapResult;
import org.onetwo.common.result.Result;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.DataWrapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BootJsonView extends MappingJackson2JsonView implements InitializingBean {
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";

	/*public static final String FILTER_KEYS = ":filterKeys";
	public static final String JSON_DATAS = ":jsonDatas";*/
	
	private static final Logger logger = JFishLoggerFactory.getLogger(BootJsonView.class);
	
	private boolean wrapModelAsDataResult = true;
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	public BootJsonView(){
//		this.configJson();
	}
	
	
	public void setWrapModelAsDataResult(boolean wrapModelAsDataResult) {
		this.wrapModelAsDataResult = wrapModelAsDataResult;
	}

	public void afterPropertiesSet() throws Exception {
		this.setContentType(CONTENT_TYPE);
//		setExtractValueFromSingleKeyModel(true);
		ObjectMapper mapper = SpringUtils.getBean(applicationContext, ObjectMapper.class);
		if(mapper!=null){
			this.setObjectMapper(mapper);
		}else{
			mapper = BootWebUtils.createObjectMapper(applicationContext);
			this.setObjectMapper(mapper);
		}
		
	}

	protected Object filterModel(Map<String, Object> model) {
		model.remove(BootFirstInterceptor.NOW_KEY);
		
		Object result = null;
		for(Map.Entry<String, Object> entry : model.entrySet()){
			if(Result.class.isInstance(entry.getValue())){
				result = entry.getValue();
				logJsonData(result);
				return result;
			}else if(DataWrapper.class.isInstance(entry.getValue())){
				result = ((DataWrapper)entry.getValue()).getValue();
				logJsonData(result);
				return result;
			}
		}

//		setExtractValueFromSingleKeyModel(false);
//		filterModelByCallback(model);
		if(result==null){
			result = super.filterModel(model);
		}

		if(wrapModelAsDataResult)
			result = wrapAsDataResultIfNeed(result);
		return logJsonData(result);
	}
	
	private Object logJsonData(Object data){
		try {
			String json = getObjectMapper().writeValueAsString(data);
			logger.info("json ---> {}", json);
		} catch (JsonProcessingException e) {
			logger.warn("log json error: " + data, e);
		}
		return data;
	}
	
	private Object wrapAsDataResultIfNeed(Object result){
		if(Result.class.isInstance(result)){
			return result;
		}else if(Map.class.isInstance(result)){
			Map<String, Object> map = (Map<String, Object>) result;
			MapResult dataResult = MapResult.createSucceed("");
			for(Entry<String, Object> entry : map.entrySet()){
				if(BindingResult.class.isInstance(entry.getValue())){
					BindingResult br = (BindingResult) entry.getValue();
					if(br.hasErrors()){
						dataResult.setCode(MapResult.FAILED);
					}
					
				}else if(ModelAttr.MESSAGE.equalsIgnoreCase(entry.getKey())){
					dataResult.setCode(MapResult.SUCCEED);
					dataResult.setMessage(entry.getValue().toString());
					
				}else if(ModelAttr.ERROR_MESSAGE.equalsIgnoreCase(entry.getKey())){
					dataResult.setCode(MapResult.FAILED);
					dataResult.setMessage(entry.getValue().toString());
					
				}else{
					dataResult.putData(entry.getKey(), entry.getValue());
				}
			}
			return dataResult;
		}else{
			SimpleDataResult<Object> dataResult = SimpleDataResult.createSucceed("", result);
			return dataResult;
		}
	}
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}


}
