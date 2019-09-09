package org.onetwo.boot.module.alioss;

import lombok.Data;

/**
 * https://help.aliyun.com/document_detail/44688.html?spm=a2c4g.11186623.2.15.5add3bc24VYiC4#concept-hxj-c4n-vdb
 * 
 * @author weishao zeng
 * <br/>
 */
@Data
public class ResizeProperties {
	boolean enabled;
	/***
	 * 指定缩略的模式：
lfit：等比缩放，限制在指定w与h的矩形内的最大图片。
mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
pad：固定宽高，缩略填充。
fixed：固定宽高，强制缩略。
	 */
	String mode;
	/***
	 * 指定目标缩略图的宽度
	 */
	Integer width;
	Integer heigh;
	/***
	 * 指定目标缩略图的最长边
	 */
	Integer maxLong;
	Integer minShort;
	/**
	 * 指定当目标缩略图大于原图时是否处理。值是 1 表示不处理；值是 0 表示处理。
	 */
	Integer limit;
	/***
	 * 当缩放模式选择为 pad（缩略填充）时，可以选择填充的颜色(默认是白色)参数的填写方式：采用 16 进制颜色码表示，如 00FF00（绿色）。
	 */
	String color;
}

