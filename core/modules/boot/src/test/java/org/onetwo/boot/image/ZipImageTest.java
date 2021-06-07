package org.onetwo.boot.image;

import java.io.IOException;
import java.util.Arrays;

import net.coobird.thumbnailator.Thumbnails;

import org.junit.Test;

/**
 * @author wayshall
 * <br/>
 */
public class ZipImageTest {
	
	@Test
	public void testZip() throws Exception{
		String path = "F:/test.rar";
		Thumbnails.fromFilenames(Arrays.asList(path))
					.scale(1)
					.outputQuality(0.01)
					.toFile("d:/test/zip2.jpg");;
	}

}
