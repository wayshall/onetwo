package org.onetwo.common.utils;

import java.text.DecimalFormat;

import org.onetwo.common.exception.BaseException;

/**
 * 非原创，来自网络
 */
public abstract class DistanceUtils {
	// 地球平均半径  （米）
    private static final double EARTH_RADIUS = 6378137;
    
    //把经纬度转为度（°）  
    private static double toAngle(double d) {  
        return d * Math.PI / 180.0;  
    }
    
    /***
     * 计算两个坐标之间的距离，单位：米
     * @author weishao zeng
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double calcDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
    	if(lng1==null || lat1==null || lng2==null || lat2==null){
//    		return Double.NaN;
    		throw new BaseException("参数错误，坐标不能为空！")
    							.put("lng1", lng1)
    							.put("lat1", lat1)
    							.put("lng2", lng2)
    							.put("lat2", lat2);
    	}
        double lat1Angle = toAngle(lat1);  
        double lat2Angle = toAngle(lat2);  
        double latDifferAngle = lat1Angle - lat2Angle;  
        double lngDifferAngle = toAngle(lng1) - toAngle(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDifferAngle / 2), 2) + Math.cos(lat1Angle) * Math.cos(lat2Angle) * Math.pow(Math.sin(lngDifferAngle / 2), 2)));  
        s = s * EARTH_RADIUS;  
        DecimalFormat df = new DecimalFormat("#.00");  
        s = Double.parseDouble(df.format(s));  
        return s;
    }
    
	
	private DistanceUtils(){
	}

}
