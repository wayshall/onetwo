
package org.onetwo.boot.plugins.swagger.entity;

import io.swagger.models.Info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.onetwo.dbm.annotation.DbmIdGenerator;
import org.onetwo.dbm.annotation.DbmJsonField;
import org.onetwo.dbm.id.SnowflakeGenerator;
import org.onetwo.dbm.jpa.BaseEntity;

/***
 * swagger文档基础配置和信息
 */
@SuppressWarnings("serial")
@Entity
@Table(name="api_swagger")
@Data
@EqualsAndHashCode(callSuper=true)
public class SwaggerEntity extends BaseEntity  {

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="snowflake") 
    @DbmIdGenerator(name="snowflake", generatorClass=SnowflakeGenerator.class)
    @NotNull
    Long id;
    
    /***
     * 所属导入模块
     */
    Long moduleId;
    
    /***
     * 冗余的分组名称
     */
    @NotBlank
    @Length(max=100)
    @SafeHtml
    String applicationName;
    
    /***
     * 主机
     */
    @NotBlank
    @Length(max=100)
    @SafeHtml
    String host;
    
    /***
     * 基础路径
     */
    @NotBlank
    @Length(max=100)
    @SafeHtml
    String basePath;
    
    /***
     * 
     */
    @NotBlank
    @Length(max=20)
    @SafeHtml
    String swagger;
    
    /***
     * 信息
     */
    @NotBlank
    @Length(max=2000)
    @SafeHtml
    @DbmJsonField
    Info info;
    
//    ExternalDocs externalDocs;
    
}