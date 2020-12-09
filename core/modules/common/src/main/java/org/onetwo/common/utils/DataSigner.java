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
	
	default String sign(String signKey, long timestamp, Object request, String... excludeProperties) {
		SigningData data = new SigningData();
		data.setSigningKey(signKey);
		data.setTimestamp(timestamp);
		data.setParams(request);
		return sign(data, excludeProperties);
	}
	String sign(SigningData data, String... excludeProperties);
	void checkSign(SigningConfig config, SignableRequest signRequest, Object actualRequest, String... excludeProperties);

	public interface SignableRequest {
		public static final String FIELD_SIGNKEY = "signkey";
		public static final String FIELD_TIMESTAMP = "timestamp";
		
		
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
		private String signingKey;
		private long timestamp;
		/***
		 * 请求参数
		 */
		private Object params;
		private boolean debug;
		
		public SigningData() {
		}

		public SigningData(String signingKey, long timestamp, Object params) {
			super();
			this.signingKey = signingKey;
			this.timestamp = timestamp;
			this.params = params;
		}
		
		public String getSigningKey() {
			return signingKey;
		}
		public void setSigningKey(String signingKey) {
			this.signingKey = signingKey;
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
		private String signingKey;
		/***
		 * 是否打印源字符串和签名字符串，用于调试
		 */
		private boolean debug;
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
			return signingKey;
		}
		public void setSigningKey(String signingKey) {
			this.signingKey = signingKey;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}
		public boolean isDebug() {
			return debug;
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
			Assert.hasText(data.getSigningKey(), "signKey can not blank");
			Map<String, Object> requestMap = getBeanToMapConvertor(excludeProperties).toFlatMap(data.getParams());
			final String paramString = ParamUtils.comparableKeyMapToParamString(requestMap);
			String sourceString = data.getSigningKey() + paramString + data.getTimestamp();
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
		
		public void checkSign(SigningConfig config, SignableRequest signRequest, Object actualRequest, String... excludeProperties){
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
			
			Object request = actualRequest==null?signRequest:actualRequest;
			SigningData signingData = new SigningData(config.getSigningKey(), signRequest.getTimestamp(), request);
			signingData.setDebug(config.isDebug());
//			String sourceString = convertToSourceString(config.getSigningKey(), signRequest.getTimestamp(), request, excludeProperties);
			String sourceString = convertToSourceString(signingData, excludeProperties);
			MessageDigestHasher hasher = Hashs.sha1(false, CodeType.HEX);
			String hashString = hasher.hash(sourceString);
			if(config.isDebug()){
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
