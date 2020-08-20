package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;

public class BinaryUtilsTest {

	
	@Test
	public void testByte() {
		// 测试最后两位是否为1, 
		byte b = 4;
		assertThat((b & 0b11) == 0b11).isFalse();
		b = 3;
		assertThat((b & 0b11) == 0b11).isTrue();
		// 0b11=3
		b = 4;
		assertThat((b & 3) == 3).isFalse();
		b = 3;
		assertThat((b & 3) == 3).isTrue();
		String bin = Integer.toBinaryString(b);
		System.out.println("bin: " + bin);
		assertThat(BinaryUtils.getBitValue(b, 1)).isEqualTo((byte)1);
		assertThat(BinaryUtils.getBitValue(b, 1)).isEqualTo((byte)1);
		assertThat(BinaryUtils.getBitValue(b, 2)).isEqualTo((byte)0);
		assertThat(BinaryUtils.getBitValue(b, 3)).isEqualTo((byte)0);
		

		int value = 1000;
		bin = Integer.toBinaryString(value);
		System.out.println("value bin: " + bin);
		assertThat(BinaryUtils.getBitValue(value, 1)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(value, 1)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(value, 2)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(value, 3)).isEqualTo(1);
		assertThat(BinaryUtils.getBitValue(value, 4)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(value, 5)).isEqualTo(1);
		

		assertThat(BinaryUtils.getBitValue(0xF, 3)).isEqualTo(1);
		assertThat(BinaryUtils.getBitValue(0x6, 3)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(6, 3)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(0x63, 7)).isEqualTo(0);
		assertThat(BinaryUtils.getBitValue(0xF3, 7)).isEqualTo(1);
		
		assertThat(BinaryUtils.getHighBits((byte)0xF3)).isEqualTo((byte)0xF);
		assertThat(BinaryUtils.getLowBits((byte)0xF3)).isEqualTo((byte)0x3);
		
		String hex = "000332";
		byte[] bytes = LangUtils.hex2Bytes(hex);
		int result = BinaryUtils.bytesToInt(bytes);
		System.out.println("result: " + result);
		assertThat(result).isEqualTo(818);
		
		hex = "7FFFFFFF";// 0111 1111 1111 1111 1111 1111 1111 1111 4个字节，32位，最高位0位正数，即为int类型的最大值
		bytes = LangUtils.hex2Bytes(hex);
		result = BinaryUtils.bytesToInt(bytes);
		System.out.println("result: " + result);
		assertThat(result).isEqualTo(Integer.MAX_VALUE);
	}
	
	
	@Test
	public void testBinaryToOct(){
		String bin = "01";
		int val = Integer.valueOf(bin, 2);
		Assert.assertEquals(1, val);
		bin = "11";
		val = Integer.valueOf(bin, 2);
		Assert.assertEquals(3, val);
	}
	
	@Test
	public void testOctToBinary(){
		int val = 10;
		String bin = Integer.toBinaryString(val);
		Assert.assertEquals("1010", bin);
		val = 4;
		bin = Integer.toBinaryString(val);
		Assert.assertEquals("100", bin);
	}

}
