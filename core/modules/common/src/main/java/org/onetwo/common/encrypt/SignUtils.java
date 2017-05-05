package org.onetwo.common.encrypt;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

/**
 * @author wayshall
 * <br/>
 */
public abstract class SignUtils {
	private static final Logger logger = JFishLoggerFactory.getLogger(SignUtils.class); 
	private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String signWithRSA(String privateKey, String content) {
    	return signWithRSA(privateKey, content, LangUtils.UTF8);
    }
    
    public static String signWithRSA(String privateKey, String content, String charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(LangUtils.getBytes(privateKey)));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            byte[] bytes = Base64.encodeBase64(signed);
            return LangUtils.newString(bytes, charset);
        } catch (Exception e) {
        	logger.error("sign error, content: {}", content);
            throw new BaseException("sign error", e);
        }
    }
}
