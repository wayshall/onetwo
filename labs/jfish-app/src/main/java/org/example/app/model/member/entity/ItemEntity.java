package org.example.app.model.member.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.example.app.model.utils.BaseEntity;

@Entity
@Table(name="T_ITEM")
@SequenceGenerator(name="seqOrder", sequenceName="SEQ_T_ITEM")
public class ItemEntity extends BaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9155922909733213586L;
	
	private Long id;
	private Long productId;
	private BigDecimal price;
	private int count;
	
	private OrderEntity order;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

//	@ManyToOne
	/*@JoinTable(
			name="T_ORDER_ITEM",
			joinColumns=@JoinColumn(name="ITEM_ID"),
			inverseJoinColumns=@JoinColumn(name="ORDER_ID")
	)*/
	@Transient
	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}
	
}
