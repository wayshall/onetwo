package org.onetwo.common.web.view.jsp;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.csrf.CsrfPreventor;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.view.jsp.form.FormTagBean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

final public class TagUtils {

	public static String PARAM_FORMAT = "format";
	public static String WEB_INF_DIR = "/WEB-INF/";
	public static String BASE_TAG_DIR = "/WEB-INF/tags/";
	public static String BASE_VIEW_DIR = "/WEB-INF/views/";
	public static String BASE_LAYOUT_DIR = "/WEB-INF/views/layout/";


	public static String getViewPage(String path){
		String t = StringUtils.appendEndWith(path, ".jsp");
		return getDirPage(BASE_VIEW_DIR, t);
	}
	public static String getLayoutPage(String path){
		String t = StringUtils.appendEndWith(path, ".jsp");
		return getDirPage(BASE_LAYOUT_DIR, t);
	}
	public static String getTagPage(String path){
		String t = StringUtils.appendEndWith(path, ".jsp");
		String baseTagDir = BaseSiteConfig.getInstance().getTagTheme();
		if(StringUtils.isBlank(baseTagDir)){
			baseTagDir = BASE_TAG_DIR;
		}else if(baseTagDir.startsWith("/tags/")){
			baseTagDir = WEB_INF_DIR + baseTagDir;
		}else{
			baseTagDir = BASE_TAG_DIR + baseTagDir;
		}
		return getDirPage(baseTagDir, t);
	}
	public static String getDirPage(String baseDir, String path){
		if(StringUtils.isBlank(path))
			return path;
		
		if(path.startsWith(baseDir))
			return path;
		
		if(path.startsWith("/") && baseDir.endsWith("/"))
			path = path.substring(1);
		
		return baseDir + path;
	}
	
	public static Page<Object> toPage(Object dsValue){
		Page<Object> page = null;
		if(dsValue==null){
			page = Page.create();
		}else if (dsValue instanceof Page) {
			page = (Page<Object>) dsValue;
		}else {
			List<Object> list = null;
			if(Map.class.isInstance(dsValue)){
				Map<?, ?> dataMap = (Map<?, ?>)dsValue;
				list = LangUtils.newArrayList(dataMap.size());
				for(Entry<?, ?> entry : dataMap.entrySet()){
//					list.add(KVEntry.create(entry));
					list.add(entry);
				}
			}else{
				list = LangUtils.asList(dsValue);
			}
			if (list == null)
				list = Collections.EMPTY_LIST;
			page = Page.create();
			page.setResult(list);
			page.setTotalCount(list.size());
			page.setPageSize(list.size());
		}
		return page;
	}
	
	public static String pageLink(String action, int numb){
		String result = action;
		if(numb<=1)
			return result;
		if (action.indexOf("?")!=-1){
			result += "&pageNo="+numb;
		}else{
			result += "?pageNo="+numb;
		}
		return result;
	}

	public static String appendXlsFormat(String action){
		return appendParam(action, "format", "xls");
	}
	
	public static String appendParam(String action, String name, String value){
		String result = action;
		if (action.indexOf("?")!=-1){
			result += "&"+name+"="+value;
		}else{
			result += "?"+name+"="+value;
		}
		return result;
	}
	

	public static String getFormVarName(){
		return FormTagBean.class.getSimpleName();
	}
	

	public static String getRequestUri(HttpServletRequest request){
		String surl = BaseSiteConfig.getInstance().getBaseURL()+(String)request.getAttribute(BaseInitFilter.REQUEST_URI);
		return surl;
	}
	

	public static String getUriWithQueryString(HttpServletRequest request){
		String surl = getRequestUri(request);
		String queryString = request.getQueryString();
		if(StringUtils.isBlank(queryString))
			return surl;
		if(surl.contains("?")){
			surl += "&" + queryString;
		}else{
			surl += "?" + queryString;
		}
		return surl;
	}
	

	public static String getUriWithQueryStringFilterPageNo(HttpServletRequest request){
		String surl = getRequestUri(request);
		String queryString = getQueryStringFilterPageNo(request);
		if(StringUtils.isBlank(queryString))
			return surl;
		if(surl.contains("?")){
			surl += "&" + queryString;
		}else{
			surl += "?" + queryString;
		}
		return surl;
	}


	public static String getQueryStringFilterPageNo(HttpServletRequest request) {
		String str = request.getQueryString();
		return StringUtils.isBlank(str)?"":filterPageParams(new CasualMap(str)).toParamString();
	}


	public static String getUriWithParamsFilterPageNo(HttpServletRequest request) {
		String surl = getRequestUri(request);
		String queryString = filterCsrfParams(filterPageParams(RequestUtils.getParametersWithout(request)), request, null).toParamString();
		surl += surl.contains("?")?"":"?" + queryString;
		return surl;
	}

	/****
	 * 过滤分页等一些特定的参数
	 * @param params
	 * @return
	 */
	public static CasualMap filterPageParams(CasualMap params) {
//		return params.filter("pageNo", Page.PAGINATION_KEY, "order", "orderBy");filter("aa*", HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM).
		return params.filter("pageNo", Page.PAGINATION_KEY, "aa*", HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM);
	}
	public static CasualMap filterCsrfParams(CasualMap params, HttpServletRequest request, CsrfPreventor csrfPreventor) {
		return csrfPreventor==null?params:params.filter(csrfPreventor.getFieldOfTokenFieldName(), request.getParameter(csrfPreventor.getFieldOfTokenFieldName()));
	}
	
	public static String parseAction(HttpServletRequest request, String action, CsrfPreventor csrfPreventor){
		String surl = getRequestUri(request);
		if(StringUtils.isBlank(action)){
			return surl;
		}
		surl += parseQueryString(request, action, csrfPreventor);
		return surl;
	}
	
	public static String parseQueryString(HttpServletRequest request, String action, CsrfPreventor csrfPreventor){
		String surl = "";
		String[] symbols = StringUtils.split(action, "|");
		int index = 0;
		for (String symbol : symbols) {
			if (StringUtils.isBlank(symbol))
				continue;
			String qstr = processUrlSymbol(request, symbol, csrfPreventor);
			if (StringUtils.isNotBlank(qstr)) {
				if (index == 0)
					surl += "?";
				else
					surl += "&";
				surl += qstr;
				index++;
			}
		}
		return surl;
	}
	
	public static String processUrlSymbol(HttpServletRequest request, String symbol, CsrfPreventor csrfPreventor) {
		CasualMap params = processUrlSymbolAsCasualMap(request, symbol, csrfPreventor);
		/*if (symbol.equals(":qstr")) {
			str = getQueryStringFilterPageNo(request);
		} else if (symbol.equals(":post2get")) {
			str = filterCsrfParams(filterPageParams(RequestUtils.getPostParametersWithout(request)), request, csrfPreventor).toParamString();
		} else if (symbol.equals(":params")) {
			str = filterCsrfParams(filterPageParams(RequestUtils.getParametersWithout(request)), request, csrfPreventor).toParamString();
		}else{
			str = symbol;
		}*/
		String str = params==null?symbol:params.toParamString();
		return str;
	}
	
	public static CasualMap processUrlSymbolAsCasualMap(HttpServletRequest request, String symbol, CsrfPreventor csrfPreventor) {
		CasualMap params = null;
		if (symbol.equals(":qstr")) {
			params = filterPageParams(new CasualMap(request.getQueryString()));
		} else if (symbol.equals(":post2get")) {
			params = filterCsrfParams(filterPageParams(RequestUtils.getPostParametersWithout(request)), request, csrfPreventor);
		} else if (symbol.equals(":params")) {
			params = filterCsrfParams(filterPageParams(RequestUtils.getParametersWithout(request)), request, csrfPreventor);
		}
		return params;
	}

	
	private TagUtils(){
	}
}
