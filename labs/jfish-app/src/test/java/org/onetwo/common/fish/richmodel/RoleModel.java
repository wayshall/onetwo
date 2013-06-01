package org.onetwo.common.fish.richmodel;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.richmodel.JFishModel;

@Entity
@Table(name="T_ROLE")
@SequenceGenerator(name="seqRole", sequenceName="SEQ_T_ROLE")
public class RoleModel extends JFishModel {

	/**
	 * 
	 */
	
	private Long id;
	private String name;
	
	private Collection<UserModel> users = LangUtils.newArrayList();
	
	public RoleModel(){}
	public RoleModel(String name) {
		super();
		this.name = name;
	}

	@Id
	@GeneratedValue
	@Column(name="id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(mappedBy="roles")
	public Collection<UserModel> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserModel> users) {
		this.users = users;
	}

}
