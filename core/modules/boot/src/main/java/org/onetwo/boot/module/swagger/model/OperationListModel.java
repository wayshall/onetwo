package org.onetwo.boot.module.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;


/**
 * @author wayshall
 * <br/>
 */
@Data
@ApiModel(value="Operation")
public class OperationListModel {
	@ApiModelProperty(value="id")
	String id;
	@ApiModelProperty(value="请求路径")
	String path;
	@ApiModelProperty(value="请求方法")
	String requestMethod;

	@ApiModelProperty(value="接口摘要")
    String summary;
	@ApiModelProperty(value="接口描述")
    String description;
    
    /***
     * 是否废弃
     */
	@ApiModelProperty(value="是否废弃")
    Boolean deprecated;
    
	@ApiModelProperty(value="访问协议")
    List<Scheme> schemes;
    
	@ApiModelProperty(value="外部文档")
    ExternalDocs externaldocs;
    
    /***
     * 标签（json）
     */
	@ApiModelProperty(value="标签")
    List<String> tags;
    
    
    
    /***
     * 响应格式（json）
     */
	@ApiModelProperty(value="响应格式")
	List<String> produces;
    
    /***
     * 请求格式（json）
     */
	@ApiModelProperty(value="请求格式")
    List<String> consumes;

	@ApiModel(value="OperationDetail")
	public static class OperationDetailModel extends OperationListModel {
	    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
	    private List<Parameter> parameters = new ArrayList<Parameter>();
	    private Map<String, Response> responses;
	    

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

	    public List<Parameter> getParameters() {
	        return parameters;
	    }

	    public void setParameters(List<Parameter> parameters) {
	        this.parameters = parameters;
	    }

	    public void addParameter(Parameter parameter) {
	        if (this.parameters == null) {
	            this.parameters = new ArrayList<Parameter>();
	        }
	        this.parameters.add(parameter);
	    }

	    public Map<String, Response> getResponses() {
	        return responses;
	    }

	    public void setResponses(Map<String, Response> responses) {
	        this.responses = responses;
	    }

	    public void addResponse(String key, Response response) {
	        if (this.responses == null) {
	            this.responses = new LinkedHashMap<String, Response>();
	        }
	        this.responses.put(key, response);
	    }
	}
}
