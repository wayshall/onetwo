package org.onetwo.common.http;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.onetwo.common.utils.StringUtils;

public class HttpProxy {

	private String host;
	private int port;

	private String user;
	private String password;

	private Authenticator authenticator;
	private Proxy proxy;

	public HttpProxy(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public HttpProxy(String host, int port, String user, String password, Authenticator authenticator) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.authenticator = authenticator;
	}

	public Authenticator getAuthenticator() {
		if (this.authenticator == null && StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
			this.authenticator = new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					if (this.getRequestorType().equals(RequestorType.PROXY))
						return new PasswordAuthentication(user, password.toCharArray());
					else
						return null;
				}

			};
		}
		return authenticator;
	}

	public Proxy getProxy() {
		if(proxy==null){
			proxy = new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(host, port));
		}
		return proxy;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

}
