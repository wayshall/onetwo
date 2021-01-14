package org.onetwo.boot.module.rxtx;

import java.math.BigDecimal;

/**
 * 分度值代号对应表分度值和小数点位数：
X = 0  ： d = 0.0001				X=9  ： d = 0. 1
X = 1  ： d = 0.0002				X= A  ： d = 0.2
X = 2  ： d = 0.0005				X= B  ； d = 0.5
X = 3  ： d = 0.001				X= C  ： d = 1			（常用）
X = 4  ： d = 0.002				X= D  ： d = 2
X = 5  ： d = 0.005				X= E  ： d = 5
X = 6  ： d = 0.01	
X = 7  ： d = 0.02	
X = 8  ： d = 0.05	
 * @author weishao zeng
 * <br/>
 */

public enum WeighDivisionValues {
	DIVISION_0(new BigDecimal("0.0001")),
	DIVISION_1(new BigDecimal("0.0002")),
	DIVISION_2(new BigDecimal("0.0005")),
	DIVISION_3(new BigDecimal("0.001")),
	DIVISION_4(new BigDecimal("0.002")),
	DIVISION_5(new BigDecimal("0.005")),
	DIVISION_6(new BigDecimal("0.01")),
	DIVISION_7(new BigDecimal("0.02")),
	DIVISION_8(new BigDecimal("0.05")),
	DIVISION_9(new BigDecimal("0.1")),
	DIVISION_A(new BigDecimal("0.2")),
	DIVISION_B(new BigDecimal("0.5")),
	DIVISION_C(new BigDecimal(1)),
	DIVISION_D(new BigDecimal(2)),
	DIVISION_E(new BigDecimal(5));
	
	final private BigDecimal division;

	private WeighDivisionValues(BigDecimal division) {
		this.division = division;
	}

	public BigDecimal getDivision() {
		return division;
	}

}
