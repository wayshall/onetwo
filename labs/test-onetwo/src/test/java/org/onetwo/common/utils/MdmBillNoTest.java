package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
		Long numb = seq.getAndIncrement()+8;
		String str = Long.toOctalString(numb);
		System.out.println(str);
	}
	static int[][] digis = new int[][]{
		{2, 3, 4, 5, 6},//year
		{0, 1},//month
		{7, 8, 9}//day
	};
	static int max_number = 9999;
	
	static PrimeBasicGeneraor generator = new PrimeBasicGeneraor(max_number);
	
	private final Integer MAX_NUMBER = 9999999;
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
				int index = RandUtils.randomInt(4);
				billNo.append(DATAS[index]);
			}
			billNo.append(str);
		}
		billNo.append(RandUtils.randomInt(10));
		
		return billNo.toString();
	}

	int count = 10000;// 3600;
	int times = 10;
	Collection<String> billList = new ArrayList<String>(count * times);

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
				billNo = getBillNo2();
				if (billList.contains(billNo)) {
					System.out.println(billList);
					String msg = LangUtils.toString("第${0}次时重复了：${1}", i, billNo);
					System.out.println(msg);
//					Assert.fail(msg);
					break;
				} else {
					System.out.println(i + ":" + billNo);
					billList.add(billNo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			Assert.fail(e.getMessage());
		} finally {
			System.out.println(billList);
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
