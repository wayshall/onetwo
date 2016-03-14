package org.onetwo.common.db.generator.utils;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.CUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class DbGeneratorUtills {
	
	public static Map<String, String> parse(String comment){
		Map<String, String> commentInfo = Maps.newHashMap();
		List<String> contents = CUtils.iterableToList(Splitter.on('\n').trimResults().omitEmptyStrings().split(comment));
		contents.stream().forEach(line->{
			List<String> datas = CUtils.iterableToList(Splitter.on(':').trimResults().omitEmptyStrings().split(line));
			if(datas.isEmpty())
				return;
			if(datas.size()==1){
				commentInfo.put(datas.get(0), "");
			}else if(datas.size()==2){
				commentInfo.put(datas.get(0), datas.get(1));
			}else{
				commentInfo.put(datas.get(0), Joiner.on("").join(datas.subList(1, datas.size())));
			}
		});
		return commentInfo;
	}

}
