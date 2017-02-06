package org.onetwo.common.hc;

import java.io.IOException;

import org.apache.commons.lang3.RandomUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.onetwo.common.hc.HttpClientUtils;

public class HttpClientTest {

	static CookieStore cookieStore = new BasicCookieStore();
	static HttpClient httpClient = HttpClientUtils.createHttpClient(cookieStore);
	static String USER_AGENT = "Mozilla/5.0 (Linux; Android 5.1; m3 note Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043024 Safari/537.36 MicroMessenger/6.5.4.1000 NetType/WIFI Language/zh_CN";
	
	@Test
	public void test() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = "weixinurl";
		HttpGet get = new HttpGet(url);
		HttpResponse reponse = httpClient.execute(get);
		HttpEntity entity = reponse.getEntity();
		System.out.println(EntityUtils.toString(entity));
		
		String cookieString = reponse.getFirstHeader("Set-Cookie").toString();
		System.out.println("cookieString: "+cookieString);
		String jsessionId = cookieString.substring("JSESSIONID=".length(), cookieString.indexOf(";"));
		System.out.println("jsessionId: "+jsessionId);
	}

	public static void main(String[] args) {
		BasicClientCookie cookie = new BasicClientCookie("xx", "yyy");
		cookie.setDomain("www.test.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		while(true){
			draw();
			long delay = RandomUtils.nextLong(100, 1000);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				System.err.println("sleep error:"+e.getMessage());
			}
		}
	}
	public static void draw(){
		String url = "weixinurl";
		HttpPost requestUrl = new HttpPost(url);
		requestUrl.setHeader("user-agent", USER_AGENT);
		try {
			 HttpResponse response = httpClient.execute(requestUrl);
			 printResponse(response);
		} catch (Exception e) {
			System.err.println("request error:"+e.getMessage()+", ignore...");
			return ;
		} 
		
		url = "weixinurl";
		requestUrl = new HttpPost(url);
		requestUrl.setHeader("user-agent", USER_AGENT);
		
		 try {
			 HttpResponse response = httpClient.execute(requestUrl);
			 printResponse(response);
		} catch (Exception e) {
			System.err.println("request error:"+e.getMessage()+", ignore...");
			return ;
		} 
	}
	
	static void printResponse(HttpResponse response) throws ParseException, IOException{
		System.out.println("statusCode:"+response.getStatusLine().getStatusCode());
		HeaderIterator headerIt = response.headerIterator();
		while(headerIt.hasNext()){
			Header header = headerIt.nextHeader();
			System.out.println(header);
		}
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
	}
}
