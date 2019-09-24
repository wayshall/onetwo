package org.onetwo.boot.module.alioss.video;
/**
 * https://help.aliyun.com/document_detail/64555.html?spm=a2c4g.11186623.6.1410.49865bbf4koafb
 * 
 * @author weishao zeng
 * <br/>
 */

import org.onetwo.boot.module.alioss.ObjectProcess;

public class SnapshotAction extends ObjectProcess<SnapshotProperties> {
	/**
	 * image/resize
	 */
	public SnapshotAction() {
		setAction("video/snapshot");
	}
	
	/***
	 * 截图时间
	 * @author weishao zeng
	 * @param time
	 */
	public void setTime(int time) {
		put("t", time);
	}
	
	/***
	 * 截图宽度，如果指定为0则自动计算
	 * @author weishao zeng
	 * @param width
	 */
	public void setWidth(int width) {
		put("w", width);
	}

	/***
	 * 截图高度，如果指定为0则自动计算，如果w和h都为0则输出为原视频宽高
	 * @author weishao zeng
	 * @param height
	 */
	public void setHeight(int height) {
		put("h", height);
	}
	
	/***
	 * 截图模式，不指定则为默认模式，根据时间精确截图，如果指定为fast则截取该时间点之前的最近的一个关键帧
	 * 枚举值：fast
	 * @author weishao zeng
	 */
	public void setFastMode(Modes fastMode) {
		put("m", fastMode.name().toLowerCase());
	}
	
	/***
	 * 是否根据视频信息自动旋转，如果指定为auto则会在截图生成之后根据视频旋转信息进行自动旋转
	 * @author weishao zeng
	 */
	public void setAutoRorate(Rotates rotate) {
		put("ar", rotate.name().toLowerCase());
	}
	
	/***
	 * 输出图片格式
	 * @author weishao zeng
	 * @param format
	 */
	public void setFormat(Formats format) {
		put("f", format.name().toLowerCase());
	}
	
	public static enum Formats {
		JPG,
		PNG
	}
	
	public static enum Rotates {
		AUTO;
	}
	
	public static enum Modes {
		FAST;
	}
}

