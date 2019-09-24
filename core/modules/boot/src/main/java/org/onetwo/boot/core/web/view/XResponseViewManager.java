package org.onetwo.boot.core.web.view;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.mvc.HandlerMappingListener;
import org.onetwo.common.data.DataResultWrapper;
import org.onetwo.common.data.DataResultWrapper.NoWrapper;
import org.onetwo.common.exception.BaseException;
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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;


/**
 * @author wayshall
 * <br/>
 */
public class XResponseViewManager implements HandlerMappingListener {

	public static final DataResultWrapper DATA_RESULT_WRAPPER = new DefaultDataResultWrapper();
	public static final String X_RESPONSE_VIEW = "x-response-view";
	
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private Cache<String, Map<String, XResponseViewData>> viewDataCaces = CacheBuilder.newBuilder()
																		.maximumSize(100)
																		.build();
	private boolean alwaysWrapDataResult = false;
	private DataResultWrapper dataResultWrapper = DATA_RESULT_WRAPPER;
	private boolean enableResponseView = true;
	private Map<Predicate<Object>, DataResultWrapper> matchers = Maps.newHashMap();
	
	@Override
	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		for(HandlerMethod hm : handlerMethods.values()){
			Map<String, XResponseViewData> viewDatas = findXResponseViewData(hm);
			if(!viewDatas.isEmpty()){
				viewDataCaces.put(hm.getMethod().getName(), viewDatas);
			}
		}
		Map<String, XResponseViewData> def = ImmutableMap.of(XResponseView.DEFAULT_VIEW, new XResponseViewData(XResponseView.DEFAULT_VIEW, dataResultWrapper));
		this.viewDataCaces.put(XResponseView.DEFAULT_VIEW, def);
	}


	public XResponseViewManager registerMatchPredicate(Predicate<Object> predicate){
		return registerMatchPredicate(predicate, dataResultWrapper);
	}
	public XResponseViewManager registerMatchPredicate(Predicate<Object> predicate, DataResultWrapper dataResultWrapper){
		this.matchers.putIfAbsent(predicate, dataResultWrapper);
		return this;
	}

	/***
	 * 匹配到则返回包装后的data，否则返回empty
	 * @author wayshall
	 * @param data
	 * @param defaultWrapIfNotFoud
	 * @return
	 */
	public Optional<Object> getResponseViewByPredicate(final Object data){
		Optional<Object> wrapDataOpt = Optional.empty();
		for(Entry<Predicate<Object>, DataResultWrapper> entry : matchers.entrySet()){
			if(entry.getKey().test(data)){
				return Optional.ofNullable(entry.getValue().wrapResult(data));
			}
		}
		/*if(defaultWrapIfNotFoud){
			wrapDataOpt = Optional.ofNullable(dataResultWrapper.wrapResult(data));
		}*/
		return wrapDataOpt;
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
		DataWrapper dw = (DataWrapper)model.get(responseView.get());
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
		Map<String, XResponseViewData> viewDataMap;
		try {
			// local search, 优先匹配方法注解上的
			viewDataMap = viewDataCaces.get(hm.getMethod().toGenericString(), ()->findXResponseViewData(hm));
		} catch (ExecutionException e) {
			throw new BaseException("getCurrentHandlerMatchResponseView error", e);
		}
		XResponseViewData viewData = null;
		if(LangUtils.isEmpty(viewDataMap)){
			// global search， 如果找不到，全局缓存里查找
			if(responseView.isPresent()){
				viewDataMap = viewDataCaces.getIfPresent(responseView.get());
			}
		}
		if(LangUtils.isEmpty(viewDataMap)){
//			return Optional.of(new XResponseViewData(XResponseView.DEFAULT_VIEW, dataResultWrapper));
			return Optional.empty();
		}
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
		
		public XResponseViewData(String viewName, Class<? extends DataResultWrapper> wrapperClass) {
			super();
			this.viewName = viewName;
			this.wrapper = wrapperClass==NoWrapper.class?NoWrapper.INSTANCE:ReflectUtils.newInstance(wrapperClass);
		}
		public XResponseViewData(String viewName, DataResultWrapper wrapper) {
			super();
			this.viewName = viewName;
			this.wrapper = wrapper;
		}
		public Optional<DataResultWrapper> getWrapper() {
			return Optional.ofNullable(wrapper);
		}
		public String getViewName() {
			return viewName;
		}
		
	}
	

}
