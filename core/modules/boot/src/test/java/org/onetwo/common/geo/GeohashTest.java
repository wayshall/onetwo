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
		
		// 精度为5，为一公里以内
		geohash = GeohashUtils.encodeLatLon(31.304607, 120.605975, 5);
		System.out.println("geohash: " + geohash);
		geohash = GeohashUtils.encodeLatLon(31.309172, 120.607154, 5);
		System.out.println("geohash: " + geohash);
	}
	

}
