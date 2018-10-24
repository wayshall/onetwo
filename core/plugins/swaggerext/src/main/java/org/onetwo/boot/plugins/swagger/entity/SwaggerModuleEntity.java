package org.onetwo.boot.plugins.swagger.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.onetwo.dbm.annotation.DbmIdGenerator;
import org.onetwo.dbm.id.SnowflakeGenerator;
import org.onetwo.dbm.jpa.BaseEntity;

/**
 * 每个文件一个模块
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Entity
@Table(name="api_swagger_module")
@Data
@EqualsAndHashCode(callSuper=true)
public class SwaggerModuleEntity extends BaseEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
    @DbmIdGenerator(name = "snowflake", generatorClass = SnowflakeGenerator.class)
    @NotNull
	Long id;
	
//	String groupName;
	String moduleName;
	
	@Enumerated(EnumType.STRING)
	StoreTypes storeType;
	@Enumerated(EnumType.STRING)
	Status status;
	String content;
	
	public static enum StoreTypes {
		DATA("文件内容"),
		URL("文件保存地址");
		
		@Getter
		private final String label;

		private StoreTypes(String label) {
			this.label = label;
		}
	}
	
	public static enum Status {
		ENABLED("启用"),
		DISABLED("不可用");
		
		@Getter
		private final String label;

		private Status(String label) {
			this.label = label;
		}
		
	}

}
