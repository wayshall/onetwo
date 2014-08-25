package org.onetwo.common.spring.web.mvc;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.utils.propconf.JFishProperties;

public class MvcSetting {
	public static final String MAX_UPLOAD_SIZE = "max_upload_size";
	public static final String ALLOWED_FILE_TYPES = "allowed_file_types";
	
	private JFishProperties wraper;
	private final Properties mvcSetting;
	
	public MvcSetting(Properties prop){
		this.mvcSetting = prop;
		this.wraper = new JFishProperties(prop);
	}
	
	/***
	 * byte
	 * @return
	 */
	public int getMaxUploadSize(){
		int maxUpload = this.wraper.getInt(MAX_UPLOAD_SIZE, 1024*1024*10);
		return maxUpload;
	}
	
	public List<String> getAllowedFileTypes(){
		return this.wraper.getStringList(ALLOWED_FILE_TYPES, ",");
	}

	public Properties getMvcSetting() {
		return mvcSetting;
	}

}
