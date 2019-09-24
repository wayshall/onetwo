package org.onetwo.boot.opencv;

import org.junit.Test;
import org.onetwo.boot.module.opencv.ImageMatchers;
import org.opencv.core.Core;

/**
 * -Djava.library.path=D:/mydev/java/opencv/opencv/build/java/x64
 * @author weishao zeng
 * <br/>
 */
public class ImageMatchersTest {
	
	@Test
	public void test() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        ImageMatchers.drawMatchedImage("g:/test/find-dest2.jpg", "g:/test/find-src.jpg");
    }

}

