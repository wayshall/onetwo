
package org.onetwo.boot.plugins.swagger.entity;

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
     * 所属导入文件
     */
    Long swaggerFileId;
    
    /***
     * api说明摘要
     */
    @Length(max=1000)
    @SafeHtml
    String summary;
    /***
     * 接口描述
     */
    String description;
    
    /***
     * 是否废弃
     */
    Boolean deprecated;
    
    /***
     * 请求方法
     */
    @Length(max=10)
    @SafeHtml
    String requestMethod;
    
    /***
     * 访问协议（json）
     */
    @Length(max=200)
    @SafeHtml
    @DbmJsonField
    List<Scheme> schemes;
    
    /***
     * 外部文档（json）
     */
    @Length(max=500)
    @SafeHtml
    @DbmJsonField
    ExternalDocs externaldocs;
    
    /***
     * 标签（json）
     */
    @Length(max=200)
    @SafeHtml
    @DbmJsonField
    List<String> tags;
    
    /***
     * 请求路径
     */
    @Length(max=200)
    @SafeHtml
    String path;
    
    /***
     * 
     */
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
    @Length(max=500)
    @DbmJsonField
    List<String> produces;
    
    /***
     * 请求格式（json）
     */
    @Length(max=500)
    @DbmJsonField
    List<String> consumes;
    
}