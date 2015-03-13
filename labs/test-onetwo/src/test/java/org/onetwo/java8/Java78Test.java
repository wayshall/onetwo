package org.onetwo.java8;

import java.util.Map;
import java.util.stream.Stream;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.Langs;
import org.onetwo.common.utils.list.JFishList;

import com.google.common.collect.ImmutableMap;


public class Java78Test {
	
	@Test
	public void test() {
		/*String path = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		new Thread(()->{
			System.out.println("test");
		});
		
		JFishList.wrap("aa", "bb").each((String e, int index)->{
			return true;
		});
		
		Stream.of("aa1", "aa2", "bb1", "bb2").filter((String str)->{
			return str.startsWith("aa");
		}).map((String str)->{
			return str+":map";
		}).forEach(System.out::println);
		
	}
	
	@Test
	public void mapToArray(){
		Map<String, Integer> map = ImmutableMap.of("aa", 1, "bb", 2, "cc", 3);
		Object[] arrys = Langs.toArray(map);
		Stream.of(arrys).forEach(System.out::println);
		
		Assert.assertEquals(6, arrys.length);
		Assert.assertEquals("aa", arrys[0]);
		Assert.assertEquals(1, arrys[1]);
	}

}
