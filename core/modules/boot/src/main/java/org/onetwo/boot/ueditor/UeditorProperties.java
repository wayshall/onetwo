package org.onetwo.boot.ueditor;

import java.util.Date;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.CompressConfig;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@ConfigurationProperties(UeditorProperties.UEDITOR)
@Data
public class UeditorProperties {
	public static final String UEDITOR = BootSiteConfig.PREFIX + ".ueditor";
	public static final String UEDITOR_UPLOAD_PATH = UEDITOR + ".upload.path"; // site.ueditor.upload.path: /web-admin/ueditor
	
	UeditorConfig config = new UeditorConfig(); // site.ueditor.config.
	
	CompressConfig imageCompress = new CompressConfig();
	/***
	 * 是否启用图片压缩
	 */
	boolean compressEnabled = false;
	String bizModule;
	
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
		
		public boolean isFormatPath() {
			Expression expr = ExpressionFacotry.BRACE;
			if (StringUtils.isBlank(imagePathFormat) || !expr.isExpresstion(imagePathFormat)) {
				return false;
			}
			return true;
		}
		/***
		 * https://fex.baidu.com/ueditor/#server-path
		 * {filename}  //会替换成文件名 [要注意中文文件乱码问题]
			{rand:6}    //会替换成随机数,后面的数字是随机数的位数
			{time}      //会替换成时间戳
			{yyyy}      //会替换成四位年份
			{yy}        //会替换成两位年份
			{mm}        //会替换成两位月份
			{dd}        //会替换成两位日期
			{hh}        //会替换成两位小时
			{ii}        //会替换成两位分钟
			{ss}        //会替换成两位秒
		 * 非法字符 \ : * ? " < > |
		 * @author weishao zeng
		 * @param fileName
		 * @return
		 */
		public String formatImagePath(String fileName) {
			Expression expr = ExpressionFacotry.BRACE;
			String url = expr.parse(imagePathFormat, var -> {
				String value = "";
				Date now = new Date();
				switch (var) {
				case "filename":
					value = fileName;
					break;
				case "time":
					value = now.getTime() + "";
					break;
				case "yyyy":
				case "yy":
				case "dd":
				case "hh":
				case "ss":
					value = DateUtils.format(var, now);
					break;
				case "mm":
					value = DateUtils.format("MM", now);
					break;
				case "ii":
					value = DateUtils.format("mm", now);
					break;
				default:
					if (var.startsWith("rand:")) {
						int length = Integer.parseInt(StringUtils.substringAfter(var, "rand:"));
						value = LangUtils.getRadomString(length);
					}
					break;
				}
				return value;
			});
			url = url.replaceAll("\\\\|:|\\*|\\?|\"|<|>|\\|", "");
			return url;
		}
	}

}
