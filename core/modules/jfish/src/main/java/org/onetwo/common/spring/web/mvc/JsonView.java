package org.onetwo.common.spring.web.mvc;

import java.util.Map;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class JsonView extends MappingJacksonJsonView {
	
	public JsonView(){
		this.configJson();
	}
	
	protected void configJson(){
		this.setContentType("application/json;charset=utf-8");
		setExtractValueFromSingleKeyModel(true);
		this.setObjectMapper(JsonMapper.IGNORE_NULL.getObjectMapper());
	}

	protected Object filterModel(Map<String, Object> model) {
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
		return super.filterModel(model);
	}
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}
}
