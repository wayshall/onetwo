package org.onetwo.boot.groovy;

import java.util.Date;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class GroovyScriptData {
	
	/****
	 * 查询id或代码
	 */
	String code;
	String content;
	Date updateAt;

}
