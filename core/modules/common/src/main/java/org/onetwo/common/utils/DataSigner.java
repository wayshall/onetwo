package org.onetwo.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.onetwo.common.annotation.IgnoreField;
import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.md.CodeType;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.md.MessageDigestHasher;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public interface DataSigner {
	
	default String sign(String secretkey, long timestamp, Object request, String... excludeProperties) {
		SigningData data = new SigningData();
		data.setSecretkey(secretkey);
		data.setTimestamp(timestamp);
		data.setParams(request);
		return sign(data, excludeProperties);
	}
	String sign(SigningData data, String... excludeProperties);
	
	void checkSign(SigningCheckableData checkableData, String... excludeProperties);
	
	@Deprecated
	default void checkSign(SigningConfig config, SignableRequest signRequest, Object actualRequest, String... excludeProperties) {
		SigningCheckableData sdata = new SigningCheckableData();
		sdata.setDebug(false);
		sdata.setSigningConfig(config);
		sdata.setSignRequest(signRequest);
		checkSign(sdata, excludeProperties);
	}

	public interface SignableRequest {
		public static final String FIELD_SIGNKEY = "signkey";
		public static final String FIELD_TIMESTAMP = "timestamp";
		
		/***
		 * 参数和秘钥签名后的字符串
		 * @author weishao zeng
		 * @return
		 */
		public String getSignkey();
		public Long getTimestamp();
	}
	
	public class BaseSignableRequest implements SignableRequest {
		public static final String FIELD_SIGNKEY = "signkey";
		public static final String FIELD_TIMESTAMP = "timestamp";
		@IgnoreField
		private String signkey;
		@IgnoreField
		private Long timestamp;
		
		public String getSignkey() {
			return signkey;
		}
		public void setSignkey(String signkey) {
			this.signkey = signkey;
		}
		public Long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}
	}
	

	
	public class SigningData {
		private String secretkey;
		private long timestamp;
		/***
		 * 请求参数
		 */
		private Object params;
		private boolean debug;
		
		public SigningData() {
		}

		public SigningData(String secretkey, long timestamp, Object params) {
			super();
			this.secretkey = secretkey;
			this.timestamp = timestamp;
			this.params = params;
		}

		public String getSecretkey() {
			return secretkey;
		}

		public void setSecretkey(String secretkey) {
			this.secretkey = secretkey;
		}

		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public Object getParams() {
			return params;
		}
		public void setParams(Object params) {
			this.params = params;
		}

		public boolean isDebug() {
			return debug;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}
	}
	
	public class SigningConfig {
		private int maxDelayTimeInSeconds = 60*3;
		/***
		 * 签名秘钥
		 */
		private String secretkey;
		/***
		 * 若小于0，则不判断重放攻击
		 * @author weishao zeng
		 * @return
		 */
		public int getMaxDelayTimeInSeconds() {
			return maxDelayTimeInSeconds;
		}
		public void setMaxDelayTimeInSeconds(int maxDelayTimeInSeconds) {
			this.maxDelayTimeInSeconds = maxDelayTimeInSeconds;
		}
		public String getSigningKey() {
			return secretkey;
		}
		public void setSigningKey(String signingKey) {
			this.secretkey = signingKey;
		}

		public String getSecretkey() {
			return secretkey;
		}
		public void setSecretkey(String secretkey) {
			this.secretkey = secretkey;
		}
	}
	
	public class SigningCheckableData {
		private SigningConfig signingConfig;
		private SignableRequest signRequest;

		/***
		 * 是否打印源字符串和签名字符串，用于调试
		 */
		private boolean debug;

		public SigningCheckableData() {
		}
		
		public SigningCheckableData(SigningConfig signingConfig, SignableRequest signRequest) {
			super();
			this.signingConfig = signingConfig;
			this.signRequest = signRequest;
		}
		
		public void setDebug(boolean debug) {
			this.debug = debug;
		}
		public boolean isDebug() {
			return debug;
		}
		public SigningConfig getSigningConfig() {
			return signingConfig;
		}
		public void setSigningConfig(SigningConfig signingConfig) {
			this.signingConfig = signingConfig;
		}
		public SignableRequest getSignRequest() {
			return signRequest;
		}
		public void setSignRequest(SignableRequest signRequest) {
			this.signRequest = signRequest;
		}
	}
	
	
	public class DefaultDataSigner implements DataSigner {
		final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	
		protected BeanToMapConvertor getBeanToMapConvertor(String... excludeProperties){
			List<String> excludes = Lists.newArrayList();
			excludes.add(BaseSignableRequest.FIELD_SIGNKEY);
			excludes.add(BaseSignableRequest.FIELD_TIMESTAMP);
			excludes.addAll(Arrays.asList(excludeProperties));
			BeanToMapConvertor convertor = BeanToMapBuilder.newBuilder()
															.ignoreNull()
															.excludeProperties(excludes.toArray(new String[0]))
															.enableFieldNameAnnotation()
															.build();
			return convertor;
		}
		
		protected String convertToSourceString(SigningData data, String... excludeProperties){
			Assert.hasText(data.getSecretkey(), "secretkey can not blank");
			Map<String, Object> requestMap = getBeanToMapConvertor(excludeProperties).toFlatMap(data.getParams());
			final String paramString = ParamUtils.comparableKeyMapToParamString(requestMap);
			String sourceString = data.getSecretkey() + paramString + data.getTimestamp();
			if(data.isDebug()){
				logger.info("param string: {}", paramString);
				logger.info("source string: {}", sourceString);
			}
			return sourceString;
		}
		
		public String sign(SigningData data, String... excludeProperties){
			String sourceString = convertToSourceString(data, excludeProperties);
			MessageDigestHasher hasher = Hashs.sha1(false, CodeType.HEX);
			String hashString = hasher.hash(sourceString);
			if(data.isDebug()){
				logger.info("sign source string: {}", sourceString);
				logger.info("sign hash string: {}", hashString);
			}
			return hashString;
		}
		
		protected Long getTimestampInMills(long timestampInSeconds) {
			return timestampInSeconds * 1000;
		}
		
		public void checkSign(SigningCheckableData checkableData, String... excludeProperties){
			SigningConfig config = checkableData.getSigningConfig();
			SignableRequest signRequest = checkableData.getSignRequest();
			
			if(signRequest.getTimestamp()==null){
				throw new ServiceException(SignErrors.TIMESTAMP_ERR);
			}
			if(StringUtils.isBlank(signRequest.getSignkey())){
				throw new ServiceException(SignErrors.SIGNKEY_ERR);
			}
			if(config==null){
				throw new ServiceException(SignErrors.CONFIG_ERR);
			}
			long timestampInMills = getTimestampInMills(signRequest.getTimestamp());
			DateTime requestTime = new DateTime(timestampInMills);
			//如果请求时间+延迟时间后少于当前时间，则可能是攻击
			this.checkMaxDelayTimeError(requestTime, config);
			
//			Object request = actualRequest==null?signRequest:actualRequest;
			SigningData signingData = new SigningData(config.getSigningKey(), signRequest.getTimestamp(), signRequest);
			signingData.setDebug(checkableData.isDebug());
//			String sourceString = convertToSourceString(config.getSigningKey(), signRequest.getTimestamp(), request, excludeProperties);
			String sourceString = convertToSourceString(signingData, excludeProperties);
			MessageDigestHasher hasher = Hashs.sha1(false, CodeType.HEX);
			String hashString = hasher.hash(sourceString);
			if(checkableData.isDebug()){
				logger.info("checkSign hash string: {}", hashString);
			}
			
			if(!hasher.checkHash(sourceString, signRequest.getSignkey())){
				throw new ServiceException(SignErrors.INVALID_SIGNKEY);
			}
		}
		
		protected void checkMaxDelayTimeError(DateTime requestTime, SigningConfig config) {
			//如果请求时间+延迟时间后少于当前时间，则可能是攻击
			if(config.getMaxDelayTimeInSeconds()>0 && requestTime.plusSeconds(config.getMaxDelayTimeInSeconds()).isBefore(DateTime.now())){
				throw new ServiceException(SignErrors.INVALID_INVOKE);
			}
		}
		
	}
	

	public enum SignErrors implements ErrorType {

		TIMESTAMP_ERR("时间戳不能为空"),
		SIGNKEY_ERR("签名不能为空"),
		CONFIG_ERR("服务端配置错误"),
		INVALID_INVOKE("非法调用"),
		INVALID_SIGNKEY("非法签名");

		final private String message;

		private SignErrors(String message) {
			this.message = message;
		}

		@Override
		public String getErrorCode() {
			return name();
		}

		public String getErrorMessage() {
			return message;
		}

		@Override
		public Integer getStatusCode() {
			return 200;
		}

	}
	
}
