package org.onetwo.boot.module.alioss;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.input.ReaderInputStream;
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
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketList;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
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

	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private OSSClient ossClient;
	private ClientConfiguration clinetConfig;
	
	public OssClientWrapper(String endpoint, String accessKeyId, String accessKeySecret) {
		super();
		this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
	}
	
	@Override
	public void destroy() throws Exception {
		this.ossClient.shutdown();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(clinetConfig==null){
			clinetConfig = new ClientConfiguration();
		}
		configClient(clinetConfig);
		this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, clinetConfig);
	}
	
	protected void configClient(ClientConfiguration clinetConfig){
	}


	public Bucket createBucketIfNotExists(String bucketName) {
		return getBucket(bucketName, true).get();
	}
	
	public void setClinetConfig(ClientConfiguration clinetConfig) {
		this.clinetConfig = clinetConfig;
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
	
	public ObjectOperation objectOperation(String bucketName, String key){
		return new ObjectOperation(bucketName, key, this);
	}
	
	public String storeWithFileName(String bucketName, File file, ObjectMetadata meta){
		String key = FileUtils.getFileName(file.getPath());
		objectOperation(bucketName, key).store(file, meta);
		return getUrl(true, bucketName, key);
	}
	
	public String store(String bucketName, File file, ObjectMetadata meta){
		String key = UUID.randomUUID().toString();
		objectOperation(bucketName, key).store(file, meta);
		return getUrl(true, bucketName, key);
	}
	
	public String getUrl(boolean https, String bucketName, String key){
		return OssProperties.buildUrl(https, endpoint, bucketName, key);
	}
	
	public PutObjectResult putObject(PutObjectRequest request){
		return ossClient.putObject(request);
	}
	
	public OSSClient getOssClient() {
		return ossClient;
	}
	
	public void deleteObject(String bucketName, String key){
		ossClient.deleteObject(bucketName, key);
	}

	static public class ObjectOperation {
		private String bucketName;
		private String key;
		private OssClientWrapper wrapper;
		private Optional<OSSObject> ossObject;
		private OSSClient ossClient;
		private PutObjectResult storeResult;
		public ObjectOperation(String bucketName, String key, OssClientWrapper wrapper) {
			super();
			this.bucketName = bucketName;
			this.key = key;
			this.wrapper = wrapper;
			this.ossClient = wrapper.ossClient;
		}

		public String getUrl(boolean https){
			return OssProperties.buildUrl(https, wrapper.endpoint, bucketName, key);
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
			return store(file, null);
		}
		
		public ObjectOperation store(File file, ObjectMetadata meta){
			if(!file.exists()){
				throw new BaseException("file is not exists!");
			}
			putObject(new PutObjectRequest(bucketName, key, file, meta));
			return this;
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
		
		public ObjectOperation store(InputStream in, ObjectMetadata meta){
			Assert.notNull(in);
			putObject(new PutObjectRequest(bucketName, key, in, meta));
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
		
		public ObjectOperation putObject(PutObjectRequest request){
			storeResult = wrapper.putObject(request);
			return this;
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

		public Optional<PutObjectResult> storeResult() {
			return Optional.ofNullable(storeResult);
		}
		
	}

}
