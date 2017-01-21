
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
import org.onetwo.common.spring.Springs;

import projects.manager.service.impl.ProductServiceImpl;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
@Table(name="product_income")
@Data
public class ProductIncome implements Serializable  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonSerialize(using=JsonDateOnlySerializer.class)
    private Date incomeDate;
    private Long productId;
    private BigDecimal income;
    private String remark;
    

    public String getProductName(){
    	if(productId==null)
    		return null;
    	return Springs.getInstance()
    							.getBean(ProductServiceImpl.class)
    							.findById(productId)
    							.map(p->p.getName())
    							.orElse("");
    }
}