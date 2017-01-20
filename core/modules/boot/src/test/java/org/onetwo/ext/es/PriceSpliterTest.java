package org.onetwo.ext.es;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;
import org.onetwo.ext.es.SimpleSearchQueryBuilder.RangeResult;

public class PriceSpliterTest {

	@Test
	public void testCalcConfidenceSection(){
		int maxCount = 32;
		ImmutablePair<BigDecimal, BigDecimal> section = ConfidenceLevelUtils.calcConfidenceSection(57.27, 171.67);
		System.out.println("sections:"+section);
		
		final List<ImmutablePair<Integer, Integer>> splits = PriceSpliter.split(section, maxCount);
		System.out.println("splits["+splits.size()+"]:"+splits);
		
		AtomicInteger index = new AtomicInteger(0);
		List<RangeResult> rangeResult = splits.stream().map(pair->{
			RangeResult r = new RangeResult(pair.getLeft(), pair.getRight());
			if(index.get()==1){
				r.setDocCount(1);
			}else if(index.get()==2){
				r.setDocCount(2);
			}else if(index.get()==5){
				r.setDocCount(7);
			}else if(index.get()==8){
				r.setDocCount(4);
			}else if(index.get()==splits.size()){
				r.setDocCount(5);
			}
			index.incrementAndGet();
			return r;
		})
		.collect(Collectors.toList());
		System.out.println("splits["+rangeResult.size()+"]:"+rangeResult);
		
		double stdDeviation = 57;
		PriceSpliter spliter = new PriceSpliter(57, stdDeviation, rangeResult.size(), 5);
		List<RangeResult> mereged = spliter.split(rangeResult);
		System.out.println("mereged["+mereged.size()+"]:"+mereged);
		
		List<ImmutablePair<Integer, Integer>> splits2 = PriceSpliter.split(ImmutablePair.of(BigDecimal.valueOf(0), BigDecimal.valueOf(1000000)), maxCount);
		System.out.println("splits["+splits2.size()+"]:"+splits2);
	}
}
