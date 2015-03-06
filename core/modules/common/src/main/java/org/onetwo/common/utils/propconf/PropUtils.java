package org.onetwo.common.utils.propconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.FileUtils;
import org.slf4j.Logger;

public abstract class PropUtils {
	private static final Logger LOGGER = JFishLoggerFactory.getLogger(PropUtils.class);
	
	public static final String CONFIG_POSTFIX = ".properties";

	public static File parseResource(String configName) {
		File file = null;
		try {
			if (configName.indexOf(".")==-1)
				configName += CONFIG_POSTFIX;
			file = new File(configName);
			if (!file.exists()) {
				// URL path = PropConfig.class.getResource(configName);
				if (configName.indexOf(':') == -1) {
//					configName = FileUtils.getResourcePath(configName);
					configName = FileUtils.getResourcePath(PropUtils.class.getClassLoader(), configName);
					file = new File(configName);
				}
			}
		} catch (Exception e) {
			throw new BaseException("load property config error: " + configName, e);
		}
		return file;
	}

	public static Properties loadProperties(String configName) {
		/*File file = parseResource(configName);
		if(file==null)
			LangUtils.throwBaseException("can not find config file : " + configName);
		return loadProperties(file);*/
		
		try {
			InputStream inStream = FileUtils.getResourceAsStream(configName);
			if(inStream==null)
				throw new IOException("can load as stream with : " +configName );
			Properties properties = new Properties();
			properties.load(inStream);
			return properties;
		} catch (Exception e) {
			throw new ServiceException("load config error : " + configName, e);
		}
	}

	public static Properties loadProperties(File file) {
		Properties config = new Properties();
		loadProperties(file, config);
		return config;
	}

	public static Properties loadProperties(File file, Properties config) {
		FileInputStream fin = null;
		try {
			if (config == null)
				config = new Properties();
			fin = new FileInputStream(file);
			config.load(fin);
			System.out.println("loaded the property config: " + file.getPath());
		} catch (Exception e) {
			throw new RuntimeException("load config error!", e);
		} finally {
			FileUtils.close(fin);
		}
		return config;
	}

	public static Properties loadProperties(InputStream fin, Properties config) {
		try {
			if (config == null)
				config = new Properties();
			config.load(fin);
		} catch (Exception e) {
			throw new RuntimeException("load config error!", e);
		} finally {
			FileUtils.close(fin);
		}
		return config;
	}

	public static PropConfig loadPropConfig(String configName, boolean throwException) {
		PropConfig config = new PropConfig(configName);
		if (throwException)
			config.initAppConfig(true);
		else {
			try {
				config.initAppConfig(false);
			} catch (Exception e) {
				LOGGER.error("load config[" + configName + "] error: " + e.getMessage());
				return null;
			}
		}
		return config;
	}

	public static PropConfig loadPropConfig(String configName) {
		return loadPropConfig(configName, true);
	}
	

	public static SimpleProperties loadAsSimpleProperties(String configName) {
		return new SimpleProperties(configName);
	}
}
