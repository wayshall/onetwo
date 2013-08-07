package ${fullPackage};

<#list importClasses as clz>
	<#lt>import ${clz};
</#list>

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

<#--
import org.onetwo.common.fish.jpa.BaseEntity;
-->
import org.onetwo.common.fish.jpa.BaseEntity;

/*****
 * ${table.comment?default("")}
 * @Entity
 */
<#assign uncapitalClassName = table_class_name?capitalize/>
@SuppressWarnings("serial")
@Entity
@Table(name="${table.name}")
@SequenceGenerator(name="${selfClassName}Generator", sequenceName="SEQ_${table.name}")
public class ${selfClassName} extends BaseEntity {
	
<#list table.columnCollection as column>
  <#if column.javaName!='createTime' && column.javaName!='lastUpdateTime'>
	/*****
	 * ${column.comment?default("")}
	 */
	protected ${column.primaryKey?string(table.primaryKey.javaType.simpleName, column.javaType.simpleName)} ${column.javaName};
  </#if>
  
</#list>
	
	public ${selfClassName}(){
	}
	
<#list table.columnCollection as column>
  <#if column.javaName!='createTime' && column.javaName!='lastUpdateTime'>
	
	/*****
	 * ${column.comment?default("")}
	 * @return
	 */
	<#if column.primaryKey>
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="${selfClassName}Generator")
	</#if>
	<#if column.dateType>
	@Temporal(TemporalType.TIMESTAMP)
	</#if>
	@Column(name="${column.name}")
	public ${column.primaryKey?string(table.primaryKey.javaType.simpleName, column.javaType.simpleName)} ${column.readMethodName}() {
		return this.${column.javaName};
	}
	
	public void ${column.writeMethodName}(${column.primaryKey?string(table.primaryKey.javaType.simpleName, column.javaType.simpleName)} ${column.javaName}) {
		this.${column.javaName} = ${column.javaName};
	}
  </#if>
</#list>
	
}
