package org.onetwo.boot.compress;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.List;

import org.junit.Test;
import org.onetwo.boot.utils.ImageCompressor.ImageCompressorConfig;

public class ImageBatchCompressorTest {
	
	@Test
	public void testFindMatchFiles() {
		ImageBatchCompressor compressor = new ImageBatchCompressor();
		compressor.setBaseDir("文件夹");
		compressor.setAntPattern("*.png");
		compressor.initialize();
		compressor.findMatchFiles().forEach(file -> {
			System.out.println("file: " + file);
		});
	}

	@Test
	public void testFompress() {
		ImageBatchCompressor compressor = new ImageBatchCompressor();
		compressor.setBaseDir("文件夹");
		compressor.setCompressedDir("压缩后\"文件夹");
		compressor.setAntPattern("*.png");
		compressor.setCompressThresholdSize("1mb");
		compressor.setCompressorConfig(ImageCompressorConfig.builder().scale(0.2).quality(0.1).build());
		compressor.initialize();
		List<String> compressList = compressor.compress();
		compressList.stream().forEach(file -> {
			System.out.println("file: " + file);
		});
	}
}

