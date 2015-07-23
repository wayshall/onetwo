package org.onetwo.boot.core.web.view;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.ModelAttr;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.mvc.DataWrapper;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.DataResult;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.Result;
import org.onetwo.common.utils.list.Predicate;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BootJsonView extends MappingJackson2JsonView {
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";

	public static final String FILTER_KEYS = ":filterKeys";
	public static final String JSON_DATAS = ":jsonDatas";
	
	private boolean wrapModelAsDataResult = true;
	
	public BootJsonView(){
//		this.configJson();
	}
	
	
	public void setWrapModelAsDataResult(boolean wrapModelAsDataResult) {
		this.wrapModelAsDataResult = wrapModelAsDataResult;
	}

	@PostConstruct
	final public void initJsonConfig(){
		this.setContentType(CONTENT_TYPE);
//		setExtractValueFromSingleKeyModel(true);
		ObjectMapper mapper = JsonMapper.IGNORE_NULL.getObjectMapper();
		Module module = SpringApplication.getInstance().getBean(Module.class);
//		h4m.disable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
		String clsName = "com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module";
		if(ClassUtils.isPresent(clsName, ClassUtils.getDefaultClassLoader())){
			Object h4m = ReflectUtils.newInstance(clsName);
			
			Class<?> featureCls = ReflectUtils.loadClass(clsName+"$Feature");
			Object field = ReflectUtils.getStaticFieldValue(featureCls, "SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS");
			ReflectUtils.invokeMethod("enable", h4m, field);

			field = ReflectUtils.getStaticFieldValue(featureCls, "FORCE_LAZY_LOADING");
			ReflectUtils.invokeMethod("disable", h4m, field);
		}
		if(module!=null)
			mapper.registerModule(module);
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
