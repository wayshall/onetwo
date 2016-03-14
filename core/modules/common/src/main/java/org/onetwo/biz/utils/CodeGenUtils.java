package org.onetwo.biz.utils;

import java.util.Properties;
import java.util.UUID;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.propconf.AppConfig;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.MyUtils;

abstract public class CodeGenUtils {

	public static interface SeqCodeGenProvider<T> {
		public T generatedSeq(CodeGen code);
	}
	
	public static class CodeGen {
		public static final Long DEFAULT_MAX_NUMBER = 9999l;
		public static final String PREFIX_KEY = ".prefix";
		public static final String FORMAT_KEY = ".format";
		public static final String MAX_NUMBER_KEY = ".max.number";
		public static final String APPEND_STR_KEY = ".append.str";
		public static final String SEQ_NAME_KEY = ".seq.name";
		
		private String name;
		private String prefix;
		private String format;
		private Long maxNumber;
		private String appendStr;
		private String seqName;
		
		public CodeGen(){}
		
		public CodeGen(String nameKey, AppConfig config) {
			super();
			this.name = config.getProperty(nameKey, nameKey);
			this.prefix = config.getProperty(name+PREFIX_KEY);
			this.format = config.getProperty(name+FORMAT_KEY, "yyyyMMdd");
			this.maxNumber = config.getLong(name+MAX_NUMBER_KEY, DEFAULT_MAX_NUMBER);
			this.appendStr = config.getProperty(name+APPEND_STR_KEY, "0");
			this.seqName = config.getProperty(name+SEQ_NAME_KEY, "SEQ_"+name.toUpperCase());
		}
		
		public CodeGen(String nameKey, Properties config) {
			super();
			this.name = config.getProperty(nameKey, nameKey);
			this.prefix = config.getProperty(name+PREFIX_KEY);
			this.format = config.getProperty(name+FORMAT_KEY, "yyyyMMdd");
			String maxNumbStr = config.getProperty(name+MAX_NUMBER_KEY, DEFAULT_MAX_NUMBER.toString());
			try {
				this.maxNumber = Long.parseLong(maxNumbStr);
			} catch (Exception e) {
				this.maxNumber = DEFAULT_MAX_NUMBER;
			}
			this.appendStr = config.getProperty(name+APPEND_STR_KEY, "0");
			this.seqName = config.getProperty(name+SEQ_NAME_KEY, "SEQ_"+name.toUpperCase());
		}
		
		public CodeGen(String name, String prefix, String format, Long maxNumber, String appendStr) {
			super();
			this.name = name;
			this.prefix = prefix;
			this.format = format;
			this.maxNumber = maxNumber;
			this.appendStr = appendStr;
		}
		public String getPrefix() {
			return prefix;
		}
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		public String getFormat() {
			return format;
		}
		public void setFormat(String format) {
			this.format = format;
		}
		public Long getMaxNumber() {
			return maxNumber;
		}
		public void setMaxNumber(Long maxNumber) {
			this.maxNumber = maxNumber;
		}
		public String getAppendStr() {
			return appendStr;
		}
		public void setAppendStr(String appendStr) {
			this.appendStr = appendStr;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSeqName() {
			return seqName;
		}

		public void setSeqName(String seqName) {
			this.seqName = seqName;
		}

	}

	public static String generated(SeqCodeGenProvider<?> provider, CodeGen code){
		return generated(provider.generatedSeq(code), code);
	}
	
	public static String generated(Object genNumber, CodeGen code){
		String prefix = code.getPrefix(); 
		String format = code.getFormat(); 
		Long maxNumber = code.getMaxNumber();
		String appendChar = code.getAppendStr();
		
		Assert.notNull(prefix);
		Assert.notNull(maxNumber);
		Assert.hasText(format);

		Long seq = 0l;
		if(maxNumber>0){
			seq = Long.valueOf(genNumber.toString()) % maxNumber;
			if(seq==0)
				seq = maxNumber;
		}else{
			seq = Long.valueOf(genNumber.toString());
		}
		
		String numbStr = MyUtils.append(seq.toString(), maxNumber.toString().length(), appendChar);
		StringBuilder resultCode = new StringBuilder();
		resultCode.append(prefix).append(DateUtil.getString(format)).append(numbStr);
		return resultCode.toString();
	}
	
    public static String randomUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
