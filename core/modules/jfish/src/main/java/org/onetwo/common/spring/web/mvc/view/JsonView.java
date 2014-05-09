package org.onetwo.common.spring.web.mvc.view;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.mvc.JFishFirstInterceptor;
import org.onetwo.common.spring.web.mvc.SingleReturnWrapper;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.Predicate;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView extends MappingJackson2JsonView {

	public static final String FILTER_KEYS = ":filterKeys";
	public static final String JSON_DATAS = ":jsonDatas";
	
	public JsonView(){
		this.configJson();
	}
	
	protected void configJson(){
		this.setContentType("application/json;charset=utf-8");
		setExtractValueFromSingleKeyModel(true);
		ObjectMapper mapper = JsonMapper.IGNORE_NULL.getObjectMapper();
		Module module = SpringApplication.getInstance().getBean(Module.class);
//		h4m.disable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
//		h4m.enable(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
		if(module!=null)
			mapper.registerModule(module);
		this.setObjectMapper(mapper);
	}

	protected Object filterModel(Map<String, Object> model) {
		model.remove(JFishFirstInterceptor.NOW_KEY);
		
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
			SingleReturnWrapper singleModelWrapper = null;
			String key = null;
			for(Map.Entry<String, Object> entry : model.entrySet()){
				if(SingleReturnWrapper.class.isInstance(entry.getValue())){
					singleModelWrapper = (SingleReturnWrapper) entry.getValue();
					key = entry.getKey();
					break;
				}
			}
			if(singleModelWrapper!=null){
				model.clear();
				model.put(key, singleModelWrapper.getValue());
			}else{
	//			model.remove(UrlHelper.MODEL_KEY);
			}
		}
		return super.filterModel(model);
	}
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}
}
