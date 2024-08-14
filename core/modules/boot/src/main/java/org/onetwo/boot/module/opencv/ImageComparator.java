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
        try {
            return compareImages(image1, image2);
        } finally {
            image1.release();
            image2.release();
        }
    }

    public double compareImages(String path1, String path2) {
        Mat image1 = Imgcodecs.imread(path1);
        Mat image2 = Imgcodecs.imread(path2);
        try {
            return compareImages(image1, image2);
        } finally {
            image1.release();
            image2.release();
        }
    }

    public double compareImages(Mat image1, Mat image2) {
        Mat grayImage1 = new Mat();
        Mat grayImage2 = new Mat();
        Mat histImage1 = new Mat();
        Mat histImage2 = new Mat();

        try {
            // Convert images to grayscale 转为灰度
            Imgproc.cvtColor(image1, grayImage1, Imgproc.COLOR_BGR2HSV);
            Imgproc.cvtColor(image2, grayImage2, Imgproc.COLOR_BGR2HSV);

            // Calculate histograms 计算直方图
            calcHist(grayImage1, histImage1);
            calcHist(grayImage2, histImage2);

            // Normalize histograms
            normalize(histImage1);
            normalize(histImage2);

            // Compare histograms using one of the comparison methods
            return Imgproc.compareHist(histImage1, histImage2, Imgproc.CV_COMP_CORREL);
        } finally {
            grayImage1.release();
            grayImage2.release();
            histImage1.release();
            histImage2.release();
        }
    }
    
    private void calcHist(Mat grayImage, Mat histImage) {
    	// Calculate histograms
    	MatOfInt channels = new MatOfInt(0);
    	Mat mask = new Mat();
    	MatOfInt histSize = new MatOfInt(256);
    	MatOfFloat ranges = new MatOfFloat(0, 256);
    	try {
            Imgproc.calcHist(Arrays.asList(grayImage), channels, mask, histImage,
            		histSize, ranges);
		} finally {
			channels.release();
			mask.release();
			histSize.release();
			ranges.release();
		}
    }
    
    private void normalize(Mat histImage1) {
    	Mat mask = new Mat();
    	try {
            Core.normalize(histImage1, histImage1, 0, histImage1.rows(), Core.NORM_MINMAX, -1, mask);
		} finally {
			mask.release();
		}
    }
}

