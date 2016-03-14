package org.onetwo.boot.core.web.view;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.utils.ModelAttr;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.Result;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.utils.DataWrapper;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator.MapResultBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BootJsonView extends MappingJackson2JsonView implements InitializingBean {
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";

	/*public static final String FILTER_KEYS = ":filterKeys";
	public static final String JSON_DATAS = ":jsonDatas";*/
	
//	private static final Logger logger = JFishLoggerFactory.getLogger(BootJsonView.class);
	
	private boolean wrapModelAsDataResult = true;
	
	@Autowired
	private ApplicationContext applicationContext;
//	@Autowired
//	private BootJFishConfig bootJFishConfig;
	
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
				processData(result);
				return result;
			}else if(DataWrapper.class.isInstance(entry.getValue())){
				result = ((DataWrapper)entry.getValue()).getValue();
				processData(result);
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
		return processData(result);
	}
	
	private Object processData(Object data){
		/*try {
			String json = getObjectMapper().writeValueAsString(data);
			logger.info("json ---> {}", json);
		} catch (JsonProcessingException e) {
			logger.warn("log json error: " + data, e);
		}*/
		return data;
	}
	
	private Result<?, ?> wrapAsDataResultIfNeed(Object result){
		if(Result.class.isInstance(result)){
			return (Result<?, ?>)result;
		}else if(Map.class.isInstance(result)){
			Map<String, Object> map = (Map<String, Object>) result;
			MapResultBuilder dataResult = WebResultCreator.creator().map().success();
			for(Entry<String, Object> entry : map.entrySet()){
				if(BindingResult.class.isInstance(entry.getValue())){
					BindingResult br = (BindingResult) entry.getValue();
					if(br.hasErrors()){
						dataResult.error();
					}
					
				}else if(ModelAttr.MESSAGE.equalsIgnoreCase(entry.getKey())){
					dataResult.success(entry.getValue().toString());
					
				}else if(ModelAttr.ERROR_MESSAGE.equalsIgnoreCase(entry.getKey())){
					dataResult.error(entry.getValue().toString());
					
				}else{
					dataResult.put(entry.getKey(), entry.getValue());
				}
			}
			return dataResult.buildResult();
		}else{
			SimpleDataResult<Object> dataResult = SimpleDataResult.success("", result);
			return dataResult;
		}
	}
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}


}
