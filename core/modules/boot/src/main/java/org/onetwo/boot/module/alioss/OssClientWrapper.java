package org.onetwo.boot.module.alioss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.module.alioss.OssProperties.WaterMaskProperties;
import org.onetwo.boot.module.alioss.image.ResizeAction;
import org.onetwo.boot.module.alioss.image.WatermarkAction;
import org.onetwo.boot.module.alioss.video.SnapshotAction;
import org.onetwo.boot.module.alioss.video.SnapshotProperties;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketList;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.GenericResult;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

/**
 * @author wayshall
 * <br/>
 */
public class OssClientWrapper implements InitializingBean, DisposableBean {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	/*private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;*/
	private OssProperties ossProperties;
	private OSSClient ossClient;
	private ClientConfiguration clientConfig;
	
	public OssClientWrapper(OssProperties ossProperties) {
		super();
		/*this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;*/
		this.ossProperties = ossProperties;
	}
	
	@Override
	public void destroy() throws Exception {
		this.ossClient.shutdown();
	}

	/***
	 * alioss底层使用了httpclient，在没有设置超时的情况下，使用后没有及时关闭资源，导致死锁的bug
	 * 
	 * https://www.cnblogs.com/549294286/p/11241277.html
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		configClient(clientConfig);
		DefaultCredentialProvider credential = new DefaultCredentialProvider(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
		this.ossClient = new OSSClient(ossProperties.getEndpoint(), 
										credential, 
										clientConfig);
	}
	
	protected void configClient(ClientConfiguration clinetConfig){
		if(clientConfig==null){
			clientConfig = new ClientConfiguration();
			// 从httpclient连接池获取连接时等待的超时时间，默认-1，表示阻塞，直接到连接获取为止
			// 从连接池获取连接的timeout. MainClientExec.execute -> PoolingHttpClientConnectionManager.requestConnection
			clientConfig.setConnectionRequestTimeout(30_000); // 设置为30秒
			// 客户端和服务器建立连接的timeout
			clientConfig.setConnectionTimeout(30_000);
			// 连接建立后，request没有回应的timeout
			clientConfig.setSocketTimeout(30_000);
			// 决定是使用com.aliyun.oss.common.comm.TimeoutServiceClient，还是 com.aliyun.oss.common.comm.DefaultServiceClient
			clientConfig.setRequestTimeoutEnabled(true);
			// 默认5分钟
			clientConfig.setRequestTimeout(ClientConfiguration.DEFAULT_REQUEST_TIMEOUT);
			// 默认1024 ClientConfiguration.DEFAULT_MAX_CONNECTIONS
			clientConfig.setMaxConnections(ClientConfiguration.DEFAULT_MAX_CONNECTIONS);
		} else {
			// 没有设置，则强制设置超时
			if (clientConfig.getConnectionRequestTimeout()==ClientConfiguration.DEFAULT_CONNECTION_REQUEST_TIMEOUT) {
				clientConfig.setConnectionRequestTimeout(30_000); // 设置为30秒
			}
		}
	}


	public Bucket createBucketIfNotExists(String bucketName) {
		return getBucket(bucketName, true).get();
	}
	
	public void setClientConfig(ClientConfiguration clinetConfig) {
		this.clientConfig = clinetConfig;
	}

	public Optional<Bucket> getBucket(String bucketName, boolean createIfNotExist) {
		Bucket bucket = null;
		if (!ossClient.doesBucketExist(bucketName)) {
			if(!createIfNotExist){
				return Optional.empty();
			}
            /*
             * Create a new OSS bucket
             */
			if(logger.isInfoEnabled()){
				logger.info("Creating bucket {}", bucketName);
			}
//            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            bucket = ossClient.createBucket(createBucketRequest);
        }else{
        	bucket = ossClient.getBucketInfo(bucketName).getBucket();
        }
		return Optional.ofNullable(bucket);
	}

	public List<Bucket> listBuckets(){
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setMaxKeys(500);
        BucketList buckList = ossClient.listBuckets(listBucketsRequest);
        return buckList.getBucketList();
	}
	
	public ObjectOperation objectOperation(String key){
		return new ObjectOperation(ossProperties.getBucketName(), key, this);
	}
	
	public ObjectOperation objectOperation(String bucketName, String key){
		return new ObjectOperation(bucketName, key, this);
	}
	
	public String storeWithFileName(String bucketName, File file, ObjectMetadata meta){
		String key = FileUtils.getFileName(file.getPath());
		objectOperation(bucketName, key).store(file, meta, null);
		return getUrl(true, bucketName, key);
	}
	
	public String store(String bucketName, File file, ObjectMetadata meta){
		String key = UUID.randomUUID().toString();
		objectOperation(bucketName, key).store(file, meta, null);
		return getUrl(true, bucketName, key);
	}
	
	public String getUrl(boolean https, String bucketName, String key){
		return OssProperties.buildUrl(https, ossProperties.getEndpoint(), bucketName, key);
	}
	
	public PutObjectResult putObject(PutObjectRequest request){
		PutObjectResult result = null;
		try {
			result = ossClient.putObject(request);
		} finally {
			closeReponse(result);
		}
		return result;
	}
	
	private void closeReponse(GenericResult result) {
		if (result!=null) {
			try {
				result.getResponse().getHttpResponse().close();
			} catch (IOException e) {
				logger.error("oss关闭httpclient连接时出错：" + e.getMessage(), e);
			}
		}
	}
	
	public boolean doesObjectExist(String objectKey){
		if (StringUtils.isBlank(objectKey)) {
			throw new IllegalArgumentException("illegal objectKey : " + objectKey);
		}
		return ossClient.doesObjectExist(ossProperties.getBucketName(), objectKey);
	}
	
	public boolean doesObjectExist(String bucketName, String objectKey){
		if (StringUtils.isBlank(objectKey)) {
			throw new IllegalArgumentException("illegal objectKey : " + objectKey);
		}
		return ossClient.doesObjectExist(bucketName, objectKey);
	}
	
	public OSSClient getOssClient() {
		return ossClient;
	}
	
	public void deleteObject(String bucketName, String key){
		ossClient.deleteObject(bucketName, key);
	}

	static public class ObjectOperation {
		protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
		
		private String bucketName;
		private String key;
		private OssClientWrapper wrapper;
		private Optional<OSSObject> ossObject;
		private OSSClient ossClient;
//		private PutObjectResult storeResult;
		public ObjectOperation(String bucketName, String key, OssClientWrapper wrapper) {
			super();
			this.bucketName = bucketName;
			this.key = key;
			this.wrapper = wrapper;
			this.ossClient = wrapper.ossClient;
		}

		public String getUrl(boolean https){
			return OssProperties.buildUrl(https, wrapper.ossProperties.getEndpoint(), bucketName, key);
		}
		
		public Optional<OSSObject> getOSSObject(){
			if(ossObject!=null){
				return ossObject;
			}
			if(!ossClient.doesObjectExist(bucketName, key)){
				ossObject = Optional.empty();
			}else{
				ossObject = Optional.ofNullable(ossClient.getObject(bucketName, key));
			}
			return ossObject;
		}
		
		public ObjectOperation store(File file){
			return store(file, null, null);
		}
		
		public ObjectOperation store(File file, ObjectMetadata meta, Consumer<PutObjectResult> onCompleted){
			if(!file.exists()){
				throw new BaseException("file is not exists!");
			}
			PutObjectResult result = putObject(new PutObjectRequest(bucketName, key, file, meta));
			if (onCompleted!=null) {
				onCompleted.accept(result);
			} else {
				if (result.getResponse()!=null && !result.getResponse().isSuccessful()) {
					throw new BaseException("uplaod to oss error: " + result.getResponse().getErrorResponseAsString());
				} else {
					logger.info("store result: {}",  result);
				}
			}
			return this;
		}
		
		public ObjectOperation resize(String targetKey, ResizeProperties config) {
			return resize(targetKey, config, null);
		}
		
		public ObjectOperation resize(ResizeProperties config, Consumer<String> onSuccess) {
			if (config==null || !config.isEnabled()) {
				return this;
			}
			String dirPath = FileUtils.getParentpath(key);
			String keyWithoutPostfix = FileUtils.getFileNameWithoutExt(key);
			String resizeKey = dirPath + "/" + keyWithoutPostfix + "-min" + FileUtils.getExtendName(key, true);
			resize(resizeKey, config, result -> {
				if (!result.getResponse().isSuccessful()) {
					throw new BaseException("resize error: " + result.getResponse().getErrorResponseAsString());
				} else if (onSuccess!=null) {
					onSuccess.accept(resizeKey);
				}
			});
			return this;
		}
		
		/***
		 * 缩放
		 * @author weishao zeng
		 * @param targetKey
		 * @param config
		 * @param onCompleted
		 * @return
		 */
		public ObjectOperation resize(String targetKey, ResizeProperties config, Consumer<GenericResult> onCompleted) {
			Assert.hasText(targetKey, "targetKey must has text!");
			Assert.notNull(config, "resize config can not be null!");
			ResizeAction obj = new ResizeAction();
			obj.setBucketName(bucketName);
			obj.setSourceKey(key);
			obj.setTargetKey(targetKey);
			obj.configBy(config);
			GenericResult result = this.ossClient.processObject(obj.buildRequest());
			if (onCompleted!=null) {
				onCompleted.accept(result);
			} else {
				if (!result.getResponse().isSuccessful()) {
					throw new BaseException("resize error: " + result.getResponse().getErrorResponseAsString());
				}
			}
			return this;
		}
		
		/***
		 * 原图加水印
		 * @author weishao zeng
		 * @param config
		 * @return
		 */
		public ObjectOperation watermask(WaterMaskProperties config) {
			return watermask(key, config, null);
		}
		
		/****
		 * 生成水印
		 * @author weishao zeng
		 * @param targetKey
		 * @param config
		 * @param onCompleted
		 * @return
		 */
		public ObjectOperation watermask(String targetKey, WaterMaskProperties config, Consumer<GenericResult> onCompleted) {
			if (!config.isEnabled()) {
				logger.warn("water mask config is disabled.");
				return this;
			}
			String fileExt = FileUtils.getExtendName(key);
			if (!config.isSupportFileType(fileExt)) {
				logger.warn("water mask unsupport the file ext: {}, supported file types: {}", fileExt, config.getSupportFileTypes());
				return this;
			}
			
			WatermarkAction obj = new WatermarkAction();
			obj.setBucketName(bucketName);
			obj.setSourceKey(key);
			obj.setTargetKey(targetKey);
			obj.configBy(config);
			
			if (logger.isInfoEnabled()) {
				logger.info("watermask style: {}", obj.toStyleWithName());
			}
			
			GenericResult result = this.ossClient.processObject(obj.buildRequest());
			if (onCompleted!=null) {
				onCompleted.accept(result);
			} else {
				if (!result.getResponse().isSuccessful()) {
					throw new BaseException("watermask error: " + result.getResponse().getErrorResponseAsString());
				}
			}
			return this;
		}
		

		public ObjectOperation videoSnapshot(SnapshotProperties config, Consumer<String> onSuccess) {
			String dirPath = FileUtils.getParentpath(key);
			String keyWithoutPostfix = FileUtils.getFileNameWithoutExt(key);
			String cutImageKey = dirPath + "/" + keyWithoutPostfix + "-cover." + config.getFormat().name().toLowerCase();
			videoSnapshot(cutImageKey, config, result -> {
				if (result.getResponse()!=null && !result.getResponse().isSuccessful()) {
					throw new BaseException("video snapshot error: " + result.getResponse().getErrorResponseAsString());
				}else if (onSuccess!=null) {
					onSuccess.accept(cutImageKey);
				}
			});
			return this;
		}
		
		public Optional<String> videoSnapshot(String targetKey, SnapshotProperties config, Consumer<GenericResult> onCompleted) {
			if (!config.isEnabled()) {
				logger.warn("video snapshot config is disabled.");
				return Optional.empty();
			}
			String fileExt = FileUtils.getExtendName(key);
			if (!config.isSupportFileType(fileExt)) {
				logger.warn("video snapshot unsupport the file ext: {}, video snapshot file types: {}", fileExt, config.getSupportFileTypes());
				return Optional.empty();
			}
			SnapshotAction obj = new SnapshotAction();
			obj.setBucketName(bucketName);
			obj.setSourceKey(key);
			obj.setTargetKey(targetKey);
			obj.configBy(config);
//			System.out.println("style: " + obj.toStyleWithName());
			GenericResult result = this.ossClient.processObject(obj.buildRequest());
			if (onCompleted!=null) {
				onCompleted.accept(result);
			} else {
				if (result.getResponse()!=null && !result.getResponse().isSuccessful()) {
					throw new BaseException("video snapshot error: " + result.getResponse().getErrorResponseAsString());
				} else {
					logger.info("videoSnapshot result: {}", result);
					return Optional.of(targetKey);
				}
			}
			return Optional.empty();
		}
		
		public ObjectOperation store(InputStream in){
			return store(in, null);
		}
		
		public ObjectOperation storeAsJson(Object object){
			/*String json = JsonMapper.DEFAULT_MAPPER.toJson(object);
			StringReader sr = new StringReader(json);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			return store(new ReaderInputStream(sr), meta);*/
			return storeAsJson(object, MediaType.APPLICATION_JSON_UTF8_VALUE);
		}
		
		/****
		 * 
		 * @author wayshall
		 * @param object
		 * @param contentType example: MediaType.APPLICATION_OCTET_STREAM_VALUE
		 * @return
		 */
		public ObjectOperation storeAsJson(Object object, String contentType){
			String json = JsonMapper.DEFAULT_MAPPER.toJson(object);
			StringReader sr = new StringReader(json);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			return store(new ReaderInputStream(sr), meta);
		}
		
		public ObjectOperation store(InputStream inputStream, ObjectMetadata meta){
			Assert.notNull(inputStream, "inputStream can not be null");
			putObject(new PutObjectRequest(bucketName, key, inputStream, meta));
			return this;
		}
		
		public ObjectOperation override(File file){
			Optional<OSSObject> opt = getOSSObject();
			if(!opt.isPresent()){
				throw new BaseException("key["+key+"] is not exists in bucket["+bucketName+"]");
			}
			return store(file);
		}
		
		public ObjectOperation delete(){
			wrapper.deleteObject(bucketName, key);
			return this;
		}
		
		public PutObjectResult putObject(PutObjectRequest request){
			PutObjectResult result = wrapper.putObject(request);
			return result;
		}
		
		public ObjectOperation accessPrivate(){
			access(CannedAccessControlList.Private);
			return this;
		}
		
		public ObjectOperation accessPublicRead(){
			access(CannedAccessControlList.PublicRead);
			return this;
		}
		
		public ObjectOperation access(CannedAccessControlList access){
			wrapper.ossClient.setObjectAcl(bucketName, key, access);
			return this;
		}

		/*public Optional<PutObjectResult> storeResult() {
			return Optional.ofNullable(storeResult);
		}*/
		
	}

}
