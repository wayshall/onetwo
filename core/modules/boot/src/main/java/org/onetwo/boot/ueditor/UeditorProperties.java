package org.onetwo.boot.ueditor;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@ConfigurationProperties(UeditorProperties.UEDITOR)
@Data
public class UeditorProperties {
	public static final String UEDITOR = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".ueditor";
	public static final String UEDITOR_UPLOAD_PATH = UEDITOR + ".upload.path";
	
	UeditorConfig config = new UeditorConfig();
	
	CompressConfig imageCompress = new CompressConfig();
	
	@Data
	public static class UeditorConfig {
		public static final String IMAGE_ACTION_NAME = "uploadimage";
		public static final String IMAGE_FIELD_NAME = "upfile";
		
		String imageActionName = IMAGE_ACTION_NAME; // {String} [默认值："uploadimage"] //执行上传图片的action名称,
		String imageFieldName = IMAGE_FIELD_NAME; // {String} [默认值："upfile"] //提交的图片表单名称
		int imageMaxSize = 2048000; // {Number} [默认值：2048000] //上传大小限制，单位B
		String[] imageAllowFiles = new String[] {".png", ".jpg", ".jpeg", ".gif", ".bmp" }; // {String} , //上传图片格式显示 默认值： [".png", ".jpg", ".jpeg", ".gif", ".bmp"]
		boolean imageCompressEnable = true; // {Boolean} [默认值：true] //是否压缩图片,默认是true
		int imageCompressBorder = 1600; // {Number} [默认值：1600] //图片压缩最长边限制
		String imageInsertAlign = "none"; // {String} [默认值："none"] //插入的图片浮动方式
		String imageUrlPrefix = ""; // {String} [默认值：""] //图片访问路径前缀
		String imagePathFormat = "/ueditor"; // {String} [默认值："/ueditor/php/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}"] //上传保存路径,可以自定义保存路径和文件名格式，上传路径配置
	}

}
