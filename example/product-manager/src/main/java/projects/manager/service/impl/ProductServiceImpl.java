
package projects.manager.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projects.manager.entity.Product;

@Service
@Transactional
public class ProductServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;


    public Optional<Product> findById(Long id) {
		return Optional.ofNullable(baseEntityManager.findById(Product.class, id));
	}
    
    public void save(Product entity) {
		int count = this.baseEntityManager.countRecord(Product.class, 
									"name", entity.getName())
									.intValue();
		if(count>0){
			throw new ServiceException("添加失败：此产品的记录已存在，请检查！");
		}
		baseEntityManager.persist(entity);
	}
    
	public void update(Product entity) {
		int count = this.baseEntityManager.countRecord(Product.class, 
									"name", entity.getName(),
									"id:!=", entity.getId())
									.intValue();
		if(count>0){
			throw new ServiceException("更新失败：此产品的记录已存在，请检查！");
		}
		baseEntityManager.update(entity);
	}
    
    public void findPage(Page<Product> page, Product product){
        Querys.from(baseEntityManager, Product.class)
        		.where()
        		.addFields(product)
        		.ignoreIfNull()
        		.end()
        		.toQuery()
        		.page(page);
    }

	public Collection<Product> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(Product.class, id);
	}
}