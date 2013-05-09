package org.onetwo.justtest.vm.btrace;

import java.util.Random;

public class Case1 {
	 public static void main(String[] args) throws Exception{
	      Random random=new Random();
	      CaseObject object=new CaseObject();
	      boolean result=true;
	      while(result){
	         result=object.execute(random.nextInt(1000));
	         Thread.sleep(1000);
	      }
	   }

}
