package org.onetwo.common.web.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.bbcode.BBCode;
import org.onetwo.common.web.utils.bbcode.BBCodeHandler;

import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

public class Tool {

	public static final String BEAN_NAME = "Tool";
	public static final String ALIAS_NAME = "func";
	public static final String ENCODING = "UTF-8";
	
	private static Tool instance;

	protected BBCodeHandler htmlFilter = new BBCodeHandler("html_filter.xml");
//	private static boolean init;
	
	public static void initialize() {
		if(instance==null){
			try {
				instance = ReflectUtils.newInstance(SiteConfig.getInstance().getProperty("tool.class"));
			} catch (Exception e) {
			}
			if(instance==null){
				instance = new Tool();
			}
		}
	}
	
	public static boolean isInit() {
		return instance != null;
	}

	public static Tool getInstance(){
		if(!isInit())
			initialize();
		return instance;
	}
	
	protected Tool(){
	}
	
	public String format(Date date, String pattern){
		return DateUtil.format(pattern, date);
	}
	
	public NiceDate getNiceDate(){
		return new NiceDate();
	}
	
	public String encode(String str){
		if(StringUtils.isBlank(str))
			return  "";
		try {
			return  URLEncoder.encode(str, ENCODING);
		} catch (Exception e) {
			return str;
		}
	}

	public String decode(String str){
		try {
			return URLDecoder.decode(str, ENCODING);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public String getDataLanguage(){
		return StrutsUtils.getDataLanguage();
	}
	
	public String getTheDataLanguage(){
		return StrutsUtils.getTheDataLanguage();
	}
	
	public String getTheLanguage(){
		return StrutsUtils.getTheLanguage();
	}
	
	public String getRequestURL(){
		HttpServletRequest request = StrutsUtils.getRequest();
		String fullUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+StrutsUtils.getRequestURI();
		return fullUrl;
	}
	
	public String getRequestURI(){
//		return StrutsUtils.getRequestURI();
		return getRequestURL();
	}
	
	public String getActionQueryURI(){
		return StrutsUtils.getRequestURI();
	}
	
	public String getActionURI(){
		return StrutsUtils.getServletPath();
	}

	public String getRefereURL(){
		return StrutsUtils.getRefereURL();
	}
	public static void main(String[] args){
	}
	
	public boolean isDev(){
		return SiteConfig.getInstance().isDev();
	}
	
	public String subString(String s, int length) {
		return MyUtils.subString(s, length, 1);
	}
	
	public String filterHtml(String text){
		return this.filterHtml(text, true, true);
	}
	
	public String filterEnHtml(String text){
		return this.filterHtml(text, false, true);
	}
	
	public String filterHtml(String text,Boolean isFilterSpace,Boolean isFilteLine){
		for (BBCode bbcode : htmlFilter.getBbList()) {
			text = text.replaceAll(bbcode.getRegex(), bbcode.getReplace());
		}
		if(isFilterSpace){
			text = text.replaceAll(" ", "");
			text = text.replaceAll("　", "");
			text = text.replaceAll("\\s+","");
		}
		if(isFilteLine){
			text = text.replaceAll("\\n+","");
		}
		return text;
	}
	
	public void push(String name){
		UtilTimerStack.push(name);
	}
	
	public void pop(String name){
		UtilTimerStack.pop(name);
	}
	
}
