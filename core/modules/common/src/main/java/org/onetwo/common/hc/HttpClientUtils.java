package org.onetwo.common.hc;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.onetwo.common.exception.BaseException;

public class HttpClientUtils {

	
	public static String toString(HttpResponse response) throws ParseException, IOException{
		StringBuilder str = new StringBuilder();
		str.append("statusCode:")
			.append(response.getStatusLine().getStatusCode())
			.append("\n");
		HeaderIterator headerIt = response.headerIterator();
		while(headerIt.hasNext()){
			Header header = headerIt.nextHeader();
			str.append(header).append("\n");
		}
		HttpEntity entity = response.getEntity();
		str.append(EntityUtils.toString(entity));
		return str.toString();
	}
	
	public static HttpClient createHttpClient(CookieStore cookieStore){
		try {
			return createHttpClient0(cookieStore);
		} catch (Exception e) {
			throw new BaseException("create http client error:"+e.getMessage(), e);
		}
	}
	private static HttpClient createHttpClient0(CookieStore cookieStore) throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException{
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory http = new PlainConnectionSocketFactory();
		registryBuilder.register("http", http);
		
		/*TrustManager trustManager = new X509TrustManager(){
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	  
			}
	  
			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	  
			}
	  
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		}; */
		/***
		 * setConnectTimeout：设置连接超时时间，单位毫秒。
setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
		 */
		RequestConfig reqConfig = createDefaultRequestConfig();
		KeyStore trustStory = KeyStore.getInstance(KeyStore.getDefaultType());
		TrustStrategy anyTrustStrategy = new TrustStrategy(){
			@Override
			public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				return true;
			}
		};
		SSLContext sslContext = SSLContexts.custom()
											.useProtocol("TLS")
											.loadTrustMaterial(trustStory, anyTrustStrategy)
											.build();
		LayeredConnectionSocketFactory https = new SSLConnectionSocketFactory(sslContext);
		registryBuilder.register("https", https);
		
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		PoolingHttpClientConnectionManager poolMgr = new PoolingHttpClientConnectionManager(registry);
		return HttpClientBuilder.create()
								.setDefaultCookieStore(cookieStore)
								.setConnectionManager(poolMgr)
								.setDefaultRequestConfig(reqConfig)
								.build();
	}
	
	public static RequestConfig createDefaultRequestConfig(){
		RequestConfig reqConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(1000)
				.setConnectTimeout(3000)
				.setSocketTimeout(3000)
				.build();
		return reqConfig;
	}
}
