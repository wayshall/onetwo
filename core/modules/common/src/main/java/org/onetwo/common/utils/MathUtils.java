package org.onetwo.common.utils;

import java.util.ArrayList;
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
	
	private MathUtils(){
	}

}
