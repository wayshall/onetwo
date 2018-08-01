
package org.onetwo.boot.module.swagger.entity;

import io.swagger.models.ExternalDocs;
import io.swagger.models.Scheme;

import java.util.List;

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
    Boolean deprecated;
    
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
    @DbmJsonField
    List<Scheme> schemes;
    
    /***
     * 外部文档（json）
     */
    @NotBlank
    @Length(max=500)
    @SafeHtml
    @DbmJsonField
    ExternalDocs externaldocs;
    
    /***
     * 标签（json）
     */
    @NotBlank
    @Length(max=200)
    @SafeHtml
    @DbmJsonField
    List<String> tags;
    
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
    @DbmJsonField
    List<String> consumes;
    
}