package org.onetwo.boot.module.alioss;

/**
 * https://help.aliyun.com/document_detail/44688.html?spm=a2c4g.11186623.6.1382.49865bbfI9lDiK
 * 
 * @author weishao zeng
 * <br/>
 */
public class ResizeAction extends ObjectProcess<ResizeProperties> {
	
	/**
	 * image/resize
	 */
	public ResizeAction() {
		setAction("image/resize");
	}
	
	/***
	 * 指定缩略的模式：
lfit：等比缩放，限制在指定w与h的矩形内的最大图片。
mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
pad：固定宽高，缩略填充。
fixed：固定宽高，强制缩略。
	 * @author weishao zeng
	 * @param text
	 */
	public void setMode(String mode) {
		put("m", mode);
	}
	
	/***
	 * 指定目标缩略图的宽度。
	 * 1-4096
	 * @author weishao zeng
	 * @param width
	 */
	public void setWidth(int width) {
		if (width < 1) {
			width = 1;
		}else if (width > 4096) {
			width = 4096;
		}
		put("w", String.valueOf(width));
	}

	/***
	 * 指定目标缩略图的高度。
	 * 1-4096
	 * @author weishao zeng
	 * @param heigh
	 */
	public void setHeigh(int heigh) {
		if (heigh < 1) {
			heigh = 1;
		}else if (heigh > 4096) {
			heigh = 4096;
		}
		put("h", String.valueOf(heigh));
	}

	/***
	 * 指定目标缩略图的最长边
	 * 1-4096
	 * @author weishao zeng
	 * @param maxLong
	 */
	public void setMaxLong(int maxLong) {
		if (maxLong < 1) {
			maxLong = 1;
		}else if (maxLong > 4096) {
			maxLong = 4096;
		}
		put("l", String.valueOf(maxLong));
	}

	/***
	 * 指定目标缩略图的最短边
	 * 1-4096
	 * @author weishao zeng
	 * @param minShort
	 */
	public void setMinShort(int minShort) {
		if (minShort < 1) {
			minShort = 1;
		}else if (minShort > 4096) {
			minShort = 4096;
		}
		put("s", String.valueOf(minShort));
	}
	/***
	 * 指定当目标缩略图大于原图时是否处理。值是 1 表示不处理；值是 0 表示处理。
	 * @author weishao zeng
	 * @param limit
	 */
	public void setLimit(int limit) {
		if (limit > 0) {
			limit = 1;
		}
		put("limit", String.valueOf(limit));
	}
	/***
	 * 参数意义：文字水印的文字的颜色
参数的构成必须是：六个十六进制数，如：000000表示黑色。 000000每两位构成RGB颜色， FFFFFF表示的是白色
默认值：000000黑色
	 * @author weishao zeng
	 * @param color
	 */
	public void setColor(String color) {
		if (color==null || color.length()!=6) {
			throw new IllegalArgumentException("error color: " + color);
		}
		put("color", color);
	}
	
	/***
	 * 倍数百分比。 小于 100，即是缩小，大于 100 即是放大。
	 * @author weishao zeng
	 * @param proportion
	 */
	public void setProportion(int proportion) {
		if (proportion < 1) {
			proportion = 1;
		}else if (proportion > 4096) {
			proportion = 4096;
		}
		put("p", String.valueOf(proportion));
	}
}

