package org.onetwo.plugins.batch.utils;

import org.onetwo.common.exception.BaseException;
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
}
