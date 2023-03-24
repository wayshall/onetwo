package org.onetwo.boot.opencv;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.boot.module.opencv.ImageComparator;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import nu.pattern.OpenCV;

public class ImageComparatorTest {
	ImageComparator imageComparator;
	
	@Before
	public void setup() {
		OpenCV.loadLocally();
		imageComparator = new ImageComparator();
	}
	
	@Test
	public void test() {

    	OpenCV.loadLocally();
    	System.out.println(System.getProperty("java.library.path"));
    	System.out.println(System.getProperty("os.arch"));
    	
		Mat image1 = Imgcodecs.imread("/Users/way/data/pic/1.jpeg");
	    Mat image2 = Imgcodecs.imread("/Users/way/data/pic/1.jpeg");
		double res = imageComparator.compareImages(image1, image2);
		System.out.println("percent1: " + res);
		
		image1 = Imgcodecs.imread("/Users/way/data/pic/1.jpeg");
	    image2 = Imgcodecs.imread("/Users/way/data/pic/2.jpeg");
		res = imageComparator.compareImages(image1, image2);
		System.out.println("percent2 1-2: " + res);
		
		image1 = Imgcodecs.imread("/Users/way/data/pic/1.jpeg");
	    image2 = Imgcodecs.imread("/Users/way/data/pic/3.jpeg");
		res = imageComparator.compareImages(image1, image2);
		System.out.println("percent3 1-3: " + res);
		
		image1 = Imgcodecs.imread("/Users/way/data/pic/1.jpeg");
	    image2 = Imgcodecs.imread("/Users/way/data/pic/4.jpeg");
		res = imageComparator.compareImages(image1, image2);
		System.out.println("percent4 1-4: " + res);
		
		image1 = Imgcodecs.imread("/Users/way/data/pic/1.jpeg");
	    image2 = Imgcodecs.imread("/Users/way/data/pic/5.png");
		res = imageComparator.compareImages(image1, image2);
		System.out.println("percent5 1-5: " + res);
		
		image1 = Imgcodecs.imread("/Users/way/data/pic/6.jpeg");
	    image2 = Imgcodecs.imread("/Users/way/data/pic/7.jpeg");
		res = imageComparator.compareImages(image1, image2);
		System.out.println("percent6 6-7: " + res);
		
		image1 = Imgcodecs.imread("/Users/way/data/pic/8.jpeg");
	    image2 = Imgcodecs.imread("/Users/way/data/pic/9.jpeg");
		res = imageComparator.compareImages(image1, image2);
		System.out.println("percent5 8-9: " + res);
	}

}
