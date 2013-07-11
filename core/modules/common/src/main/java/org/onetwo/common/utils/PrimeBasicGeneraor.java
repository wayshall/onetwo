package org.onetwo.common.utils;


public class PrimeBasicGeneraor {

	public static PrimeBasicGeneraor generatedMaxNumber(long maxNumber){
		return new PrimeBasicGeneraor(maxNumber);
	}
	public static PrimeBasicGeneraor generatedCount(int primeCount){
		Long[] cache = MathUtils.generatedPrimes(primeCount);
		return new PrimeBasicGeneraor(cache);
	}
	private Long[] primesCache; 
	

	public PrimeBasicGeneraor(long maxNumber){
		if(maxNumber>1){
			this.primesCache = MathUtils.generatedPrimesUntil(maxNumber);
		}else{
			this.primesCache = new Long[0];
		}
	}
	
	private PrimeBasicGeneraor(Long[] primesCache){
		this.primesCache = primesCache;
	}
	
	protected boolean isPrime(long n){
		return MathUtils.isPrime(n);
	}

	public long getPrime(int index){
		if(index<0)
			throw new IllegalArgumentException();
		if(primesCache.length!=0 && primesCache.length>index)
			return primesCache[index];
		return MathUtils.getPrime(index);
	}
	
	public int getPrimeCount(){
		return this.primesCache.length;
	}
	
	public String toString(){
		return StringUtils.join(primesCache, ", ");
	}
}
