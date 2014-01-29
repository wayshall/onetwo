package org.onetwo.plugins.batch.utils;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class BatchUtils {
	
	private BatchUtils(){
	}

	public static <T> SimpleFlatFileItemReader<T> newFlatFileItemReader(String path, String title, FieldSetMapper<T> mapper){
		return newFlatFileItemReader(new FileSystemResource(path), 1, DelimitedLineTokenizer.DELIMITER_COMMA, title, mapper);
	}

	public static <T> SimpleFlatFileItemReader<T> newFlatFileItemReader(String path, int linesToSkip, String delimiter, String title, FieldSetMapper<T> mapper){
		return newFlatFileItemReader(new FileSystemResource(path), linesToSkip, delimiter, title, mapper);
	}
	public static <T> SimpleFlatFileItemReader<T> newFlatFileItemReader(Resource resource, int linesToSkip, String delimiter, String title, FieldSetMapper<T> mapper){
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(delimiter);
		tokenizer.setNames(StringUtils.tokenizeToStringArray(title, delimiter));
		DefaultLineMapper<T> lineMapper = new DefaultLineMapper<T>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(mapper);
		
		SimpleFlatFileItemReader<T> reader = new SimpleFlatFileItemReader<T>();
		reader.setLineMapper(lineMapper);
		reader.setResource(resource);
		reader.setLinesToSkip(linesToSkip);
		/*try {
			reader.afterPropertiesSet();
		} catch (Exception e) {
			throw new BaseException("create FlatFileItemReader error: " + e.getMessage(), e);
		}*/
//		reader.open(new ExecutionContext());
		
		return reader;
	}
}
