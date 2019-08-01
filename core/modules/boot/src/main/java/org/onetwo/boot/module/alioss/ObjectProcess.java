package org.onetwo.boot.module.alioss;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.utils.LangUtils;
import org.springframework.util.Assert;

import com.aliyun.oss.model.ProcessObjectRequest;
import com.google.common.collect.Maps;

import groovy.transform.ToString;

/**
 * @author weishao zeng
 * <br/>
 */
public class ObjectProcess {
	
	/**
	 * x-oss-process:
	 * image/watermark
	 */
	private String action;
	private String sourceKey;
	private String targetKey;
	private String bucketName;
	
	private Map<String, AttrValue> attrs;
	
	final public void setAction(String action) {
		this.action = action;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public ObjectProcess put(String name, String value) {
		return put(name, value, false);
	}
	public ObjectProcess put(String name, String value, boolean encodeBase64) {
		AttrValue val = new AttrValue(value, encodeBase64);
		put(name, val);
		return this;
	}
	public ObjectProcess put(String name, AttrValue value) {
		if (attrs==null) {
			attrs = Maps.newHashMap();
		}
		attrs.put(name, value);
		return this;
	}
	
	static protected final String encode(String value) {
		return LangUtils.newString(Base64.encodeBase64(value.getBytes()));
	}
	
	public String toStyleWithName() {
		return toStyle("x-oss-process");
	}
	/***
	 * "%s|sys/saveas,o_%s,b_%s"
	 * @author weishao zeng
	 * @return
	 */
	public String toStyle(String name) {
		Assert.hasText(action, "action must has text");
		Assert.hasText(bucketName, "bucketName must has text");
		Assert.hasText(sourceKey, "sourceKey must has text");
		Assert.hasText(targetKey, "targetKey must has text");
		
		StringBuilder style = new StringBuilder();
		if (StringUtils.isNotBlank(name)) {
			style.append(name).append("=");
		}
		style.append(action);
		if (attrs!=null) {
			style.append(",");
			int index = 0;
			for(Entry<String, AttrValue> entry : attrs.entrySet()) {
				if (index!=0) {
					style.append(",");
				}
				style.append(entry.getKey()).append("_").append(entry.getValue().getValue());
				index++;
			}
		}
		style.append("|sys/saveas,")
			// target key name
			.append("o_").append(encode(targetKey)).append(",")
			// bucket name;
			.append("b_").append(encode(bucketName));
		String str = style.toString();
		return str;
	}
	
	public ProcessObjectRequest buildRequest() {
		ProcessObjectRequest req = new ProcessObjectRequest(bucketName, sourceKey, toStyle(null));
		return req;
	}

	@ToString
	static class AttrValue {
		final private Object value;
		final private boolean encodeBase64;
		final private boolean encodeUrl;
		public AttrValue(Object value, boolean encodeBase64) {
			this(value, encodeBase64, false);
		}
		
		public AttrValue(Object value, boolean encodeBase64, boolean encodeUrl) {
			super();
			this.value = value;
			this.encodeBase64 = encodeBase64;
			this.encodeUrl = encodeUrl;
		}
		
		public Object getValue() {
			Object val = encodeBase64?encode(value.toString()):value;
			val = encodeUrl?LangUtils.encodeUrl(val.toString()):val;
			return val;
		}
		
	}
}

