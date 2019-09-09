package org.onetwo.boot.opencv;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/***
 * -Djava.library.path=D:/mydev/java/opencv/opencv/build/java/x64
 * @author way
 *
 */
public class ImageTest {
	
	@Test
	public void imgMatching2() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		Mat src_base = Imgcodecs.imread("D:\\test\\test5.jpg");
//		Mat src_test = Imgcodecs.imread("D:\\test\\test3.jpg");

		Mat src_base = Imgcodecs.imread("g:/test/find-src.jpg");
		Mat src_test = Imgcodecs.imread("g:/test/find-dest2.jpg");
		
		Mat gray_base = new Mat();
		Mat gray_test = new Mat();
		// 转换为灰度
		Imgproc.cvtColor(src_base, gray_base, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(src_test, gray_test, Imgproc.COLOR_RGB2GRAY);
		// 初始化ORB检测描述子
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);//特别提示下这里opencv暂时不支持SIFT、SURF检测方法，这个好像是opencv(windows) java版的一个bug,本人在这里被坑了好久。
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		// 关键点及特征描述矩阵声明
		MatOfKeyPoint keyPoint1 = new MatOfKeyPoint(), keyPoint2 = new MatOfKeyPoint();
		Mat descriptorMat1 = new Mat(), descriptorMat2 = new Mat();
		// 计算ORB特征关键点
		featureDetector.detect(gray_base, keyPoint1);
		featureDetector.detect(gray_test, keyPoint2);
		

        Mat output=new Mat();
        Features2d.drawKeypoints(gray_base, keyPoint1, output );
        Imgcodecs.imwrite("g:/test/out.jpg", output);
        
		// 计算ORB特征描述矩阵
		descriptorExtractor.compute(gray_base, keyPoint1, descriptorMat1);
		descriptorExtractor.compute(gray_test, keyPoint2, descriptorMat2);
		float result = 0;
		// 特征点匹配
		System.out.println("test5：" + keyPoint1.size());
		System.out.println("test3：" + keyPoint2.size());
		if (!keyPoint1.size().empty() && !keyPoint2.size().empty()) {
			// FlannBasedMatcher matcher = new FlannBasedMatcher();
			DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
			MatOfDMatch matches = new MatOfDMatch();
			matcher.match(descriptorMat1, descriptorMat2, matches);
			// 最优匹配判断
			double minDist = 100;
			DMatch[] dMatchs = matches.toArray();
			int num = 0;
			for (int i = 0; i < dMatchs.length; i++) {
				if (dMatchs[i].distance <= 2 * minDist) {
					result += dMatchs[i].distance * dMatchs[i].distance;
					num++;
				}
			}
			// 匹配度计算
			result /= num;
		}
		System.out.println(result);
	}
	
	@Test
	public void testSift1() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat src = Imgcodecs.imread("g:/test/find-src.jpg");
		Mat dst = Imgcodecs.imread("g:/test/find-dest.jpg");
		
		MatOfRect mr = getFace(dst);
		Mat sub = dst.submat(mr.toArray()[0]);
		
		Imgcodecs.imwrite("g:/test/sift-result.jpg", FeatureSiftLannbased(src.t(), sub));
	}
	
	public static Mat FeatureSiftLannbased(Mat src, Mat dst){
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.SIFT);
		DescriptorExtractor de = DescriptorExtractor.create(DescriptorExtractor.SIFT);
		DescriptorMatcher Matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
		
		MatOfKeyPoint mkp = new MatOfKeyPoint();
		fd.detect(src, mkp);
		Mat desc = new Mat();
		de.compute(src, mkp, desc);
		Features2d.drawKeypoints(src, mkp, src);
		
		MatOfKeyPoint mkp2 = new MatOfKeyPoint();
		fd.detect(dst, mkp2);
		Mat desc2 = new Mat();
		de.compute(dst, mkp2, desc2);
		Features2d.drawKeypoints(dst, mkp2, dst);
		
		
		// Matching features
		MatOfDMatch Matches = new MatOfDMatch();
		Matcher.match(desc, desc2, Matches);
		
		List<DMatch> l = Matches.toList();
		List<DMatch> goodMatch = new ArrayList<DMatch>();
		for (int i = 0; i < l.size(); i++) {
			DMatch dmatch = l.get(i);
			if (Math.abs(dmatch.queryIdx - dmatch.trainIdx) < 10f) {
				goodMatch.add(dmatch);
			}
			
		}
		
		Matches.fromList(goodMatch);
		// Show result
		Mat OutImage = new Mat();
		Features2d.drawMatches(src, mkp, dst, mkp2, Matches, OutImage);
		
		return OutImage;
	}

	public static MatOfRect getFace(Mat src) {
		Mat result = src.clone();
		if (src.cols() > 1000 || src.rows() > 1000) {
			Imgproc.resize(src, result, new Size(src.cols() / 3, src.rows() / 3));
		}

		CascadeClassifier faceDetector = new CascadeClassifier("D:\\mydev\\java\\opencv\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt2.xml");
		MatOfRect objDetections = new MatOfRect();
		faceDetector.detectMultiScale(result, objDetections);
		
		return objDetections;
	}
}

