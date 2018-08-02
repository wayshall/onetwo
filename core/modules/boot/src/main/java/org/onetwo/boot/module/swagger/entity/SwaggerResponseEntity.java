
package org.onetwo.boot.module.swagger.entity;

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
import org.onetwo.dbm.id.SnowflakeGenerator;
import org.onetwo.dbm.jpa.BaseEntity;

/***
 * swagger响应表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="api_swagger_response")
@Data
@EqualsAndHashCode(callSuper=true)
public class SwaggerResponseEntity extends BaseEntity  {

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="snowflake") 
    @DbmIdGenerator(name="snowflake", generatorClass=SnowflakeGenerator.class)
    @NotNull
    Long id;
    
    /***
     * 
     */
    @NotBlank
    @Length(max=200)
    @SafeHtml
    String jsonType;
    
    /***
     * 名称
     */
    @NotBlank
    @Length(max=50)
    @SafeHtml
    String responseCode;
    
    /***
     * 描述
     */
    @NotBlank
    @Length(max=1000)
    @SafeHtml
    String description;
    
    /***
     * 所属操作id，全局参数为0
     */
    Long operationId;
    
    /***
     * 
     */
    @NotBlank
    @Length(max=2000)
    @SafeHtml
    String jsonData;
    
}