package org.onetwo.common.biz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.PrimeBasicGeneraor;
import org.onetwo.common.utils.RandUtils;
import org.onetwo.common.utils.propconf.Environment;

public class MdmBillNoTest {
	static AtomicLong seq = new AtomicLong(1);
	static Date start = DateUtil.parse("2013-7-11 00:00:00");


	@Before
	public void before() {
		System.out.println(generator);
	}

	public String getBillNo(Date date, int prime) {
		int maxSeq = 1000;
		// Date now = DateUtil.parse("2013-7-10 00:00:00");
		long diff = date.getTime() - start.getTime();
		int days = (int) TimeUnit.MILLISECONDS.toDays(diff);
		Long primeOfDays = generator.getPrime(days);
		System.out.println("diff:" + days + ", primeOfHours:" + primeOfDays);
		primeOfDays += prime;
		long seqNumb = seq.getAndIncrement();
		int repeat = (int) seqNumb / maxSeq;
		if (repeat > 0) {
			primeOfDays += repeat;
			Long primeOfNextDays = generator.getPrime(days + 1);
			System.out.println("next:" + (days + 1) + ", primeOfnext:" + primeOfNextDays);
			primeOfNextDays += prime;
			if (primeOfDays >= primeOfNextDays)
				throw new RuntimeException("无法继续生成数列……");
		}
		String str = primeOfDays.toString();
		System.out.println("str:" + str);
		str = str + MyUtils.append(String.valueOf(seqNumb % maxSeq), 2, "0") + RandUtils.randomInt(10);
		str = MyUtils.append(str, 9, "0");
		return str;
		// return RandUtils.padLeftWithRandom(str, 9);
	}

	@Test
	public void test(){
		System.out.println(Long.toString(5000000, 7));
		System.out.println(Long.toString(5000000, 7).length());
	}
	static int[][] digis = new int[][]{
		{2, 3, 4, 5, 6},//year
		{0, 1},//month
		{7, 8, 9}//day
	};
	static int max_number = 9999;
	
	static PrimeBasicGeneraor generator = new PrimeBasicGeneraor(max_number);
	
	private final int[] DATAS = new int[]{0, 7, 8, 9};
	private final int RADIX = 7;
	/*****
	 * 生成12位编码，前面3位固定，后面9位要求非顺序的唯一序列
	 * 后9位：
前后个一位随机数
中间7位是序列的7进制
	 * @return
	 */
	public String getBillNo2() {
		StringBuilder billNo = new StringBuilder();
		String env = "dev";

		if(Environment.PRODUCT.equals(env)) {
			billNo.append("5");
		} else {
			billNo.append("4");
		}
		billNo.append("13");
		
		int head = RandUtils.randomInt(10);
		int seqLength = MAX_NUMBER.toString().length();
		billNo.append(head);
		String str = Long.toString(seq.getAndIncrement()%MAX_NUMBER, RADIX);
		if(str.length()>seqLength){
			billNo.append(str.substring(0, seqLength));
		}else{
			int count = seqLength - str.length();
			for(int i=0; i<count; i++){
				int index = RandUtils.randomInt(DATAS.length);
				billNo.append(DATAS[index]);
			}
			billNo.append(str);
		}
		billNo.append(RandUtils.randomInt(10));
		
		return billNo.toString();
	}

	/****
	 * 0必须在第一个，因为如果在其它位置，hash之后会出现0xxx的数字，导致很快出现重复
	 * 因为7位七进制的最大数为十进制的5百多万，所以前面5位尽量不要排大于5的数字
	 * 比如一个序列是1百多万，hash之后，如果1 hash 到了9，就变成了9百多万，溢出了（相对最多5百多万）
	 */
	static int[] NUMBERS = new int[]{0, 4, 2, 3, 1, 9, 7, 5, 8, 6};

	private final Integer MAX_NUMBER = 9999999;
	public String getBillNo3() {
		StringBuilder billNo = new StringBuilder();
		String env = "dev";

		if(Environment.PRODUCT.equals(env)) {
			billNo.append("5");
		} else {
			billNo.append("4");
		}
		billNo.append("13");
		
		long seqIndex = seq.getAndIncrement();
		long numb1 = generator.getPrime((int)seqIndex);
		long numb2 = generator.getPrime((int)seqIndex+1);
		Long numb = numb1 + RandUtils.randomInt((int)(numb2-numb1));
		
		String str = Long.toString(numb, RADIX);
		int seqLength = 9;//MAX_NUMBER.toString().length();
		if(str.length()>seqLength){
			str = str.substring(0, seqLength);
		}else{
			int count = seqLength - str.length();
			for(int i=0; i<count; i++){
				int index = RandUtils.randomInt(4);
				str = DATAS[index] + str;
			}
		}
		
		for(Character ch : str.toCharArray()){
			billNo.append(NUMBERS[Integer.parseInt(ch.toString())]);
		}
		if(billNo.length()>12)
			throw new BaseException("error");
		
		return billNo.toString();
	}
	

	/*****
	 * 序列，大乱，然后转为七进制
	 * @return
	 */
	public String getBillNo4(int forIndex) {
		StringBuilder billNo = new StringBuilder();
		String env = "dev";

		if(Environment.PRODUCT.equals(env)) {
			billNo.append("5");
		} else {
			billNo.append("4");
		}
		billNo.append("13");
		
		Long numb = seq.getAndIncrement();

		String hashStr = "";
		for(Character ch : numb.toString().toCharArray()){
			hashStr = hashStr + NUMBERS[Integer.parseInt(ch.toString())];
		}
		numb = Long.parseLong(hashStr);
		String radixStr = Long.toString(numb, RADIX);
		int seqLength = 8;//MAX_NUMBER.toString().length();
		if(radixStr.length()>seqLength){
			radixStr = radixStr.substring(0, seqLength);
		}else{
			int count = seqLength - radixStr.length();
			for(int i=0; i<count; i++){
				int index = RandUtils.randomInt(DATAS.length);
				radixStr = DATAS[index] + radixStr;
			}
		}

		billNo.append(radixStr.substring(0, radixStr.length()-1));
		billNo.append(RandUtils.randomInt(10));
		billNo.append(radixStr.substring(radixStr.length()-1));
		
		if(billNo.length()!=12)
			throw new BaseException("生成序列错误：" + billNo);
		
		String billStr = billNo.toString();
		if (billList.contains(billStr)) {
			//第5000006次时重复了：413136333044, 递增序列：5000008, hash:9000005, hashStr: 13633304
			String msg = LangUtils.toString("第${0}次时重复了：${1}, 递增序列：${2}, hash:${3}, hashStr: ${4}", forIndex, billStr, seq.get(), hashStr, radixStr);
			System.out.println(msg);
//			System.out.println(billList);
			Assert.fail(msg);
		} else {
//			System.out.println(forIndex + ":" + billNo);
			if(seq.get()/100000>0 && seq.get()%100000==0){
				System.out.println(forIndex + ":" + billStr);
			}
			billList.add(billStr);
		}
		
		return billStr;
	}
	
	int count = 10000000;// 3600;
	int times = 1;
	Collection<String> billList = new HashSet<String>(count * times);

	@Test
	public void testBillNo() {
		 Date now = new Date();//DateUtil.parse("2023-7-11 00:00:00");
//		Date now = DateUtil.parse("2014-7-10 00:00:00");
		for (int i = 0; i < times; i++) {
			this.testBillNo2(DateUtil.addDay(now, i));
		}
	}

	public void testBillNo2(Date date) {
		System.out.println("---");
		for(int i : digis[0]){
			System.out.print(i+", ");
		}
		System.out.println("--");
		String billNo = "";

		try {

			for (int i = 0; i < count; i++) {
				billNo = getBillNo4(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			Assert.fail(e.getMessage());
		} finally {
//			System.out.println(billList);
			System.out.println("size: " + billList.size());
		}
	}

	public void testBillNo(Date date) {
		String billNo = "";

		try {

			for (int i = 0; i < count; i++) {
				billNo = getBillNo(date, 7);
				if (billList.contains(billNo)) {
					System.out.println(billList);
					String msg = LangUtils.toString("第${0}次时重复了：${1}", i, billNo);
					Assert.fail(msg);
				} else {
					System.out.println(i + ":" + billNo);
					billList.add(billNo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			System.out.println(billList);
			System.out.println("size: " + billList.size());
		}
	}

}
