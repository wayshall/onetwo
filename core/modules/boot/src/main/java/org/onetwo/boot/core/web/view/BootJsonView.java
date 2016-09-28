package org.onetwo.boot.core.web.view;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.ModelAttr;
import org.onetwo.common.data.Result;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.mvc.utils.DataWrapper;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator.MapResultBuilder;
import org.onetwo.common.web.utils.Browsers.BrowserMeta;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.common.collect.Lists;

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
		/*ObjectMapper mapper = SpringUtils.getBean(applicationContext, ObjectMapper.class);
		if(mapper!=null){
			this.setObjectMapper(mapper);
		}else{
			mapper = BootWebUtils.createObjectMapper(applicationContext);
			this.setObjectMapper(mapper);
		}*/
		this.setObjectMapper(JsonMapper.ignoreNull().getObjectMapper());
	}
	
	@Override
	protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {
		BrowserMeta meta = RequestUtils.getBrowerMetaByAgent(request);
		if(meta.isFuckingBrowser()){
			String contextType = ResponseUtils.HTML_TYPE;
			response.setContentType(contextType);
		}else{
			super.setResponseContentType(request, response);
		}
	}
	
	protected Object filterModel(Map<String, Object> model) {
		model.remove(BootFirstInterceptor.NOW_KEY);
		
		Object result = null;
		List<Entry<String, Object>> entryList = Lists.newArrayList(model.entrySet());
		for(Map.Entry<String, Object> entry : entryList){
			if(Result.class.isInstance(entry.getValue())){
				result = entry.getValue();
				processData(result);
				return result;
			}else if(DataWrapper.class.isInstance(entry.getValue())){
				result = ((DataWrapper)entry.getValue()).getValue();
				processData(result);
				return result;
			}
			
			if(BindingResult.class.isInstance(entry.getValue()) ||
					UserDetails.class.isInstance(entry.getValue())){
				model.remove(entry.getKey());
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
		}
		if(Map.class.isInstance(result)){
			@SuppressWarnings("unchecked")
			MapResultBuilder dataResult = createMapResultBuilder((Map<String, Object>) result);
			return dataResult.buildResult();
		}else{
			SimpleDataResult<Object> dataResult = SimpleDataResult.success("", result);
			return dataResult;
		}
	}
	
	private MapResultBuilder createMapResultBuilder(Map<String, Object> map){
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
		return dataResult;
	}
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}


}
