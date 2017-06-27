
package projects.manager.service.impl;

import java.math.BigDecimal;
import java.util.Collection;

import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projects.manager.dao.ProductStatisDao;
import projects.manager.entity.ProductIncome;
import projects.manager.vo.BudgetStatisVo;

@Service
@Transactional
public class ProductIncomeServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private ProductStatisDao productStatisDao;
    
    public void findPage(Page<ProductIncome> page, ProductIncome productIncome){
        Querys.from(baseEntityManager, ProductIncome.class)
        		.where()
        		.addFields(productIncome)
        		.ignoreIfNull()
        		.end()
        		.toQuery()
        		.page(page);
    }
    
    public void save(ProductIncome entity) {
    	int count = this.baseEntityManager.countRecord(ProductIncome.class, 
													"incomeDate", entity.getIncomeDate(),
													"productId", entity.getProductId())
													.intValue();
		if(count>0){
			throw new ServiceException("添加失败：此日期和产品的记录已存在，请检查！");
		}
		baseEntityManager.persist(entity);
	}

	public void update(ProductIncome entity) {
    	int count = this.baseEntityManager.countRecord(ProductIncome.class, 
														"incomeDate", entity.getIncomeDate(),
														"productId", entity.getProductId(),
														"id:!=", entity.getId())
														.intValue();
		if(count>0){
			throw new ServiceException("更新失败：此日期和产品的记录已存在，请检查！");
		}
		baseEntityManager.update(entity);
	}
    
    public ProductIncome findById(Long id) {
		return baseEntityManager.findById(ProductIncome.class, id);
	}

	public Collection<ProductIncome> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(ProductIncome.class, id);
	}
	
    
    public BudgetStatisVo statisSummary(){
    	BigDecimal totalPay = this.productStatisDao.statisTotalPay();
    	BigDecimal totalIncome = this.productStatisDao.statisTotalIncome();
    	return new BudgetStatisVo(totalPay, totalIncome);
    }
}