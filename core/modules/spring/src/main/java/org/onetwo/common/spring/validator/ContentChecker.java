package org.onetwo.common.spring.validator;

import java.util.List;

/**
 * 内容检测
 * @author weishao zeng
 * <br/>
 */
public interface ContentChecker {
	
	/****
	 * 
	 * @author weishao zeng
	 * @param content  要检测是否有敏感词的文本内容
	 * @return 返回检测到敏感词
	 */
	public List<String> check(String content);

}
