package test.entity;

import java.util.List;

import org.onetwo.common.utils.LangUtils;



public class Test {

	public static void main(String[] args){
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
