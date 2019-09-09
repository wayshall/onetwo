package org.onetwo.boot.module.alioss;
/**
 * @author weishao zeng
 * <br/>
 */

import org.junit.Test;
import org.onetwo.boot.module.alioss.ObjectProcess.AttrValue;

public class ObjectProcessTest {

	@Test
	public void testAttrValue() {
		AttrValue value = AttrValue.builder()
								.encodeBase64(true)
								.safeUrl(true)
								.value("panda.png?x-oss-process=image/resize,P_30")
								.build();
		String val = value.getValue().toString();
		System.out.println("value: " + val);
	}
}

