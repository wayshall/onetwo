package org.onetwo.ext.es;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.onetwo.ext.es.SimpleSearchQueryBuilder.RangeQueryer;
import org.onetwo.ext.es.SimpleSearchQueryBuilder.RangeResult;

import com.google.common.collect.ImmutableMap;

public class PriceSpliter {
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
	
	final private long totalCount;
	final private List<ImmutablePair<Integer, Integer>> confidenceSections;
	private int expectSectionSize;
	
	public PriceSpliter(double averagePrice, double stdDeviation, long totalCount, int expectSectionSize) {
		super();
		this.expectSectionSize = expectSectionSize;
		this.totalCount = totalCount;
		ImmutablePair<BigDecimal, BigDecimal> confidenceSection = calcConfidenceSection(averagePrice, stdDeviation);
		this.confidenceSections = split(confidenceSection);
	}

	private ImmutablePair<BigDecimal, BigDecimal> calcConfidenceSection(double avg, double sd){
		Double zscore = getZscore(95);
		return calcConfidenceSection(BigDecimal.valueOf(avg), BigDecimal.valueOf(sd), zscore);
	}
	
	
	/***
	 * (μ-Ζα/2σ , μ+Ζα/2σ)
	 * 
	 * @param avg
	 * @param sd
	 * @param zscore
	 * @return
	 */
	private ImmutablePair<BigDecimal, BigDecimal> calcConfidenceSection(BigDecimal avg, BigDecimal sd, double zscore){
		avg = avg.setScale(2, RoundingMode.HALF_UP);
		BigDecimal zscoreDecimal = BigDecimal.valueOf(zscore);
		BigDecimal start = avg.subtract(sd.multiply(zscoreDecimal));
		if(start.doubleValue()<0){
			start = BigDecimal.ZERO;
		}
		BigDecimal end = avg.add(sd.multiply(zscoreDecimal));
		ImmutablePair<BigDecimal, BigDecimal> pair = ImmutablePair.of(start, end);
		return pair;
	}
	
	private List<ImmutablePair<Integer, Integer>> split(ImmutablePair<BigDecimal, BigDecimal> section){
		return split(section, 32);
	}
	
	/***
	 * 根据平均值切分区间
	 * @param section
	 * @param maxSplitCount
	 * @return
	 */
	private List<ImmutablePair<Integer, Integer>> split(ImmutablePair<BigDecimal, BigDecimal> section, int maxSplitCount){
		int start = section.getLeft().setScale(0, RoundingMode.DOWN).intValue();
		final int end = section.getRight().setScale(0, RoundingMode.UP).intValue();
		int interval = (end-start)/maxSplitCount;
		if(interval<10){
			interval = 10;
		}
		int currentValue = start;
		List<ImmutablePair<Integer, Integer>> sections = new ArrayList<>(maxSplitCount+1);
		while(currentValue<end){
			currentValue = start + interval;
			if(currentValue/100>1){
				int remainer = currentValue%100;
				currentValue += 99-remainer;
			}else{
				int remainer = currentValue%10;
				currentValue += 9-remainer;
			}
			ImmutablePair<Integer, Integer> data = ImmutablePair.of(start, currentValue>=end?null:currentValue);
			sections.add(data);
			start = currentValue+1;
		}
		return sections;
	}
	
	public List<RangeQueryer> getRangeQueryers(){
		List<RangeQueryer> rangeParams = confidenceSections.stream().map(pair->{
			return new RangeQueryer(pair.getLeft(), pair.getRight());
		})
		.collect(Collectors.toList());
		return rangeParams;
	}
	
	public List<String> splitAndGetKeys(List<RangeResult> rangeResult){
		List<RangeResult> splitRange = split(rangeResult);
		List<String> keys = splitRange.stream()
									.map(r->r.getKey())
									.collect(Collectors.toList());
		return keys;
	}
	public List<RangeResult> split(List<RangeResult> rangeResult){
		int expectMergeSize = this.expectSectionSize;
		int sum = rangeResult.stream().mapToInt(r->r.getDocCount()).sum();
		int avgSize = sum/(rangeResult.size()>expectMergeSize?expectMergeSize:rangeResult.size());
		System.out.println("avgSize:"+avgSize);
		
		List<RangeResult> mereged = new ArrayList<>();
		Iterator<RangeResult> it = rangeResult.iterator();
		int sectionSum = 0;
		RangeResult current = null;
		RangeResult meregedRes = null;
		while(it.hasNext()){
			current = it.next();
			if(meregedRes==null){
				meregedRes = new RangeResult(current.getFrom(), null);
			}
			sectionSum += current.getDocCount();
			meregedRes.setTo(current.getTo());
			meregedRes.setDocCount(sectionSum);
			if(sectionSum>avgSize){
				mereged.add(meregedRes);
				meregedRes = null;
				sectionSum = 0;
			}
		}
		if(meregedRes!=null){
			mereged.add(meregedRes);
		}
		return mereged;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	
}
