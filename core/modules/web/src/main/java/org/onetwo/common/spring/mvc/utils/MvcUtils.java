package org.onetwo.common.spring.mvc.utils;

import java.util.Map;
import java.util.Optional;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.web.servlet.ModelAndView;

public class MvcUtils {
	
	
	public static ModelAndView createModelAndView(String viewName, Object... models){
		ModelAndView mv = new ModelAndView(viewName);
//		mv.getModel().put(UrlHelper.MODEL_KEY, getUrlMeta());
		if(LangUtils.isEmpty(models)){
			return mv;
		}
		
		if(models.length==1){
			if(Map.class.isInstance(models[0])){
				mv.addAllObjects((Map<String, ?>)models[0]);
				
			}else if(DataWrapper.class.isInstance(models[0])){
//				mv.addObject(models[0]);
				mv.addObject(DataWrapper.DEFAULT_NAME, models[0]);
				
			}else if(ModelAttr.class.isInstance(models[0])){
				ModelAttr attr = (ModelAttr) models[0];
				mv.addObject(attr.getName(), attr.getValue());
				
			}else if(Optional.class.isInstance(models[0])){
				Optional<?> attr = (Optional<?>) models[0];
				if(attr.isPresent()){
					mv.addObject(attr.get());
				}
			}else if(models[0]!=null){
				mv.addObject(models[0]);
//				mv.addObject(JsonWrapper.wrap(models[0]));
//				mv.addObject(SINGLE_MODEL_FLAG_KEY, true);
			}
		}else{
			/*Map<String, ?> modelMap = LangUtils.asMap(models);
			mv.addAllObjects(modelMap);*/
			
			for (int i = 0; i < models.length; i++) {
				if(DataWrapper.class.isInstance(models[i])){
//					mv.addObject(models[i]);
					mv.addObject(DataWrapper.DEFAULT_NAME, models[i]);
					
				}else if(ModelAttr.class.isInstance(models[i])){
					ModelAttr attr = (ModelAttr) models[i];
					mv.addObject(attr.getName(), attr.getValue());
					
				}else{
					Object name = models[i];
					if(!String.class.isInstance(name)){
						throw new BaseException("model key must be a string, but is : " + name);
					}
					i++;
					if(i>=models.length){
						throw new BaseException("no value for model key : " + name);
					}
					mv.addObject(name.toString(), models[i]);
				}
			}
		}
		return mv;
	}


	private MvcUtils(){}
}
