package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MathUtils {
	
	public static boolean isPrime(long n){
		if(n<=1)
			return false;
		for(int i=2; (i*i)<(n+1); i++){
			if(n%i==0)
				return false;
		}
		return true;
	}
	
	public static long getPrime(int index){
		if(index<0)
			throw new IllegalArgumentException();
		int pcount = 0;
		long numb = 2;
		while(true){
	        if (isPrime(numb)){
	        	pcount++;
	        }
	        if(pcount-1==index)
	        	break;
	        numb++;
	    }
		return numb;
	}
	
	public static Long[] generatedPrimes(int primeCount){
		int pcount = 0;
		long numb = 2;
		
		Long[] result = new Long[primeCount];
		while(true){
	        if (isPrime(numb)){
	        	result[pcount++]=numb;
	        }
	        if(pcount==primeCount)
	        	break;
	        numb++;
	    }
		return result;
	}
	

	public static Long[] generatedPrimesUntil(long maxNumber){
		long numb = 2;
		
		List<Long> primes = new ArrayList<Long>();
		while(true){
			if(numb>maxNumber)
				break;
			
	        if (isPrime(numb)){
	        	primes.add(numb);
	        }
	        numb++;
	    }
		return primes.toArray(new Long[primes.size()]);
	}


	public static int sum(int... counts){
		if(counts==null)
			return 0;
		int total = 0;
		for(int c : counts){
			total += c;
		}
		return total;
	}

	public static long sum(long... counts){
		if(counts==null)
			return 0;
		long total = 0;
		for(long c : counts){
			total += c;
		}
		return total;
	}

	public static double sum(double... counts){
		if(counts==null)
			return 0;
		double total = 0;
		for(double c : counts){
			total += c;
		}
		return total;
	}

	public static BigDecimal sumNumbers(Number[] counts){
		BigDecimal total = BigDecimal.ZERO;
		for(Number c : counts){
			total.add(BigDecimal.valueOf(c.doubleValue()));
		}
		return total;
	}

	public static BigDecimal sumNumbers(Collection<? extends Number> counts){
		BigDecimal total = BigDecimal.ZERO;
		for(Number c : counts){
			total.add(BigDecimal.valueOf(c.doubleValue()));
		}
		return total;
	}
	
	private MathUtils(){
	}

}
