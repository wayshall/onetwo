package org.onetwo.boot.module.qrcode;

import org.junit.Test;

public class QRCodeCreatorTest {
	
	@Test
	public void test() {
		QRCodeCreator creator = new QRCodeCreator("test");
		creator.writeTo("C:\\Users\\way\\Desktop\\data\\qrcode.png");
	}

}
