
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
 * swagger操作表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="api_swagger_operation")
@Data
@EqualsAndHashCode(callSuper=true)
public class SwaggerOperationEntity extends BaseEntity  {

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="snowflake") 
    @DbmIdGenerator(name="snowflake", generatorClass=SnowflakeGenerator.class)
    @NotNull
    Long id;
    
    /***
     * api说明摘要
     */
    @NotBlank
    @Length(max=1000)
    @SafeHtml
    String summary;
    
    /***
     * 是否废弃
     */
    Byte deprecated;
    
    /***
     * 请求方法
     */
    @NotBlank
    @Length(max=10)
    @SafeHtml
    String requestMethod;
    
    /***
     * 访问协议（json）
     */
    @NotBlank
    @Length(max=200)
    @SafeHtml
    String schemes;
    
    /***
     * 外部文档（json）
     */
    @NotBlank
    @Length(max=500)
    @SafeHtml
    String externaldocs;
    
    /***
     * 标签（json）
     */
    @NotBlank
    @Length(max=200)
    @SafeHtml
    String tags;
    
    /***
     * 请求路径
     */
    @NotBlank
    @Length(max=200)
    @SafeHtml
    String path;
    
    /***
     * 
     */
    @NotBlank
    @Length(max=500)
    @SafeHtml
    String security;
    
    /***
     * 所属swagger文档
     */
    Long swaggerId;
    
    /***
     * 操作名称
     */
    @NotBlank
    @Length(max=50)
    @SafeHtml
    String name;
    
    /***
     * 响应格式（json）
     */
    @NotBlank
    @Length(max=500)
    @SafeHtml
    String produces;
    
    /***
     * 请求格式（json）
     */
    @NotBlank
    @Length(max=500)
    @SafeHtml
    String consumes;
    
}