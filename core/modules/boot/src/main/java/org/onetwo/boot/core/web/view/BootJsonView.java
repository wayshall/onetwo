package org.onetwo.boot.core.web.view;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.utils.BootWebHelper;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.data.DataResultWrapper;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.mvc.utils.DataWrapper;
import org.onetwo.common.web.utils.Browsers.BrowserMeta;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.common.collect.Lists;

/****
 * 统一处理json对象
 * DataWrapper类型的对象获取value直接返回，其它包装成Result类型对象
 * @author way
 *
 */
public class BootJsonView extends MappingJackson2JsonView implements InitializingBean {

	public static final DataResultWrapper DATA_RESULT_WRAPPER = new DefaultDataResultWrapper();
	
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";
	
//	protected final Logger log = JFishLoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext applicationContext;
//	@Autowired
//	private BootJFishConfig bootJFishConfig;
	
	@Autowired(required=false)
	private XResponseViewManager xresponseViewManager;

	
	public BootJsonView(){
//		this.configJson();
		JsonMapper jsonMapper = JsonMapper.ignoreNull();
		this.setObjectMapper(jsonMapper.getObjectMapper());
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
	
	/*****
	 * DataWrapper类型的对象获取value直接返回，其它包装成Result类型对象
	 * spring新版本加了MappingJacksonValue对象，是否统一处理掉它？
	 */
	protected Object filterModel(Map<String, Object> model) {
		model.remove(BootFirstInterceptor.NOW_KEY);
		
		//先根据header获取特定的DataWrapper返回的数据
		Optional<Object> data = getResponseViewFromModel(model);
		if(data.isPresent()){//因为这里是已经经过header查找到的DataWrapper类型的view，所以直接返回
			return data.get();
		}

		Object result = null;
		
		List<Entry<String, Object>> entryList = Lists.newArrayList(model.entrySet());
		for(Map.Entry<String, Object> entry : entryList){
			if(DataResult.class.isInstance(entry.getValue())){
				result = entry.getValue();
//				result = processData(result); //Result 特殊类型无需另外处理
				return result;
			}else if(DataWrapper.class.isInstance(entry.getValue())){
//				result = entry.getValue();
				//如果不是默认的DataWrapper，则忽略
				if(!DataWrapper.DEFAULT_NAME.equals(entry.getKey())){
					continue;
				}
				result = ((DataWrapper)entry.getValue()).getValue();
//				result = processData(result, model);
				//默认的DataWrapper也需要查找是否有response view指定的包装器处理
				result = getResponseViewFromAnnotation(result);
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

		result = processData(result, model);
		return result;
	}
	
	protected Object processData(Object data, Map<String, Object> model){
		return getResponseViewFromAnnotation(data);
	}
	
	/****
	 * 根据header获取view，view必须是DataWrapper类型
	 * @author wayshall
	 * @param model
	 * @return
	 */
	protected Optional<Object> getResponseViewFromModel(Map<String, Object> model){
		return xresponseViewManager.getResponseViewFromModel(model);
	}
	
	/***
	 * @author wayshall
	 * @param data
	 * @return
	 */
	protected Object getResponseViewFromAnnotation(final Object data){
		BootWebHelper helper = BootWebUtils.webHelper();
		if(helper==null || helper.getControllerHandler()==null){
			return data;
		}
		HandlerMethod hm = helper.getControllerHandler();
		if(xresponseViewManager!=null){
			return xresponseViewManager.getHandlerMethodResponseView(hm, data);
		}
		return data;
	}
	
	
	
	protected Object delegateSpringFilterModel(Map<String, Object> model) {
		return super.filterModel(model);
	}
	

}
