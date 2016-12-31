package projects.manager.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class BudgetStatisVo {
	private BigDecimal totalPay;
	private BigDecimal totalIncome;

	public BudgetStatisVo(BigDecimal totalPay, BigDecimal totalIncome) {
		super();
		this.totalPay = totalPay==null?BigDecimal.valueOf(0):totalPay;
		this.totalIncome = totalIncome==null?BigDecimal.valueOf(0):totalIncome;
	}
	
	public double getRate(){
		if(totalIncome.doubleValue()<=0 || totalPay.doubleValue()<=0)
			return 0;
		return totalIncome.divide(totalPay, 5, RoundingMode.HALF_UP).doubleValue();
	}
}
