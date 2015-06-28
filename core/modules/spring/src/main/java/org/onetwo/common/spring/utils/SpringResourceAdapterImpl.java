package org.onetwo.common.spring.utils;

import java.io.File;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.propconf.ResourceAdapterImpl;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;

public class SpringResourceAdapterImpl extends ResourceAdapterImpl<Resource> {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
			
	public SpringResourceAdapterImpl(Resource resource) {
		super(resource);
	}
	public boolean isSupportedToFile() {
		try {
			return resource.getFile()!=null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public File getFile() {
		try {
			return resource.getFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}


	@Override
	public String getName() {
		try {
			return resource.getFilename();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return resource.toString();
		}
	}
	
	public String toString(){
		return resource.toString();
	}
	

}
