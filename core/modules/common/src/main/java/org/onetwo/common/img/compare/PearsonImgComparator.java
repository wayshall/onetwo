package org.onetwo.common.img.compare;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.onetwo.common.exception.BaseException;

public class PearsonImgComparator {

    /**
     * 可选值为：1,2,4,8,16,32
     * 当值为64时会抛出异常，此时需要实现64位转10进制
     * radix 64 greater than Character.MAX_RADIX
     */
    public int compareLevel = 32;
    
    public PearsonImgComparator() {
    }
    
    public PearsonImgComparator(int compareLevel) {
    	this.compareLevel = 4;
    }

    public Double compare(File file1, File file2) {
	   	 List<Double> data1 = getPicArrayData(readImage(file1));;
	     final List<Double> data2 = getPicArrayData(readImage(file2));
	     Double percent = PearsonUtils.getPearsonBydim(data1, data2);
	     return percent;
    }
    public Double compare(InputStream input1, InputStream input2) {
    	 List<Double> data1 = getPicArrayData(readImage(input1));;
         final List<Double> data2 = getPicArrayData(readImage(input2));
         Double percent = PearsonUtils.getPearsonBydim(data1, data2);
         return percent;
    }
    
    public BufferedImage readImage(InputStream input) {
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			throw new BaseException("read image to buffer errro: " + e.getMessage(), e);
		}
    }
    
    public BufferedImage readImage(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			throw new BaseException("read image to buffer errro: " + e.getMessage(), e);
		}
    }
    
    public List<Double> getPicArrayData(BufferedImage image) {

        //初始化集合
        final List<Double> picFingerprint = new ArrayList<>(compareLevel*compareLevel*compareLevel);
        IntStream.range(0, compareLevel*compareLevel*compareLevel).forEach(i->{
            picFingerprint.add(i, 0.0);
        });
        //遍历像素点
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                Color color = new Color(image.getRGB(i, j));
                //对像素点进行计算
                putIntoFingerprintList(picFingerprint, color.getRed(), color.getGreen(), color.getBlue());
            }
        }

        return picFingerprint;
    }

    /**
     * 放入像素的三原色进行计算，得到List的位置
     * @param picFingerprintList picFingerprintList
     * @param r r
     * @param g g
     * @param b b
     * @return
     */
    private List<Double> putIntoFingerprintList(List<Double> picFingerprintList, int r, int g, int b){
        //比如r g b是126, 153, 200 且 compareLevel为16进制，得到字符串：79c ,然后转10进制，这个数字就是List的位置
        final Integer index = Integer.valueOf(getBlockLocation(r) + getBlockLocation(g) + getBlockLocation(b), compareLevel);
        final Double origin = picFingerprintList.get(index);
        picFingerprintList.set(index, origin + 1);
        return picFingerprintList;
    }

    /**w
     * 计算 当前原色应该分在哪个区块
     * @param colorPoint colorPoint
     * @return
     */
    private String getBlockLocation(int colorPoint){
        return IntStream.range(0, compareLevel)
                //以10进制计算分在哪个区块
                .filter(i -> {
                    int areaStart = (256 / compareLevel) * i;
                    int areaEnd = (256 / compareLevel) * (i + 1) - 1;
                    return colorPoint >= areaStart && colorPoint <= areaEnd;
                })
                //如果compareLevel大于10则转为对应的进制的字符串
                .mapToObj(location -> compareLevel > 10 ? Integer.toString(location, compareLevel) : location+"")
                .findFirst()
                .orElseThrow(()->new BaseException("can not find the block for colorPoint: " + colorPoint));
    }

}
