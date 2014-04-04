package org.onetwo.plugins.batch.utils;

import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.list.JFishList;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;

public class SimpleFlatFileItemReader<T> extends FlatFileItemReader<T> {

	public void fastOpen(){
		try {
			afterPropertiesSet();
		} catch (Exception e) {
			throw new BaseException("create SimpleFlatFileItemReader error: " + e.getMessage(), e);
		}
		open(new ExecutionContext());
	}
	
	public List<T> readList(){
		T data = null;
		List<T> datalist = JFishList.create();
		try {
			while((data=read())!=null){
				datalist.add(data);
			}
		} catch (Exception e) {
			throw new BaseException("read data error : " + e.getMessage(), e);
		} finally{
			close();
		}
		return datalist;
	}
}
