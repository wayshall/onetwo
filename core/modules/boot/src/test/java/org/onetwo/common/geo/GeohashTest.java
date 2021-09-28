package org.onetwo.common.geo;
/**
 * @author weishao zeng
 * <br/>
 */

import org.junit.Test;

import com.spatial4j.core.io.GeohashUtils;

public class GeohashTest {
	
	@Test
	public void test() {
		String geohash = GeohashUtils.encodeLatLon(31.308519, 120.58107);
		System.out.println("geohash: " + geohash);
	}

}
