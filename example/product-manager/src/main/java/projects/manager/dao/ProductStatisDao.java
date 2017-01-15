package projects.manager.dao;

import java.math.BigDecimal;

import org.onetwo.common.utils.Page;

import projects.manager.vo.ActiveIncomeStatisVo;
import projects.manager.vo.ProductActiveStatisVo;

public interface ProductStatisDao {
	
	public void statisActiveByDatePage(Page<ProductActiveStatisVo> page);
	public ProductActiveStatisVo statisActive();
	public BigDecimal statisTotalPay();
	public BigDecimal statisTotalIncome();
	

	public void statisActiveIncomeByGroup(Page<ActiveIncomeStatisVo> page, Long userId, boolean hasSubUser);
	public ActiveIncomeStatisVo statisActiveIncomeTotal(Long userId, boolean hasSubUser);

}
