package org.onetwo.ext.es;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.onetwo.ext.es.SimpleSearchQueryBuilder.RangeQueryer;
import org.onetwo.ext.es.SimpleSearchQueryBuilder.RangeResult;

public class PriceSpliter {

	
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

	/****
	 * 置信区间
	 * @param avg
	 * @param sd
	 * @return
	 */
	private ImmutablePair<BigDecimal, BigDecimal> calcConfidenceSection(double avg, double sd){
		if(Double.isNaN(avg) || Double.isNaN(sd)){
			throw new IllegalArgumentException("avg or sd is not a number: "+avg+", "+sd);
		}
		Double zscore = ConfidenceLevelUtils.getZscore(95);
		return ConfidenceLevelUtils.calcConfidenceSection(BigDecimal.valueOf(avg), BigDecimal.valueOf(sd), zscore);
	}
	
	
	
	private List<ImmutablePair<Integer, Integer>> split(ImmutablePair<BigDecimal, BigDecimal> section){
		return split(section, 32);
	}
	
	/***
	 * 根据最大切分数量，利用平均值，切分区间
	 * @param section
	 * @param maxSplitCount
	 * @return
	 */
	public static List<ImmutablePair<Integer, Integer>> split(ImmutablePair<BigDecimal, BigDecimal> section, int maxSplitCount){
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
