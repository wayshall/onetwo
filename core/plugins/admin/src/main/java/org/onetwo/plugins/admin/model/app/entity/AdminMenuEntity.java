package org.onetwo.plugins.admin.model.app.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.onetwo.common.utils.Closure;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;
import org.onetwo.plugins.permission.MenuUtils;
import org.onetwo.plugins.permission.entity.IFunction;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.entity.IPermission;
import org.onetwo.plugins.permission.entity.PermissionType;

@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_MENU")
//@DiscriminatorValue("MENU")
public class AdminMenuEntity extends AdminPermissionEntity implements IMenu<AdminMenuEntity, AdminFunctionEntity>{

	@Size(max=255, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String url;
	@Size(max=255, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String method;

	@Size(max=200, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String cssClass;
	@Size(max=1000, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String showProps;
	
	private AdminMenuEntity parent;

	private List<AdminMenuEntity> children;
	private List<AdminFunctionEntity> functions;
	
	private Date createTime;
	private Date lastUpdateTime;
	
	public AdminMenuEntity(){
		this.setPtype(PermissionType.MENU);
	}


	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	@ManyToOne
	@JoinColumn(name="PARENT_CODE")
//	@Fetch(FetchMode.JOIN)
	public AdminMenuEntity getParent() {
		return parent;
	}

	public void setParent(AdminMenuEntity parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy="menu", fetch=FetchType.EAGER)
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE})
	@Fetch(FetchMode.SUBSELECT)
	public List<AdminFunctionEntity> getFunctions() {
		return functions;
	}

	public void setFunctions(List<AdminFunctionEntity> functions) {
		this.functions = functions;
	}
	

	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
//	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE})
	public List<AdminMenuEntity> getChildren() {
		return children;
	}

	public void setChildren(List<AdminMenuEntity> children) {
		this.children = children;
	}
	
	@Override
	public void addFunction(IFunction<AdminMenuEntity> func){
		if(functions==null)
			functions = LangUtils.newArrayList();
		func.setMenu(this);
		functions.add((AdminFunctionEntity)func);
	}
	
	@Override
	public void addChild(IMenu<AdminMenuEntity, AdminFunctionEntity> menu){
		if(children==null)
			this.children = LangUtils.newArrayList();
		AdminMenuEntity menuEntity = (AdminMenuEntity) menu;
		menuEntity.setParent(this);
		this.children.add(menuEntity);
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


	public String getCssClass() {
		return cssClass;
	}


	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getShowProps() {
		return showProps;
	}

	public void setShowProps(String showProps) {
		this.showProps = showProps;
	}


	@Override
	public void onRemove() {
		super.onRemove();
		if(this.getChildren()!=null){
			for(AdminMenuEntity chm :getChildren()){
				chm.setParent(null);
			}
		}
		if(this.getFunctions()!=null){
			for(AdminFunctionEntity func : getFunctions()){
				func.setMenu(null);
			}
		}
	}

	@Transient
	public String getHtmlString(){
		final StringBuilder str = new StringBuilder();
		MenuUtils.buildString(str, this, "--", new Closure<IPermission>() {
			
			@Override
			public void execute(IPermission obj) {
				if(AdminMenuEntity.class.isInstance(obj)){
					AdminMenuEntity menu = (AdminMenuEntity)obj;
					str.append(menu.getName()).append("(").append(menu.getCode()).append(")");
					str.append(":").append(menu.getUrl()==null?"":menu.getUrl() );
				}else if(AdminFunctionEntity.class.isInstance(obj)){
					AdminFunctionEntity p = (AdminFunctionEntity)obj;
					str.append(p.getName()).append("(").append(p.getCode()).append(")");
				}
				str.append("<br/>");
			}
		});
		return str.toString();
	}

}
