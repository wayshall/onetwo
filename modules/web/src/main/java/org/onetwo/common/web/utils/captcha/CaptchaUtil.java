package org.onetwo.common.web.utils.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.common.web.utils.ResponseUtils;

public class CaptchaUtil {
	
	public static final String CAPTCHA_KEY = "captcha";
	
	public class Captcha {
		private InputStream image;
		
		public InputStream getImage() {
			return image;
		}
		public void setImage(InputStream image) {
			this.image = image;
		}
		public String getCaptcha() {
			return captcha;
		}
		public void setCaptcha(String captcha) {
			this.captcha = captcha;
		}
		private String captcha;
	}
	
	public static Captcha createCaptcha() {
		// 在内存中创建图象
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// 获取图形上下文
		Graphics g = image.getGraphics();
		
		// 生成随机类
		Random random = new Random();
		
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		
		// 取随机产生的认证码(6位数字)
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 13 * i + 6, 16);
		}
		
		// 图象生效
		g.dispose();

		InputStream imageIn = null;
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			
			ImageIO.write(image, "JPEG", bos);
			
			imageIn = new ByteArrayInputStream(bos.toByteArray());
			//ImageIO..write(image, "JPEG", imageOut);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Captcha c = new CaptchaUtil().new Captcha();
		c.setImage(imageIn);
		c.setCaptcha(sRand);
		
		return c;
	}

	/*
	 * 给定范围获得随机颜色
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static boolean validateFromCookie(HttpServletRequest request, String key, String captcha) {
		String encrypt = ResponseUtils.getCookieValue(request, key);
		if(encrypt != null && MDFactory.getMDEncrypt().checkEncrypt(captcha, encrypt)) {
			return true;
		}
		return false; 
	}
	
	public static void setCaptcha2Cookie(HttpServletResponse response, String key, String captcha){
		ResponseUtils.setHttpOnlyCookie(response, key, MDFactory.getMDEncrypt().encryptWithSalt(captcha));
	}
}
