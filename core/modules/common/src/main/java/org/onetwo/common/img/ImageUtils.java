package org.onetwo.common.img;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public abstract class ImageUtils {
	
	public static BufferedImage readBufferedImageFromPath(String path){
		try {
			return ImageIO.read(new URL(path));
		}catch (Exception e) {
			try {
				return ImageIO.read(new File(path));
			} catch (IOException e1) {
				throw new RuntimeException("read image error from [" + path + "] : "+e1.getMessage(), e1);
			}
		}
	}
	public static BufferedImage readBufferedImage(Object data){
		if(data instanceof byte[]){
			try {
				BufferedImage buf = ImageIO.read(new ByteArrayInputStream((byte[])data));
				return buf;
			} catch (IOException e) {
				throw new RuntimeException("read image error from [" + data + "] : "+e.getMessage(), e);
			}
		}else{
			return readBufferedImageFromPath(data.toString());
		}
	}
	
	public static ImageIcon createImageIconFromPath(String path){
		try {
			return new ImageIcon(new URL(path));
		}catch (Exception e) {
			return new ImageIcon(path);
		}
	}
	
	public static ImageIcon createImageIcon(Object data){
		if(data instanceof byte[]){
			try {
				BufferedImage buf = ImageIO.read(new ByteArrayInputStream((byte[])data));
				return new ImageIcon(buf);
			} catch (IOException e) {
				throw new RuntimeException("read image error from [" + data + "] : "+e.getMessage(), e);
			}
		}else{
			return createImageIconFromPath(data.toString());
		}
	}

}
