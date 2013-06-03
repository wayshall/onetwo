package org.onetwo.common.web.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;


@SuppressWarnings({ "serial"})
public class WebLocaleUtils {


	public static final String ATTRIBUTE_KEY = "WW_TRANS_I18N_LOCALE";//I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE;
	
	public static final String DATA_LOCALE_ATTRIBUTE_KEY = "data_locale";
	public static final String ID_LANGUAGE_ATTRIBUTE_KEY = "id.language";
	
	public static final String DEFAULT_SITE_LOCALE = "__DEFAULT_SITE_LOCALE";
	
	//中文版 zh
	public static final Locale CHINESE = Locale.CHINESE;
	
	
	/**
	 * 在这里增加增加语言支持(数据语言）
	 */
	public static final Map<String, Locale> VALUES;
	
	public static final Map<String, Map<String, String>> LABELS; 
	
	static {
//		String supportStr = BaseSiteConfig.getInstance().getLanguageSupport();
		Map<String, Locale> valTemp = new HashMap<String, Locale>(){
			{
				put(CHINESE.toString().toLowerCase(), CHINESE);
				put(DEFAULT_SITE_LOCALE, CHINESE);
			}
		};
		/*List<JSON> datalist = JSONUtils.getJSONArray(supportStr);
		Locale local = null;
		for(JSON j : datalist){
			if(!(j instanceof JSONObject))
				continue;
			JSONObject jsonb = (JSONObject) j;
			String language = jsonb.getString("language");
			String country = jsonb.getString("country");
			Boolean isDefault = jsonb.getBoolean("default");
			if(StringUtils.isBlank(language) || StringUtils.isBlank(country))
				LangUtils.throwBaseException(LangUtils.toString("unsupported : ${0}, ${1}", language, country));
			local = new Locale(language, country.toUpperCase());
			valTemp.put(local.toString(), local);
			if(isDefault){
				valTemp.put(DEFAULT_SITE_LOCALE, local);
			}
		}*/
		VALUES = Collections.unmodifiableMap(valTemp);
		
		LABELS = new HashMap<String, Map<String, String>>(){
			{
				put(CHINESE.toString(), new HashMap<String, String>(){
					{
						put(CHINESE.toString(), "中文");
					}
				});
			}
		};
	}
	/**
	 * 返回第一个语言作为默认的语言
	 * @author weishao.zeng
	 * @return
	 */
	public static final Locale getDefault(){
		return VALUES.get(DEFAULT_SITE_LOCALE);
	}
	
	public static final Locale getLocale(String localeStr){
		Assert.notNull(localeStr);
		Locale locale = VALUES.get(localeStr);
		if(locale==null)
			locale = VALUES.get(localeStr.substring(0, localeStr.indexOf('_')));
		return locale;
	}
	
	public static final Locale getClosestLocale(String localeStr){
		return getClosestLocale(localeStr, getDefault());
	}
	
	public static final Locale getClosestLocale(String localeStr, Locale def){
		if(StringUtils.isBlank(localeStr)){
			if(def!=null)
				return def;
			Assert.notNull(localeStr);
		}
		
		Locale cslosestLocale = VALUES.get(localeStr.toString().toLowerCase());
		if(cslosestLocale==null && localeStr.indexOf('_')!=-1){
			String[] strs = StringUtils.split(localeStr, '_');
			cslosestLocale = VALUES.get(strs[0]);
		}

		/*if(cslosestLocale==null)
			cslosestLocale = getDefault();*/
		
		return cslosestLocale==null?def:cslosestLocale;
	}
	
	/*public static final Locale getClosestLocale(Locale locale){
		Assert.notNull(locale);
		
		Locale cslosestLocale = VALUES.get(locale.toString().toLowerCase());
		if(cslosestLocale==null)
			cslosestLocale = VALUES.get(locale.getLanguage());
		
		if(cslosestLocale==null)
			cslosestLocale = getDefault();
		
		return cslosestLocale;
	}*/
	
	public static final boolean isSupport(String localeStr){
		boolean isSupport = VALUES.keySet().contains(localeStr.toLowerCase());
		if(isSupport)
			return true;
		
		if(localeStr.indexOf('_')!=-1){
			String[] strs = StringUtils.split(localeStr, '_');
			isSupport = VALUES.keySet().contains(strs[0]);
		}
		
		return isSupport;
	}
	
	public static final boolean isSameLocale(Locale locale1, Locale locale2){
		if(locale1==locale2)
			return true;
		if(locale1==null || locale2==null)
			return false;
		if(locale1.equals(locale2))
			return true;
		if(locale1.getLanguage().equals(locale2.getLanguage()) && locale1.getCountry().equals(locale2.getCountry()))
			return true;
		return false;
	}
	
	public static final boolean isDefault(Locale locale){
		return isSameLocale(getDefault(), locale);
	}
	
	public static void main(String[] args){
		System.out.println(CHINESE.toString().toLowerCase());
	}
}
