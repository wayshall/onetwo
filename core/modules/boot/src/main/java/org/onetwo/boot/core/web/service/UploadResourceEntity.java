package org.onetwo.boot.core.web.service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.dbm.annotation.DbmIdGenerator;
import org.onetwo.dbm.id.SnowflakeGenerator;
import org.onetwo.dbm.jpa.BaseEntity;

/***
 * 上传资源表，冗余上传的资源数据
 */
@SuppressWarnings("serial")
@Entity
@Table(name="data_upload_resource")
@Data
@EqualsAndHashCode(callSuper=true)
public class UploadResourceEntity extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="snowflake") 
    @DbmIdGenerator(name="snowflake", generatorClass=SnowflakeGenerator.class)
    @NotNull
    private Long id;
    
    /***
     * 访问路径
     */
    @NotNull
    @NotBlank
    @Length(max=255)
    private String filePath;
    
    /***
     * 文件类型，一般为文件后缀
     */
    @NotNull
    @NotBlank
    @Length(max=10)
    private String fileType;
    
    /***
     * 业务模块
     */
    private String bizModule;
    
    /***
     * 原始文件名称
     */
    @NotNull
    @NotBlank
    @Length(max=100)
    private String originName;
    
}