package org.onetwo.ext.es;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.onetwo.common.utils.MathUtils;

import com.google.common.collect.ImmutableMap;

public class ConfidenceLevelUtils {

	/***
	 * 置信水平 : 对应标准分数
	 */
	final private static Map<Integer, Double> CONFIDENCE_LEVELS = ImmutableMap.of(95, 1.96,
																					  90, 1.65,
																					  80, 1.28);
	

	/****
	 * 根据confidenceLevel获取分数
	 * @param confidenceLevel
	 * @return
	 */
	public static double getZscore(Integer confidenceLevel){
		if(!CONFIDENCE_LEVELS.containsKey(confidenceLevel)){
			throw new IllegalArgumentException("error confidenceLevel: " + confidenceLevel);
		}
		double zscore = CONFIDENCE_LEVELS.get(confidenceLevel);
		return zscore;
	}
	

	public static ImmutablePair<BigDecimal, BigDecimal> calcConfidenceSection(double avg, double stdDeviation){
		return calcConfidenceSection(BigDecimal.valueOf(avg), BigDecimal.valueOf(stdDeviation));
	}
	public static ImmutablePair<BigDecimal, BigDecimal> calcConfidenceSection(BigDecimal avg, BigDecimal sd){
		Double zscore = getZscore(95);
		return calcConfidenceSection(avg, sd, zscore);
	}

	/***
	 * (μ-Ζα/2σ , μ+Ζα/2σ)
	 * 置信区间
	 * @param avg
	 * @param stdDeviation 标准差
	 * @param zscore
	 * @return
	 */
	public static ImmutablePair<BigDecimal, BigDecimal> calcConfidenceSection(BigDecimal avg, BigDecimal stdDeviation, double zscore){
		avg = avg.setScale(2, RoundingMode.HALF_UP);
		BigDecimal zscoreDecimal = BigDecimal.valueOf(zscore);
		BigDecimal start = avg.subtract(stdDeviation.multiply(zscoreDecimal));
		if(start.doubleValue()<0){
			start = BigDecimal.ZERO;
		}
		BigDecimal end = avg.add(stdDeviation.multiply(zscoreDecimal));
		ImmutablePair<BigDecimal, BigDecimal> pair = ImmutablePair.of(start, end);
		return pair;
	}
	
	/***
	 * 计算标准差
	 * @param numbs
	 * @return
	 */
	public static double calcStdDeviation(double[] numbs){
		int n = numbs.length;
		double avg = MathUtils.sum(numbs)/n;
		double sum = 0;
		for(double num : numbs){
			sum += Math.pow(num-avg, 2);
		}
		double result = Math.sqrt(sum/n);
		return result;
	}
	
}
