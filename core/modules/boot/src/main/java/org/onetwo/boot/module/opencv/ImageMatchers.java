package org.onetwo.boot.module.opencv;

import java.io.File;

import org.onetwo.common.file.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 图像匹配
 * 在一张原图中查找从中截出来的另一张图片
 * 
 * @author weishao zeng
 * <br/>
 */
//@SuppressWarnings("deprecation")
abstract public class ImageMatchers {

	static  public void drawMatchedImage(String destImagePath, String srcImagePath) {
        //将文件读入为OpenCV的Mat格式
        Mat source = Imgcodecs.imread(srcImagePath);
        Mat destImage = Imgcodecs.imread(destImagePath);
		Core.MinMaxLocResult result = matchResult(source, destImage);
		Point matchLoc = result.minLoc;
        //在原图上的对应模板可能位置画一个绿色矩形
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + destImage.width(), matchLoc.y + destImage.height()), new Scalar(0, 255, 0));
        //将结果输出到对应位置
        String srcDir = new File(srcImagePath).getParent();
    	String srcImageName = FileUtils.getFileNameWithoutExt(srcImagePath);
    	String srcExt = FileUtils.getExtendName(srcImagePath);
    	String destImageName = FileUtils.getFileNameWithoutExt(destImagePath);
        Imgcodecs.imwrite(srcDir + "/" + srcImageName + "-matched-"+destImageName+"." + srcExt, source);
	}
	
    static public Core.MinMaxLocResult matchResult(Mat source, Mat destImage) {
        //创建于原图相同的大小，储存匹配度
        Mat result = Mat.zeros(source.rows() - destImage.rows() + 1, source.cols() - destImage.cols() + 1, CvType.CV_32FC1);
        //调用模板匹配方法
        Imgproc.matchTemplate(source, destImage, result, Imgproc.TM_SQDIFF_NORMED);
        //规格化
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
        //获得最可能点，MinMaxLocResult是其数据格式，包括了最大、最小点的位置x、y
        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);
        return mlr;
	}
}

