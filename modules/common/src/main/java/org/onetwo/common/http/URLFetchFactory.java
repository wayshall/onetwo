package org.onetwo.common.http;


public class URLFetchFactory {

	private static final URLFetch fetch = new DefaultURLFetch();

	public static URLFetch getURLFetch(){
		return fetch;
	}
	
	public static URLFetch getURLFetch(String basic){
		URLFetch fetch = new DefaultURLFetch(basic);
		return fetch;
	}
	
	public static URLFetch getURLFetch(String authUser, String authPwd){
		URLFetch fetch = new DefaultURLFetch(HttpRequest.generatedBasic(authUser, authPwd));
		return fetch;
	}
	
	public static URLFetch getURLFetch(String basic, String proxyHost, int proxyPort){
		URLFetch fetch = new DefaultURLFetch(basic, proxyHost, proxyPort);
		return fetch;
	}
	
	public static URLFetch getURLFetch(String basic, HttpProxy proxy){
		URLFetch fetch = new DefaultURLFetch(basic, proxy);
		return fetch;
	}
	
	public static URLFetch getURLFetch(HttpProxy proxy){
		URLFetch fetch = new DefaultURLFetch(proxy);
		return fetch;
	}

}
