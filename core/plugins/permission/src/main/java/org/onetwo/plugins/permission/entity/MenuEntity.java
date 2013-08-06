package org.onetwo.plugins.permission.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_MENU")
@PrimaryKeyJoinColumn(name="PERMISSION_ID")
public class MenuEntity extends PermissionEntity {

	private String name;
	private String url;
	private MenuEntity parent;

	private List<MenuEntity> children;
	private List<PageElementEntity> pageElements;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@ManyToOne
	@JoinColumn(name="PARENT_ID")
//	@Fetch(FetchMode.JOIN)
	public MenuEntity getParent() {
		return parent;
	}

	public void setParent(MenuEntity parent) {
		this.parent = parent;
	}

	@OneToMany
	@JoinColumn(referencedColumnName="MENU_ID")
	public List<PageElementEntity> getPageElements() {
		return pageElements;
	}

	public void setPageElements(List<PageElementEntity> pageElements) {
		this.pageElements = pageElements;
	}

	@OneToMany(mappedBy="parent")
	public List<MenuEntity> getChildren() {
		return children;
	}

	public void setChildren(List<MenuEntity> children) {
		this.children = children;
	}

}
