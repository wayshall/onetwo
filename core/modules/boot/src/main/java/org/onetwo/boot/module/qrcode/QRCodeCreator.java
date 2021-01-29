package org.onetwo.boot.module.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeCreator {
	private String charset = "utf-8";
    private String content;
    // 二维码尺寸
    private int size = 300;
    private String format = "png";
    
    
    public QRCodeCreator(String content) {
		super();
		this.content = content;
	}

	public QRCodeCreator(String content, int size) {
		super();
		this.content = content;
		this.size = size;
	}
	
	public void writeTo(String targetPath) {
		File file = new File(targetPath);
		BufferedImage bufImg = toImage();
		FileUtils.writeToFile(bufImg, format, file);
	}
    
	public byte[] toByteArray() {
		BufferedImage bufImg = toImage();
		return FileUtils.toByteArray(bufImg, format);
	}
	
    public BufferedImage toImage() {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size,
                    hints);
        } catch (WriterException e) {
            throw new BaseException("create qrcode error: " + e.getMessage(), e);
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
    
}
