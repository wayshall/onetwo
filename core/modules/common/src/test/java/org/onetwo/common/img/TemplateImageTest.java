package org.onetwo.common.img;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.onetwo.common.date.DateUtil;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.img.TemplateImage.Horizontal;

public class TemplateImageTest {
	
	@Test
	public void testImg(){
		String basePath = FileUtils.getResourcePath("org/onetwo/common/img");
		TemplateImage ti = TemplateImage.create(basePath+"/blank.jpg");
		//+20
		ti.addTextDefined("userName", Horizontal.CENTER, 98, Color.WHITE, new Font("微软雅黑", Font.PLAIN, 36));
		ti.addTextDefined("now", 330, 29, Color.WHITE, new Font("微软雅黑", Font.PLAIN, 26));
		ti.addTextDefined("first", 177, 210, Color.BLACK, new Font("微软雅黑", Font.PLAIN, 30));
		ti.addTextDefined("productName", 177, 656, Color.BLACK, new Font("微软雅黑", Font.PLAIN, 30));
		ti.addTextDefined("unit", 177, 762, Color.RED, new Font("微软雅黑", Font.PLAIN, 20));
		ti.addTextDefined("productPrice", 194, 762, Color.RED, new Font("微软雅黑", Font.BOLD, 30));
		ti.addImageDefined("productImg", 177, 349, 481, 271);
		ti.addImageDefined("avatar", 31, 163, 77, 76);
		ti.addImageDefined("avatar", 31, 324, 77, 76);
		ti.addImageDefined("avatar", 31, 829, 77, 76);
		ti.addImageDefined("qrcode", 207, 918, 181, 179);
		File file = ti.drawTo(basePath+"/test1.jpg", 
				"now", DateUtil.format("h:mm a", new Date()),
				"userName", "12号12号12号",
				"first", "我刚申请了一个免费商品，赶紧来\n跟我一起拼团吧！",
				"unit", "¥",
				"productPrice", "108",
				"productName", "海洋王国+横琴岛剧院（二等座平日\n套全票）【7-12月】",
				"productImg", basePath+"/product.jpg",
				"avatar", basePath+"/icon.jpg",
				"qrcode", basePath+"/dg.jpg");
		System.out.println("save to: " + file);
		
		ti.drawTo("G:/tmp/test2.jpg", 
				"now", DateUtil.format("h:mm a", new Date()),
				"userName", "深圳市市长",
				"first", "我想申请今年的租房补贴！",
				"productImg", "G:/tmp/dg.jpg",
				"avatar", "G:/tmp/icon.jpg");
		
	}

}
