package org.onetwo.boot.utils;

import org.junit.Test;
import org.onetwo.boot.utils.ImageCompressor.ImageCompressorConfig;

/**
 * @author wayshall
 * <br/>
 */
public class ImageCompressorTest {
	
	@Test
	public void test(){
		ImageCompressor compressor = new ImageCompressor();
//		compressor.setScale(1);
//		compressor.setHeight(350);
		compressor.setConfig(ImageCompressorConfig.builder().scale(0.8).quality(0.7).build());
		compressor.compressTo("", "");
		
		/*compressor = new ImageCompressor();
		//不可以只设置width，同时又设置scale
		compressor.setConfig(ImageCompressorConfig.builder().width(350).build());
		compressor.compressTo("C:/Users/way/Desktop/bak/18da/hst.jpg", "C:/Users/way/Desktop/bak/18da/hst-compress.jpg");*/
	}
	
	@Test
	public void testOSX(){
		ImageCompressor compressor = new ImageCompressor();
		compressor.setConfig(ImageCompressorConfig.builder().scale(0.3).quality(0.3).build());
		compressor.compressTo("/Users/way/data/defulat_cover.jpg", 
				"/Users/way/data/defulat_cover_min.jpg");
		
	}

}
