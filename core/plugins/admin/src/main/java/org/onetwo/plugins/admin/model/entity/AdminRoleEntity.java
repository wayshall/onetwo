package org.onetwo.plugins.admin.model.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.fish.exception.JFishServiceException;
import org.onetwo.common.hibernate.TimestampBaseEntity;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.DefaultUserDetail;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.utils.AdminErrorCodes.SystemErrors;
import org.onetwo.plugins.admin.utils.WebConstant;


/*****
 * 
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ADMIN_ROLE")
@TableGenerator(pkColumnValue="SEQ_ADMIN_ROLE", table=WebConstant.SEQ_TABLE_NAME, 
			pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", allocationSize=50, initialValue=50000, name="RoleEntityGenerator")
//@DataQueryFilter(fields={K.JOIN, ".app.code"}, values={"apps:app", WebConstant.APP_CODE})
public class AdminRoleEntity extends TimestampBaseEntity implements ILogicDeleteEntity {
	
	/*****
	 * 
	 */
	private Long id;
  
	/*****
	 * 
	 */
	@Length(min=1, max=50)
	private String name;

//	@Length(min=1, max=50)
	private String roleCode;
	private RoleStatus status;
  

	private String appCode;
//	private List<AppEntity> apps;
	
	/*****
	 * 
	 */
	private String remark;
  
	
	private Set<AdminPermissionEntity> permissions;
	
	private List<AdminUserEntity> users;
  
	public AdminRoleEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="RoleEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="NAME")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="REMARK")
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
//	@Enumerated(EnumType.STRING)
	@Transient
	public RoleCode getCode() {
		return RoleCode.valueBy(roleCode);
	}

	public void setCode(RoleCode code) {
		this.roleCode = code.toString();
	}


	@ManyToMany
	@JoinTable(name="ADMIN_ROLE_PERMISSION", joinColumns=@JoinColumn(name="ROLE_ID"), inverseJoinColumns=@JoinColumn(name="PERMISSION_CODE"))
	public Set<AdminPermissionEntity> getPermissions() {
		return permissions;
	}


	public void setPermissions(Set<AdminPermissionEntity> permissions) {
		this.permissions = permissions;
	}

	
	@ManyToMany(mappedBy="roles")
	public List<AdminUserEntity> getUsers() {
		return users;
	}


	public void setUsers(List<AdminUserEntity> users) {
		this.users = users;
	}

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="varchar(20) default 'NORMAL'")
	public RoleStatus getStatus() {
		return status;
	}

	public void setStatus(RoleStatus status) {
		this.status = status;
	}
	
	@Column(name="code")
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
/*
	@ManyToMany
	@JoinTable(name="ADMIN_APP_ROLE", joinColumns=@JoinColumn(name="role_id"), inverseJoinColumns=@JoinColumn(name="app_code"))
	public List<AppEntity> getApps() {
		return apps;
	}


	public void setApps(List<AppEntity> apps) {
		this.apps = apps;
	}
	
	public void addApp(AppEntity app){
		if(this.apps==null){
			this.apps = LangUtils.newArrayList();
		}
		this.apps.add(app);
	}*/

	@Override
	public void deleted() {
		if(isSystemRoot())
			throw JFishServiceException.create(SystemErrors.ROOT_ROLE_CANNOT_DELETE);
		setStatus(RoleStatus.DELETE);
		if(getUsers()!=null){
			for(AdminUserEntity user : getUsers()){
				user.getRoles().clear();
			}
		}
		if(getPermissions()!=null)
			getPermissions().clear();
	}

	@Transient
	public boolean isMerchantBindedRole(){
		return getCode() == RoleCode.MERCHANT || getCode() == RoleCode.OPERATOR;
	}

	@Transient
	public boolean isSystemRoot(){
		return SYSTEM_ROOT_ROLE_ID==getId();
	}
	

	/*@Transient
	public boolean isBindTheApp(String appCode){
		for(AppEntity app : this.getApps()){
			if(app.getCode().equals(appCode))
				return true;
		}
		return false;
	}*/

	public String getAppCode() {
		return appCode;
	}


	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}


	public static boolean isSystemRootRole(Long... roleIds){
		return ArrayUtils.contains(roleIds, SYSTEM_ROOT_ROLE_ID);
	}
	public static boolean isSystemRootRole(List<Long> roleIds){
		return roleIds!=null && roleIds.contains(SYSTEM_ROOT_ROLE_ID);
	}
	private static final Long SYSTEM_ROOT_ROLE_ID = DefaultUserDetail.SYSTEM_ROOT_ROLE_ID;
	
	public static enum RoleCode {
		COMMON("运营"),
		MERCHANT("商户"),
		OPERATOR("充值"),
//		PSAM("PSAM卡制卡员"),
		ISSUE("制卡操作员");
		
		final private String name;

		private RoleCode(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}


		public String getValue() {
			return toString();
		}
		
		public static boolean isSystemRootRole(List<Long> roleIds){
			return roleIds!=null && roleIds.contains(SYSTEM_ROOT_ROLE_ID);
		}
		
		public static RoleCode valueBy(String name){
			if(StringUtils.isBlank(name))
				return COMMON;
			try{
				return valueOf(name);
			}catch(Exception e){
				return COMMON;
			}
		}
		
	}
	
	public static enum RoleStatus {
		NORMAL("正常"),
		DELETE("删除");
		
		private final String label;
		RoleStatus(String label){
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
		public String getValue(){
			return toString();
		}
	}
	

}