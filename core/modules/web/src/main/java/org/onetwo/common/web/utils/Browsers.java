package org.onetwo.common.web.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.onetwo.common.convert.Types;

abstract public class Browsers {
	public static final BrowserMeta FIREFOX = new BrowserMeta("firefox", "Firefox", "");
	public static final BrowserMeta CHROME = new BrowserMeta("chrome", "Chrome", "");
	public static final BrowserMeta MSIE_4 = new BrowserMeta("msie 4.0", "MSIE", "4.0");
	public static final BrowserMeta MSIE_5 = new BrowserMeta("msie 5.0", "MSIE", "5.0");
	public static final BrowserMeta MSIE_6 = new BrowserMeta("msie 6.0", "MSIE", "6.0");
	public static final BrowserMeta MSIE_7 = new BrowserMeta("msie 7.0", "MSIE", "7.0");
	public static final BrowserMeta MSIE_8 = new BrowserMeta("msie 8.0", "MSIE", "8.0");
	public static final BrowserMeta MSIE_9 = new BrowserMeta("msie 9.0", "MSIE", "9.0");
	public static final BrowserMeta WECHAT = new BrowserMeta("micromessenger", "", "9.0");
	public static final BrowserMeta OPERA = new BrowserMeta("opera", "Opera", "");
	public static final BrowserMeta NETSCAPE = new BrowserMeta("mozilla/5.0", "Netscape", "");
	public static final BrowserMeta UNKNOW = new BrowserMeta("UNKNOW", "UNKNOW", "");
	

	private static final Map<String, BrowserMeta> AGENT_BROWSER = new LinkedHashMap<>();
	
	static {
		addBrowsers(FIREFOX,
					CHROME,
					MSIE_4,
					MSIE_5,
					MSIE_6,
					MSIE_7,
					MSIE_8,
					MSIE_9,
					WECHAT,
					OPERA,
					NETSCAPE,
					UNKNOW);
	}
	
	public static Map<String, BrowserMeta> getAgentBrowsers() {
		return Collections.unmodifiableMap(AGENT_BROWSER);
	}
	
	public static BrowserMeta getBrowser(String key){
		BrowserMeta browser = AGENT_BROWSER.get(key);
		return browser==null?UNKNOW:browser;
	}

	public static void addBrowsers(BrowserMeta...browsers){
		Stream.of(browsers).forEach(b->{
			AGENT_BROWSER.put(b.getAgentKey(), b);
		});
	}
	
	public static class BrowserMeta {
		final private String agentKey;
		final private String name;
		final private String version;
		public BrowserMeta(String agentKey, String name, String version) {
			super();
			this.agentKey = agentKey;
			this.name = name;
			this.version = version;
		}
		public boolean isWechat(){
			return "micromessenger".equalsIgnoreCase(agentKey);
		}
		public boolean isIE(){
			return "MSIE".equalsIgnoreCase(name);
		}
		public boolean isNetscape(){
			return "Netscape".equalsIgnoreCase(name);
		}
		public boolean isFuckingBrowser(){
			return isNetscape() || (isIE() && getVersionAsNumber()<Browsers.MSIE_9.getVersionAsNumber());
		}
		public double getVersionAsNumber(){
			return Types.asValue(version, double.class, 0.0);
		}
		public String getAgentKey() {
			return agentKey;
		}
		public String getName() {
			return name;
		}
		public String getVersion() {
			return version;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((agentKey == null) ? 0 : agentKey.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((version == null) ? 0 : version.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BrowserMeta other = (BrowserMeta) obj;
			if (agentKey == null) {
				if (other.agentKey != null)
					return false;
			} else if (!agentKey.equals(other.agentKey))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}
		
	}
}
