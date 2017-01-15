package projects.manager.vo;

import lombok.Data;

@Data
public class ProductActiveStatisVo {
	
	private String activeDate;
	private Long amount;

	public Long getAmount(){
		if(amount==null)
			return 0L;
		return amount;
	}
}
