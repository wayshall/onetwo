package org.onetwo.boot.utils;

import org.junit.Test;

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
		compressor.setWidth(350);
		compressor.compressTo("C:/Users/way/Desktop/bak/18da/a.jpg", "C:/Users/way/Desktop/bak/18da/a-compress.jpg");
		
		compressor = new ImageCompressor();
		//不可以只设置width，同时又设置scale
		compressor.setWidth(350);
		compressor.compressTo("C:/Users/way/Desktop/bak/18da/hst.jpg", "C:/Users/way/Desktop/bak/18da/hst-compress.jpg");
	}

}
