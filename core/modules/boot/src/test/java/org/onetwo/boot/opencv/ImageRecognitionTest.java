package org.onetwo.boot.opencv;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.boot.module.opencv.ImageRecognition;
import org.opencv.core.Core;

public class ImageRecognitionTest {
	
	@Test
	public void test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String destImagePath = "g:/test/find-dest.jpg";
        String srcImagePath = "g:/test/find-src.jpg";
        
		ImageRecognition imageRecognition = new ImageRecognition();
		imageRecognition.setWriteDebugImage(true);
		imageRecognition.setDebugImageDir("g:/test");
		imageRecognition.init();
		
		boolean res = imageRecognition.match(destImagePath, srcImagePath);
		assertThat(res).isTrue();
	}

}

