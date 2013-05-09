package org.onetwo.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.onetwo.common.exception.ServiceException;

public class PropertiesUtils {
	private static String encoding = "UTF-8";

	/**
	 * read the message with key from the resource bundle
	 * 
	 * @param bundlePath
	 * @param key
	 * @return
	 */
	@Deprecated
	public static String getString(String bundlePath, String key) {
		return getString(bundlePath, key, null);
	}

	/**
	 * read the message with key from the resource bundle
	 * 
	 * @param bundlePath
	 * @param key
	 * @param args
	 * @return
	 */
	@Deprecated
	public static String getString(String bundlePath, String key, Object[] args) {
		// we will throw a MessingResourceException if the bundle name is
		// invalid
		ResourceBundle bundle = getBundle(bundlePath);
		try {
			String msg = bundle.getString(key);
			return formatMessage(args, msg);
		} catch (MissingResourceException e) {
			return "";
		}
	}

	/**
	 * get ResourceBundle with bundlePath
	 * 
	 * @param bundlePath
	 * @return
	 * @throws MissingResourceException
	 *             if resource is messing
	 */
	private static ResourceBundle getBundle(String bundlePath) {
		Locale locale = Locale.getDefault();
		ResourceBundle bundle = ResourceBundle.getBundle(bundlePath, locale,
				getClassLoader());
		return bundle;
	}

	/**
	 * get the current ClassLoader
	 * 
	 * @return
	 */
	protected static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * convert msg encoding
	 * 
	 * @param msg
	 * @return
	 */
	public static String convertISO(String msg) {
		if (msg != null) {
			try {
				return new String(msg.getBytes("iso-8859-1"), getEncoding());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * get encoding
	 * 
	 * @return
	 */
	public static String getEncoding() {
		return encoding;
	}

	/**
	 * set encoding
	 * 
	 * @param encoding
	 */
	public static void setEncoding(String encoding) {
		PropertiesUtils.encoding = encoding;
	}
	
	public static String getPath(){
		String classPath = PropertiesUtils.class.getClassLoader().getResource("").getPath();
		String path = classPath.substring(1,classPath.indexOf("WEB-INF"));
		return path; 
	}
	
	private static Properties instance;
	
	public static void createProperty(String propertyFileName){
        createProperty(propertyFileName, PropertiesUtils.class.getClassLoader());
	}
	
	public static void createProperty(String propertyFileName, ClassLoader classLoader){
		instance = new Properties(); 
        InputStream in = classLoader.getResourceAsStream("/" + propertyFileName + ".properties"); 
        try { 
        	if (in == null) {
        		in = classLoader.getResourceAsStream(propertyFileName + ".properties"); 
        		if (in == null) {
        			throw new ServiceException("读取Properties文件错误");
        		}
        	}
        	instance.load(in); 
        } catch (IOException e) { 
        	throw new ServiceException("读取Properties文件错误", e);
        } 
	}
	
	public static String getProperty(String key){
		if (instance == null) {
			throw new ServiceException("未初始化Properties属性");
		}
		return instance.getProperty(key);
	}
	
	public static String getProperty(String key, Object[] args) {
		String msg = getProperty(key);
		return formatMessage(args, msg);
	}
	
	public static String getProperty(String propertyFileName, ClassLoader classLoader, String key, Object[] args) {
		String msg = getProperty(propertyFileName, classLoader, key);
		return formatMessage(args, msg);
	}
	
	public static String getProperty(String propertyFileName, String key, Object[] args) {
		String msg = getProperty(propertyFileName, getClassLoader(), key);
		return formatMessage(args, msg);
	}
	
	public static String getProperty(String propertyFileName, String key){
		createProperty(propertyFileName);
		return getProperty(key);
	}
	
	public static String getProperty(String propertyFileName, ClassLoader classLoader, String key){
		createProperty(propertyFileName, classLoader);
		return getProperty(key);
	}
	
	private static String formatMessage(Object[] args, String msg) {
		if (msg == null) {
			return "";
		}
		return msg = MessageFormat.format(msg, args);
	}

	public static void main(String[] args) {
		final String BUNDLE_PATH = "sms";
		PropertiesUtils.createProperty(BUNDLE_PATH);
		String value = PropertiesUtils.getProperty("providerUrl");
		System.out.println(value);
	}
}
