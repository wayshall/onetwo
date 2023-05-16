package org.onetwo.boot.module.opencv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

public class ImageComparator {

	public double compareImages(MultipartFile file1, MultipartFile file2) {
		try {
			return compareImages(file1.getBytes(), file2.getBytes());
		} catch (IOException e) {
			throw new BaseException("read MultipartFile error: " + e.getMessage(), e);
		}
	}

	public double compareImages(InputStream input1, InputStream input2) {
		return compareImages(FileUtils.toByteArray(input1), FileUtils.toByteArray(input2));
	}
	
	public double compareImages(byte[] bytes1, byte[] bytes2) {
		Mat image1 = Imgcodecs.imdecode(new MatOfByte(bytes1), Imgcodecs.IMREAD_UNCHANGED);
	    Mat image2 = Imgcodecs.imdecode(new MatOfByte(bytes2), Imgcodecs.IMREAD_UNCHANGED);
	    return compareImages(image1, image2);
	}
	
	public double compareImages(String path1, String path2) {
		Mat image1 = Imgcodecs.imread(path1);
	    Mat image2 = Imgcodecs.imread(path2);
	    return compareImages(image1, image2);
	}
	
    public double compareImages(Mat image1, Mat image2) {
        Mat grayImage1 = new Mat();
        Mat grayImage2 = new Mat();
//        MatOfInt histSize = new MatOfInt(256);
//        MatOfFloat ranges = new MatOfFloat(0f, 180f);

        // 将图片转换为灰度图像
        Imgproc.cvtColor(image1, grayImage1, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(image2, grayImage2, Imgproc.COLOR_BGR2HSV);

        // Calculate histograms 直方图计算
        Mat histImage1 = new Mat();
        Mat histImage2 = new Mat();
        Imgproc.calcHist(Arrays.asList(grayImage1), new MatOfInt(0), new Mat(), histImage1, new MatOfInt(256), new MatOfFloat(0, 256));
        Imgproc.calcHist(Arrays.asList(grayImage2), new MatOfInt(0), new Mat(), histImage2, new MatOfInt(256), new MatOfFloat(0, 256));

        // Normalize histograms 图片归一化
        Core.normalize(histImage1, histImage1, 0, histImage1.rows(), Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(histImage2, histImage2, 0, histImage1.rows(), Core.NORM_MINMAX, -1, new Mat());

        // Compare histograms using one of the comparison methods 直方图比较
        double res = Imgproc.compareHist(histImage1, histImage2, Imgproc.CV_COMP_CORREL);
        
        return res;
    }
}
