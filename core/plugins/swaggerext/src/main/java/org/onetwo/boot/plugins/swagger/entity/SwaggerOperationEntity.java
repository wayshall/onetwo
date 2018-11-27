
package org.onetwo.boot.plugins.swagger.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.onetwo.boot.plugins.swagger.util.SwaggerUtils;
import org.onetwo.dbm.annotation.DbmJsonField;
import org.onetwo.dbm.jpa.BaseEntity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import io.swagger.models.ExternalDocs;
import io.swagger.models.Scheme;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * swagger操作表
 * operationId，文档内唯一
 * x-api-id: id，全局唯一
 */
@SuppressWarnings("serial")
@Entity
@Table(name="api_swagger_operation")
@Data
@EqualsAndHashCode(callSuper=true)
public class SwaggerOperationEntity extends BaseEntity  {
	/***
	 * 全局唯一，id别名
	 */
	public static final String KEY_API_ID = "x-api-id";
	/***
	 * 作者
	 */
	public static final String KEY_AUTHOR = SwaggerUtils.EXTENSION_PREFIX + "author";
	/***
	 * 维护者
	 */
	public static final String KEY_VINDICATOR = SwaggerUtils.EXTENSION_PREFIX + "vindicator";
	/***
	 * api版本
	 */
	public static final String KEY_VERSION = SwaggerUtils.EXTENSION_PREFIX + "api-version";

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator="snowflake") 
//    @DbmIdGenerator(name="snowflake", generatorClass=SnowflakeGenerator.class)
    @NotNull
    String id;
    /***
     * 所属导入模块
     */
    Long moduleId;
    
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
    /***
     * 文档内唯一
     */
    String operationId;

    @DbmJsonField
    Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
    
    //扩展属性，写成字段便于检索需求
    /***
     * 作者
     */
    String author;
    /***
     * 维护者
     */
    String vindicator;
    /***
     * 版本
     */
    String apiVersion;

    @JsonAnyGetter
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    @JsonAnySetter
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtensions.put(name, value);
        }
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }
}