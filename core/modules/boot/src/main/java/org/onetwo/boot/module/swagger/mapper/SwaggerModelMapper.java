package org.onetwo.boot.module.swagger.mapper;

import io.swagger.models.Model;
import io.swagger.models.Response;
import io.swagger.models.parameters.Parameter;

import org.onetwo.boot.module.swagger.SwaggerUtils;
import org.onetwo.boot.module.swagger.entity.SwaggerModelEntity;
import org.onetwo.boot.module.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.module.swagger.entity.SwaggerParameterEntity;
import org.onetwo.boot.module.swagger.entity.SwaggerResponseEntity;
import org.onetwo.boot.module.swagger.model.OperationListModel;
import org.onetwo.boot.module.swagger.model.OperationListModel.OperationDetailModel;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.copier.CopyUtils;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerModelMapper {
	
	public OperationListModel map2Operation(SwaggerOperationEntity op){
		OperationListModel swgOp = new OperationListModel();
		CopyUtils.copy(swgOp, op);
		return swgOp;
	}
	
	public OperationDetailModel map2OperationDetail(SwaggerOperationEntity op){
		OperationDetailModel swgOp = new OperationDetailModel();
		CopyUtils.copy(swgOp, op);
		return swgOp;
	}
	
	public Parameter map2Parameter(SwaggerParameterEntity param){
		Class<?> targetType = ReflectUtils.loadClass(param.getJsonType());
		Parameter swgParameter = SwaggerUtils.getJsonMapper().fromJson(param.getJsonData(), targetType);
		return swgParameter;
	}
	
	public Response map2Response(SwaggerResponseEntity response){
		Class<?> targetType = ReflectUtils.loadClass(response.getJsonType());
		Response swgResponse = SwaggerUtils.getJsonMapper().fromJson(response.getJsonData(), targetType);
		return swgResponse;
	}
	
	public Model map2Model(SwaggerModelEntity model){
		Class<?> targetType = ReflectUtils.loadClass(model.getJsonType());
		Model swgModel = SwaggerUtils.getJsonMapper().fromJson(model.getJsonData(), targetType);
		return swgModel;
	}

}
