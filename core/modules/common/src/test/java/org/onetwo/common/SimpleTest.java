package org.onetwo.common;

import java.math.BigInteger;

import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangOps;

public class SimpleTest {
	
	@Test
	public void test(){
		String milis = "014";
		BigInteger time = new BigInteger("13164449"+milis);
		time = time.multiply(new BigInteger("20"));
		time = time.add(new BigInteger(milis));
		time = time.mod(new BigInteger("3000"));
		System.out.println("time:" + time);

		int alength = 100;
		int[] a = new int[alength];
		for(int i=0; i<alength; i++){
			a[i] = i;
		}

		int blength = 10000;
		int[] b = new int[blength];
		for(int i=0; i<blength; i++){
			b[i] = i;
		}
		LangOps.timeIt("100次", ()->{
			for(int i=0; i<alength; i++){
				if(a[i]==99){
					System.out.println("finish!");
				}
			}
		});
		LangOps.timeIt("10000次", ()->{
			for(int i=0; i<blength; i++){
				if(b[i]==9999){
					System.out.println("finish!");
				}
			}
		});
	}

}
