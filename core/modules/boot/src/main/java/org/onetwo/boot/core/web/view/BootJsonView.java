package org.onetwo.boot.core.web.view;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.utils.ModelAttr;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.DataResult;
import org.onetwo.common.result.Result;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.DataWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BootJsonView extends MappingJackson2JsonView {
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";

	/*public static final String FILTER_KEYS = ":filterKeys";
	public static final String JSON_DATAS = ":jsonDatas";*/
	
	private boolean wrapModelAsDataResult = true;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public BootJsonView(){
//		this.configJson();
	}
	
	
	public void setWrapModelAsDataResult(boolean wrapModelAsDataResult) {
		this.wrapModelAsDataResult = wrapModelAsDataResult;
	}

	@PostConstruct
	public void initJsonConfig(){
		this.setContentType(CONTENT_TYPE);
//		setExtractValueFromSingleKeyModel(true);
		ObjectMapper mapper = SpringUtils.getBean(applicationContext, ObjectMapper.class);
		if(mapper!=null){
			this.setObjectMapper(mapper);
			return ;
		}
		
		mapper = BootWebUtils.createObjectMapper(applicationContext);
		this.setObjectMapper(mapper);
	}

	protected Object filterModel(Map<String, Object> model) {
		model.remove(BootFirstInterceptor.NOW_KEY);
		
		Object result = null;
		for(Map.Entry<String, Object> entry : model.entrySet()){
			if(Result.class.isInstance(entry.getValue())){
				result = entry.getValue();
				return result;
			}else if(DataWrapper.class.isInstance(entry.getValue())){
				result = ((DataWrapper)entry.getValue()).getValue();
				return result;
			}
		}

//		setExtractValueFromSingleKeyModel(false);
//		filterModelByCallback(model);
		if(result==null){
			result = super.filterModel(model);
		}

		if(wrapModelAsDataResult)
			return wrapAsDataResultIfNeed(result);
		return result;
	}
	
	
	private Object wrapAsDataResultIfNeed(Object result){
		if(Result.class.isInstance(result)){
			return result;
		}else if(Map.class.isInstance(result)){
			Map<String, Object> map = (Map<String, Object>) result;
			DataResult dataResult = DataResult.createSucceed("");
			for(Entry<String, Object> entry : map.entrySet()){
				if(BindingResult.class.isInstance(entry.getValue())){
					BindingResult br = (BindingResult) entry.getValue();
					if(br.hasErrors()){
						dataResult.setCode(DataResult.FAILED);
					}
					
				}else if(ModelAttr.MESSAGE.equalsIgnoreCase(entry.getKey())){
					dataResult.setCode(DataResult.SUCCEED);
					dataResult.setMessage(entry.getValue().toString());
					
				}else if(ModelAttr.ERROR_MESSAGE.equalsIgnoreCase(entry.getKey())){
					dataResult.setCode(DataResult.FAILED);
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
