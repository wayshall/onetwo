package org.onetwo.common.img.compare;

import java.io.File;

import org.junit.Test;

public class PearsonImgComparatorTest {
	
	@Test
	public void test() {
		PearsonImgComparator c = new PearsonImgComparator();
		File file1 = new File("/Users/way/data/pic/1.jpeg");
		File file2 = new File("/Users/way/data/pic/1.jpeg");
		Double percent = c.compare(file1, file2);
		System.out.println("percent1: " + percent);
		
		file1 = new File("/Users/way/data/pic/1.jpeg");
		file2 = new File("/Users/way/data/pic/2.jpeg");
		percent = c.compare(file1, file2);
		System.out.println("percent2: " + percent);
		
		file1 = new File("/Users/way/data/pic/1.jpeg");
		file2 = new File("/Users/way/data/pic/3.jpeg");
		percent = c.compare(file1, file2);
		System.out.println("percent3: " + percent);
		
		file1 = new File("/Users/way/data/pic/1.jpeg");
		file2 = new File("/Users/way/data/pic/4.jpeg");
		percent = c.compare(file1, file2);
		System.out.println("percent4: " + percent);
		
		file1 = new File("/Users/way/data/pic/1.jpeg");
		file2 = new File("/Users/way/data/pic/5.png");
		percent = c.compare(file1, file2);
		System.out.println("percent5: " + percent);
		
		file1 = new File("/Users/way/data/pic/6.jpeg");
		file2 = new File("/Users/way/data/pic/7.jpeg");
		percent = c.compare(file1, file2);
		System.out.println("percent6: " + percent);
		
		file1 = new File("/Users/way/data/pic/8.jpeg");
		file2 = new File("/Users/way/data/pic/9.jpeg");
		percent = c.compare(file1, file2);
		System.out.println("percent7: " + percent);
	}

}
