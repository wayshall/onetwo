package org.onetwo.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.onetwo.common.utils.LangUtils;

public enum ByteCoders {
	
	HEX {
		public String encode(byte[] data, String encoding) {
			String result = ByteUtils.toHexString(data);
			return result;
		}
		
		public byte[] decode(String data) {
			byte[] bytes = ByteUtils.fromHexString(data);
			return bytes;
		}
	},
	
	BASE64 {
		public String encode(byte[] data, String encoding) {
			byte[] result = Base64.encodeBase64(data);
			return LangUtils.newString(result, encoding);
		}
		
		public byte[] decode(String data) {
			byte[] bytes = Base64.decodeBase64(data);
			return bytes;
		}
	};
	
	
	abstract public String encode(byte[] data, String encoding);
	abstract public byte[] decode(String data);

}
