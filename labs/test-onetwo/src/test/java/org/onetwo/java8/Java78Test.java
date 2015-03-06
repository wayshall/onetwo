package org.onetwo.java8;

import java.util.stream.Stream;

import org.junit.Test;
import org.onetwo.common.utils.list.JFishList;


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

}
