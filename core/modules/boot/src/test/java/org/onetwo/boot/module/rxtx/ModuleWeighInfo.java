package org.onetwo.boot.module.rxtx;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class ModuleWeighInfo {
	
	/***
	 * 十六进制编号
	 */
	String moduleNo;
	
	/***
	 * 称重数
	 */
	BigDecimal weigh;
	
	WeighDivisionValues division;
	
	Long deviceId;


}
