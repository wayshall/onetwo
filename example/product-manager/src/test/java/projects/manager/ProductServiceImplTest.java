package projects.manager;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import projects.manager.entity.Product;
import projects.manager.service.impl.ProductServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectStarter.class)
//@WebAppConfiguration
public class ProductServiceImplTest {
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@Test
	public void testSave(){
		Product entity = new Product();
		entity.setName("测试");
		entity.setPrice(BigDecimal.valueOf(1.1));
		productServiceImpl.save(entity);
	}

}
