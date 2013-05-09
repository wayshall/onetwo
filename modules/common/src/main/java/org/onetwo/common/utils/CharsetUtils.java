package org.onetwo.common.utils;

import java.nio.charset.Charset;

public final class CharsetUtils {
	
	private CharsetUtils(){}

	public static final String UTF_8 = "utf-8";
	public static final String GBK = "GBK";
	public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);
	public static final Charset CHARSET_GBK = Charset.forName(GBK);

}
