package org.onetwo.boot.utils;

import java.util.Arrays;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class MediaUrlJsonSerializer extends ImageUrlJsonSerializer {

	public MediaUrlJsonSerializer() {
		super();
		this.fileTypes = Lists.newArrayList();
		// 视频
		this.fileTypes.addAll(Arrays.asList("mp3", "mp4", "avi", "mov"));
		// 图片
		this.fileTypes.addAll(Arrays.asList("jpg", "jpeg", "gif", "png", "bmp"));
		// 音频
		this.fileTypes.addAll(Arrays.asList("mp3","aac","flac","m4a"));
	}

}
