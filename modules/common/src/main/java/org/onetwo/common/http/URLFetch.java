package org.onetwo.common.http;

import java.util.List;

public interface URLFetch {
	
	public HttpResponse fetch(HttpRequest request);
	public HttpResponse fetch(HttpRequest request, String method);
	public HttpResponse get(String url);
	public HttpResponse get(String url, String encode);
	public HttpResponse get(String url, boolean verify);
	public HttpResponse get(String url, boolean verify, Object...params);
	
	public HttpResponse post(String url, boolean verify, Object...values);
	public HttpResponse post(String url, List<HttpParam> params);
	public HttpResponse post(String url, List<HttpParam> params, boolean verify);

}
