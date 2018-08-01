
package org.onetwo.boot.module.swagger.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

import org.onetwo.dbm.annotation.DbmIdGenerator;
import org.onetwo.dbm.id.SnowflakeGenerator;
import org.onetwo.dbm.jpa.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * 所属导入文件
     */
    Long swaggerFileId;
    
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
    String info;
    
}