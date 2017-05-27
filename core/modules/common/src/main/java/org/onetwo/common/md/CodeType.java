package org.onetwo.common.md;

import org.apache.commons.codec.binary.Base64;
import org.onetwo.common.utils.LangUtils;

/**
 * @author wayshall
 * <br/>
 */
public enum CodeType {
	HEX{
		@Override
		public String encode(byte[] bytes, String charset) {
			return LangUtils.toHex(bytes);
		}

		@Override
		public byte[] decode(String source, String charset) {
			return LangUtils.hex2Bytes(source);
		}
	},
	BASE64{
		@Override
		public String encode(byte[] bytes, String charset) {
			return LangUtils.newString(Base64.encodeBase64(bytes), charset);
		}

		@Override
		public byte[] decode(String source, String charset) {
			return Base64.decodeBase64(LangUtils.getBytes(source, charset));
		}
	};
	
	abstract public String encode(byte[] bytes, String charset);
	abstract public byte[] decode(String source, String charset);

}
