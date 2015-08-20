package org.onetwo.boot.core.web.view;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.utils.ModelAttr;
import org.onetwo.common.result.DataResult;
import org.onetwo.common.result.Result;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.DataWrapper;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BootJsonView extends MappingJackson2JsonView {
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";

	public static final String FILTER_KEYS = ":filterKeys";
	public static final String JSON_DATAS = ":jsonDatas";
	
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
		
		if(model.containsKey(JSON_DATAS)){
			Object datas = model.get(JSON_DATAS);
			model.clear();
			model.put(JSON_DATAS, datas);
		}else if(model.containsKey(FILTER_KEYS)){
//			Iterator<Map.Entry<String, Object>> it = model.entrySet().iterator();
			final List<String> filterKeys = LangUtils.asList(model.get(FILTER_KEYS));
			CUtils.filter(model, new Predicate<Entry<String, Object>>() {

				@Override
				public boolean apply(Entry<String, Object> entry) {
					return !filterKeys.contains(entry.getKey());
				}
				
			});
		}else{
			for(Map.Entry<String, Object> entry : model.entrySet()){
				if(Result.class.isInstance(entry.getValue())){
					return entry.getValue();
				}else if(DataWrapper.class.isInstance(entry.getValue())){
					return ((DataWrapper)entry.getValue()).getValue();
				}
			}
		}

//		setExtractValueFromSingleKeyModel(false);
//		filterModelByCallback(model);
		Object result = super.filterModel(model);
		
		return wrapAsDataResultIfNeed(result);
	}
	
	
	private Object wrapAsDataResultIfNeed(Object result){
		if(!wrapModelAsDataResult)
			return result;
		
		if(Map.class.isInstance(result)){
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
			return result;
		}
	}
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}
}
