package org.onetwo.boot.module.cos;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.input.ReaderInputStream;
import org.onetwo.boot.module.alioss.OssProperties;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.ListBucketsRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;

/**
 * @author wayshall
 * <br/>
 */
public class CosClientWrapper implements InitializingBean, DisposableBean {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private CosProperties cosProperties;
	private COSClient cosClient;
	private ClientConfig clientConfig;
	
	public CosClientWrapper(CosProperties cosProperties) {
		super();
		this.cosProperties = cosProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(cosProperties.getAccessKey());
		Assert.hasText(cosProperties.getSecretKey());
		Assert.hasText(cosProperties.getRegionName());
		COSCredentials credentials = new BasicCOSCredentials(cosProperties.getAccessKey(), cosProperties.getSecretKey());
		if(clientConfig==null){
			clientConfig = cosProperties.getClient();
		}
		configClient(clientConfig);
		cosClient = new COSClient(credentials, clientConfig);
	}
	
	protected void configClient(ClientConfig clientConfig){
	}
	
	public Bucket createBucketIfNotExists(String bucketName) {
		return getBucket(bucketName, true).get();
	}


	public Optional<Bucket> getBucket(String bucketName, boolean createIfNotExist) {
		Bucket bucket = null;
		if (!cosClient.doesBucketExist(bucketName)) {
			if(!createIfNotExist){
				return Optional.empty();
			}
            /*
             * Create a new OSS bucket
             */
			if(logger.isInfoEnabled()){
				logger.info("Creating bucket {}", bucketName);
			}
			CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            bucket = cosClient.createBucket(createBucketRequest);
        }else{
        	bucket = new Bucket(bucketName);
        }
		return Optional.ofNullable(bucket);
	}
	
	@Override
	public void destroy() throws Exception {
		if(cosClient!=null){
			this.cosClient.shutdown();
		}
	}

	public List<Bucket> listBuckets(){
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
//        listBucketsRequest.setMaxKeys(500);
        return cosClient.listBuckets(listBucketsRequest);
	}
	
	public ObjectOperation objectOperation(String bucketName, String key){
		return new ObjectOperation(bucketName, key, this);
	}
	
	public String storeWithFileName(String bucketName, File file, ObjectMetadata meta){
		String key = FileUtils.getFileName(file.getPath());
		objectOperation(bucketName, key).store(file, meta);
		return getUploadUrl(true, bucketName, key);
	}
	
	public String store(String bucketName, File file, ObjectMetadata meta){
		String key = UUID.randomUUID().toString();
		objectOperation(bucketName, key).store(file, meta);
		return getUploadUrl(true, bucketName, key);
	}
	
	public String getDownloadUrl(boolean https, String bucketName, String key){
		return OssProperties.buildUrl(https, cosProperties.getDownloadEndPoint(), bucketName, key);
	}
	
	public String getUploadUrl(boolean https, String bucketName, String key){
		return OssProperties.buildUrl(https, cosProperties.getUploadEndPoint(), bucketName, key);
	}
	
	public PutObjectResult putObject(PutObjectRequest request){
		return cosClient.putObject(request);
	}
	
	public COSClient getCosClient() {
		return cosClient;
	}
	
	public void deleteObject(String bucketName, String key){
		cosClient.deleteObject(bucketName, key);
	}

	static public class ObjectOperation {
		private String bucketName;
		private String key;
		private CosClientWrapper wrapper;
		private Optional<COSObject> cosObject;
		private COSClient cosClient;
		private PutObjectResult storeResult;
		public ObjectOperation(String bucketName, String key, CosClientWrapper wrapper) {
			super();
			this.bucketName = bucketName;
			this.key = key;
			this.wrapper = wrapper;
			this.cosClient = wrapper.cosClient;
		}

		/*public String getUrl(boolean https){
			return OssProperties.buildUrl(https, wrapper.endpoint, bucketName, key);
		}*/
		
		public Optional<COSObject> getCosObject(){
			if(cosObject!=null){
				return cosObject;
			}
			if(!cosClient.doesObjectExist(bucketName, key)){
				cosObject = Optional.empty();
			}else{
				cosObject = Optional.ofNullable(cosClient.getObject(bucketName, key));
			}
			return cosObject;
		}
		
		public ObjectOperation store(File file){
			return store(file, null);
		}
		
		public ObjectOperation store(File file, ObjectMetadata meta){
			if(!file.exists()){
				throw new BaseException("file is not exists!");
			}
			PutObjectRequest putReq = new PutObjectRequest(bucketName, key, file);
			if(meta!=null){
				putReq.withMetadata(meta);
			}
			putObject(putReq);
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
			Optional<COSObject> opt = getCosObject();
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
			wrapper.cosClient.setObjectAcl(bucketName, key, access);
			return this;
		}

		public Optional<PutObjectResult> storeResult() {
			return Optional.ofNullable(storeResult);
		}
		
	}

}
