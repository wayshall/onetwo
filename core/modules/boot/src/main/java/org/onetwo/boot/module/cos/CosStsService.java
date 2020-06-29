package org.onetwo.boot.module.cos;

import java.util.TreeMap;

import org.json.JSONObject;
import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.tencent.cloud.CosStsClient;

/**
 * @author weishao zeng <br/>
 */

public class CosStsService {
	
	@Autowired
	private CosProperties cosProperties;
	@Autowired
	private QCloudBaseProperties baseProperties;
	
	public JSONObject getCredential() {
		TreeMap<String, Object> config = new TreeMap<String, Object>();

		try {
			// 云 api 密钥 SecretId
			config.put("secretId", baseProperties.getSecretId());
			// 云 api 密钥 SecretKey
			config.put("secretKey", baseProperties.getSecretKey());

//			if (properties.containsKey("https.proxyHost")) {
//				System.setProperty("https.proxyHost", properties.getProperty("https.proxyHost"));
//				System.setProperty("https.proxyPort", properties.getProperty("https.proxyPort"));
//			}

			// 设置域名
			// config.put("host", "sts.internal.tencentcloudapi.com");

			// 临时密钥有效时长，单位是秒
			config.put("durationSeconds", cosProperties.getSts().getDurationInSeconds());

			// 换成你的 bucket
			config.put("bucket", cosProperties.getAppBucketName());
			// 换成 bucket 所在地区
			config.put("region", cosProperties.getRegionName());

			// 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险,
			// 请谨慎评估使用)
//			config.put("allowPrefix", cosProperties.getSts().getAllowPrefix());
			// 可以通过 allowPrefixes 指定前缀数组
            config.put("allowPrefixes", cosProperties.getSts().getAllowPrefixs());

			// 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看
			// https://cloud.tencent.com/document/product/436/31923
			String[] allowActions = cosProperties.getSts().getAllowActions();
			config.put("allowActions", allowActions);

			JSONObject credential = CosStsClient.getCredential(config);
//			System.out.println(credential.toString(4));
			return credential;
		} catch (Exception e) {
			throw new BaseException("生成cos临时秘钥错误！", e);
		}
	}

}
