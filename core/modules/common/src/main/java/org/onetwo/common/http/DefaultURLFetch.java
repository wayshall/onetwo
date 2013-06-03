package org.onetwo.common.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class DefaultURLFetch implements URLFetch {
	private String basic;
	private HttpProxy proxy;
	
	public DefaultURLFetch(){
	}
	
	public DefaultURLFetch(String basic){
		this.basic = basic;
	}
	
	public DefaultURLFetch(String basic, String proxyHost, int proxyPort){
		this.basic = basic;
		if(StringUtils.isNotBlank(proxyHost)){
			this.proxy = new HttpProxy(proxyHost, proxyPort<=0?80:proxyPort);
		}
	}
	
	public DefaultURLFetch(HttpProxy proxy){
		this.proxy = proxy;
	}
	
	public DefaultURLFetch(String basic, HttpProxy proxy){
		this.basic = basic;
		this.proxy = proxy;
	}
	
	public HttpResponse fetch(HttpRequest request){
		return fetch(request, HttpRequest.GET);
	}

	public HttpResponse fetch(HttpRequest request, String method){
		if(HttpRequest.POST.equals(method))
			request.post();
		else
			request.get();
		HttpResponse response = new HttpResponse(request.getHttpConnection());
		return response;
	}

	public HttpResponse get(String url){
		return get(url, false);
	}

	public HttpResponse get(String url, String encode){
		return get(url, false, HttpRequest.ENCODE_KEY, encode);
	}

	public HttpResponse get(String url, boolean verify){
		return get(url, verify, (Object[])null);
	}
	
	protected String parseParmas(Map<Object, Object> map){
		if(map==null || map.isEmpty())
			return "";
		StringBuilder activeUrl = new StringBuilder();
		activeUrl.append("?");
		int index = 0;
		for(Map.Entry<Object, Object> p : map.entrySet()){
			if(StringUtils.isBlank(p.getKey().toString()) || p.getValue()==null)
				continue;
			
			if(index>0)
				activeUrl.append("&");
			activeUrl.append(p.getKey().toString()).append("=").append(p.getValue().toString());
			index++;
		}
		return activeUrl.toString();
	}
	
	protected String parseEnocde(Map<Object, Object> map){
		if(map==null)
			return null;
		String encode = null;
		if(map.containsKey(HttpRequest.ENCODE_KEY)){
			encode = (String)map.get(HttpRequest.ENCODE_KEY);
			map.remove(HttpRequest.ENCODE_KEY);
			if(StringUtils.isBlank(encode))
				encode = HttpRequest.DEFAULT_ENCODE;
		}
		return encode;
	}

	@Override
	public HttpResponse get(String url, boolean verify, Object... params) {
		StringBuilder activeUrl = new StringBuilder(url);
		String encode = null;
		Map<Object, Object> map = MyUtils.convertParamMap(params);
		encode = parseEnocde(map);
		String paramsStr = parseParmas(map);
		activeUrl.append(paramsStr);
		
		System.out.println("url: " + activeUrl);
		HttpRequest request = new HttpRequest(activeUrl.toString());
		if(StringUtils.isNotBlank(encode))
			request.setEncode(encode);
		if(verify){
			request.setBasic(basic);
		}
		if(proxy!=null){
			request.setProxy(proxy);
		}
		return fetch(request);
	}
	
	public HttpResponse post(String url, boolean verify, Object...values){
		List<HttpParam> params = new ArrayList<HttpParam>();
		Map paramsMap = MyUtils.convertParamMap(values);
		if(paramsMap!=null){
			HttpParam param = null;
			for(Map.Entry entry : (Set<Map.Entry>)paramsMap.entrySet()){
				if(StringUtils.isBlank(entry.getKey().toString()) || entry.getValue()==null)
					continue;
				param = new HttpParam();
				param.setName(entry.getKey().toString());
				param.setValue(entry.getValue().toString());
				params.add(param);
			}
		}
		return post(url, params, true);
	}

	public HttpResponse post(String url, List<HttpParam> params){
		return post(url, params, false);
	}

	public HttpResponse post(String url, List<HttpParam> params, boolean verify){
		HttpRequest request = new HttpRequest(url, params);
		if(verify){
			request.setBasic(basic);
		}
		if(proxy!=null){
			request.setProxy(proxy);
		}
		return fetch(request, HttpRequest.POST);
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public HttpProxy getProxy() {
		return proxy;
	}

	public void setProxy(HttpProxy proxy) {
		this.proxy = proxy;
	}
	
}
