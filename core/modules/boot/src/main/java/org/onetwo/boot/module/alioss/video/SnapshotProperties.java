package org.onetwo.boot.module.alioss.video;

import java.util.Arrays;
import java.util.List;

import org.onetwo.boot.module.alioss.video.SnapshotAction.Formats;
import org.onetwo.boot.module.alioss.video.SnapshotAction.Modes;
import org.onetwo.boot.module.alioss.video.SnapshotAction.Rotates;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class SnapshotProperties {
	boolean enabled;
	/***
	 * 单位ms，[0,视频时长]
	 * 超过视频最长时间，则截取最后一祯（试验所得)
	 */
	int time;
	int width;
	int height;
	Modes mode;
	Rotates rotate;
	Formats format = Formats.JPG;

	List<String> supportFileTypes = Arrays.asList("mp4");

	public boolean isSupportFileType(String postfix) {
		return supportFileTypes.contains(postfix.toLowerCase());
	}
}

