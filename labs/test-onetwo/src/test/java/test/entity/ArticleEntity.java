package test.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.onetwo.common.db.event.EntityAction;
import org.onetwo.common.ejb.jpa.anno.EntityCascade;

@SuppressWarnings("serial")
@Entity
@Table(name="T_ROLE")
public class ArticleEntity extends BaseEntity {
	
	private Long id;
	private String roleName;
	
	private UserEntity user;
	
	private List<ColumnEntity> columns;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Column(name="ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name="USER_ID")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@EntityCascade(action=EntityAction.Remove)
	public List<ColumnEntity> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnEntity> columns) {
		this.columns = columns;
	}
	

}
