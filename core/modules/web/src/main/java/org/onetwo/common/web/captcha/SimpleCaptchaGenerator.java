package org.onetwo.common.web.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;

import org.apache.commons.lang3.RandomStringUtils;
import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.Assert;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleCaptchaGenerator {
	
	public static final int MAX_RGB = 255;
	
	public CaptchaResult writeTo(CaptchaSettings settings, OutputStream output){
		CaptchaResult result = generate(settings);
		try {
			IOUtils.write(result.getData(), output);
		} catch (IOException e) {
			throw new BaseException("write captcha error.", e);
		}
		return result;
	}
	
	public CaptchaResult writeTo(CaptchaSettings settings, String path){
		CaptchaResult result = generate(settings);
		FileUtils.writeByteArrayToFile(new File(path), result.getData());
		return result;
	}
	
	public CaptchaResult generate(CaptchaSettings settings){
		int width = settings.getWidth();
		int height = settings.getHeight();
		//创建图片缓冲器
		BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//创建2d图形引擎
		Graphics2D g2d = bufImg.createGraphics();
		
		//fillRect(int x,int y,int width,int height)：是用预定的颜色填充一个矩形，得到一个着色的矩形块。
		g2d.fillRect(0, 0, width, height);
		
		drawBorder(g2d, settings);
		drawPuzzleLines(g2d, settings);

		String codes = RandomStringUtils.randomAlphanumeric(settings.getCodeLength()).toLowerCase();
		drawCodes(g2d, settings, codes);
		CaptchaResult result = new CaptchaResult(codes);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(128);
		FileUtils.writeToStream(bufImg, settings.getImageFormatName(), bos);
		result.setData(bos.toByteArray());
		
		return result;
	}
	
	protected void drawBorder(Graphics2D g2d, CaptchaSettings settings){
		int width = settings.getWidth();
		int height = settings.getHeight();
		g2d.setColor(Color.BLACK);
		//画边框，从xy为1开始，宽为方形的宽减2，长度也是，否则看不见边框
		g2d.drawRect(1, 1, width-2, height-2);
	}
	protected void drawPuzzleLines(Graphics2D g2d, CaptchaSettings settings){
		int width = settings.getWidth();
		int height = settings.getHeight();
		//随机画干扰线
				//创建随机数
		ThreadLocalRandom random = ThreadLocalRandom.current();
		g2d.setColor(Color.GRAY);
		int puzzleCount = settings.getPuzzleLineCount();
		for (int i = 0; i < puzzleCount; i++) {
			//xy点不超出范围
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			
			int x2 = random.nextInt(width);
			int y2 = random.nextInt(height);
			//随机颜色
			Color c = getRandomColor(getRandom());
			g2d.setColor(c);
			g2d.drawLine(x1, y1, x2, y2);
		}
	}
	protected void drawCodes(Graphics2D g2d, CaptchaSettings settings, String codes){
		int height = settings.getHeight();
		
		//设置字体
		g2d.setFont(settings.getFont());
		//验证码宽高
		int codeWidth = (settings.getWidth()-1)/codes.length();
//		int codeHeight = height-10;
		int codeIndex = 0;
		for(char code : codes.toCharArray()){
			//整个图片高度减去字体高度后的剩余高度，减去字体高度，保证画时字体完整画出来
			int surplusHeight = height-settings.getFontHeight();
			int randomMargin = getRandom().nextInt(surplusHeight);
			g2d.setColor(getRandomColor(getRandom()));
//			g2d.setColor(Color.RED);
			//xy为字母最后的点，即最右和最底部的点
			int y = height-randomMargin*5;
			y = y<height/2?height:y;
			g2d.drawString(String.valueOf(code), codeIndex*codeWidth+randomMargin, y);
			codeIndex++;
		}
	}
	
	protected Random getRandom(){
		return ThreadLocalRandom.current();
	}
	
	private Color getRandomColor(Random random){
		Color c = new Color(random.nextInt(MAX_RGB), random.nextInt(MAX_RGB), random.nextInt(MAX_RGB));
		return c;
	}
	
	@Data
	public static class CaptchaSettings {
		//验证码长度
		int codeLength = 4;
		int height = 100;
		int width = height * 4;
		//干扰线条数量
		int puzzleLineCount = 20;
		String fontName = "Fixedsys";//"Courier New";
		Font font;
		String imageFormatName = "png";
		
		public Font getFont(){
			if(font==null){
				//getHeight() - 10字体高度
				font  = new Font("Courier New", Font.PLAIN, getFontHeight());
			}
			return font;
		}
		
		public int getFontHeight(){
			return (int)(getHeight()*0.9);
		}
	}
	
	@Data
	public static class CaptchaResult {
		final String code;
		byte[] data;
		public CaptchaResult(String code) {
			super();
			this.code = code;
		}
		
		public String getDataAsBase64(){
			Assert.notNull(data, "data can not be null");
			String str = org.apache.commons.codec.binary.Base64.encodeBase64String(data);
			return str;
		}
	}
}
