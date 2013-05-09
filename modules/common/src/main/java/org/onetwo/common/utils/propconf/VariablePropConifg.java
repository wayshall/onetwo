package org.onetwo.common.utils.propconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings({ "rawtypes", "serial" })
public class VariablePropConifg extends Properties implements VariableConfig {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private PropConfig propConfig;
	
	public VariablePropConifg(){
	}
	
	public VariablePropConifg(PropConfig propConfig){
		this.propConfig = propConfig;
	}
	
	public void setProperties(Properties properties){
		this.defaults = properties;
	}

	public Object get(Object key){
		if(propConfig!=null)
			return this.propConfig.getVariable((String)key);
		else
			return super.get(key);
	}
	
	public String getOriginalProperty(String key, String defaultValue) {
		Assert.notNull(key, "key can not be null!");
		return StringUtils.trimToEmpty(super.getProperty(key, defaultValue));
	}

	public String getOriginalProperty(String key) {
		Assert.notNull(key, "key can not be null!");
		return StringUtils.trimToEmpty(super.getProperty(key));
	}

	@Override
	public Enumeration configNames() {
		return this.propertyNames();
	}

	public boolean load(String srcpath){
		File file = null;
		boolean succeed = false;
		String filepath = srcpath;
		try {
			if (filepath.indexOf(".")==-1)
				filepath += PropUtils.CONFIG_POSTFIX;
			file = new File(filepath);
			if (!file.exists()) {
				if (filepath.indexOf(':') == -1) {
					filepath = FileUtils.getResourcePath(this.getClass().getClassLoader(), filepath);
					file = new File(filepath);
				}
			}
			if(file==null || !file.exists()){
				logger.info("1. file not exist : " + (file==null?"null":file.getPath()));
				InputStream fin = getInputStream(filepath);
				if(fin==null){
					throw new FileNotFoundException("no stream: " + filepath);
				}
				PropUtils.loadProperties(fin, this);
			}else{
				PropUtils.loadProperties(file, this);
			}
			succeed = true;
		} catch (Exception e) {
			InputStream in = null;
			try {
				if(logger.isInfoEnabled()){
					logger.error("load config error: " + e.getMessage());
					logger.info("try to load config by stream : " + filepath);
				}
				in = this.getClass().getClassLoader().getResourceAsStream(srcpath);
				load(in);
				succeed = true;
			} catch (Exception e1) {
				throw new BaseException("load config error: " + filepath, e);
			}
		}finally{
			logger.info("load config finished : "+succeed);
		}
		return succeed;
	}
	
	protected InputStream getInputStream(String path) throws Exception{
		if(FileUtils.isJarURL(path)){
			URL url = new URL(path);
			URLConnection con = url.openConnection();
			try {
				return con.getInputStream();
			}
			catch (IOException ex) {
				if (con instanceof HttpURLConnection) {
					((HttpURLConnection) con).disconnect();
				}
				throw ex;
			}
		}else{
			return new FileInputStream(new File(path));
		}
	}

	@Override
	public Object setOriginalProperty(String key, Object value) {
		return super.put(key, value);
	}

	@Override
	public String remove(String key) {
		Object val = super.remove(key);
		return val==null?null:val.toString();
	}

	public void setPropConfig(PropConfig propConfig) {
		this.propConfig = propConfig;
	}


}
