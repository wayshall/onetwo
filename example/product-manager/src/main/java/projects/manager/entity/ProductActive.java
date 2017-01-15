
package projects.manager.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import org.onetwo.common.jackson.JsonDateOnlySerializer;
import org.onetwo.common.spring.SpringApplication;

import projects.manager.service.impl.ProductServiceImpl;
import projects.manager.service.impl.UserServiceImpl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
@Table(name="product_active")
@Data
public class ProductActive implements Serializable  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonSerialize(using=JsonDateOnlySerializer.class)
    private Date activeDate;
    private Long activeUserId;
    private Long productId;
    private BigDecimal productPrice;
    private Integer activeAmount;
    private String remark;
    
    public String getActiveUserName(){
    	if(activeUserId==null)
    		return "";
    	return SpringApplication.getInstance()
    							.getBean(UserServiceImpl.class)
    							.findById(activeUserId)
    							.map(u->u.getUserName())
    							.orElse("");
    }
    
    public String getProductName(){
    	if(productId==null)
    		return null;
    	return SpringApplication.getInstance()
    							.getBean(ProductServiceImpl.class)
    							.findById(productId)
    							.map(p->p.getName())
    							.orElse("");
    }
}