package org.example.app.model.member.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.example.app.model.utils.BaseEntity;

@Entity
@Table(name = "T_ORDER")
@SequenceGenerator(name = "seqOrder", sequenceName = "SEQ_T_ORDER")
public class OrderEntity extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1155170668364266882L;
	private Long id;
	private String address;

	private List<ItemEntity> items;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@OneToMany
	@JoinTable(
			name="T_ORDER_ITEM",
			joinColumns=@JoinColumn(name="ORDER_ID"),
			inverseJoinColumns=@JoinColumn(name="ITEM_ID")
	)
	public List<ItemEntity> getItems() {
		return items;
	}

	public void setItems(List<ItemEntity> items) {
		this.items = items;
	}

}
