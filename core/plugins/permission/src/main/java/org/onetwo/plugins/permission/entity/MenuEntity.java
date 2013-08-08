package org.onetwo.plugins.permission.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.utils.Closure;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.permission.MenuUtils;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_MENU")
//@DiscriminatorValue("MENU")
public class MenuEntity extends PermissionEntity implements IBaseEntity{

	private String name;
	private String url;
	private MenuEntity parent;

	private List<MenuEntity> children;
	private List<PageElementEntity> pageElements;
	
	private Date createTime;
	private Date lastUpdateTime;
	
	public MenuEntity(){
		this.setPtype("MENU");
	}

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

	@OneToMany(mappedBy="menu", fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	public List<PageElementEntity> getPageElements() {
		return pageElements;
	}

	public void setPageElements(List<PageElementEntity> pageElements) {
		this.pageElements = pageElements;
	}

	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
	public List<MenuEntity> getChildren() {
		return children;
	}

	public void setChildren(List<MenuEntity> children) {
		this.children = children;
	}
	
	public void addPageElement(PageElementEntity page){
		if(pageElements==null)
			pageElements = LangUtils.newArrayList();
		page.setMenu(this);
		pageElements.add(page);
	}
	
	public void addChild(MenuEntity menu){
		if(children==null)
			this.children = LangUtils.newArrayList();
		menu.setParent(this);
		this.children.add(menu);
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		MenuUtils.buildString(str, this, "--");
		return str.toString();
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Transient
	public String getHtmlString(){
		final StringBuilder str = new StringBuilder();
		MenuUtils.buildString(str, this, "--", new Closure<PermissionEntity>() {
			
			@Override
			public void execute(PermissionEntity obj) {
				if(MenuEntity.class.isInstance(obj)){
					MenuEntity menu = (MenuEntity)obj;
					str.append(menu.getName());
					str.append(":").append(menu.getUrl()==null?"":menu.getUrl() );
				}else if(PageElementEntity.class.isInstance(obj)){
					PageElementEntity p = (PageElementEntity)obj;
					str.append(p.getName());
				}
				str.append("<br/>");
			}
		});
		return str.toString();
	}

}
