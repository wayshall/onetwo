package org.onetwo.boot.module.opencv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nu.pattern.OpenCV;

@Configuration
public class OpencvConfiguration {
	static {
//      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		OpenCV.loadLocally();
	}
	
	@Bean
	public ImageComparator imageComparator() {
		return new ImageComparator();
	}
}
