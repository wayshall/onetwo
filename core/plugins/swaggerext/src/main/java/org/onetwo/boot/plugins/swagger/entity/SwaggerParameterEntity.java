
package org.onetwo.boot.plugins.swagger.entity;

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
 * swagger参数表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="api_swagger_parameter")
@Data
@EqualsAndHashCode(callSuper=true)
public class SwaggerParameterEntity extends BaseEntity  {

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
    String name;
    
    /***
     * 描述
     */
    @NotBlank
    @Length(max=500)
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