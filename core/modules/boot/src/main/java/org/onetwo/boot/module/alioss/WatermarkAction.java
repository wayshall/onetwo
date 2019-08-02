package org.onetwo.boot.module.alioss;

import org.onetwo.boot.module.alioss.OssProperties.WaterMaskProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * https://help.aliyun.com/document_detail/44957.html?spm=a2c4g.11186623.2.19.5add3bc24VYiC4#concept-hrt-sv5-vdb
 * 
 * @author weishao zeng
 * <br/>
 */
public class WatermarkAction extends ObjectProcess<WaterMaskProperties> {
	
	/**
	 * image/watermark
	 */
	public WatermarkAction() {
		setAction("image/watermark");
	}
	
	public void setText(String text) {
		put("text", new AttrValue(text, true, true));
	}
	
	/***
	 * 可选参数
	 * 表示文字水印的文字类型(必须编码)
	 * @author weishao zeng
	 * @param type
	 */
	public void setType(String type) {
		put("type", type, true);
	}
	
	public void setFont(WatermarkFonts font) {
		setType(font.getValue());
	}
	
	/***
	 * 参数意义：文字水印的文字大小(px)
取值范围：(0，1000]
默认值：40
	 * @author weishao zeng
	 * @param size
	 */
	public void setSize(int size) {
		if (size > 1000) {
			size = 1000;
		}
		put("size", String.valueOf(size));
	}

	/***
	 * 参数意义：文字水印的阴影透明度
取值范围：[0,100]
	 * @author weishao zeng
	 * @param shadow
	 */
	public void setShadow(int shadow) {
		if (shadow > 100) {
			shadow = 100;
		}
		put("shadow", String.valueOf(shadow));
	}
	/***
	 * 透明度, 如果是图片水印，就是让图片变得透明，如果是文字水印，就是让水印变透明。
默认值：100， 表示 100%（不透明）
取值范围: [0-100]
	 * @author weishao zeng
	 * @param transparency
	 */
	public void setTransparency(int transparency) {
		if (transparency > 100) {
			transparency = 100;
		}
		put("t", String.valueOf(transparency));
	}
	
	/**
	 * 水平边距, 就是距离图片边缘的水平距离， 这个参数只有当水印位置是左上，左中，左下， 右上，右中，右下才有意义。
默认值：10
取值范围：[0 – 4096]
单位：像素（px）
	 * @author weishao zeng
	 * @param transparency
	 */
	public void setX(int x) {
		if (x < 0) {
			x = 0;
		}else if (x > 4096) {
			x = 4096;
		}
		put("x", String.valueOf(x));
	}
	/**
	 * 垂直边距, 就是距离图片边缘的垂直距离， 这个参数只有当水印位置是左上，中上， 右上，左下，中下，右下才有意义
默认值：10
取值范围：[0 – 4096]
单位：像素(px)
	 * @author weishao zeng
	 * @param y
	 */
	public void setY(int y) {
		if (y < 0) {
			y = 0;
		}else if (y > 4096) {
			y = 4096;
		}
		put("y", String.valueOf(y));
	}

	/***
	 * 中线垂直偏移，当水印位置在左中，中部，右中时，可以指定水印位置根据中线往上或者往下偏移
默认值：0
取值范围：[-1000, 1000]
单位：像素(px)
	 * @author weishao zeng
	 * @param voffset
	 */
	public void setVoffset(int voffset) {
		if (voffset < -1000) {
			voffset = -1000;
		}else if (voffset > 1000) {
			voffset = 1000;
		}
		put("voffset", String.valueOf(voffset));
	}
	
	/***
	 * 参数意义：进行水印铺满的效果
取值范围：[0,1]，1表示铺满，0表示效果无效
	 * @author weishao zeng
	 * @param fill
	 */
	public void setFill(int fill) {
		if (fill > 0) {
			fill = 1;
		}
		put("fill", String.valueOf(fill));
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
	 * 位置，水印打在图的位置，详情参考下方区域数值对应图。
取值范围：[nw,north,ne,west,center,east,sw,south,se]
	 * @author weishao zeng
	 * @param location
	 */
	public void setLocation(String location) {
		put("g", location);
	}
	
	/***
	 *  水印图片为当前的Bucket下Object，直接针对Object名称进行base64编码。
	 * @author weishao zeng
	 * @param image
	 */
	public void setImage(String image) {
		put("image", image, true);
	}
	
	@AllArgsConstructor
	public static enum WatermarkFonts {
		WQY_ZENHEI("文泉驿正黑"),
		DROIDSANSFALLBACK("DroidSansFallback"),
		FANGZHENGFANGSONG("方正仿宋"),
		FANGZHENGKAITI("方正楷体");
		@Getter
		final private String labe;
		
		public String getValue() {
			return name().toLowerCase().replace('_', '-');
		}
	}

}

