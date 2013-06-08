package org.onetwo.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		if (classPath.indexOf("WEB-INF") > -1) {
			classPath = classPath.substring(1,classPath.indexOf("WEB-INF"));
		}
		return classPath; 
	}
	
	private static Properties instance;
	
	public static void createProperty(String propertyFileName){
        createProperty(propertyFileName, PropertiesUtils.class.getClassLoader());
	}
	
	public static void createProperty(String propertyFileName, ClassLoader classLoader){
		Assert.hasLength(propertyFileName);
		instance = new Properties(); 
        final String name = propertyFileName.endsWith(".properties") ? propertyFileName : (propertyFileName + ".properties");
		final String fileNames = "/" + name;
		InputStream in = classLoader.getResourceAsStream(fileNames); 
        try { 
    		if (in == null) {
    			in = getClassLoader().getResourceAsStream(name);
				if (in == null) {
	    			try {
						in = new FileInputStream(getPath() + name);
					} catch (Exception e) {
						throw new ServiceException("读取Properties文件错误");
					}
				}
    		}
        	instance.load(in); 
        } catch (IOException e) { 
        	throw new ServiceException("读取Properties文件错误", e);
        } finally {
        	if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new ServiceException("关闭Properties文件错误", e);
				}
        	}
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
	
	public static void setProperty(String propertyFileName, ClassLoader classLoader, String key, String value){
		createProperty(propertyFileName, classLoader);
		setProperty(propertyFileName, key, value);
	}
	
	public static void setProperty(String propertyFileName, String key, String value) {
		OutputStream out = null;
		if (instance == null) {
			throw new ServiceException("未初始化Properties属性");
		}
		instance.setProperty(key, value);
		try {
			String path = ClassLoader.getSystemResource(".").getPath();
			final String name = propertyFileName.endsWith(".properties") ? propertyFileName : (propertyFileName + ".properties");
			//final String fileNames = "/" + name;
			out = new FileOutputStream(path + name);
			instance.store(out, "");
		} catch (FileNotFoundException e) {
			throw new ServiceException("未找到Properties属性文件");
		} catch (IOException e) {
			throw new ServiceException("写入Properties属性文件出错");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new ServiceException("关闭Properties文件错误", e);
				}
			}
		}
	}

	public static void main(String[] args) {
		final String BUNDLE_PATH = "sms";
		PropertiesUtils.createProperty(BUNDLE_PATH);
		String value = PropertiesUtils.getProperty("providerUrl");
		System.out.println(value);
	}
}
