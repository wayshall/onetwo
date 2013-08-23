package org.onetwo.common.spring.web.mvc;

import java.util.Properties;

import org.onetwo.common.utils.propconf.PropertiesWraper;

public class MvcSetting {
	public static final String MAX_UPLOAD_SIZE = "max_upload_size";
	private PropertiesWraper wraper;
	
	public MvcSetting(Properties prop){
		this.wraper = new PropertiesWraper(prop);
	}
	
	public int getMaxUploadSize(){
		int maxUpload = this.wraper.getInt(MAX_UPLOAD_SIZE, 1024*1024*10);
		return maxUpload;
	}

}
