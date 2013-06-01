package test.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.common.fish.annotation.JFishEntityListeners;

@SuppressWarnings("serial")
@Entity
@Table(name="T_USER")
@org.nutz.dao.entity.annotation.Table("T_USER")
@SequenceGenerator(name="seqUser", sequenceName="SEQ_T_USER")
@JFishEntityListeners(TestEntityListener.class)
public class UserEntity extends BaseEntity  {

	@org.nutz.dao.entity.annotation.Id
	@org.nutz.dao.entity.annotation.Column("id")
	private Long id;
	
	@org.nutz.dao.entity.annotation.Column("USER_NAME") 
	private String userName;

	private String status;
	
	@org.nutz.dao.entity.annotation.Column("email")
	private String email;
	
	@org.nutz.dao.entity.annotation.Column("age")
	private Integer age;

	@org.nutz.dao.entity.annotation.Column("BIRTH_DAY")
	private Date birthDay;

	@org.nutz.dao.entity.annotation.Column("height")
	private Float height;
	
	private List<RoleEntity> roles;
	
//	@Override
	@Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	@Column(name="USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name="BIRTH_DAY")
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	@Column(name="HEIGHT")
	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void addRole(RoleEntity role){
		if(this.roles==null)
			this.roles = new ArrayList<RoleEntity>();
		this.roles.add(role);
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public Date getCreateTime() {
		// TODO Auto-generated method stub
		return super.getCreateTime();
	}

	@Override
	public void setCreateTime(Date createTime) {
		// TODO Auto-generated method stub
		super.setCreateTime(createTime);
	}

	@Override
	public Date getLastUpdateTime() {
		// TODO Auto-generated method stub
		return super.getLastUpdateTime();
	}

	@Override
	public void setLastUpdateTime(Date lastUpdateTime) {
		// TODO Auto-generated method stub
		super.setLastUpdateTime(lastUpdateTime);
	}

}
