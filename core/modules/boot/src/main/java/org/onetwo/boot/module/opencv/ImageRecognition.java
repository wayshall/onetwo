package org.onetwo.boot.module.opencv;

import java.util.LinkedList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 图像识别
 * 在一张图片中识别另一张图片中的事物
 * 
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("deprecation")
public class ImageRecognition {

    private float nndrRatio = 0.7f;//这里设置既定值为0.7，该值可自行调整
    
	private int detectorType = FeatureDetector.ORB;
	private int extractorType = DescriptorExtractor.ORB;
	
	private FeatureDetector featureDetector;
	private DescriptorExtractor descriptorExtractor;
	
	/***
	 * 超过指定的匹配数，即可被认为匹配成功
	 */
	private int successMatchCount = 4;
	
	private String debugImageDir;
	/***
	 * 是否输出带有特征点的目标图片
	 */
	private boolean writeDestImageKeyPoints;
	/***
	 * 是否把在来源图片中匹配到的目标图片裁剪输出
	 */
	private boolean writeCutMatchedImageFromSrc;
	/***
	 * 是否输出特征点匹配过程的图片
	 */
	private boolean writeMatchingImage;
	/***
	 * 是否输出匹配成功的图片
	 * 即在原图上用框框标处匹配成功的目标图片
	 */
	private boolean writeDrawMatchedLineImage;
	
	/***
	 * debug图片总开关
	 */
	private boolean writeDebugImage;
	
	public void init() {
		featureDetector = FeatureDetector.create(detectorType);
		descriptorExtractor = DescriptorExtractor.create(extractorType);
		
		if (this.writeDebugImage) {
			this.writeDestImageKeyPoints = true;
			this.writeCutMatchedImageFromSrc = true;
			this.writeMatchingImage = true;
			this.writeDrawMatchedLineImage = true;
		}
	}
	
	private void checkDebugImageDir() {
		if (StringUtils.isBlank(debugImageDir)) {
			throw new BaseException("debugImageDir not set!");
		}
	}
	
	private void writeDebugImage(String name, Mat outputImage) {
		this.checkDebugImageDir();
		String path = FileUtils.convertDir(debugImageDir) + name; 
        Imgcodecs.imwrite(path, outputImage);
	}

    public boolean match(String destImagePath, String srcImagePath) {
    	Mat toFindImage = readImage(destImagePath);
    	Mat srcImage = readImage(srcImagePath);
    	
    	// 获取目标图片的特征点
    	KeyPoints keyPoints = detectAndExtractKeyPoints(toFindImage);
    	
    	// 获取原图的特征点
    	KeyPoints srcKeyPoints = detectAndExtractKeyPoints(srcImage);
    	
    	
    	List<MatOfDMatch> matches = matchKeyPoints(keyPoints.getExtractorKeyPoints(), srcKeyPoints.getExtractorKeyPoints());
    	List<DMatch> bestMatchs = findBestMatches(matches);
    	boolean matched =  bestMatchs.size() >= this.successMatchCount;
    	if (matched) {
        	MatchContext context = MatchContext.builder()
											.destKeyPoints(keyPoints)
											.srcKeyPoints(srcKeyPoints)
											.destImagePath(destImagePath)
											.destImage(toFindImage)
											.srcImagePath(srcImagePath)
											.srcImage(srcImage)
											.bestMatchs(bestMatchs)
											.build();
    		this.drawDebugImages(context);
    	}
    	return matched;
    }
    
    private void drawDebugImages(MatchContext context) {
    	String destImageName = FileUtils.getFileNameWithoutExt(context.getDestImagePath());
    	String destExt = FileUtils.getExtendName(context.getDestImagePath());
    	
    	if (writeDestImageKeyPoints) {
    		Mat outputImage = createMatAndDrawKeypoints(context.getDestImage(), context.getDestKeyPoints().getDetectKeyPoints());
    		String destKeyPointsName = destImageName + "-1-keypoints." + destExt;
    		writeDebugImage(destKeyPointsName, outputImage);
    	}
    	
    	MatOfKeyPoint templateKeyPoints = context.getDestKeyPoints().getDetectKeyPoints();
    	MatOfKeyPoint originalKeyPoints = context.getSrcKeyPoints().getDetectKeyPoints();
    	
        List<KeyPoint> templateKeyPointList = templateKeyPoints.toList();
        List<KeyPoint> originalKeyPointList = originalKeyPoints.toList();
        LinkedList<Point> objectPoints = new LinkedList<>();
        LinkedList<Point> scenePoints = new LinkedList<>();
        context.getBestMatchs().forEach(goodMatch -> {
            objectPoints.addLast(templateKeyPointList.get(goodMatch.queryIdx).pt);
            scenePoints.addLast(originalKeyPointList.get(goodMatch.trainIdx).pt);
        });
        MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
        objMatOfPoint2f.fromList(objectPoints);
        MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
        scnMatOfPoint2f.fromList(scenePoints);
        //使用 findHomography 寻找匹配上的关键点的变换
        Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);


        Mat templateImage = context.getDestImage();
        Mat originalImage = context.getSrcImage();
        
        /**
         * 透视变换(Perspective Transformation)是将图片投影到一个新的视平面(Viewing Plane)，也称作投影映射(Projective Mapping)。
         */
        Mat templateCorners = new Mat(4, 1, CvType.CV_32FC2);
        Mat templateTransformResult = new Mat(4, 1, CvType.CV_32FC2);
        templateCorners.put(0, 0, new double[]{0, 0});
        templateCorners.put(1, 0, new double[]{templateImage.cols(), 0});
        templateCorners.put(2, 0, new double[]{templateImage.cols(), templateImage.rows()});
        templateCorners.put(3, 0, new double[]{0, templateImage.rows()});
        //使用 perspectiveTransform 将模板图进行透视变以矫正图象得到标准图片
        Core.perspectiveTransform(templateCorners, templateTransformResult, homography);

        //矩形四个顶点，格式为：[col（列数）, row（行数）]
        double[] pointA = templateTransformResult.get(0, 0);
        double[] pointB = templateTransformResult.get(1, 0);
        double[] pointC = templateTransformResult.get(2, 0);
        double[] pointD = templateTransformResult.get(3, 0);

        //指定取得数组子集的范围
        int rowStart = (int) pointA[1]; // 左上点的行号
        int rowEnd = (int) pointC[1]; // 右下点的行号 
        int colStart = (int) pointD[0]; // 左下点的列号
        int colEnd = (int) pointB[0]; // 右上点的列号
        
        // 如果最大行超出了图片的最大行数
        if (rowEnd>originalImage.rows()) {
        	rowEnd = originalImage.rows();
        }
        if (colStart<0) {
        	colStart = 0;
        }
        // 如果最大列数超出了图片的最大列数
        if (colEnd>originalImage.cols()) {
        	colEnd = originalImage.cols();
        }

        //将匹配的图像用用四条线框出来
        Imgproc.line(originalImage, new Point(pointA), new Point(pointB), new Scalar(0, 255, 0), 4);//上 A->B
        Imgproc.line(originalImage, new Point(pointB), new Point(pointC), new Scalar(0, 255, 0), 4);//右 B->C
        Imgproc.line(originalImage, new Point(pointC), new Point(pointD), new Scalar(0, 255, 0), 4);//下 C->D
        Imgproc.line(originalImage, new Point(pointD), new Point(pointA), new Scalar(0, 255, 0), 4);//左 D->A

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(context.getBestMatchs());
        Mat matchOutput = new Mat(originalImage.rows() * 2, originalImage.cols() * 2, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Features2d.drawMatches(templateImage, templateKeyPoints, originalImage, originalKeyPoints, goodMatches, matchOutput, new Scalar(0, 255, 0), new Scalar(255, 0, 0), new MatOfByte(), 2);

        //特征点匹配过程.
        if (writeMatchingImage) {
//            Imgcodecs.imwrite("g:/test/find-doing.jpg", matchOutput);
        	String matchingImageName = destImageName + "-2-matching." + destExt;
    		writeDebugImage(matchingImageName, matchOutput);
        }
        //模板图在原图中的位置
        if (writeDrawMatchedLineImage) {
        	// Imgcodecs.imwrite("g:/test/find-result.jpg", originalImage);
        	String drawMatchedImageName = destImageName + "-3-drawMatchedLine." + destExt;
    		writeDebugImage(drawMatchedImageName, originalImage);
        }
        
        if (this.writeCutMatchedImageFromSrc) {
            Mat subMat = originalImage.submat(rowStart, rowEnd, colStart, colEnd);
        	//原图中的匹配图
//        	Imgcodecs.imwrite("g:/test/find-src-dest.jpg", subMat);
        	String matchedImageFromSrc = destImageName + "-4-cutMatchedImageFromSrc." + destExt;
    		writeDebugImage(matchedImageFromSrc, subMat);
        }
    }
    
    private Mat readImage(String path) {
    	return Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_COLOR);
    }
    
    /***
     * 寻找最佳匹配
     * @author weishao zeng
     * @param matches
     * @return
     */
    private List<DMatch> findBestMatches(List<MatOfDMatch> matches) {
//        System.out.println("计算匹配结果");
        LinkedList<DMatch> bestMatchesList = new LinkedList<>();
      //对匹配结果进行筛选，依据distance进行筛选
        matches.forEach(match -> {
            DMatch[] dmatcharray = match.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                bestMatchesList.addLast(m1);
            }
        });
        return bestMatchesList;
    }
    
    /***
     * 匹配关键点
     * @author weishao zeng
     * @param destKeyPoints
     * @param srcKeyPoints
     * @return
     */
    private List<MatOfDMatch> matchKeyPoints(MatOfKeyPoint destKeyPoints, MatOfKeyPoint srcKeyPoints) {
    	 List<MatOfDMatch> matches = new LinkedList<>();
         DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
//         System.out.println("寻找最佳匹配");
         /**
          * knnMatch方法的作用就是在给定特征描述集合中寻找最佳匹配
          * 使用KNN-matching算法，令K=2，则每个match得到两个最接近的descriptor，然后计算最接近距离和次接近距离之间的比值，当比值大于既定值时，才作为最终match。
          */
         descriptorMatcher.knnMatch(destKeyPoints, srcKeyPoints, matches, 2);
         return matches;
    }
    
    private Mat createMatAndDrawKeypoints(Mat imageMat, MatOfKeyPoint detectKeyPoints) {
    	//显示模板图的特征点图片
        Mat outputImage = new Mat(imageMat.rows(), imageMat.cols(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        //在图片上显示提取的特征点
//        System.out.println("在图片上显示提取的特征点");
        Features2d.drawKeypoints(imageMat, detectKeyPoints, outputImage, new Scalar(255, 0, 0), 0);
        return outputImage;
    }
    /****
     * 检测和提取图片特征点
     * @author weishao zeng
     * @param imageMat
     * @return
     */
    private KeyPoints detectAndExtractKeyPoints(Mat imageMat) {
    	MatOfKeyPoint detectKeyPoints = new MatOfKeyPoint();
        //指定特征点算法SURF
        //获取要查找图片的特征点
        featureDetector.detect(imageMat, detectKeyPoints);
        
      //提取模板图的特征点
        MatOfKeyPoint extractorKeyPoints = new MatOfKeyPoint();
//        System.out.println("提取模板图的特征点");
        descriptorExtractor.compute(imageMat, detectKeyPoints, extractorKeyPoints);
        
        return KeyPoints.builder()
        				.detectKeyPoints(detectKeyPoints)
        				.extractorKeyPoints(extractorKeyPoints)
        				.build();
    }
    
    
    
    public void setNndrRatio(float nndrRatio) {
		this.nndrRatio = nndrRatio;
	}

	public void setDetectorType(int detectorType) {
		this.detectorType = detectorType;
	}

	public void setExtractorType(int extractorType) {
		this.extractorType = extractorType;
	}

	public void setSuccessMatchCount(int successMatchCount) {
		this.successMatchCount = successMatchCount;
	}

	public void setWriteDestImageKeyPoints(boolean writeDestImageKeyPoints) {
		this.writeDestImageKeyPoints = writeDestImageKeyPoints;
	}

	public void setWriteMatchedImageFromSrc(boolean writeMatchedImageFromSrc) {
		this.writeCutMatchedImageFromSrc = writeMatchedImageFromSrc;
	}

	public void setWriteMatchingImage(boolean writeMatchingImage) {
		this.writeMatchingImage = writeMatchingImage;
	}

	public void setWriteDrawMatchedLineImage(boolean writeDrawMatchedLineImage) {
		this.writeDrawMatchedLineImage = writeDrawMatchedLineImage;
	}

	public void setWriteDebugImage(boolean writeDebugImage) {
		this.writeDebugImage = writeDebugImage;
	}

	public void setDebugImageDir(String debugImageDir) {
		this.debugImageDir = debugImageDir;
	}



	@Data
    @AllArgsConstructor
    @Builder
    protected static class MatchContext {
    	String destImagePath;
    	String srcImagePath;
    	Mat destImage;
    	Mat srcImage;
    	KeyPoints destKeyPoints;
    	KeyPoints srcKeyPoints;
    	List<DMatch> bestMatchs;
    }
    
    @Data
    @AllArgsConstructor
    @Builder
    protected static class KeyPoints {
    	MatOfKeyPoint detectKeyPoints;
    	MatOfKeyPoint extractorKeyPoints;
    }
    
}

