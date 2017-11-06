package org.onetwo.boot.core.web.view;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.mvc.HandlerMappingListener;
import org.onetwo.common.data.DataResultWrapper;
import org.onetwo.common.data.DataResultWrapper.NoWrapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.mvc.utils.DataWrapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


/**
 * @author wayshall
 * <br/>
 */
public class XResponseViewManager implements HandlerMappingListener {

	public static final DataResultWrapper DATA_RESULT_WRAPPER = new DefaultDataResultWrapper();
	public static final String X_RESPONSE_VIEW = "x-response-view";
	
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private Cache<Method, Map<String, XResponseViewData>> viewDataCaces = CacheBuilder.newBuilder()
																		.maximumSize(1000)
																		.build();
	private boolean alwaysWrapDataResult = false;
	private DataResultWrapper dataResultWrapper = DATA_RESULT_WRAPPER;
	private boolean enableResponseView = true;
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		for(HandlerMethod hm : handlerMethods.values()){
			Map<String, XResponseViewData> viewDatas = findXResponseViewData(hm);
			if(!viewDatas.isEmpty()){
				viewDataCaces.put(hm.getMethod(), viewDatas);
			}
		}
	}
	
	/****
	 * 直接从model里查找view，并强转为DataWrapper
	 * @author wayshall
	 * @param model
	 * @return
	 */
	public Optional<Object> getResponseViewFromModel(Map<String, Object> model){
		if(!enableResponseView){
			return Optional.empty();
		}
		Optional<String> responseView = getXResponseView();
		if(!responseView.isPresent()){
			return Optional.empty();
		}
		//直接从model里查找view，并强转为DataWrapper
		DataWrapper dw = (DataWrapper)model.get(responseView);
		if(dw!=null){
			Object result = dw.getValue();
			return Optional.ofNullable(result);
		}
		return Optional.empty();
	}

	/*****
	 * 根据header和当前controller注解配置处理
	 * @author wayshall
	 * @param hm
	 * @param data
	 * @return
	 */
	public Object getHandlerMethodResponseView(HandlerMethod hm, final Object data, boolean defaultWrapIfNotFoud){
		if(!enableResponseView || hm==null){
			return data;
		}
		Optional<String> responseView = getXResponseView();
		
		if(logger.isDebugEnabled()){
			logger.debug("x-response-view: " + responseView);
		}

		Object wrapData = data;
		Optional<XResponseViewData> viewData = getCurrentHandlerMatchResponseView(responseView, hm);
		if(viewData.isPresent()){
			if(viewData.get().getWrapper().isPresent()){
				wrapData = viewData.get().getWrapper().get().wrapResult(wrapData);
			}
		}else if(this.alwaysWrapDataResult){
			wrapData = this.dataResultWrapper.wrapResult(data);
		}else if(defaultWrapIfNotFoud){
			wrapData = this.dataResultWrapper.wrapResult(data);
		}
		
		/*Optional<XResponseViewData> viewData = getCurrentHandlerMatchResponseView(responseView, hm);
		if(!responseView.isPresent() && !viewData.isPresent()){
			return data;
		}
		Object wrapData = data;
		if(viewData.isPresent()){
			wrapData = viewData.get().getWrapper().wrapResult(data);
		}else if(this.alwaysWrapDataResult){
			wrapData = this.dataResultWrapper.wrapResult(data);
		}else if(defaultWrapIfNotFoud){
			wrapData = this.dataResultWrapper.wrapResult(data);
		}*/
		return wrapData;
	}

	protected Optional<XResponseViewData> getCurrentHandlerMatchResponseView(Optional<String> responseView, HandlerMethod hm){
		Map<String, XResponseViewData> viewDataMap = viewDataCaces.getIfPresent(hm.getMethod());
		if(viewDataMap==null){
			return Optional.empty();
		}
		XResponseViewData viewData = null;
		if(!responseView.isPresent()){
			viewData = viewDataMap.get(XResponseView.DEFAULT_VIEW);
		}else{
			viewData = viewDataMap.get(responseView.get());
		}
		return Optional.ofNullable(viewData);
	}
	

	public void setAlwaysWrapDataResult(boolean alwaysWrapDataResult) {
		this.alwaysWrapDataResult = alwaysWrapDataResult;
	}

	public void setEnableResponseView(boolean enableResponseView) {
		this.enableResponseView = enableResponseView;
	}

	static Map<String, XResponseViewData> findXResponseViewData(HandlerMethod hm){
		Set<XResponseView> attrs = AnnotatedElementUtils.getMergedRepeatableAnnotations(hm.getMethod(), XResponseView.class);
		if(LangUtils.isEmpty(attrs)){
			attrs = AnnotatedElementUtils.getMergedRepeatableAnnotations(hm.getBeanType(), XResponseView.class);
			if(LangUtils.isEmpty(attrs)){
				return Collections.emptyMap();
			}
		}
		return attrs.stream().map(view->{
			return new XResponseViewData(view.value(), view.wrapper());
		})
		.collect(Collectors.toMap(e->e.getViewName(), e->e));
	}

	
	public static Optional<String> getXResponseView(){
		Optional<HttpServletRequest> requestOpt = WebHolder.getRequest();
		if(!requestOpt.isPresent()){
			return Optional.empty();
		}
		String responseView = requestOpt.get().getHeader(X_RESPONSE_VIEW);
		return Optional.ofNullable(responseView);
	}
	
	public static class XResponseViewData {
		final String viewName;
		final DataResultWrapper wrapper;
		public XResponseViewData(String viewName,
				Class<? extends DataResultWrapper> wrapperClass) {
			super();
			this.viewName = viewName;
			this.wrapper = wrapperClass==NoWrapper.class?NoWrapper.INSTANCE:ReflectUtils.newInstance(wrapperClass);
		}
		public Optional<DataResultWrapper> getWrapper() {
			return Optional.ofNullable(wrapper);
		}
		public String getViewName() {
			return viewName;
		}
		
	}
	

}
