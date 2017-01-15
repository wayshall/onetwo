package projects.manager.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActiveIncomeStatisVo {
	
	private String activeDate;
	private String userName;
	private String productName;
	private Long activeAmount;
	private BigDecimal productPrice;
	private BigDecimal income;
	private String remark;
	
	public Long getActiveAmount(){
		if(activeAmount==null)
			return 0L;
		return activeAmount;
	}

}
