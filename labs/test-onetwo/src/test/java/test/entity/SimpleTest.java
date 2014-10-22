package test.entity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;



public class SimpleTest {

	@Test
	public void test(){
	}

//	@Test
	public void testSplitList(){
		List<String> idcardList = LangUtils.newArrayList("aa", "bb", "cc", "dd", "ee", "ff", "GG");
		int processSize = 8;
		int count = idcardList.size()%processSize==0?(idcardList.size()/processSize):(idcardList.size()/processSize+1);
		for(int i=0; i<count; i++){
			int fromIndex = processSize*i;
			int endIndex = fromIndex+processSize;
			if(endIndex>idcardList.size()){
				endIndex = idcardList.size();
			}
			List<String> sublist = idcardList.subList(fromIndex, endIndex);
			System.out.println("sublist: " + sublist);
		}
	}
}
