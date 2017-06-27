
package projects.manager.service.impl;

import java.util.Collection;

import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projects.manager.dao.ProductStatisDao;
import projects.manager.entity.Product;
import projects.manager.entity.ProductActive;
import projects.manager.utils.Enums.UserTypes;
import projects.manager.vo.ActiveIncomeStatisVo;
import projects.manager.vo.LoginUserInfo;
import projects.manager.vo.ProductActiveStatisVo;

@Service
@Transactional
public class ProductActiveServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private ProductStatisDao productStatisDao;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    
    public void findPage(Page<ProductActive> page, ProductActive productActive){
        Querys.from(baseEntityManager, ProductActive.class)
        		.where()
        		.addFields(productActive)
        		.ignoreIfNull()
        		.end()
        		.toQuery()
        		.page(page);
    }
    
    public void statisActiveByDatePage(Page<ProductActiveStatisVo> page){
    	this.productStatisDao.statisActiveByDatePage(page);
    }
    
    public ProductActiveStatisVo statisActive(){
    	return this.productStatisDao.statisActive();
    }

	public void save(ProductActive entity) {
		int count = this.baseEntityManager.countRecord(ProductActive.class, 
									"activeDate", entity.getActiveDate(),
									"activeUserId", entity.getActiveUserId(),
									"productId", entity.getProductId())
									.intValue();
		if(count>0){
			throw new ServiceException("添加失败：此日期、账户和产品的记录已存在，请检查！");
		}
		Product product = this.baseEntityManager.load(Product.class, entity.getProductId());
		entity.setProductPrice(product.getPrice());
		baseEntityManager.persist(entity);
	}

	public void update(ProductActive entity) {
		int count = this.baseEntityManager.countRecord(ProductActive.class, 
									"activeDate", entity.getActiveDate(),
									"activeUserId", entity.getActiveUserId(),
									"productId", entity.getProductId(),
									"id:!=", entity.getId())
									.intValue();
		if(count>0){
			throw new ServiceException("更新失败：此日期、账户和产品的记录已存在，请检查！");
		}
		baseEntityManager.update(entity);
	}

	public ProductActive findById(Long id) {
		return baseEntityManager.findById(ProductActive.class, id);
	}

	public Collection<ProductActive> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(ProductActive.class, id);
	}

	public void statisActiveIncomeByGroup(Page<ActiveIncomeStatisVo> page, LoginUserInfo loginUser) {
		productStatisDao.statisActiveIncomeByGroup(page, loginUser.getUserId(), UserTypes.FIRST==loginUser.getUserTypes());
	}

	public ActiveIncomeStatisVo statisActiveIncomeTotal(LoginUserInfo loginUser) {
		return productStatisDao.statisActiveIncomeTotal(loginUser.getUserId(), UserTypes.FIRST==loginUser.getUserTypes());
	}
	
	

}