package org.onetwo.plugins.codegen.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.codegen.db.SqlTypeFactory.DataBase;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;
import org.onetwo.plugins.fmtag.annotation.JFieldViewMeta;

@SuppressWarnings("serial")
@Entity
@Table(name = DatabaseEntity.TABLE_NAME)
@SequenceGenerator(name = "DataBaseEntityGenerator", sequenceName = "SEQ_codegen_database")
@JEntryViewMeta(label="数据库")
public class DatabaseEntity extends BaseEntity {

	public static final String TABLE_NAME = "codegen_database";

	private Long id;
	private String label;
	private String driverClass;
	private String jdbcUrl;
	private String username;
	private String password;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DataBaseEntityGenerator")
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JFieldViewMeta(label="数据库名称", order=1)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name="driver_class")
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	@Column(name="jdbc_url")
	@JFieldViewMeta(label="链接串", order=3)
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	@JFieldViewMeta(label="数据库用户", order=2)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Transient
	public DataBase getDataBase(){
		if(StringUtils.isBlank(jdbcUrl))
			throw new IllegalArgumentException("Unknown Database : " + jdbcUrl);
		
		if (jdbcUrl.contains(":mysql:")) {
			return DataBase.MySQL;
		} else if (jdbcUrl.contains(":sqlserver:")) {
			return DataBase.Sqlserver;
		} else if (jdbcUrl.contains(":oracle:")) {
			return DataBase.Oracle;
		} else {
			for(DataBase db : DataBase.values()){
				if(jdbcUrl.contains(":"+db.toString().toLowerCase()+":"))
					return db;
			}
//			throw new IllegalArgumentException("Unknown Database : " + jdbcUrl);
			return DataBase.Unknow;
		}
	}


}
