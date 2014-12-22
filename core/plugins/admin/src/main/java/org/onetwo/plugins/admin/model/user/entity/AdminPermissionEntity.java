package org.onetwo.plugins.admin.model.user.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.onetwo.plugins.permission.entity.IPermission;
import org.onetwo.plugins.permission.entity.PermissionType;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorColumn(name="PTYPE", discriminatorType=DiscriminatorType.STRING)
@Table(name="ADMIN_PERMISSION")
public class AdminPermissionEntity implements Serializable, IPermission {

//	private Long id;
	private String code;
	private String name;
	private Integer sort;
	private Boolean hidden;
	private PermissionType ptype;
	
	private Set<AdminRoleEntity> roles;
	private String appCode;
	
	/*@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}*/
	@Id
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public void setCode(String code) {
		this.code = code;
	}
	
	@Enumerated(EnumType.STRING)
	public PermissionType getPtype() {
		return ptype;
	}
	
	public void setPtype(PermissionType ptype) {
		this.ptype = ptype;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@ManyToMany(mappedBy="permissions")
	public Set<AdminRoleEntity> getRoles() {
		return roles;
	}
	public void setRoles(Set<AdminRoleEntity> roles) {
		this.roles = roles;
	}
	@Override
	public void onRemove() {
		if(this.getRoles()!=null){
			for(AdminRoleEntity role : this.getRoles()){
				role.getPermissions().remove(this);
			}
		}
	}
	public Boolean isHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}
